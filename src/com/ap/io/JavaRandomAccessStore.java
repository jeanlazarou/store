package com.ap.io;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class JavaRandomAccessStore implements RandomAccessStore {

	public JavaRandomAccessStore(String name, String mode)  {
		try {
			impl = new RandomAccessFile(name, mode);
		} catch (FileNotFoundException e) {
			throw new StoreException(e);
		}
	}
	
	public long length() {
		try {
			return impl.length();
		} catch (IOException e) {
			throw new StoreException(e);
		}
	}

	public long getFilePointer() {
		try {
			return impl.getFilePointer();
		} catch (IOException e) {
			throw new StoreException(e);
		}
	}
	
	public void seek(long pos) {		
		try {
			impl.seek(pos);
		} catch (IOException e) {
			throw new StoreException(e);
		}
	}

	public int read(byte[] b) {
		try {
			return impl.read(b);
		} catch (IOException e) {
			throw new StoreException(e);
		}
	}

	public int read(byte b[], int off, int len) {
		try {
			return impl.read(b, off, len);
		} catch (IOException e) {
			throw new StoreException(e);
		}
	}

	public void close() {
		try {
			impl.close();
		} catch (IOException e) {
			throw new StoreException(e);
		}
	}
	
	RandomAccessFile impl;

}
