package org.cakelab.soapbox.testscene.blenderRaw;

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

import java.util.HashMap;

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

public class BlenderRenderer extends Renderer {
	// TODO: don't associate the mesh data with the object (waste of memory)
	public class RenderData {

		public int vao;
		public int vertex_buffer;
		private int numVectors;
		private int drawMethod;
		public int vectorSize;

		public RenderData(TriangleMesh mesh) {
			if (mesh instanceof TriangleMesh) {
				drawMethod = GL_TRIANGLES;
				vectorSize = 3;
				numVectors = mesh.getNumVertices();
			}
		}

	}

	private static BlenderRenderer SINGLETON;

	private HashMap<Mesh, RenderData> renderData = new HashMap<Mesh, RenderData>();
	
	
	public static BlenderRenderer getInstance() {
		return SINGLETON ;
	}

	
	
	public BlenderRenderer() throws GLException {
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
		
		SINGLETON = this;
	}

	public void delete() {
		for (RenderData data : renderData.values()) {
			glDeleteBuffers(data.vertex_buffer);
			glDeleteVertexArrays(data.vao);
		}
		super.delete();
	}

	
	@Override
	public void prepare(GraphicContext context, double currentTime) {
		super.prepare(context, currentTime);
		
	}

	public void draw(double currentTime, VisualObject vobj) {
		BlenderObject bobj = (BlenderObject) vobj;
		RenderData data = renderData.get(bobj.getMesh());
		glBindVertexArray(data.vao);
		glBindBuffer(GL_ARRAY_BUFFER, data.vertex_buffer);
		glVertexAttribPointer(0, data.vectorSize, GL_FLOAT, false, 0, MemoryUtil.NULL); 
        glEnableVertexAttribArray(0);
		glDrawArrays(data.drawMethod, 0, data.numVectors);
	}
	
	public void registerMesh(TriangleMesh mesh) {
		if (!renderData.containsKey(mesh)) {
			RenderData data = new RenderData(mesh);
			data.vao = glGenVertexArrays();
			glBindVertexArray(data.vao);
			data.vertex_buffer = glGenBuffers();
			glBindBuffer(GL_ARRAY_BUFFER, data.vertex_buffer);
			glBufferData(GL_ARRAY_BUFFER, mesh.getFloatBuffer(), GL_STATIC_DRAW);

			glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, MemoryUtil.NULL); 
	        glEnableVertexAttribArray(0);
	        renderData.put(mesh, data);
		}
	}


}
