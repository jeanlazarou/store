package com.ap.io.text;

import com.ap.io.LineTooLongException;
import com.ap.io.RandomAccessStore;

public class Buffer {

	public Buffer(RandomAccessStore in, int bufferSize) {
		
		this.in = in;
		this.fileLength = in.length();
		this.buffer = newBuffer(bufferSize);
		
	}

	public void read() {
		
		in.seek(filePos);
		
		buffer[0] = 0;
		buffer[1] = 0;

		if (filePos == 0) {
			bufFill = in.read(buffer, 1, bufferLength) + 1;
		} else {
			bufFill = in.read(buffer, 0, bufferLength);
		}
		
		if (bufFill > 0) {
			
			if (bufFill < bufferLength) {
			
//				byte c = buffer[bufFill - 1];
//			
//				if (c != '\n' && c != '\r') {
//					
//					buffer[bufFill++] = '\n';
//					
//				}
				
				buffer[bufFill++] = 0;
			
			} else {
				buffer[bufFill++] = -1;
			}
			
		} else {
			bufFill = 2;
		}

	}
	
	public void increaseBuffer() {

		if (bufferLength * 2 > MaxBufferSize) {
			throw new LineTooLongException("Maximum size buffer reached:" + MaxBufferSize);
		}
		
		buffer = newBuffer(bufferLength * 2);
		
	}

	public void dumpBuffer() {

		StringBuffer value = new StringBuffer();
		StringBuffer indexes1 = new StringBuffer();
		StringBuffer indexes2 = new StringBuffer();
		
		for (int i = 0; i < bufFill; i++) {
			
			if (buffer[i] == '0') {
			} else if (buffer[i] == '\r') {
				value.append("\\r");
				indexes1.append(" ");
				indexes2.append(" ");
			} else if (buffer[i] == '\n') {
				value.append("\\n");
				indexes1.append(" ");
				indexes2.append(" ");
			} else {
				value.append((char)buffer[i]);
			}

			indexes1.append(i%10);

			if (i%10 == 0) 
				indexes2.append(i/10);
			else
				indexes2.append(' ');

		}
		
		System.err.println("'" + value.toString() + "'");
		System.err.println(" " + indexes1.toString() + " ");
		System.err.println(" " + indexes2.toString() + " ");
		
	}
	
	private byte[] newBuffer(int size) {
		
		bufferLength = size;
		
		return new byte[bufferLength + 3]; // sentinel values + last line break
		
	}
	
	public int windowStart;
	public int windowPosition;
	
	public int bufFill;
	public int bufferLength;
	
	public long filePos;
	public long fileLength;
	
	public byte[] buffer;

	RandomAccessStore in;

	static final int MaxBufferSize = 20000;
	
}
