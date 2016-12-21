package org.cakelab.oge.utils;

import static org.lwjgl.opengl.GL20.*;


import org.cakelab.oge.Camera;
import org.cakelab.oge.Renderer;
import org.cakelab.oge.app.ApplicationContext;
import org.cakelab.oge.scene.VisualObject;
import org.cakelab.oge.shader.GLException;
import org.cakelab.oge.shader.Program;

public abstract class SingleProgramRendererBase implements Renderer {
	protected Program shaderProgram;

	private BufferedMatrix4f mv_matrix = new BufferedMatrix4f();
	private int uniform_mv_matrix;
	private int uniform_proj_matrix;

	private double preparedLast;
	
	public void setShaderProgram(Program program) throws GLException {
		shaderProgram = program;
		uniform_mv_matrix = shaderProgram.getUniformLocation("mv_matrix");
		uniform_proj_matrix = shaderProgram.getUniformLocation("proj_matrix");
	}

	public void delete() {
		shaderProgram.delete();
	}

	public Program getProgram() {
		return shaderProgram;
	}

	public int getProjectionMatrixUniform() {
		return uniform_proj_matrix;
	}

	/**
	 * This method is supposed to contain code that needs to be executed
	 * exactly once per render pass. That means in case there are multiple
	 * objects to be rendered by a particular renderer, contents of this 
	 * method is executed once while the {@link #render(ApplicationContext, double, VisualObject)}
	 * is executed for each object.
	 * 
	 * This method calls prepareRenderPass exactly once for each render pass.
	 */
	public void prepare(ApplicationContext context, double currentTime) {
		if (preparedLast != currentTime) {
			glUseProgram(shaderProgram.getProgramId());
			glUniformMatrix4fv(uniform_proj_matrix, false, context.getProjectionTransform().getFloatBuffer());
			prepareRenderPass(context, currentTime);
			preparedLast = currentTime;
		}
	}

	
	public abstract void prepareRenderPass(ApplicationContext context, double currentTime);

	public void render(ApplicationContext context, double currentTime, VisualObject vobj) {
		Camera camera = context.getActiveCamera();
		mv_matrix.identity()
			.mul(camera.matrices.getViewTransform())
			.mul(vobj.matrices.getWorldTransform())
		;
		
		glUniformMatrix4fv(uniform_mv_matrix, false, mv_matrix.getFloatBuffer());
		draw(currentTime, vobj);
	}

	public abstract void draw(double currentTime, VisualObject vobj);

	public abstract boolean needsNormals();

}
