package com.ap.io;

import com.ap.io.text.Buffer;

public class BackwardReader implements Reader {
	
	public BackwardReader(Buffer buffer) {
		
		this.buffer = buffer;
		
		restart();
				
	}
	
	public void restart() {
		
		buffer.filePos = buffer.fileLength - buffer.bufferLength;
		
		if (buffer.filePos < 0) buffer.filePos  = 0;

		buffer.read();

		buffer.windowPosition = buffer.bufFill - 1;
		
	}

	public void mark() {
		buffer.windowStart = buffer.windowPosition;
	}

	public byte next() {
		
		if (isEnd()) return -1;
		
		buffer.windowPosition--;
		
		if (isEmpty()) preload();
		
		byte b = buffer.buffer[buffer.windowPosition];
		
		if (b == 0) return -1;
		
		return b;

	}

	public void unget() {
		buffer.windowPosition++;		
	}

	public String windowString() {
		
		if (isEmpty()) preload();

		return new String(buffer.buffer, buffer.windowPosition, 
				          buffer.windowStart - buffer.windowPosition + 1);
		
	}

	public String windowString(int length) {
		
		if (isEmpty()) preload();

		return new String(buffer.buffer, buffer.windowPosition, length);
		
	}
	
	private boolean isEnd() {
		return buffer.windowPosition == 0 && 
		       buffer.buffer[buffer.windowPosition] == 0;
	}

	private boolean isEmpty() {
		return buffer.windowPosition == 0 && 
		       buffer.buffer[buffer.windowPosition] != 0;
	}

	private int preload() {
		
		if (buffer.windowStart >= buffer.bufferLength - 1) buffer.increaseBuffer();

		long oldPos = buffer.filePos;
		
		buffer.filePos -= buffer.bufferLength - (buffer.windowStart + 1);
		
		if (buffer.filePos < 0) buffer.filePos = 0;
		
		int offset = (int) (oldPos - buffer.filePos);

		buffer.read();

		if (buffer.buffer[0] == 0) offset++;

		buffer.windowPosition += offset;
		buffer.windowStart += offset;
			
		return offset;

	}

	Buffer buffer;
	
}
