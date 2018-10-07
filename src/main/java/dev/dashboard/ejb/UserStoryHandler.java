package dev.dashboard.ejb;

import javax.persistence.EntityManager;

import java.net.URI;
import java.util.List;

import javax.ejb.Stateless;
import javax.json.Json;
import javax.json.JsonObject;
import javax.persistence.PersistenceContext;

import dev.dashboard.entities.Story;

@Stateless
public class UserStoryHandler {

	@PersistenceContext
	EntityManager em;

	public List<Story> findAllBook() {
		return this.em.createNamedQuery(Story.FIND_ALL, Story.class).getResultList();
	}

	public Story findById(Long id) {
		return this.em.createNamedQuery(Story.FIND_BY_ID, Story.class).setParameter("id", id).getSingleResult();
	}

	public void addUserStory(Story story) {
		this.em.persist(story);
	}

	public void updateStory(Long sid, Story story) {
		Story s = findById(sid);
		Story.setId(s.getId());
		this.em.merge(story);
	}

	public void remove(Long id) {
		final Story managedBook = findById(id);

		this.em.remove(managedBook);
	}

	public JsonObject checkOrder(Long id, URI self) {
		Story story = findById(id);
		return Json.createObjectBuilder().add("id", story.getId()).add("summary", story.getSummary())
				.add("dev_sequence", story.getDevelopmentSequence()).add("status", story.getStatus())
				.add("estimate", story.getEstimate())
				.add("_links", Json.createObjectBuilder().add("rel", "self").add("href", self.toString())).build();
	}
}