package com.ap.io;

public class BackwardReaderTest extends ReaderTest {

	public void add(StringBuffer buffer, byte b) {
		buffer.insert(0, (char) b);
	}
	
	protected void setUp() throws Exception {
		super.setUp();
		reader = fixture.getBackwardReader();
	}
	
	public BackwardReaderTest(IteratorFixture fixture, String name) {
		super(fixture, name);
	}
	
}
