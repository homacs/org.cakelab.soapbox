package org.cakelab.soapbox.testscene.coords;

import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import java.io.IOException;

import org.cakelab.oge.GraphicContext;
import org.cakelab.oge.Renderer;
import org.cakelab.oge.VisualObject;
import org.cakelab.oge.shader.FragmentShader;
import org.cakelab.oge.shader.GLException;
import org.cakelab.oge.shader.Program;
import org.cakelab.oge.shader.VertexShader;
import org.cakelab.soapbox.testscene.coords.resources.CoordPlaneResources;


public class CoordPlaneRenderer extends Renderer {


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
	public void prepare(GraphicContext context, double currentTime) {
		super.prepare(context, currentTime);
		glBindVertexArray(vao);
	}

	@Override
	protected void draw(double currentTime, VisualObject cube) {
		// Draw 3 lines (i.e. 6 vertices)
        glDrawArrays(GL_LINES, 0, 6);
	}

	@Override
	public void delete() {
		glDeleteVertexArrays(vao);
		super.delete();
	}

	
}
