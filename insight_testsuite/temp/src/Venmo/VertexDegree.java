package Venmo;
/* This class is used to define the objects in medianDegreeList.
 * It is ordered by the degree of the node.
 */
public class VertexDegree {
	private int degree;
	private String actor;
	
	public VertexDegree(int degree, String actor){
		this.degree = degree;
		this.actor = actor;
	}
	
	public int getDegree() {
		return this.degree;
	}
	
	public String getActor() {
		return this.actor;
	}

	public int setDegree(int degree) {
		this.degree = degree;
		return this.degree;
	}
	
	public String toString() {
    return "{degree: " + degree + ", actor: " + actor + "}";
  }
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
