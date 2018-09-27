package dev.dashboard.util;

public enum UserStoryStatusNotInProgress {
		
	Readyfordev("Readyfordev"), ReadyforPO("ReadyforPO"), PendingBA("PendingBA"), New("New"), Confirmed("Confirmed");
    
	
	private String status;
    private UserStoryStatusNotInProgress (String stat) {
        this.status = stat;
    }
   
	public static boolean contains(String status) {

	    for (UserStoryStatusNotInProgress c : UserStoryStatusNotInProgress.values()) {
	        if (c.name().equals(status)) {
	            return true;
	        }
	    }

	    return false;
	}
}
