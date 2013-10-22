package com.ap.io;

import junit.framework.TestCase;

abstract public class ReaderTest extends TestCase {
	
	abstract public void add(StringBuffer buffer, byte b);

	public void testReadFile() {
		
		StringBuffer actual = new StringBuffer();
		
		for (byte b = reader.next(); b != -1; b = reader.next()) {
			add(actual, b);
		}
		
		assertEquals(fixture.getContent(), actual.toString());
		
	}
	
	public void testRestart() {
		
		while (reader.next() != -1);

		reader.restart();
		
		StringBuffer actual = new StringBuffer();
		
		for (byte b = reader.next(); b != -1; b = reader.next()) {
			add(actual, b);
		}
		
		assertEquals(fixture.getContent(), actual.toString());
	}
	
	public void testUnget() {
		
		byte b = reader.next();
		
		if (b != -1) {

			for (int i = 0; i < 10; i++) {
				reader.unget();
				assertEquals(b, reader.next());
			}
			
		}

		b = reader.next();
		b = reader.next();
		b = reader.next();
		
		if (b != -1) {

			for (int i = 0; i < 10; i++) {
				reader.unget();
				assertEquals(b, reader.next());
			}
			
		}
		
	}
	
	public void testAtEnd() {
		
		while (reader.next() != -1);

		assertEquals(-1, reader.next());
		assertEquals(-1, reader.next());
		assertEquals(-1, reader.next());
		assertEquals(-1, reader.next());
		
	}
	
	public void testUngetAtEnd() {

		while (reader.next() != -1);

		for (int i = 0; i < 10; i++) {
		
			reader.unget();
		
			assertEquals(-1, reader.next());
		
		}
				
	}
	
	public void testWindow() {
	
		StringBuffer actual = new StringBuffer();

		reader.mark();
		
		for (int i = 0; i < 10; i++) {
		
			byte b = reader.next();
			
			if (b == -1) break;
			
			add(actual, b);
			
		}
		
		assertEquals(actual.toString(), reader.windowString());
		
	}
	
	protected void setUp() throws Exception {
		fixture.setUp();
	}

	protected void tearDown() throws Exception {
		fixture.tearDown();
	}
	
	public ReaderTest(IteratorFixture fixture, String name) {
		
		super(name);
		
		this.fixture = fixture;
		
	}
	
	Reader reader;
	IteratorFixture fixture;
	
}
