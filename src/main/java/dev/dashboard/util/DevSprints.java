package dev.dashboard.util;

public enum DevSprints {
	SPRINT_1, SPRINT_2, SPRINT_3, SPRINT_4, SPRINT_5, SPRINT_6, SPRINT_7, SPRINT_8, SPRINT_9, SPRINT_10, SPRINT_11, SPRINT_12, SPRINT_13, SPRINT_14, SPRINT_15, SPRINT_16, SPRINT_17, SPRINT_18, SPRINT_19, SPRINT_20, SPRINT_21;
	
	
	
	
	public static boolean contains(String status) {

		for (DevSprints c : DevSprints.values()) {
			if (c.name().equals(status)) {
				return true;
			}
		}

		return false;
	}

	
	
}