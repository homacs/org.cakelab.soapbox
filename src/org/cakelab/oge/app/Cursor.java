package org.cakelab.oge.app;

import java.nio.DoubleBuffer;
import static org.lwjgl.glfw.GLFW.glfwGetCursorPos;

public class Cursor {
	DoubleBuffer xBuf = DoubleBuffer.allocate(1);
	DoubleBuffer yBuf = DoubleBuffer.allocate(1);

	private double x;
	private double y;

	
	private double lastUpdate = 0;
	
	void update(long window) {
		if (lastUpdate < GlobalClock.frameTime) {
			glfwGetCursorPos(window, xBuf, yBuf);
			x = xBuf.get();
			y = yBuf.get();
			lastUpdate = GlobalClock.frameTime;
		}
	}
	
	
	public Cursor(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

}
