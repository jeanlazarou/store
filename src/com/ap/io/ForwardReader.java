package com.ap.io;

import com.ap.io.text.Buffer;

public class ForwardReader implements Reader {

	public ForwardReader(Buffer buffer) {
		
		this.buffer = buffer;
		
		restart();
		
	}
	
	public void restart() {

		buffer.filePos = 0;

		buffer.read();
		
		buffer.windowPosition = 0;
		
		mark();

	}

	public void mark() {
		buffer.windowStart = buffer.windowPosition;
	}

	public byte next() {
		
		if (isEnd()) return -1;
		
		buffer.windowPosition++;
		
		if (isEmpty()) preload();
		
		byte b = buffer.buffer[buffer.windowPosition];
		
		if (b == 0) return -1;
		
		return b;
		
	}

	public void unget() {
		buffer.windowPosition--;
	}

	public String windowString() {
		
		if (isEmpty()) preload();

		int offset = buffer.windowStart;
		int length = buffer.windowPosition - offset + 1;
		
		if (buffer.buffer[offset] == 0) {
			offset++;
			length--;
		}
		
		return new String(buffer.buffer, offset, length);
		
	}
	
	public String windowString(int length) {
		
		if (isEmpty()) preload();

		return new String(buffer.buffer, buffer.windowStart, length);
		
	}

	private boolean isEnd() {
		return buffer.windowPosition == buffer.bufFill - 1 && 
			   buffer.buffer[buffer.windowPosition] == 0;
	}
	
	private boolean isEmpty() {
		return buffer.windowPosition == buffer.bufFill - 1 && 
			   buffer.buffer[buffer.windowPosition] != 0;
	}

	private int preload() {

		if (buffer.windowStart == 0 && buffer.buffer[0] == 0) {
			buffer.increaseBuffer();
			buffer.read();
			
			return 0;
		}
			
		int offset = buffer.windowStart;
		
		boolean wasBOF = buffer.buffer[0] == 0;
		
		if (wasBOF) offset--;
		
		if (buffer.windowStart < 2) buffer.increaseBuffer();
		
		buffer.filePos += offset;
		
		buffer.read();

		if (wasBOF && buffer.buffer[0] != 0) offset++;
		
		buffer.windowPosition -= offset;
		buffer.windowStart -= offset;
			
		return offset;

	}

	Buffer buffer;
	
}
