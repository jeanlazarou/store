package com.ap.io;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

public class StringRandomContentProvider implements RandomContentProvider {

	public StringRandomContentProvider(String content) {
		this.content = content;
	}
	
	/**
	 * Linebreak character sequence used is the default platform sequence
	 * 
	 * @param lines must contain the lines of the file (strings)
	 * @param linebreakAtLast if true last line has a line break
	 */
	public StringRandomContentProvider(List lines, boolean linebreakAtLast) {

		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		
		for (int i = 0; i < lines.size() - 1; i++) {
			pw.println(lines.get(i));
		}
		
		if (lines.size() > 0) {
			
			if (linebreakAtLast) {
				pw.println(lines.get(lines.size() - 1));
			} else {
				pw.print(lines.get(lines.size() - 1));
			}
			
		}

		content = sw.toString();
		
	}

	/**
	 * @param lines must contain the lines of the file (strings)
	 * @param linebreakAtLast if true last line has a line break
	 * @param linebreak character sequence to use a line separator
	 */
	public StringRandomContentProvider(List lines, boolean linebreakAtLast, String linebreak) {

		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		
		for (int i = 0; i < lines.size() - 1; i++) {
			pw.print(lines.get(i));
			pw.print(linebreak);
		}
		
		if (lines.size() > 0) {
			
			pw.print(lines.get(lines.size() - 1));

			if (linebreakAtLast) {
				pw.print(linebreak);
			}
			
		}

		content = sw.toString();
				
	}
	
	public RandomAccessStore open() {

		return new RandomAccessStore() {

			{
				buffer = content.getBytes();
			}
			
			public long length() {
				return content.length();
			}

			public long getFilePointer() {
				return currentPos;
			}
			
			public void seek(long pos) {
				currentPos = (int) pos;
			}

			public int read(byte[] b) {

				if (currentPos >= buffer.length) {
					return -1;
				}
				
				int length = b.length;
				
				if (currentPos + b.length > buffer.length) {
					length = buffer.length - currentPos;
				}

				System.arraycopy(buffer, currentPos, b, 0, length); 				
				
				currentPos += length;
				
				return length;
				
			}

			public int read(byte[] b, int off, int len) {

				if (currentPos >= buffer.length) {
					return -1;
				}
				
				int length = len;
				
				if (currentPos + length > buffer.length) {
					length = buffer.length - currentPos;
				}

				System.arraycopy(buffer, currentPos, b, off, length); 				
				
				currentPos += length;
				
				return length;
				
			}
			
			public void close() {
			}
			
			byte[] buffer;
			int currentPos = 0;
		};
	}
	
	String content;

}
