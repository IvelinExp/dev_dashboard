package dev.dashboard.plan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import dev.dashboard.entities.Story;
import dev.dashboard.util.*;

public class SprintMapper implements AbstractSprintMapper {

	final static Logger LOGGER = Logger.getLogger(SprintMapper.class.getName());

	public void populateSprint() {
		int countOfUSNotInProcess = 0;
		int countOfUSInProcess = 0;

		//Get Developers 
		Map<Long, String> devs = new HashMap<>(HibernateUtil.getDevelopers());
		//Get Stories 
		Map<Long, Story> stories = new HashMap<Long, Story>(HibernateUtil.getPOCUserStories());
		//Create Map with plannedStories = all stories - those that are planned
		Map<String, HashMap<String, List<Story>>> plannedStories = new HashMap<String, HashMap<String, List<Story>>>();
		Map<String, HashMap<String, List<Story>>> notPlannedStories = new HashMap<String, HashMap<String, List<Story>>>();

		
		
		Iterator<Entry<Long, Story>> it = stories.entrySet().iterator();
		while (it.hasNext()) {
			Entry<Long, Story> pair = it.next();
			Story tmpStory = (Story) pair.getValue();
			//if Userstory not worked on
			if (UserStoryStatusNotInProgress.contains(tmpStory.getStatus().replaceAll("\\s+", ""))) {
				countOfUSNotInProcess++;
				it.remove();
				if (!notPlannedStories.containsKey(tmpStory.getIteration())) {

					notPlannedStories.put(tmpStory.getIteration(), new HashMap<String, List<Story>>());
				}
				
				
				System.out.printf("Story: %s - %s is not planned, tech group: %s \n", tmpStory.getId(),tmpStory.getSummary(), tmpStory.getMaincomponent());

				
				
				
				

			}
			if (UserStoryIterationNotInProgressOrDone.contains(tmpStory.getStatus().replaceAll("\\s+", ""))) {
				countOfUSInProcess++;
				//Checking if bucket with Itteration already exisits in the plannedStories
				if (!plannedStories.containsKey(tmpStory.getIteration())) {

					plannedStories.put(tmpStory.getIteration(), new HashMap<String, List<Story>>());
				}
				// checking within the itteration bucket if a bucket with Developer exists
				if (!plannedStories.get(tmpStory.getIteration()).containsKey(tmpStory.getUserStoryTechnicalOwner())) {

					plannedStories.get(tmpStory.getIteration()).put(tmpStory.getUserStoryTechnicalOwner(),
							new ArrayList<Story>());
				}

				ArrayList<Story> current = (ArrayList<Story>) plannedStories.get(tmpStory.getIteration())
						.get(tmpStory.getUserStoryTechnicalOwner());
				current.add(tmpStory);

				plannedStories.get(tmpStory.getIteration()).put(tmpStory.getUserStoryTechnicalOwner(), current);

				String logStr = "ID " + tmpStory.getId() + " " + tmpStory.getIteration() + " Assigned to "
						+ tmpStory.getUserStoryTechnicalOwner();
				LOGGER.log(Level.INFO, "Putting {0}", logStr);

			}

		}

		for (Entry<String, HashMap<String, List<Story>>> entry : plannedStories.entrySet()) {

			System.out.printf(">>>>>>> : %s  <<<<<< %n", entry.getKey());

			for (Entry<String, List<Story>> entry2 : entry.getValue().entrySet()) {
				System.out.printf("Engineer : %s :: UserStory Count: %s %n", entry2.getKey(), entry2.getValue().size());

			}

		}

		// LOGGER.log(Level.INFO,"Sprints Completed:
		// {0}",plannedStories.keySet());
		// LOGGER.log(Level.INFO,"Stories to be Planned
		// {0}",countOfUSNotInProcess);
		// LOGGER.log(Level.INFO,"Stories Done {0}",countOfUSInProcess);

		// LOGGER.log(Level.INFO, "DEVELOPERS ARE {0}", devSprint.keySet());
		// LOGGER.log(Level.INFO, "Stories are ARE {0}", stories.keySet());

	}

}
