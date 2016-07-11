package Venmo;

/* This class is define the fields of the transaction.
 * This is used by the priority queue to maintain ordering on the timestamp.
 */
public class Transaction implements Comparable<Transaction> {
  private long createdTime;
  private String actor;
  private String target;
  
  public Transaction(long createdTime, String actor, String target) {
    this.createdTime = createdTime;
    this.actor = actor;
    this.target = target;
  }
  
  public long getCreatedTime() {
    return this.createdTime;
  }
  
  public String getActor() {
    return this.actor;
  }
  
  public String getTarget(){
    return this.target;
  }
  
  public int compareTo(Transaction o){
    if (this.createdTime == o.createdTime) {
      return 0;
    } else if (this.createdTime < o.createdTime) {
      return -1;
    }
    return 1;
  }
  
  public static void main(String[] args) {
    // TODO Auto-generated method stub
  }

}
