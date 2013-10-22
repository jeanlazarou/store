package com.ap.io;

import java.util.List;

import junit.framework.TestCase;

import com.ap.io.text.LineIterator;

public class TextFileIteratorTest extends TestCase {

	public void testFailToReadBeforePositioning() {
		
		LineIterator it = fixture.getIterator();

		try {
			
			it.next();
			
			fail("Should not be able to read without first positioning");
		
		} catch (IllegalStateException e) {
			// fine
		}

		try {
			
			it.previous();
			
			fail("Should not be able to read without first positioning");
		
		} catch (IllegalStateException e) {
			// fine
		}

	}
	
	public void testReadFileForward() {
		
		LineIterator it = fixture.getIterator();
		
		List list = fixture.getList();

		it.beforeFirst();

		for (int i = 0; i < list.size(); i++) {
			assertEquals(list.get(i), it.next());
		}

		assertNull(it.next());
		
	}

	public void testReadFileBackward() {
		
		LineIterator it = fixture.getIterator();
		
		List list = fixture.getList();

		it.afterLast();

		for (int i = list.size() - 1; i >= 0; i--) {
			assertEquals(list.get(i), it.previous());
		}

		assertNull(it.previous());
		
	}

	public void testReadFileForwardThenBack() {
		
		LineIterator it = fixture.getIterator();
		
		List list = fixture.getList();

		it.beforeFirst();

		for (int i = 0; i < list.size(); i++) {
			it.next();
		}

		for (int i = list.size() - 1; i >= 0; i--) {
			assertEquals(list.get(i), it.previous());
		}

		assertNull(it.previous());
		
	}

	public void testReadFileBackwardThenBack() {
		
		LineIterator it = fixture.getIterator();
		
		List list = fixture.getList();

		it.afterLast();

		for (int i = list.size() - 1; i >= 0; i--) {
			it.previous();
		}

		for (int i = 0; i < list.size(); i++) {
			assertEquals(list.get(i), it.next());
		}

		assertNull(it.next());
		
	}
	
	public void testBackAndForthDoesNotMoveFromBOF() {
		
		LineIterator it = fixture.getIterator();
		
		List list = fixture.getList();

		it.beforeFirst();

		if (list.size() > 0) {

			String line = it.next();
			
			assertEquals(line, it.previous());
			assertEquals(line, it.next());
			assertEquals(line, it.previous());

			if (list.size() > 1) {
				
				it.next();
				
				line = it.next();
				
				assertEquals(line, it.previous());
				assertEquals(line, it.next());
				assertEquals(line, it.previous());

				if (list.size() > 2) {
					
					it.next();
					
					line = it.next();
					
					assertEquals(line, it.previous());
					assertEquals(line, it.next());
					assertEquals(line, it.previous());
					
				}
				
			}
		}
		
	}
	
	public void testBackAndForthDoesNotMoveFromEOF() {
		
		LineIterator it = fixture.getIterator();
		
		List list = fixture.getList();

		it.afterLast();

		if (list.size() > 0) {

			String line = it.previous();
			
			assertEquals(line, it.next());
			assertEquals(line, it.previous());
			assertEquals(line, it.next());

			if (list.size() > 1) {
				
				it.previous();
				
				line = it.previous();
				
				assertEquals(line, it.next());
				assertEquals(line, it.previous());
				assertEquals(line, it.next());

				if (list.size() > 2) {
					
					it.previous();
					
					line = it.previous();
					
					assertEquals(line, it.next());
					assertEquals(line, it.previous());
					assertEquals(line, it.next());
					
				}
				
			}
		}
		
	}
	
	protected void setUp() throws Exception {
		fixture.setUp();
	}

	protected void tearDown() throws Exception {
		fixture.tearDown();
	}
	public TextFileIteratorTest(IteratorFixture fixture, String name) {
		
		super(name);
		
		this.fixture = fixture;
		
	}

	IteratorFixture fixture;
}
