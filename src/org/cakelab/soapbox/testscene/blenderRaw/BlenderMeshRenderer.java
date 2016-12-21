package org.cakelab.soapbox.testscene.blenderRaw;

import static org.lwjgl.opengl.GL11.glDrawArrays;

import org.cakelab.oge.GraphicContext;
import org.cakelab.oge.RenderAssets;
import org.cakelab.oge.Renderer;
import org.cakelab.oge.VisualObject;
import org.cakelab.oge.shader.FragmentShader;
import org.cakelab.oge.shader.GLException;
import org.cakelab.oge.shader.Program;
import org.cakelab.oge.shader.VertexShader;

public class BlenderMeshRenderer extends Renderer {
	private static BlenderMeshRenderer SINGLETON;

	public static BlenderMeshRenderer getInstance() {
		return SINGLETON ;
	}

	
	
	public BlenderMeshRenderer() throws GLException {
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

	@Override
	public void prepareRenderPass(GraphicContext context, double currentTime) {
	}

	public void draw(double currentTime, VisualObject vobj) {
		BlenderObject bobj = (BlenderObject) vobj;
		RenderAssets assets = bobj.getRenderAssets();
		assets.bind();
		glDrawArrays(assets.getDrawingMethod(), 0, assets.getNumVertices());
	}



	@Override
	public boolean needsNormals() {
		return false;
	}
	


}
