package com.ap.io.text;

import com.ap.io.FilePosition;

public interface LineIterator {

	void beforeFirst();
	void afterLast();
	
	String next();
	String previous();

	/**
	 * Returns an object that represents the last retrieved element, by using 
	 * {@link next} or {@link previous}.
	 * <p>
	 * Returns null if no last position exists.
	 *  
	 * @return last retrieved element or null if none was retrieved 
	 */
	FilePosition lastPosition();

	/**
	 * Sets the iterator position so that next call to <tt>next</tt> returns the
	 * line identified by the given position object
	 * 
	 * @param position the line position to go to
	 */
	void setNext(FilePosition position);

	/**
	 * Sets the iterator position so that next call to <tt>previous</tt> returns the
	 * line identified by the given position object
	 * 
	 * @param position the line position to go to
	 */
	void setPrevious(FilePosition position);
	
	/**
	 * Closes the iteratror so that resources can be disposed
	 *
	 */
	void close();
	
}
