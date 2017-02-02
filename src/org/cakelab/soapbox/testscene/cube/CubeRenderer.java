package org.cakelab.soapbox.testscene.cube;

import static org.lwjgl.opengl.GL11.glDrawArrays;

import org.cakelab.oge.app.ApplicationContext;
import org.cakelab.oge.opengl.BufferObject.Usage;
import org.cakelab.oge.opengl.MeshVertexArray;
import org.cakelab.oge.scene.VisualEntity;
import org.cakelab.oge.shader.FragmentShader;
import org.cakelab.oge.shader.GLException;
import org.cakelab.oge.shader.Program;
import org.cakelab.oge.shader.VertexShader;
import org.cakelab.oge.utils.OGEMeshRenderData;
import org.cakelab.oge.utils.SingleProgramRendererBase;
import org.cakelab.soapbox.model.Mesh;
import org.cakelab.soapbox.model.TriangleMesh;

public class CubeRenderer extends SingleProgramRendererBase {
	private TriangleMesh mesh;
	private OGEMeshRenderData assets;

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
		
		mesh = new TriangleMesh(Mesh.FrontFaceVertexOrder.Clockwise, 3, new float[] {
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
		}, -1, -1);

		assets = new OGEMeshRenderData(new MeshVertexArray(mesh, 0, Usage.STATIC_DRAW), mesh.getGlDrawingMethod(), mesh.getNumVertices());

	}

	public void delete() {
		assets.delete();
		super.delete();
	}

	
	@Override
	public void prepareRenderPass(ApplicationContext context, double currentTime) {
		assets.bind();
	}

	public void draw(double currentTime, VisualEntity cube) {
		glDrawArrays(assets.getDrawingMethod(), 0, assets.getNumVertices());
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
