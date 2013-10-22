package com.ap.io;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import junit.framework.AssertionFailedError;

import com.ap.io.text.Buffer;

public class BufferAllocationTest extends TestCase {

	public void testEmptyForward() {

		reader = createForward(empty, 100);
		
		buffer.setExpectedSize(100);
		buffer.setResizeCount(0);

		while(reader.next() != -1);
		
		buffer.verify();

		reader = createForward(empty, 1);
		
		buffer.setExpectedSize(1);
		buffer.setResizeCount(0);

		while(reader.next() != -1);
		
		buffer.verify();

	}

	public void testEmptyBackward() {	
		
		reader = createBackward(empty, 100);
	
		buffer.setExpectedSize(100);
		buffer.setResizeCount(0);
	
		while(reader.next() != -1);
		
		buffer.verify();
		
		reader = createBackward(empty, 1);
	
		buffer.setExpectedSize(1);
		buffer.setResizeCount(0);
	
		while(reader.next() != -1);
		
		buffer.verify();
	
	}

	
	public void testBigBufferForward() {
		
		reader = createForward(fiveLines, 100);

		buffer.setExpectedSize(100);
		buffer.setResizeCount(0);

		while(reader.next() != -1);
		
		buffer.verify();

	}
	
	public void testBigBufferBackward() {	
			
		reader = createBackward(fiveLines, 100);

		buffer.setExpectedSize(100);
		buffer.setResizeCount(0);

		while(reader.next() != -1);
		
		buffer.verify();
		
	}

	public void testNormalBufferForward() {
		
		reader = createForward(fiveLines, 10);

		buffer.setExpectedSize(40);
		buffer.setResizeCount(2);

		while(reader.next() != -1);
		
		buffer.verify();

	}

	public void testNormalBufferBackward() {
		
		reader = createBackward(fiveLines, 10);

		buffer.setExpectedSize(40);
		buffer.setResizeCount(2);

		while(reader.next() != -1);
		
		buffer.verify();

	}

	public void testSmallBufferForward() {
		
		reader = createForward(fiveLines, 3);

		buffer.setExpectedSize(48);
		buffer.setResizeCount(4);

		while(reader.next() != -1);
		
		buffer.verify();

	}

	public void testSmallBufferBackward() {
		
		reader = createBackward(fiveLines, 3);

		buffer.setExpectedSize(48);
		buffer.setResizeCount(4);

		while(reader.next() != -1);
		
		buffer.verify();

	}

	public void testContentForward() {
		
		reader = createForward(threeLines, 3);

		buffer.setExpectedSize(12);
		buffer.setResizeCount(2);

		reader.mark();

		assertEquals( 'l', reader.next());
		assertEquals( 'i', reader.next());
		assertEquals( 'n', reader.next());
		assertEquals( 'e', reader.next());
		assertEquals( ' ', reader.next());
		assertEquals( '0', reader.next());
		assertEquals( '0', reader.next());
		assertEquals( '1', reader.next());
		assertEquals('\n', reader.next());
		
		reader.mark();
		
		assertEquals( '0', reader.next());
		assertEquals( '0', reader.next());
		assertEquals( '2', reader.next());
		assertEquals('\n', reader.next());
		
		reader.mark();
		
		assertEquals( 'l', reader.next());
		assertEquals( 'i', reader.next());
		assertEquals( 'n', reader.next());
		assertEquals( 'e', reader.next());
		assertEquals( ' ', reader.next());
		assertEquals( '3', reader.next());

		assertEquals(  -1, reader.next());
		
		buffer.verify();
		
	}

	public void testControledForward() {
		
		reader = createForward(threeLines, 3);

		buffer.setExpectedSize(12);
		buffer.setResizeCount(2);

		reader.next();
		reader.mark();
		
		for (int i = 0; i < 8; i++) {
			reader.next();
		}
		
		assertEquals("line 001\n", reader.windowString());

		reader.next();
		reader.mark();
		
		for (int i = 0; i < 3; i++) {
			reader.next();
		}
		
		assertEquals("002\n", reader.windowString());

		reader.next();
		reader.mark();
		
		for (int i = 0; i < 5; i++) {
			reader.next();
		}
		
		assertEquals("line 3", reader.windowString());
		
		assertEquals(-1, reader.next());

		buffer.verify();

	}

	public void testContentBackward() {
		
		reader = createBackward(threeLines, 3);

		buffer.setExpectedSize(12);
		buffer.setResizeCount(2);

		reader.mark();

		assertEquals( '3', reader.next());
		assertEquals( ' ', reader.next());
		assertEquals( 'e', reader.next());
		assertEquals( 'n', reader.next());
		assertEquals( 'i', reader.next());
		assertEquals( 'l', reader.next());
		
		reader.mark();
		
		assertEquals('\n', reader.next());
		assertEquals( '2', reader.next());
		assertEquals( '0', reader.next());
		assertEquals( '0', reader.next());

		reader.mark();

		assertEquals('\n', reader.next());
		assertEquals( '1', reader.next());
		assertEquals( '0', reader.next());
		assertEquals( '0', reader.next());
		assertEquals( ' ', reader.next());
		assertEquals( 'e', reader.next());
		assertEquals( 'n', reader.next());
		assertEquals( 'i', reader.next());
		assertEquals( 'l', reader.next());

		assertEquals(  -1, reader.next());
		
		buffer.verify();

	}

	public void testControledBackward() {
		
		reader = createBackward(threeLines, 3);

		buffer.setExpectedSize(12);
		buffer.setResizeCount(2);

		reader.next();
		reader.mark();
		
		for (int i = 0; i < 5; i++) {
			reader.next();
		}
		
		assertEquals("line 3", reader.windowString());

		reader.next();
		reader.mark();
		
		for (int i = 0; i < 3; i++) {
			reader.next();
		}
		
		assertEquals("002\n", reader.windowString());

		reader.next();
		reader.mark();
		
		for (int i = 0; i < 8; i++) {
			reader.next();
		}
		
		assertEquals("line 001\n", reader.windowString());
		
		buffer.verify();

	}

	protected void setUp() throws Exception {
		
		fiveLines = new ArrayList();
		fiveLines.add("line 001");
		fiveLines.add("002");
		fiveLines.add("line 3");
		fiveLines.add("line 004");
		fiveLines.add("005");
		
		threeLines = new ArrayList();
		threeLines.add("line 001");
		threeLines.add("002");
		threeLines.add("line 3");

		empty = new ArrayList();
		
	}
	
	Reader createForward(List lines, int size) {

		RandomContentProvider provider = new StringRandomContentProvider(lines, false, "\n");

		RandomAccessStore in = new TextFile(provider).open();
		
		buffer = new InstrumentedBuffer(in, size);
		
		return new ForwardReader(buffer);
		
	}

	Reader createBackward(List lines, int size) {

		RandomContentProvider provider = new StringRandomContentProvider(lines, false, "\n");

		RandomAccessStore in = new TextFile(provider).open();
		
		buffer = new InstrumentedBuffer(in, size);
				
		return new BackwardReader(buffer);
		
	}
	
	Reader reader;
	InstrumentedBuffer buffer;
	
	List empty, threeLines, fiveLines;
}

class InstrumentedBuffer extends Buffer {

	public InstrumentedBuffer(RandomAccessStore in, int bufferSize) {
		super(in, bufferSize);
	}
	
	public void setExpectedSize(int expectedSize) {
		this.expectedSize = expectedSize;
	}
	
	public void setResizeCount(int expectedResizings) {
		this.count = 0;
		this.expectedResizings = expectedResizings;
	}
	
	public void increaseBuffer() {
				
		if (++count > expectedResizings) {
			throw new AssertionFailedError("Unexpected resizing, maximum allowed is " + expectedResizings + " but was " + count);
		}
		
		super.increaseBuffer();
		
		if (bufferLength > expectedSize) {
			throw new AssertionFailedError("Maximum expected length " + expectedSize + " but was " + bufferLength);
		}
		
	}
	
	public void verify() {

		if (expectedResizings != count) {
			throw new AssertionFailedError("Expected number of resizing " + expectedResizings + " but was " + count);
		}

		if (expectedSize != bufferLength) {
			throw new AssertionFailedError("Expected buffer length " + expectedSize + " but was " + bufferLength);
		}
		
	}
	
	int expectedResizings;
	int expectedSize;
	int count;
	
}
