package dev.dashboard.util;

public enum UserStoryStatusInProgresOrDone {
		
	Done("Done"), Implemented("Implemented"), InProgress("InProgress"),DeployedonParisdev("DeployedonParisdev");
	

	
    private String status;
    private UserStoryStatusInProgresOrDone (String stat) {
        this.status = stat;
    }
   
    
	public static boolean contains(String status) {

	    for (UserStoryStatusInProgresOrDone c : UserStoryStatusInProgresOrDone.values()) {
	        if (c.name().equals(status)) {
	            return true;
	        }
	    }

	    return false;
	}
    
    
    @Override
    public String toString(){
        return status;
    }


}
