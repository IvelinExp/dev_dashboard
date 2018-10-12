package dev.dashboard.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;

import dev.dashboard.entities.Story;
import dev.dashboard.entities.Task;

public class UserStoryDao implements UserStoryDaoInterface<Story, String> {

	private Session currentSession;

	private Transaction currentTransaction;

	public UserStoryDao() {

	}

	public Session openCurrentSession() {

		currentSession = getSessionFactory().openSession();
		return currentSession;

		// change here
	}

	public Session openCurrentSessionwithTransaction() {

		currentSession = getSessionFactory().openSession();

		currentTransaction = currentSession.beginTransaction();

		return currentSession;

	}

	public void closeCurrentSession() {

		currentSession.close();

	}

	public void closeCurrentSessionwithTransaction() {

		currentTransaction.commit();

		if (currentSession.isConnected()) {
			currentSession.close();

		}

	}

	private static SessionFactory getSessionFactory() {

		Configuration configuration = new Configuration().configure();

		configuration.addAnnotatedClass(Story.class);
		configuration.addAnnotatedClass(Task.class);

		StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()

				.applySettings(configuration.getProperties());

		SessionFactory sessionFactory = configuration.buildSessionFactory(builder.build());

		return sessionFactory;

	}

	public Session getCurrentSession() {

		return currentSession;

	}

	public void setCurrentSession(Session currentSession) {

		this.currentSession = currentSession;

	}

	public Transaction getCurrentTransaction() {

		return currentTransaction;

	}

	public void setCurrentTransaction(Transaction currentTransaction) {

		this.currentTransaction = currentTransaction;

	}

	public void persist(Story entity) {

		getCurrentSession().save(entity);

	}

	public void update(Story entity) {

		getCurrentSession().update(entity);

	}

	public Story findById(String id) {

		Story story = (Story) getCurrentSession().get(Story.class, Long.parseLong(id));

		story.setEstimate(getEstimateforStory(story.getId()));

		persist(story);
		return story;

	}

	public Double getEstimateforStory(Long id) {

		double returnSum = 0.0;

		Story story = (Story) getCurrentSession().get(Story.class, id);

		
		try {
			Criteria crit = getCurrentSession().createCriteria(Task.class);

			List<?> tasks = crit.add(Restrictions.like("summary", "DEV%"))
					.add(Restrictions.eq("parent", "#" + story.getId())).list();

			if (tasks.isEmpty()) {
				/*
				 * could not find estimate, might be refactored to look only for /* US in status
				 * Ready For Development
				 */
				return calculateAvgTimeSpendForUserStoryInDays(story);
			} else {

				for (int i = 0; i < tasks.size(); i++) {
					Task task = (Task) tasks.get(i);
					returnSum += task.getEstimateh();
				}
				return returnSum / 8;
			}
		} catch (Exception e) {

			e.printStackTrace();
			return 0.0;
		}

	}

	private Double calculateAvgTimeSpendForUserStoryInDays(Story userStory) {

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
		Story story = (Story) getCurrentSession().get(Story.class, userStory.getId());

		Query qry = getCurrentSession()
				.createQuery("SELECT sum ( t.estimateh ) / count(s.id) from Story s, Task t    " +
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
			// new PrintSysOutImp("Could not find history for component : " +
			// userStory.getMaincomponent());

		}

		// new PrintSysOutImp("Estimate for US: " + userStory.getId() + " with
		// Feature ID: " + userStory.getParent()
		// + " = " + avgEst);
		return avgEst;
	}

	public Map<String, String> getUsEstimates() {

		Map<String, String> map = new HashMap<String, String>();

		Query qry = getCurrentSession().createSQLQuery(
				"select substr( parent, -6), round(sum(tasks.estimateh)/count(tasks.id),2) as estimate from tasks where tasks.summary like '%DEV_%' and parent like '#%' group by parent");

		List<Object[]> list = qry.list();

		for (Object[] result : list) {
			if (result[0] != null && result[1] != null) {
				map.put(result[0].toString(), result[1].toString());

			}
		}

		return map;

	};

	public void delete(Story entity) {

		getCurrentSession().delete(entity);

	}

	@SuppressWarnings("unchecked")

	public List<Story> findAll() {

		List<Story> books = (List<Story>) getCurrentSession().createQuery("from Story").list();

		return books;

	}

	public void deleteAll() {

		List<Story> entityList = findAll();

		for (Story entity : entityList) {

			delete(entity);

		}

	}

}
