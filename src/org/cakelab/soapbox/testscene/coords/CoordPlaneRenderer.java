package org.cakelab.soapbox.testscene.coords;

import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import java.io.IOException;

import org.cakelab.oge.app.ApplicationContext;
import org.cakelab.oge.scene.VisualEntity;
import org.cakelab.oge.shader.FragmentShader;
import org.cakelab.oge.shader.GLException;
import org.cakelab.oge.shader.Program;
import org.cakelab.oge.shader.VertexShader;
import org.cakelab.oge.utils.SingleProgramRendererBase;
import org.cakelab.soapbox.testscene.coords.resources.CoordPlaneResources;


public class CoordPlaneRenderer extends SingleProgramRendererBase {


	private int vao;

	public CoordPlaneRenderer () throws GLException, IOException {
		
		loadModel();
		
		VertexShader vertexShader = new VertexShader("CoordPlane Vertex Shader", CoordPlaneResources.asInputStream(CoordPlaneResources.VERTEX_SHADER));
		FragmentShader fragmentShader = new FragmentShader("CoordPlane Fragment Shader", CoordPlaneResources.asInputStream(CoordPlaneResources.FRAGMENT_SHADER));

		setShaderProgram(new Program("CoordPlane Shader Program", vertexShader, fragmentShader));
		
		vertexShader.delete();
		fragmentShader.delete();
	}

	private void loadModel() {
		vao = glGenVertexArrays();
	}

	
	
	@Override
	public void prepareRenderPass(ApplicationContext context, double currentTime) {
		glBindVertexArray(vao);
	}

	@Override
	public void draw(double currentTime, VisualEntity cube) {
		// Draw 3 lines (i.e. 6 vertices)
        glDrawArrays(GL_LINES, 0, 6);
	}

	@Override
	public void delete() {
		glDeleteVertexArrays(vao);
		super.delete();
	}

	@Override
	public boolean needsNormals() {
		return false;
	}

	@Override
	public boolean needsUv() {
		return false;
	}

	
}
