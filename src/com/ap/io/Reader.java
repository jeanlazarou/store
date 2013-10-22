package com.ap.io;

/**
 * The <code>Reader</code> interface should be implemented by any
 * class that is able to provide a <tt>byte</tt> sequence, one byte at a time.
 * 
 * @author jea-laz
 */
public interface Reader {
	
	/**
	 * Sets the reader at the beginning of the bytes sequence, a mark
	 * is also set.
	 */
	void restart();
	
	/**
	 * Sets a mark to the current position.
	 */
	void mark();

	/**
	 * Returns the next available byte or -1 if the end was reached.
	 * 
	 * @return the next <tt>byte</tt> or -1 when at the end of the bytes flow
	 */
	byte next();
	
	/**
	 * Sets the reader so that next call to <tt>next</tt> returns the
	 * same value.
	 * <p>
	 * The methods should be called only once, the reader is not to
	 * complain about or prevent from behaving in a wrong way
	 * <p>
	 * If the end of the bytes was reached ungetting will return -1 
	 *
	 */
	void unget();

	/**
	 * Returns a string containing the characters starting at the last
	 * mark set and the current position
	 * 
	 * @return the next <tt>byte</tt> or -1 when at the end of the bytes flow
	 */
	String windowString();

	/**
	 * Returns a string containing the characters starting at the last
	 * mark set with the given length
	 * 
	 * @param length number of characters to retrieve
	 * @return the next <tt>byte</tt> or -1 when at the end of the bytes flow
	 */
	String windowString(int length);
	
}
