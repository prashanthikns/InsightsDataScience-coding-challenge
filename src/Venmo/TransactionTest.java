package Venmo;

import static org.junit.Assert.*;

import org.junit.Test;

public class TransactionTest {

	@Test
	public void testTransaction() {
		Transaction t= new Transaction(200, "Jamie", "Jackie");
		Transaction t1 = new Transaction(100, "Jackie", "KKK");	

	}

	@Test
	public void testGetCreatedTime() {
		Transaction t1 = new Transaction(100, "Jackie", "KKK");
		assertEquals(100, t1.getCreatedTime());
	}

	@Test
	public void testGetActor() {
		Transaction t1 = new Transaction(100, "Jackie", "KKK");
		assertEquals("Jackie", t1.getActor());
	}

	@Test
	public void testGetTarget() {
		Transaction t1 = new Transaction(100, "Jackie", "KKK");
		assertEquals("KKK", t1.getTarget());
	}

	@Test
	public void testCompareTo() {
		Transaction t1 = new Transaction(100, "Jackie", "KKK");
		Transaction t2 = new Transaction(200, "Jackie", "KKK");
		
		assertEquals(-1, t1.compareTo(t2));
		
		Transaction m1 = new Transaction(500, "Jackie", "KKK");
		Transaction m2 = new Transaction(200, "Jackie", "KKK");
		
		assertEquals(1, m1.compareTo(m2));
		
		Transaction n1 = new Transaction(300, "Jackie", "KKK");
		Transaction n2 = new Transaction(300, "Jackie", "KKK");
		
		assertEquals(0, n1.compareTo(n2));
	}
}
