package dev.dashboard.resources;

import java.util.List;

import dev.dashboard.dao.UserStoryDao;
import dev.dashboard.entities.Story;

public class UserStoryService {

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
		userStoryDao.closeCurrentSession();

		return estimate;

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
