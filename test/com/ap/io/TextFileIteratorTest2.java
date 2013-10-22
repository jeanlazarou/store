package com.ap.io;

import java.util.List;

import junit.framework.TestCase;

public class TextFileIteratorTest2 extends TestCase {
/*
	public void testTopHasNoPrevious() throws Exception {
		
		LineIterator it = fixture.head();
		
		assertTrue(!it.hasPrevious());
		assertTrue(it.hasNext());
		
	}
	
	public void testBottomHasNoNext() throws Exception {
		
		LineIterator it = fixture.tail();
		
		assertTrue(!it.hasNext());
		assertTrue(it.hasPrevious());
		
	}

	public void testReadFirst() throws Exception {
		
		LineIterator it = fixture.head();
		
		assertEquals(list.get(0), it.next());

	}

	public void testReadLast() throws Exception {
		
		LineIterator it = fixture.tail();
		
		assertEquals(list.get(4), it.previous());

	}

	public void _testBlindForward() throws Exception {
		
		LineIterator it = fixture.head();
		
		assertEquals(list.get(0), it.next());
		assertEquals(list.get(1), it.next());
		assertEquals(list.get(2), it.next());
		assertEquals(list.get(3), it.next());
		assertEquals(list.get(4), it.next());

	}

	public void _testReadFileForward() throws Exception {
		
		LineIterator it = fixture.head();
		
		assertTrue(!it.hasPrevious());

		assertTrue(it.hasNext());
		assertEquals(list.get(0), it.next());
		assertTrue(it.hasPrevious());
		assertTrue(it.hasNext());
		assertEquals(list.get(1), it.next());
		assertTrue(it.hasPrevious());
		assertTrue(it.hasNext());
		assertEquals(list.get(2), it.next());
		assertTrue(it.hasPrevious());
		assertTrue(it.hasNext());
		assertEquals(list.get(3), it.next());
		assertTrue(it.hasPrevious());
		assertTrue(it.hasNext());
		assertEquals(list.get(4), it.next());
		
		assertTrue(it.hasPrevious());
		assertTrue(!it.hasNext());
	}

	public void _testBlindBackward() throws Exception {
		
		LineIterator it = fixture.tail();
		
		assertEquals(list.get(4), it.previous());
		assertEquals(list.get(3), it.previous());
		assertEquals(list.get(2), it.previous());
		assertEquals(list.get(1), it.previous());
		assertEquals(list.get(0), it.previous());

	}

	public void _testReadFileBackward() throws Exception {
		
		LineIterator it = fixture.tail();
		
		assertTrue(!it.hasNext());
		assertTrue(it.hasPrevious());
		assertEquals(list.get(4), it.previous());
		assertTrue(it.hasNext());
		assertTrue(it.hasPrevious());
		assertEquals(list.get(3), it.previous());
		assertTrue(it.hasNext());
		assertTrue(it.hasPrevious());
		assertEquals(list.get(2), it.previous());
		assertTrue(it.hasNext());
		assertTrue(it.hasPrevious());
		assertEquals(list.get(1), it.previous());
		assertTrue(it.hasNext());
		assertTrue(it.hasPrevious());
		assertEquals(list.get(0), it.previous());
		
		assertTrue(it.hasNext());
		assertTrue(!it.hasPrevious());
	}

	public void _testReadFileBlindlyBackAndForth() throws Exception {
		
		LineIterator it = fixture.tail();
		
		assertEquals(list.get(4), it.previous());
		assertEquals(list.get(3), it.previous());
		assertEquals(list.get(2), it.previous());
		assertEquals(list.get(1), it.previous());
		assertEquals(list.get(0), it.previous());

		assertEquals(list.get(0), it.next());
		assertEquals(list.get(1), it.next());
		assertEquals(list.get(2), it.next());
		assertEquals(list.get(3), it.next());
		assertEquals(list.get(4), it.next());
		
	}

	public void _testReadFileBackAndForth() throws Exception {
		
		LineIterator it = fixture.tail();
		
		assertTrue(it.hasPrevious());
		assertEquals(list.get(4), it.previous());
		assertTrue(it.hasPrevious());
		assertEquals(list.get(3), it.previous());
		assertTrue(it.hasPrevious());
		assertEquals(list.get(2), it.previous());
		assertTrue(it.hasPrevious());
		assertEquals(list.get(1), it.previous());
		assertTrue(it.hasPrevious());
		assertEquals(list.get(0), it.previous());
		
		assertTrue(!it.hasPrevious());
		
		assertTrue(it.hasNext());
		assertEquals(list.get(0), it.next());
		assertTrue(it.hasNext());
		assertEquals(list.get(1), it.next());
		assertTrue(it.hasNext());
		assertEquals(list.get(2), it.next());
		assertTrue(it.hasNext());
		assertEquals(list.get(3), it.next());
		assertTrue(it.hasNext());
		assertEquals(list.get(4), it.next());
		
		assertTrue(!it.hasNext());
		
	}

	public void _testReadPastBOF() throws Exception {

		LineIterator it = fixture.head();
		
		try {
			
			it.previous();
			
			fail("Should complain reading previous at BOF");
			
		} catch (NoSuchElementException e) {
			// yes!
		}
		
	}

	public void _testReadPastEOF() throws Exception {

		LineIterator it = fixture.tail();
		
		try {
			
			it.next();
			
			fail("Should complain reading after EOF");
			
		} catch (NoSuchElementException e) {
			// yes!
		}
		
	}
	
	public void _testRepositioning() throws Exception {
		
		LineIterator it = fixture.head();
		
		String line0 = (String) it.next();
		String line1 = (String) it.next();
		
		FilePosition pos = it.lastPosition();
		
		String line2 = (String) it.next();
		String line3 = (String) it.next();
		
		it.setNext(pos);
		
		assertEquals(line1, it.next());
		assertEquals(line2, it.next());
		assertEquals(line3, it.next());
		
		it.setPrevious(pos);
		
		assertEquals(line1, it.previous());
		assertEquals(line0, it.previous());

		assertTrue(!it.hasPrevious());
		
	}
	
	public void _testJumpAtLast() throws Exception {
		
		LineIterator it = fixture.tail();
		
		String line = (String) it.previous();

		FilePosition pos = it.lastPosition();

		it.previous();
		it.previous();
		it.previous();
		
		it.setPrevious(pos);
		
		assertEquals(line, it.previous());
		
		assertTrue(it.hasPrevious());
		assertTrue(it.hasNext());
		
		it.setNext(pos);
		
		assertEquals(line, it.next());
		
		assertTrue(it.hasPrevious());
		assertTrue(!it.hasNext());

	}

	public void _testJumpAtFirst() throws Exception {
		
		LineIterator it = fixture.head();
		
		String line = (String) it.next();

		FilePosition pos = it.lastPosition();

		it.next();
		it.next();
		it.next();
		
		it.setNext(pos);
		
		assertEquals(line, it.next());
		
		assertTrue(it.hasPrevious());
		assertTrue(it.hasNext());
		
		it.setPrevious(pos);
		
		assertEquals(line, it.previous());
		
		assertTrue(!it.hasPrevious());
		assertTrue(it.hasNext());

		
	}

	public void _testHasNextIsStable() throws Exception {

		LineIterator it = fixture.head();
		
		it.next();
		it.next();
		
		FilePosition pos = it.lastPosition();

		assertTrue(it.hasNext());
		assertTrue(it.hasNext());
		assertTrue(it.hasNext());
		assertTrue(it.hasNext());

		assertEquals(pos, it.lastPosition());
		
		it.previous();
		it.previous();

		assertTrue(!it.hasPrevious());
	}
 
	public void _testHasPreviousIsStable() throws Exception {

		LineIterator it = fixture.tail();
		
		it.previous();
		it.previous();

		FilePosition pos = it.lastPosition();

		assertTrue(it.hasPrevious());
		assertTrue(it.hasPrevious());
		assertTrue(it.hasPrevious());
		assertTrue(it.hasPrevious());

		assertEquals(pos, it.lastPosition());

		it.next();
		it.next();

		assertTrue(!it.hasNext());

	}

	public void _testUsingHasMethodsIsStable() throws Exception {

		LineIterator it = fixture.tail();
		
		it.previous();
		it.previous();

		FilePosition pos = it.lastPosition();
		
		assertTrue(it.hasNext());
		assertTrue(it.hasPrevious());
		assertTrue(it.hasNext());
		assertTrue(it.hasPrevious());

		assertEquals(pos, it.lastPosition());

		it.next();
		it.next();

		assertTrue(!it.hasNext());

	}

	protected void setUp() throws Exception {
		
		list = fixture.getList();
		
		if (list.size() < 5) {
			throw new IllegalArgumentException("This test set need a 5 line text file");
		}
		
	}
*/
	public TextFileIteratorTest2(IteratorFixture fixture, String name) {
		
		super(name);
		
		this.fixture = fixture;
		
	}
	
	List list;
	IteratorFixture fixture;
	
}
