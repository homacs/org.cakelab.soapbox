package org.cakelab.oge;

import org.cakelab.oge.app.ApplicationContext;
import org.cakelab.oge.module.Module;
import org.cakelab.oge.scene.Scene;
import org.cakelab.oge.scene.VisualEntity;
import org.cakelab.oge.shader.GLException;

public interface RenderEngine extends Module {
	// TODO [5] clarify rendering model (engine, renderer, shader, shader program, render tasks, render data, etc.)
	void setup(Scene scene) throws GLException;

	void setup(VisualEntity entity) throws GLException;
	
	/**
	 * Screen width and height in num pixels.
	 * Field of view in degrees.
	 * @param width Screen width in pixels 
	 * @param height Screen width in pixels
	 * @param fov Field of view in degrees
	 * @throws GLException
	 */
	void setView(int width, int height, float fov) throws GLException;

	void render(ApplicationContext context, double currentTime, Scene scene) throws GLException;

	DebugView getDebugView();

	

}
