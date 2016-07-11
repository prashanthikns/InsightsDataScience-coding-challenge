package Venmo;

import static org.junit.Assert.*;

import org.junit.Test;

public class VenmoTest {

	@Test
	public void testOneTansactionInsert() {
		Venmo venmo = new Venmo();
		venmo.insertTransaction(new Transaction(200000, "Jamie", "Jackie"));
		assertEquals(1, venmo.getMedian(), .01);
	}
	
	@Test
	public void testUpdateEdgeWithin60Secs() {
		Venmo venmo = new Venmo();
		venmo.insertTransaction(new Transaction(200000, "Jamie", "Jackie"));
		assertEquals(1, venmo.getMedian(), .01);
		
		venmo.insertTransaction(new Transaction(210000, "Jamie", "Jackie"));
		assertEquals(1, venmo.getMedian(), .01);
	}
	
	@Test
	public void testUpdateEdgeAfter60Secs() {
		Venmo venmo = new Venmo();
		venmo.insertTransaction(new Transaction(200000, "Jamie", "Jackie"));
		assertEquals(1, venmo.getMedian(), .01);
		
		venmo.insertTransaction(new Transaction(300000, "Jamie", "Jackie"));
		assertEquals(1, venmo.getMedian(), .01);
	}
	
	@Test
	public void testReverseTransactionWithin60Secs() {
		Venmo venmo = new Venmo();
		venmo.insertTransaction(new Transaction(200000, "Jamie", "Jackie"));
		assertEquals(1, venmo.getMedian(), .01);
		
		venmo.insertTransaction(new Transaction(230000, "Jackie", "Jamie"));
		assertEquals(1, venmo.getMedian(), .01);
	}
	
	@Test
	public void testReverseTransactionAfter60Secs() {
		Venmo venmo = new Venmo();
		venmo.insertTransaction(new Transaction(200000, "Jamie", "Jackie"));
		assertEquals(1, venmo.getMedian(), .01);
		
		venmo.insertTransaction(new Transaction(500000, "Jackie", "Jamie"));
		assertEquals(1, venmo.getMedian(), .01);
	}
	
	@Test
	public void testMultipleTransactionWithSameActorAfter60Secs() {
		Venmo venmo = new Venmo();
		venmo.insertTransaction(new Transaction(200000, "Jamie", "Jackie"));
		assertEquals(1, venmo.getMedian(), .01);
		
		venmo.insertTransaction(new Transaction(230000, "Jamie", "Janet"));
		assertEquals(1, venmo.getMedian(), .01);
		
		venmo.insertTransaction(new Transaction(300000, "Jamie", "David"));
		assertEquals(1, venmo.getMedian(), .01);
	}
	
	@Test
	public void testMultipleTransactionWithSameTargetWithin60Secs() {
		Venmo venmo = new Venmo();
		venmo.insertTransaction(new Transaction(200000, "Jamie", "Jackie"));
		assertEquals(1, venmo.getMedian(), .01);
		
		venmo.insertTransaction(new Transaction(230000, "B", "Jackie"));
		assertEquals(1, venmo.getMedian(), .01);
		
		venmo.insertTransaction(new Transaction(240000, "A", "Jackie"));
		assertEquals(1, venmo.getMedian(), .01);
	}
	

	@Test
	public void testMultipleTransactionWithSameTargetAfter60Secs() {
		Venmo venmo = new Venmo();
		venmo.insertTransaction(new Transaction(200000, "Jamie", "Jackie"));
		assertEquals(1, venmo.getMedian(), .01);
		
		venmo.insertTransaction(new Transaction(230000, "B", "Jackie"));
		assertEquals(1, venmo.getMedian(), .01);
		
		venmo.insertTransaction(new Transaction(340000, "A", "Jackie"));
		assertEquals(1, venmo.getMedian(), .01);
	}
	
	@Test
	public void testdeleteAllEntriesAfter60Secs() {
		Venmo venmo = new Venmo();
		venmo.insertTransaction(new Transaction(200000, "Jamie", "Jackie"));
		assertEquals(1, venmo.getMedian(), .01);
		
		venmo.insertTransaction(new Transaction(230000, "A", "B"));
		assertEquals(1, venmo.getMedian(), .01);
		
		venmo.insertTransaction(new Transaction(230000, "C", "D"));
		assertEquals(1, venmo.getMedian(), .01);
		
		venmo.insertTransaction(new Transaction(230000, "E", "F"));
		assertEquals(1, venmo.getMedian(), .01);
		
		venmo.insertTransaction(new Transaction(230000, "G", "H"));
		assertEquals(1, venmo.getMedian(), .01);
		
		venmo.insertTransaction(new Transaction(300000, "Jamie", "David"));
		assertEquals(1, venmo.getMedian(), .01);
	}
	
	@Test
	public void testMultipleActorTargetSwapLessThan60Secs() {
		Venmo venmo = new Venmo();
		venmo.insertTransaction(new Transaction(200000, "A", "B"));
		assertEquals(1, venmo.getMedian(), .01);
		
		venmo.insertTransaction(new Transaction(230000, "B", "c"));
		assertEquals(1, venmo.getMedian(), .01);
		
		venmo.insertTransaction(new Transaction(230000, "C", "A"));
		assertEquals(1.5, venmo.getMedian(), .01);
	}
	
	@Test
	public void testMultipleActorTargetSwapGreaterThan60Secs() {
		Venmo venmo = new Venmo();
		venmo.insertTransaction(new Transaction(200000, "A", "B"));
		assertEquals(1, venmo.getMedian(), .01);
		
		venmo.insertTransaction(new Transaction(330000, "B", "c"));
		assertEquals(1, venmo.getMedian(), .01);
		
		venmo.insertTransaction(new Transaction(430000, "C", "A"));
		assertEquals(1, venmo.getMedian(), .01);
	}
	
	@Test
	public void testMultipleEdgesFromSameNodeWithin60secs() {
		Venmo venmo = new Venmo();
		venmo.insertTransaction(new Transaction(200000, "A", "B"));
		assertEquals(1, venmo.getMedian(), .01);
		
		venmo.insertTransaction(new Transaction(230000, "A", "C"));
		assertEquals(1, venmo.getMedian(), .01);
		
		venmo.insertTransaction(new Transaction(230000, "A", "D"));
		assertEquals(1, venmo.getMedian(), .01);
		
		venmo.insertTransaction(new Transaction(230000, "A", "E"));
		assertEquals(1, venmo.getMedian(), .01);
		
		venmo.insertTransaction(new Transaction(230000, "A", "F"));
		assertEquals(1, venmo.getMedian(), .01);
		
		venmo.insertTransaction(new Transaction(230000, "A", "G"));
		assertEquals(1, venmo.getMedian(), .01);
	}
	
	@Test
	public void testComplexWorkflow() {
		Venmo venmo = new Venmo();
		venmo.insertTransaction(new Transaction(200000, "A", "B"));
		venmo.printVenmo();
		assertEquals(1, venmo.getMedian(), .01);
		
		venmo.insertTransaction(new Transaction(230000, "A", "C"));
		venmo.printVenmo();
		assertEquals(1, venmo.getMedian(), .01);
		
		venmo.insertTransaction(new Transaction(230000, "A", "D"));
		venmo.printVenmo();
		assertEquals(1, venmo.getMedian(), .01);
		
		venmo.insertTransaction(new Transaction(230000, "C", "D"));
		venmo.printVenmo();
		assertEquals(2, venmo.getMedian(), .01);
		
		venmo.insertTransaction(new Transaction(230000, "C", "B"));
		venmo.printVenmo();
		assertEquals(2.5, venmo.getMedian(), .01);
	
		venmo.insertTransaction(new Transaction(231000, "C", "D"));
		venmo.printVenmo();
		assertEquals(2.5, venmo.getMedian(), .01);

		venmo.insertTransaction(new Transaction(231000, "C", "B"));
		venmo.printVenmo();
		assertEquals(2.5, venmo.getMedian(), .01);

		venmo.insertTransaction(new Transaction(259000, "B", "D"));
		venmo.printVenmo();
		assertEquals(3, venmo.getMedian(), .01);

		venmo.insertTransaction(new Transaction(291000, "B", "D"));
		venmo.printVenmo();
		assertEquals(2, venmo.getMedian(), .01);

		venmo.insertTransaction(new Transaction(292000, "B", "D"));
		venmo.printVenmo();
		assertEquals(1, venmo.getMedian(), .01);
		
		venmo.insertTransaction(new Transaction(232000, "A", "D"));
		venmo.printVenmo();
		assertEquals(1, venmo.getMedian(), .01);
		
		venmo.insertTransaction(new Transaction(231000, "C", "B"));
		venmo.printVenmo();
		assertEquals(1, venmo.getMedian(), .01);
		
	}	
}
