package dev.dashboard.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.naming.directory.NoSuchAttributeException;
import javax.persistence.EntityManager;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionException;
import org.hibernate.SessionFactory;
import org.hibernate.StaleStateException;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.CriteriaQuery;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.service.ServiceRegistry;
import dev.dashboard.entities.Developer;
import dev.dashboard.entities.Feature;
import dev.dashboard.entities.Story;
import dev.dashboard.entities.Task;

public class HibernateUtil {
	static Session sessionObj;
	static SessionFactory sessionFactoryObj;
	final static Logger LOGGER = Logger.getLogger(HibernateUtil.class.getName());

	// This Method Is Used To Create The Hibernate's SessionFactory Object
	private static SessionFactory buildSessionFactory() throws NamingException {
		// Creating Configuration Instance & Passing Hibernate Configuration
		// File
		Configuration configObj = new Configuration();
//		configObj.configure("hibernate.cfg.xml");
		configObj.addAnnotatedClass(dev.dashboard.entities.Developer.class)
				.addAnnotatedClass(dev.dashboard.entities.Story.class)
				.addAnnotatedClass(dev.dashboard.entities.Feature.class)
				.addAnnotatedClass(dev.dashboard.entities.Task.class)
				.setProperty("hibernate.connection.datasource", "jdbc/OracleDS")
				.setProperty("hibernate.dialect", "org.org.hibernate.dialect.Oracle10gDialect");

		// Since Hibernate Version 4.x, ServiceRegistry Is Being Used
		ServiceRegistry serviceRegistryObj = new StandardServiceRegistryBuilder()
				.applySettings(configObj.getProperties()).build();

		// Creating Hibernate SessionFactory Instance
		// sessionFactoryObj =
		// configObj.configure().buildSessionFactory(serviceRegistryObj);
		// sessionObj = sessionFactoryObj.openSession();
		InitialContext ctx = new InitialContext();
		sessionFactoryObj = (SessionFactory) ctx.lookup("java:hibernate/SessionFactory");
		sessionObj = sessionFactoryObj.openSession();
		return sessionFactoryObj;
	}

	private static void startSession() throws HibernateException, NamingException {

		if (sessionObj == null) {
			sessionObj = buildSessionFactory().openSession();
			sessionObj.beginTransaction();
		}
		if (sessionObj.isConnected() == false) {
			sessionObj = buildSessionFactory().openSession();
			sessionObj.beginTransaction();
		}

	}

	public static void createDeveloper(String name) {
		Developer devObj;
		try {
			startSession();
			devObj = new Developer.DeveloperBuilder().setDeveloperName(name).build();

			sessionObj.save(devObj);

			new PrintSysOutImp("Developer " + devObj.getName() + " Added to DB  ");

			sessionObj.getTransaction().commit();
		} catch (Exception sqlException) {
			if (null != sessionObj.getTransaction()) {
				LOGGER.log(Level.SEVERE, "\n.......Transaction Is Being Rolled Back.......");
				sessionObj.getTransaction().rollback();
			}
			sqlException.printStackTrace();
		} finally {
			if (sessionObj != null) {
				sessionObj.close();
			}
		}
	}

	public static void removeDeveloper(Long id) {
		Developer devObj = new Developer.DeveloperBuilder().setDeveloperId(id).buildIDonly();
		try {
			startSession();
			devObj.setId(id);

			sessionObj.delete(devObj);

			// Committing The Transactions To The Database //
			sessionObj.getTransaction().commit();
			LOGGER.log(Level.INFO, "Developer with ID {0} Removed From DB  ", devObj.getId());

		} catch (StaleStateException staleStateException) {

			LOGGER.log(Level.SEVERE, "Develper with Id {0} not found !", id);

		} catch (Exception sqlException) {

			if (null != sessionObj.getTransaction()) {
				LOGGER.log(Level.SEVERE, "\n.......Transaction Is Being Rolled Back.......");
				sessionObj.getTransaction().rollback();
			}
			sqlException.printStackTrace();
		} finally {
			if (sessionObj != null) {
				sessionObj.close();
			}
		}
	}

	public static void planUserStoryForSprint(Story story, Developer dev, String devSprint)
			throws HibernateException, NamingException {

		startSession();

		story.setPlDev(dev.getId());

		if (DevSprints.contains(devSprint)) {
			story.setPlSprint(devSprint);
		} else {
			System.out.println("Dev Sprint not found, defaulting to null");
			story.setPlSprint(null);
		}
		sessionObj.getTransaction().commit();

	}

	public static Story getStoryByID(Long storyID) throws HibernateException, NamingException {
		startSession();
		Criteria criteria = sessionObj.createCriteria(Story.class);

		criteria.add(Restrictions.eq("id", storyID));
		List<?> stories = criteria.list();

		if (stories.size() == 0)
			return null;
		return (Story) stories.get(0);

	}

	public static Developer getDevByID(Long devID) throws HibernateException, NamingException {
		startSession();
		Criteria criteria = sessionObj.createCriteria(Developer.class);

		criteria.add(Restrictions.eq("id", devID));
		List<?> devs = criteria.list();

		if (devs.size() == 0)
			return null;
		return (Developer) devs.get(0);

	}

	public static void printDevelopers() throws HibernateException, NamingException {

		try {
			startSession();
			List<?> names = sessionObj.createQuery("from Developer").list();
			sessionObj.getTransaction().commit();
			for (int i = 0; i < names.size(); i++) {
				Developer dev = (Developer) names.get(i);
				System.out.println("Developer id =  " + dev.getId() + " Name " + dev.getName());
				// LOGGER.log(Level.INFO, "Developer {0} ", dev.getName());
			}

		} catch (SessionException se) {

			se.printStackTrace();
			System.out.print(" NO Session Found ");
		}

		finally {
			if (sessionObj != null) {
				sessionObj.close();
			}
		}
	}

	public static Double getEstimateForFeature(Integer featureId) throws HibernateException, NamingException {
		startSession();

		Criteria criteria = sessionObj.createCriteria(Story.class);

		criteria.add(Restrictions.like("parent", "%" + featureId.toString()));

		String[] userStoryStatusesNotInDevelopmentOrDone = { "Ready for dev", "Ready for PO", "Pending BA", "New",
				"Confirmed" };
		criteria.add(Restrictions.in("status", userStoryStatusesNotInDevelopmentOrDone));
		criteria.add(Restrictions.in("iteration",
				Arrays.toString(UserStoryIterationNotInProgressOrDone.values()).replaceAll("^.|.$", "").split(", ")));

		List<?> stories = criteria.list();
		Double returnSum = 0.0;

		for (int i = 0; i < stories.size(); i++) {
			returnSum = returnSum + getEstimateForUserStoryInDays((Story) stories.get(i));
		}
		new PrintSysOutImp(" Feature " + featureId + " has " + stories.size() + " User Stories with total estimate = "
				+ returnSum);
		return returnSum;
	}

	public static Double getEstimateForUserStoryInDays(Story userStory) {

		Double returnSum = 0.0;

		try {
			startSession();

			Criteria crit = sessionObj.createCriteria(Task.class);

			List<?> tasks = crit.add(Restrictions.like("summary", "DEV%"))
					.add(Restrictions.eq("parent", "#" + userStory.getId())).list();

			if (tasks.isEmpty()) {
				/*
				 * could not find estimate, might be refactored to look only for /* US in status
				 * Ready For Development
				 */
				return calculateAvgTimeSpendForUserStoryInDays(userStory);
			} else {

				for (int i = 0; i < tasks.size(); i++) {
					Task task = (Task) tasks.get(i);
					returnSum = returnSum + task.getEstimateh();
				}
				new PrintSysOutImp("Estimate for US: " + userStory.getId() + " = " + returnSum / 8);
				return returnSum / 8;
			}
		} catch (Exception e) {

			e.printStackTrace();
			return 0.0;
		}

	}

	private static Double calculateAvgTimeSpendForUserStoryInDays(Story userStory) {

		/**
		 * select MAINCOMPONENT, Round( SUM(ESTIMATEH) / COUNT(STORIES.ID) / 8, 2) as
		 * avgTSP, COUNT(STORIES.ID)
		 * 
		 * from STORIES
		 * 
		 * 
		 * Right join TASKS
		 * 
		 * on TASKS.PARENT = concat('#',STORIES.ID )
		 * 
		 * where STORIES.MAINCOMPONENT <> 'Unidentified' and TASKS.SUMMARY like 'DEV%'
		 * and STORIES.ITERATION not in ('Sprint 4', 'Sprint 3', 'Sprint 1
		 * (1.0)','Sprint 2 (1.0)')
		 * 
		 * group by STORIES.MAINCOMPONENT order by avgTSP desc;
		 **/

		Query qry = sessionObj.createQuery("SELECT sum ( t.estimateh ) / count(s.id) from Story s, Task t    " +
		// concat('#',STORIES.ID )

				"where concat ('#', s.id) =  t.parent and " + "s.maincomponent <> :unIdent and " +
				// 'Unidentified'
				"t.summary like :devPRC and " +
				// 'DEV%'
				"s.iteration not in (:notSprints) and " +
				// 'Sprint 4', 'Sprint 3', 'Sprint 1 (1.0)','Sprint 2
				// (1.0)'
				"s.maincomponent= :mainCo ")
				// .setParameter("conHashStories", "'#',STORIES.ID ")
				.setParameter("unIdent", "Unidentified").setParameter("devPRC", "DEV%")
				.setParameter("notSprints", "'Sprint 4', 'Sprint 3', 'Sprint 1(1.0)','Sprint 2'")
				.setParameter("mainCo", userStory.getMaincomponent());

		List<?> letSee = qry.list();
		Double avgEst;
		if (!letSee.isEmpty()) {

			avgEst = (Double) letSee.get(0) / 8;
			// new PrintSysOutImp("Avg time spend for component : " +
			// userStory.getMaincomponent() + " = " + avgEst);

		} else {

			avgEst = 2.5;
			new PrintSysOutImp("Could not find history for component : " + userStory.getMaincomponent());

		}

		// new PrintSysOutImp("Estimate for US: " + userStory.getId() + " with
		// Feature ID: " + userStory.getParent()
		// + " = " + avgEst);
		return avgEst;
	}

	public static Map<String, ArrayList<Story>> getUserStoriesForReleaseSortedByFeature(String release) {

		HashMap<String, ArrayList<Story>> sortedbyFeature = new HashMap<String, ArrayList<Story>>();

		try {
			startSession();

			Criteria criteria = sessionObj.createCriteria(Story.class);

			criteria.add(Restrictions.like("developmentSequence", release));
			List<?> stories = criteria.list();

			for (int i = 0; i < stories.size(); i++) {
				Story story = (Story) stories.get(i);
				if (story.getParent() == null) {
					story.setParent("#100000");
					// throw new NoSuchAttributeException("Missing Parrent for
					// US : " + story.getId());
				}

				story.setEstimate(getEstimateForUserStoryInDays(story));
				if (UserStoryStatusNotInProgress.contains(story.getStatus().replace(" ", ""))) {

					if (sortedbyFeature.get(story.getParent().replace("#", "")) != null) {

						List<Story> wtfList = new ArrayList<Story>(
								sortedbyFeature.get(story.getParent().replace("#", "")));
						wtfList.add(story);
						sortedbyFeature.put(story.getParent().replace("#", ""), (ArrayList<Story>) wtfList);

					} else {

						List<Story> wtfList = new ArrayList<Story>();
						wtfList.add(story);
						sortedbyFeature.put(story.getParent().replace("#", ""), (ArrayList<Story>) wtfList);

					}

				}
			}
			Double sumOfEffort;
			for (String key : sortedbyFeature.keySet()) {
				sumOfEffort = 0.0;
				List<Story> wtfList = new ArrayList<Story>(sortedbyFeature.get(key));

				for (int i = 0; i < wtfList.size(); i++) {
					sumOfEffort = sumOfEffort + wtfList.get(i).getEstimate();

				}

				new PrintSysOutImp("Feature : " + key + " Has Size of " + sortedbyFeature.get(key).size()
						+ " with Total Estimate: " + sumOfEffort);
			}

			return sortedbyFeature;
		} catch (Exception e) {

			e.printStackTrace();
			return null;
		}

		finally {
			if (sessionObj != null) {
				sessionObj.close();
			}
		}

	}

	public static HashMap<Long, String> getDevelopers() {
		long emp_id;
		Developer devObj;
		HashMap<Long, String> devArray = new HashMap<Long, String>();
		try {
			startSession();
			// Get The Employee Details Whose Emp_Id is 1
			emp_id = 1;
			while (true) {
				try {
					devObj = (Developer) sessionObj.get(Developer.class, new Long(emp_id));
					if (devObj != null) {
						devArray.put(emp_id, devObj.getName());
						emp_id++;
					} else {

						return devArray;
					}
				} catch (Exception e) {
					e.printStackTrace();
					break;
				}
			}
			// Get The Employee Details Whose Emp_Id is 6
		} catch (Exception sqlException) {
			try {
				if (null != sessionObj.getTransaction()) {
					LOGGER.log(Level.SEVERE, "\n.......Transaction Is Being Rolled Back.......");
					sessionObj.getTransaction().rollback();
					return devArray;
				}
			} catch (Exception e) {

				e.printStackTrace();

			}

			sqlException.printStackTrace();
		} finally {
			if (sessionObj != null) {
				sessionObj.close();
				return devArray;
			}
		}
		return devArray;

	}

	public static HashMap<Long, Story> getPOCUserStories() throws HibernateException, NamingException {
		HashMap<Long, Story> storyMap = new HashMap<Long, Story>();
		try {
			startSession();
			// while (true) {
			try {
				Query qry = (Query) sessionObj.createQuery("from Story p where development_sequence = :poc");
				qry.setParameter("poc", "PoC");

				List<?> list = qry.list();
				// System.out.println("Total Number Of Records : " +
				// list.size());
				Iterator<?> it = list.iterator();
				while (it.hasNext()) {
					Object o = (Object) it.next();
					Story storyObject = (Story) o;
					storyMap.put((Long) storyObject.getId(), storyObject);

				}

			} catch (Exception e) {

				e.printStackTrace();

			}

			// sqlException.printStackTrace();
		} finally {
			if (sessionObj != null) {
				sessionObj.close();
				return storyMap;
			}
		}
		return storyMap;

	}

	public static void getAvaliableReleases() throws HibernateException, NamingException {
		try {
			startSession();

			Criteria criteria = sessionObj.createCriteria(Story.class);
			criteria.setProjection(Projections.distinct(Projections.property("developmentSequence")));
			List<?> releaseList = criteria.list();

			for (int i = 0; i < releaseList.size(); i++) {

				System.out
						.println("|--->>>>>>>> Release Avaliable =  '" + (String) releaseList.get(i) + "'<<<<<<<<---|");
			}

		} catch (SessionException se) {

			se.printStackTrace();
			System.out.print(" NO Session Found ");
		}

		finally {
			if (sessionObj != null) {
				sessionObj.close();
			}
		}
	}
}