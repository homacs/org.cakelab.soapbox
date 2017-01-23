package org.cakelab.oge.app;

import org.joml.Vector2f;

public class Info {
	public String title;
	public int windowWidth;
	public int windowHeight;
	public int majorVersion;
	public int minorVersion;

	public static class Flags {
		public boolean fullscreen;
		public boolean vsync;
		public boolean cursor;
		public boolean stereo;
		public boolean debug;
		public int fps;
		public boolean softwareThrottle;
		public int samples;
	}

	public Flags settings = new Flags();
	private Vector2f center = new Vector2f();

	public int getWindowWidth() {
		return windowWidth;
	}

	public void setWindowWidth(int windowWidth) {
		this.windowWidth = windowWidth;
		this.center.x = (float) ((((float)windowWidth) +0.5)/2);
	}

	public int getWindowHeight() {
		return windowHeight;
	}

	public void setWindowHeight(int windowHeight) {
		this.windowHeight = windowHeight;
		this.center.y = (float) ((((float)windowHeight) +0.5)/2);
	}

	public Vector2f getCenter() {
		return center;
	}

}
