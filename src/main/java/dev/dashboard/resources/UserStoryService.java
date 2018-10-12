package dev.dashboard.resources;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dev.dashboard.dao.UserStoryDao;
import dev.dashboard.entities.Story;

public class UserStoryService {

	public static HashMap<String, String> storyEstimates;

	private static UserStoryDao userStoryDao;

	public UserStoryService() {
		userStoryDao = new UserStoryDao();
	}

	public void persist(Story entity) {
		userStoryDao.openCurrentSessionwithTransaction();
		userStoryDao.persist(entity);
		userStoryDao.closeCurrentSessionwithTransaction();
	}

	public void update(Story entity) {
		userStoryDao.openCurrentSessionwithTransaction();
		userStoryDao.update(entity);
		userStoryDao.closeCurrentSessionwithTransaction();
	}

	public Story findById(String id) {
		userStoryDao.openCurrentSession();
		Story story = userStoryDao.findById(id);
		userStoryDao.closeCurrentSession();
		return story;
	}

	public void delete(String id) {
		userStoryDao.openCurrentSessionwithTransaction();
		Story story = userStoryDao.findById(id);
		userStoryDao.delete(story);
		userStoryDao.closeCurrentSessionwithTransaction();
	}

	public Double getEstimateForUserStory(Long id) {

		userStoryDao.openCurrentSession();
		Double estimate = userStoryDao.getEstimateforStory(id);
		userStoryDao.persist(userStoryDao.findById(id.toString()));
		userStoryDao.closeCurrentSession();

		return estimate;

	}

	public static Map<String, String> getUsEstimates() {

		if (storyEstimates == null) {

			userStoryDao.openCurrentSession();

			storyEstimates = new HashMap<String, String>();

			//returnMap.putAll((Map<String, String>) userStoryDao.getUsEstimates());

			storyEstimates.putAll((Map<String, String>) userStoryDao.getUsEstimates());

		}

		return storyEstimates;

	}

	public List<Story> findAll() {
		userStoryDao.openCurrentSession();
		List<Story> stories = userStoryDao.findAll();
		userStoryDao.closeCurrentSession();
		return stories;
	}

	public void deleteAll() {
		userStoryDao.openCurrentSessionwithTransaction();
		userStoryDao.deleteAll();
		userStoryDao.closeCurrentSessionwithTransaction();
	}

	public UserStoryDao userStoryDao() {
		return userStoryDao;
	}
}
