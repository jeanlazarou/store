package com.ap.io;

public interface RandomAccessStore {
	
	long length();
	
	long getFilePointer();
	void seek(long pos);
	
	int read(byte b[]);
	int read(byte b[], int off, int len);
	
	void close();
	
}
