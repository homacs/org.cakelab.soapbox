package org.cakelab.soapbox.testscene.cube;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import org.cakelab.oge.GraphicContext;
import org.cakelab.oge.Renderer;
import org.cakelab.oge.VisualObject;
import org.cakelab.oge.shader.FragmentShader;
import org.cakelab.oge.shader.GLException;
import org.cakelab.oge.shader.Program;
import org.cakelab.oge.shader.VertexShader;
import org.cakelab.soapbox.model.Mesh;
import org.cakelab.soapbox.model.TriangleMesh;
import org.lwjgl.system.MemoryUtil;

public class CubeRenderer extends Renderer {
	private int vao;
	private int vertex_buffer;

	public CubeRenderer() throws GLException {
		String vs_source = "#version 410 core                                                  \n"
				+ "                                                                   \n"
				+ "in vec4 position;                                                  \n"
				+ "                                                                   \n"
				+ "out VS_OUT                                                         \n"
				+ "{                                                                  \n"
				+ "    vec4 color;                                                    \n"
				+ "} vs_out;                                                          \n"
				+ "                                                                   \n"
				+ "uniform mat4 mv_matrix;                                            \n"
				+ "uniform mat4 proj_matrix;                                          \n"
				+ "                                                                   \n"
				+ "void main(void)                                                    \n"
				+ "{                                                                  \n"
				+ "    gl_Position = proj_matrix * mv_matrix * position;              \n"
				+ "    vs_out.color = position * 2.0 + vec4(0.5, 0.5, 0.5, 0.0);      \n"
				+ "}                                                                  \n";
		String fs_source = "#version 410 core                                                  \n"
				+ "                                                                   \n"
				+ "out vec4 color;                                                    \n"
				+ "                                                                   \n"
				+ "in VS_OUT                                                          \n"
				+ "{                                                                  \n"
				+ "    vec4 color;                                                    \n"
				+ "} fs_in;                                                           \n"
				+ "                                                                   \n"
				+ "void main(void)                                                    \n"
				+ "{                                                                  \n"
				+ "    color = fs_in.color;                                           \n"
				+ "}                                                                  \n";

		VertexShader vertexShader = new VertexShader("Cube Vertex Shader", vs_source);
		FragmentShader fragmentShader = new FragmentShader("Cube Fragment Shader", fs_source);

		setShaderProgram(new Program("Cube Shader Program", vertexShader, fragmentShader));
		
		vertexShader.delete();
		fragmentShader.delete();
		
		vao = glGenVertexArrays();
		glBindVertexArray(vao);

		TriangleMesh mesh = new TriangleMesh(Mesh.FrontFace.Clockwise, 3, new float[] {
	            -0.25f,  0.25f, -0.25f,
	            -0.25f, -0.25f, -0.25f,
	             0.25f, -0.25f, -0.25f,

	             0.25f, -0.25f, -0.25f,
	             0.25f,  0.25f, -0.25f,
	            -0.25f,  0.25f, -0.25f,

	             0.25f, -0.25f, -0.25f,
	             0.25f, -0.25f,  0.25f,
	             0.25f,  0.25f, -0.25f,

	             0.25f, -0.25f,  0.25f,
	             0.25f,  0.25f,  0.25f,
	             0.25f,  0.25f, -0.25f,

	             0.25f, -0.25f,  0.25f,
	            -0.25f, -0.25f,  0.25f,
	             0.25f,  0.25f,  0.25f,

	            -0.25f, -0.25f,  0.25f,
	            -0.25f,  0.25f,  0.25f,
	             0.25f,  0.25f,  0.25f,

	            -0.25f, -0.25f,  0.25f,
	            -0.25f, -0.25f, -0.25f,
	            -0.25f,  0.25f,  0.25f,

	            -0.25f, -0.25f, -0.25f,
	            -0.25f,  0.25f, -0.25f,
	            -0.25f,  0.25f,  0.25f,

	            -0.25f, -0.25f,  0.25f,
	             0.25f, -0.25f,  0.25f,
	             0.25f, -0.25f, -0.25f,

	             0.25f, -0.25f, -0.25f,
	            -0.25f, -0.25f, -0.25f,
	            -0.25f, -0.25f,  0.25f,

	            -0.25f,  0.25f, -0.25f,
	             0.25f,  0.25f, -0.25f,
	             0.25f,  0.25f,  0.25f,

	             0.25f,  0.25f,  0.25f,
	            -0.25f,  0.25f,  0.25f,
	            -0.25f,  0.25f, -0.25f
		});
		
		vertex_buffer = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vertex_buffer);
		glBufferData(GL_ARRAY_BUFFER, mesh.getFloatBuffer(), GL_STATIC_DRAW);

		glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, MemoryUtil.NULL); 
        glEnableVertexAttribArray(0);
	}

	public void delete() {
		glDeleteBuffers(vertex_buffer);
		glDeleteVertexArrays(vao);
		super.delete();
	}

	
	@Override
	public void prepare(GraphicContext context, double currentTime) {
		super.prepare(context, currentTime);
		glBindVertexArray(vao);
		glBindBuffer(GL_ARRAY_BUFFER, vertex_buffer);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, MemoryUtil.NULL); 
        glEnableVertexAttribArray(0);
	}

	public void draw(double currentTime, VisualObject cube) {
		glDrawArrays(GL_TRIANGLES, 0, 36);
	}
}
