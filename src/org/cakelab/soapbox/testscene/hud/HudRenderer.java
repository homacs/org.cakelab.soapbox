package org.cakelab.soapbox.testscene.hud;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
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
import org.cakelab.oge.texture.ImageIOTexture;
import org.cakelab.oge.texture.Texture;
import org.cakelab.soapbox.model.Mesh;
import org.cakelab.soapbox.model.TriangleMesh;
import org.cakelab.soapbox.testscene.SBM_KTX.TextureCheckerBoard;
import org.cakelab.soapbox.testscene.SBM_KTX.resources.Resources;
import org.cakelab.soapbox.testscene.hud.resources.HudResources;
import org.lwjgl.opengl.GL11;



public class HudRenderer extends Renderer {
    private int     vao;
	private int     vertex_buffer;
	private Mesh    mesh;
	private Texture texture;

	public HudRenderer() throws GLException, IOException {
		loadShaders();
		boolean png = true;
		if (png) {
			int pixelFormat = GL11.GL_RGBA;
			boolean flipped = true;
			boolean forceAlpha = false;
			texture = new ImageIOTexture(HudResources.asImage(HudResources.TEST_TEXTURE_UNCOMPRESSED), 
					pixelFormat, flipped, forceAlpha, null, GL11.GL_NEAREST, GL11.GL_NEAREST);
		} else {
			texture = new TextureCheckerBoard();
		}
//		float scale = texture.getWidth();
		float scale = 1.0f;
		mesh = new TriangleMesh(Mesh.FrontFaceVertexOrder.Clockwise, 6, new float[] {
	            0f, 0f, 0f, 1f, 0f*scale , 0f*scale,
	            0f, 1f, 0f, 1f, 0f*scale , 1f*scale,
	            1f, 1f, 0f, 1f, 1f*scale , 1f*scale,
	           
	            1f, 1f, 0f, 1f, 1f*scale , 1f*scale,
	            1f, 0f, 0f, 1f, 1f*scale , 0f*scale,
	            0f, 0f, 0f, 1f, 0f*scale , 0f*scale,
		});
		
		vao = glGenVertexArrays();
		glBindVertexArray(vao);

		vertex_buffer = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vertex_buffer);
		glBufferData(GL_ARRAY_BUFFER, mesh.getFloatBuffer(), GL_STATIC_DRAW);

		int attrIdxPosition = 0;
		int stride = mesh.getStrideSize();
		int elemType = mesh.getElemType();
		int elemsPerVector = 4;
		glVertexAttribPointer(attrIdxPosition, elemsPerVector, elemType, false, stride, 0); 
        glEnableVertexAttribArray(attrIdxPosition);
        
        // TODO: read attr indices from program
		int attrIdxTexCoord = 4;
		glVertexAttribPointer(attrIdxTexCoord, 2, elemType, false, stride, mesh.getElemSize()*elemsPerVector); 
        glEnableVertexAttribArray(attrIdxTexCoord);
	}


	private void loadShaders() throws GLException, IOException{
		VertexShader vs = new VertexShader(HudResources.VERTEX_SHADER, HudResources.asInputStream(HudResources.VERTEX_SHADER));
		FragmentShader fs = new FragmentShader(HudResources.FRAGMENT_SHADER, HudResources.asInputStream(HudResources.FRAGMENT_SHADER));

		this.setShaderProgram(new Program("Hud Shader Program", vs, fs));
		
		vs.delete();
		fs.delete();
		
	}

	public void delete() {
		glDeleteBuffers(vertex_buffer);
		glDeleteVertexArrays(vao);
		super.delete();
	}

	
	@Override
	public void prepareRenderPass(GraphicContext context, double currentTime) {
		glBindVertexArray(vao);
		glBindBuffer(GL_ARRAY_BUFFER, vertex_buffer);
		texture.bind();
	}

	@Override
	protected void draw(double currentTime, VisualObject vobj) {
		glDrawArrays(GL_TRIANGLES, 0, mesh.getNumVertices());

	}

}
