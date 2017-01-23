package org.cakelab.oge.diagnostics.impl;

import java.io.IOException;
import java.util.ArrayList;

import org.cakelab.appbase.log.Log;
import org.cakelab.oge.diagnostics.Channel;
import org.cakelab.oge.diagnostics.Receiver;

/**
 * A diagnostics receiver, which just writes data to application log.
 * (Just as an example).
 * 
 * @author homac
 */
@SuppressWarnings("serial")
public class LogForwardReceiverImpl extends ArrayList<Channel> implements Receiver {

	@Override
	public Channel openChannel(String name) {
		Channel channel = new ChannelImpl(name){

			@Override
			public void log(double value) {
				Log.info(name + ": " + value);
			}

			@Override
			public void log(double timestamp, double value) {
				Log.info("name: " + timestamp + " " + value);
			}

			@Override
			public void close() throws IOException {
				LogForwardReceiverImpl.this.remove(this);
			}
			
		};
		add(channel);
		return channel;
	}
	
}
