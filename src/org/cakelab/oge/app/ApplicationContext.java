package org.cakelab.oge.app;

import java.util.ArrayList;

import org.cakelab.oge.Camera;
import org.cakelab.oge.scene.LightSource;
import org.cakelab.oge.utils.BufferedMatrix4f;
import org.lwjgl.opengl.GLCapabilities;

// TODO context is window and/or thread specific
public class ApplicationContext {
	private Camera camera;
	private BufferedMatrix4f projection;
	private ArrayList<LightSource> lamps;
	private Info info;
	private GLCapabilities capabilities;
	private long window;

	
	public ApplicationContext(long window, GLCapabilities capabilities, Info info) {
		this.window = window;
		this.capabilities = capabilities;
		this.info = info;
	}
	
	public void setActiveCamera(Camera camera) {
		this.camera = camera;
	}

	public Camera getActiveCamera() {
		return camera;
	}

	public void setProjectionTransform(BufferedMatrix4f projection) {
		this.projection = projection;
	}

	public BufferedMatrix4f getProjectionTransform() {
		return this.projection;
	}

	public void setActiveLamps(ArrayList<LightSource> lamps) {
		this.lamps = lamps;
	}

	public ArrayList<LightSource> getActiveLamps() {
		return lamps;
	}

	public int getWindowWidth() {
		return info.getWindowWidth();
	}

	public int getWindowHeight() {
		return info.getWindowHeight();
	}

	public GLCapabilities getCapabilities() {
		return capabilities;
	}

	public Info getApplicationInfo() {
		return info;
	}

	public long getWindow() {
		return window;
	}

}
