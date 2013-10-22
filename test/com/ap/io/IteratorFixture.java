package com.ap.io;

import java.util.List;

import com.ap.io.text.Buffer;
import com.ap.io.text.LineIterator;
import com.ap.io.text.TextFileIterator;

public class IteratorFixture {

	/**
	 * All the line in the text file content end with a line break
	 * 
	 * @param lines must contain the lines of the file (strings)
	 * @param bufferSize buffer size used to read data from the text file
	 */
	public IteratorFixture(List lines, int bufferSize) {
		this(lines, bufferSize, true);
	}

	/**
	 * @param lines must contain the lines of the file (strings)
	 * @param bufferSize buffer size used to read data from the text file
	 * @param linebreakAtLast if true last line has a line break
	 */
	public IteratorFixture(List lines, int bufferSize, boolean linebreakAtLast) {
		
		this.bufferSize = bufferSize;
				
		provider = new StringRandomContentProvider(lines, linebreakAtLast);
		
		this.file = new TextFile(provider);
		this.lines = lines;
		
	}
	
	public List getList() {
		return lines;
	}

	public LineIterator head() throws Exception {

		TextFile.setBufferSize(bufferSize);
		
		return file.head();
		
	}

	public LineIterator tail() throws Exception {

		TextFile.setBufferSize(bufferSize);
		
		return file.tail();
		
	}
	
	public Reader getForwardReader() {
		
		Buffer buffer = new Buffer(file.open(), bufferSize);
		
		return new ForwardReader(buffer);
		
	}
	
	public Reader getBackwardReader() {
		
		Buffer buffer = new Buffer(file.open(), bufferSize);
		
		return new BackwardReader(buffer);
		
	}
	
	public LineIterator getIterator() {
		return new TextFileIterator(file, bufferSize);
	}
	
	public String getContent() {
		return provider.content;
	}
	
	public void setUp() throws Exception {
	}
	
	public void tearDown() throws Exception {
	}

	List lines;
	TextFile file;
	
	int bufferSize;

	StringRandomContentProvider provider;
}
