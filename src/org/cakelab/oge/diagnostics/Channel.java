package org.cakelab.oge.diagnostics;

import java.io.Closeable;

public interface Channel extends Closeable {
	String getName();
	/** 
	 * supplier logs a new value on this channel 
	 */
	void log(double value);
	
	/**
	 * Supplier logs a new value on this channel with time stamp.
	 * @param timestamp
	 * @param value
	 */
	void log(double timestamp, double value);
	
}

