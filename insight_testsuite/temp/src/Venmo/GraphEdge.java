package Venmo;

/* This class is to define the edge of the transaction.  
 * This is used to make a mapping of all edges out of a vertex.
 */
public class GraphEdge {
	private String target;
	private long   createdTime;

	public GraphEdge(String target, long createdTime) {
		this.target = target;
		this.createdTime = createdTime;
	}
	
	public String getTarget() {
		return this.target;
	}
	
	public long getCreatedTime() {
		return this.createdTime;
	}
	
	public long setCreatedTime(long time){
		if (this.createdTime < time) {
			/* If transaction occurs between actor and target with  
			 * later timestamp, update the timestamp.
			 */
			this.createdTime = time;
		}
		return this.createdTime;
	}
	
	public String toString() {
    return "{Target: " + target + ", CreatedTime: " + createdTime + "}";
  }
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	}

}
