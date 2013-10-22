package com.ap.io;

import com.ap.io.RandomAccessStore;
import com.ap.io.RandomContentProvider;
import com.ap.io.JavaRandomAccessStore;
import com.ap.io.text.LineIterator;
import com.ap.io.text.TextFileIterator;

public class TextFile {
	
	String textfile;
	RandomContentProvider provider;

	public TextFile(String textfile) {
		
		if (textfile == null) {
			throw new IllegalArgumentException();
		}
		
		this.textfile = textfile;
	}

	public TextFile(RandomContentProvider provider) {
		
		if (provider == null) {
			throw new IllegalArgumentException();
		}
		
		this.provider = provider;
	}
		
	/**
	 * Returns a <tt>TextFileIterator</tt> iterator through the text file, starting 
	 * at the beginning of the file.
	 * 
	 * @return a list iterator of <tt>String</tt>s objects (lines)
	 * @throws StoreException 
	 */
	public LineIterator head() throws StoreException {
		return createIterator(false);
	}
	
	/**
	 * Returns a <tt>TextFileIterator</tt> through the text file, starting at the
	 * end of the file.
	 * 
	 * @return a list iterator of <tt>String</tt>s objects (lines)
	 */
	public LineIterator tail() {
		return createIterator(true);
	}
	
	public RandomAccessStore open() {
		
		if (textfile == null) {
			return provider.open();
		}
		
		return new JavaRandomAccessStore(textfile, "r");
	}
	
	/**
	 * Sets the size of the buffer that is used when reading the files.
	 * 
	 * <p>
	 * Changing this parameter only affects subsequent iterators.
	 * 
	 * @param size the size of the buffer to use when reading the text files
	 */
	public static void setBufferSize(int size) {
		bufferSize = size;
	}

	public String toString() {
		return provider == null ? this.textfile : provider.toString();
	}
	
	private LineIterator createIterator(boolean tail) {
		
		LineIterator it = new TextFileIterator(this, bufferSize);
		
		if (tail)
			it.afterLast();
		else
			it.beforeFirst();
		
		return it;
		
	}
	
	static int bufferSize;
}
