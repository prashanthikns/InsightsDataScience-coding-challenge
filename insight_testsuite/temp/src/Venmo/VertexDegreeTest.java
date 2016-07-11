package Venmo;

import static org.junit.Assert.*;

import org.junit.Test;

public class VertexDegreeTest {

	@Test
	public void testActorDegree() {
		VertexDegree a = new VertexDegree(3, "Jamie");
		VertexDegree b = new VertexDegree(3, "Jamie");
		VertexDegree c = new VertexDegree(3, "Jamie");
	}

	@Test
	public void testGetDegree() {
		VertexDegree a = new VertexDegree(3, "Jamie");
		assertEquals(3, a.getDegree());
		
		VertexDegree b = new VertexDegree(4, "Jamie");
		assertEquals(4, b.getDegree());
	}

	@Test
	public void testGetActor() {
		VertexDegree a = new VertexDegree(3, "Jackie");
		assertEquals("Jackie", a.getActor());
		
		VertexDegree b = new VertexDegree(3, "Jamie");
		assertEquals("Jamie", b.getActor());
	}

	@Test
	public void testSetDegree() {
		VertexDegree a = new VertexDegree(3, "Jamie");
		a.setDegree(5);
		assertEquals(5, a.getDegree());
		
		VertexDegree b = new VertexDegree(6, "KKK");
		b.setDegree(2);
		assertEquals(2, b.getDegree());
	}
}
