package com.ap.io.text;

import com.ap.io.Reader;
import com.ap.io.TextFile;
import com.ap.io.FilePosition;
import com.ap.io.BackwardReader;
import com.ap.io.ForwardReader;

public class TextFileIterator implements LineIterator {

	public TextFileIterator(TextFile file, int bufferSize) {
		
		this.file = file;
		
		if (bufferSize <= 0) {
			throw new IllegalArgumentException("Buffer size must a positive and non zero value");
		}
		
		if (bufferSize == 1) bufferSize = 2;

		buffer = new Buffer(file.open(), bufferSize);
		
		forward = new ForwardReader(buffer);
		backward = new BackwardReader(buffer);
		
	}

	public void beforeFirst() {
		forward.restart();
	}
	
	public void afterLast() {
		backward.restart();
	}

	public String next() {

		byte b = forward.next();
		
		forward.mark();
		forward.unget();

		if (b == -1) {
			return null;
		}
				
		locateEndOfLine();
		
		return forward.windowString();

	}

	public String previous() {
		return null;
	}

	public FilePosition lastPosition() {
		return null;
	}

	public void setNext(FilePosition position) {
	}

	public void setPrevious(FilePosition position) {
	}

	public void close() {
		buffer.in.close();
	}

	private void locateEndOfLine() {

		for (byte b = forward.next(); b != 0; b = forward.next()) {
			
			if (b == '\r') {
				
				b = forward.next();
				
				if (b != '\n') {
					forward.unget();
				}
				
				break;
				
			} else if (b == '\n') {
				break;
			} 
			
		}
		
	}

	TextFile file;
	
	Buffer buffer;
	Reader forward;
	Reader backward;

}
