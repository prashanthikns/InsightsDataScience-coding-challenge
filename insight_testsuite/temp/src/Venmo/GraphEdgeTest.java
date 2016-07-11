package Venmo;

import static org.junit.Assert.*;

import org.junit.Test;

public class GraphEdgeTest {

	@Test
	public void testGraphEdge() {
		GraphEdge g = new GraphEdge("Jamie", 100);
		GraphEdge g1 = new GraphEdge("Jackie", 200);
		GraphEdge g2 = new GraphEdge("Janet", 300);
	}

	@Test
	public void testGetTarget() {
		GraphEdge g = new GraphEdge("Jamie", 100);
		assertEquals("Jamie", g.getTarget());
		
		GraphEdge g1 = new GraphEdge("KKK", 100);
		assertEquals("KKK", g1.getTarget());
	}

	@Test
	public void testGetCreatedTime() {
		GraphEdge g = new GraphEdge("Jamie", 500);
		assertEquals(500, g.getCreatedTime());
		
		GraphEdge g1 = new GraphEdge("KKK", 600);
		assertEquals(600, g1.getCreatedTime());
	}

	@Test
	public void testSetCreatedTime() {
		GraphEdge g = new GraphEdge("Jamie", 100);
		g.setCreatedTime(500);
		assertEquals(500, g.getCreatedTime());
		
		GraphEdge g1 = new GraphEdge("KKK", 400);
		g1.setCreatedTime(25);
		assertEquals(400, g1.getCreatedTime());
	}

}
