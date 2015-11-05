package org.cakelab.oge;

import static org.lwjgl.opengl.GL20.glUniformMatrix4;
import static org.lwjgl.opengl.GL20.glUseProgram;

import org.cakelab.oge.shader.Program;
import org.cakelab.oge.utils.BufferedMatrix4f;

public abstract class Renderer {
	protected Program shaderProgram;

	private BufferedMatrix4f modelViewTransform = new BufferedMatrix4f();
	private int u_ModelViewTransform;
	private int u_projectionTransform;

	private double preparedLast;
	
	public void setShaderProgram(Program program) {
		shaderProgram = program;
		u_ModelViewTransform = shaderProgram.getUniformLocation("mv_matrix");
		u_projectionTransform = shaderProgram.getUniformLocation("proj_matrix");
	}

	public void delete() {
		shaderProgram.delete();
	}

	public Program getProgram() {
		return shaderProgram;
	}

	public int getProjectionMatrixUniform() {
		return u_projectionTransform;
	}

	/**
	 * This method is supposed to contain code that needs to be executed
	 * exactly once per render pass. That means in case there are multiple
	 * objects to be rendered by a particular renderer, contents of this 
	 * method is executed once while the {@link #render(GraphicContext, double, VisualObject)}
	 * is executed for each object.
	 * 
	 * This method calls prepareRenderPass exactly once for each render pass.
	 */
	public void prepare(GraphicContext context, double currentTime) {
		if (preparedLast != currentTime) {
			glUseProgram(shaderProgram.getProgramId());
			glUniformMatrix4(u_projectionTransform, false, context.getProjectionTransform().getFloatBuffer());
			prepareRenderPass(context, currentTime);
			preparedLast = currentTime;
		}
	}

	
	protected abstract void prepareRenderPass(GraphicContext context, double currentTime);

	public void render(GraphicContext context, double currentTime, VisualObject vobj) {
		Camera camera = context.getActiveCamera();
		modelViewTransform.identity()
			.mul(camera.getViewTransform())
			.mul(vobj.getWorldTransform())
		;
		
		glUniformMatrix4(u_ModelViewTransform, false, modelViewTransform.getFloatBuffer());
		draw(currentTime, vobj);
	}

	protected abstract void draw(double currentTime, VisualObject vobj);

}
