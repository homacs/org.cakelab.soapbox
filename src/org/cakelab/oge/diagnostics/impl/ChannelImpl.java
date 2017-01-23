package org.cakelab.oge.diagnostics.impl;

import org.cakelab.oge.diagnostics.Channel;

public abstract class ChannelImpl implements Channel {

	private String name;

	public ChannelImpl(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}


}
