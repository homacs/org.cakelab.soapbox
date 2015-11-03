package org.cakelab.oge;

import static org.lwjgl.opengl.GL11.GL_COLOR;
import static org.lwjgl.opengl.GL11.GL_DEPTH;
import static org.lwjgl.opengl.GL11.glViewport;

import org.cakelab.oge.utils.BufferedMatrix4f;
import org.cakelab.oge.utils.GLAPIHelper;
import org.lwjgl.opengl.GLContext;

public class GraphicContext {

	private static final int DEFAULT_FRAME_BUFFER = 0;
	private Camera camera;
	GLContext glContext;
	private BufferedMatrix4f projection;

	public GraphicContext(GLContext glContext) {
		this.glContext = glContext;
	}
	
	public void setActiveCamera(Camera camera) {
		this.camera = camera;
	}

	public Camera getActiveCamera() {
		return camera;
	}

	public void setViewport(int x, int y, int width, int height) {
		glViewport(x, y, width, height);
	}

	public void clearRGBA(float r, float g, float b, float a) {
		GLAPIHelper.glClearBuffer4f(GL_COLOR, DEFAULT_FRAME_BUFFER, r, g, b, a); // green
	}

	public void clearDepth(float depth) {
		GLAPIHelper.glClearBuffer1f(GL_DEPTH, 0, depth);
	}

	public void setProjectionTransform(BufferedMatrix4f projection) {
		this.projection = projection;
	}

	public BufferedMatrix4f getProjectionTransform() {
		return this.projection;
	}


}
