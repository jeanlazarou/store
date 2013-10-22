package com.ap.io;

public class ForwardReaderTest extends ReaderTest {

	public void add(StringBuffer buffer, byte b) {
		buffer.append((char) b);
	}
	
	protected void setUp() throws Exception {
		reader = fixture.getForwardReader();
		super.setUp();
	}
	
	public ForwardReaderTest(IteratorFixture fixture, String name) {
		super(fixture, name);
	}
	
}
