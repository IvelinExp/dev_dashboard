package dev.dashboard.resources;

import java.net.URI;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.jboss.logging.Logger;
import org.jboss.logging.Logger.Level;

import dev.dashboard.ejb.UserStoryHandler;
import dev.dashboard.entities.Story;

@Stateless
@Path("/us")
@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
public class UserStoryResources {

	@Context
	UriInfo uriInfo;

	@Inject
	UserStoryHandler userStoryHandler;
	@Inject
	UserStoryService userStoryService;

	private static final Logger LOGGER = Logger.getLogger(UserStoryResources.class.getName());

	@GET
	public Response allUserStories() {
		List<Story> all = this.userStoryHandler.findAllUs();

		if (all == null || all.isEmpty()) {
			return Response.noContent().build();
		}

		JsonArray data = all.stream().map(this::toJson)
				.collect(Json::createArrayBuilder, JsonArrayBuilder::add, JsonArrayBuilder::add).build();

		return Response.ok().entity(data).build();
	}

	@GET
	@Path("/sayHello")
	public String sayHello() {
		return "<h1>Hello World</h1>";
	}

	@GET
	@Path("{id}")
	public Response findById(@PathParam("id") final String id)
			throws javax.ejb.EJBException, java.lang.NullPointerException {

		try {
			Story story = this.userStoryService.findById(id);

			return Response.ok().entity(toJson(story)).build();

		} catch (Exception e) {

			LOGGER.logf(Level.WARN, "Cannot Find Story with ID %s in DB", id);
			return Response.noContent().build();

		}
	}

	@POST
	public Response create(@Valid Story story) {
		this.userStoryHandler.addUserStory(story);

		URI self = uriBuilder(story.getId());

		return Response.created(self).build();
	}

	@PUT
	@Path("{id}")
	public Response update(@PathParam("id") final Long sid, @Valid Story story) {
		URI self = uriBuilder(sid);

		this.userStoryHandler.updateStory(sid, story);

		return Response.accepted().build();
	}

//	@DELETE
//	@Path("{slug}")
//	public Response remove(@PathParam("id") final Long slug) {
//		this.userStoryHandler.remove(slug);
//
//        return Json.createObjectBuilder()
//        		 .add("id", story.getId())
//                 .add("summary", story.getSummary())
//                 .add("dev_sequence", story.getDevelopmentSequence())
//                 .add("status", story.getStatus())
//                 .add("estimate", story.getEstimate())
//                 .add("_links", Json.createObjectBuilder()
//                        .add("rel", "self")
//                       .add("href", self.toString()))
//                .build();
//    }
// 

	@GET
	@Path("{id}/orders")
	public Response checkOrder(@PathParam("id") Long id) {
		URI self = uriBuilder(id);
		JsonObject book = this.userStoryHandler.checkOrder(id, self);

		return Response.ok().entity(book).build();
	}

	private JsonObject toJson(Story story) {
		URI self = uriBuilder(story.getId());

		Double estimate = 0.0;

		if (story.getEstimate() == null) {

			estimate = this.userStoryService.getEstimateForUserStory(story.getId());

		}

		return Json.createObjectBuilder().add("id", story.getId()).add("summary", story.getSummary())
				.add("dev_sequence", story.getDevelopmentSequence()).add("status", story.getStatus())
				.add("estimate", estimate)
				.add("_links", Json.createObjectBuilder().add("rel", "self").add("href", self.toString())).build();
	}

	private URI uriBuilder(Long id) {
		return this.uriInfo.getBaseUriBuilder().path(UserStoryResources.class)
				.path(UserStoryResources.class, "findById").build(id);
	}
}