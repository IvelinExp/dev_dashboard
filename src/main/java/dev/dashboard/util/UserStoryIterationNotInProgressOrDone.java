package dev.dashboard.util;

public enum UserStoryIterationNotInProgressOrDone {

	Backlog("Backlog"), Unassigned("Unassigned");

	private String status;

	private UserStoryIterationNotInProgressOrDone(String stat) {
		this.status = stat;
	}

	public static boolean contains(String status) {

		for (UserStoryIterationNotInProgressOrDone c : UserStoryIterationNotInProgressOrDone.values()) {
			if (c.name().equals(status)) {
				return true;
			}
		}

		return false;
	}
}
