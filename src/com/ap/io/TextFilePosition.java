package com.ap.io;

public class TextFilePosition implements FilePosition {

	TextFilePosition(long pos) {
		this.pos = pos;
	}
	
	public boolean equals(Object obj) {
		
		if (this == obj) return true;
		if (obj == null) return false;
		
		if (obj instanceof TextFilePosition) {
			
			TextFilePosition other = (TextFilePosition) obj;
			
			return pos == other.pos;
			
		}
		
		return false;
	}
	
	public int hashCode() {
		return (int) pos;
	}
	
	long pos;

}
