package org.cakelab.soapbox.testscene.hud;

import static org.lwjgl.opengl.GL11.glDrawArrays;

import java.io.IOException;

import org.cakelab.oge.app.ApplicationContext;
import org.cakelab.oge.opengl.BufferObject.Usage;
import org.cakelab.oge.opengl.MeshVertexArray;
import org.cakelab.oge.scene.VisualEntity;
import org.cakelab.oge.shader.FragmentShader;
import org.cakelab.oge.shader.GLException;
import org.cakelab.oge.shader.Program;
import org.cakelab.oge.shader.VertexShader;
import org.cakelab.oge.texture.TextureImageIO;
import org.cakelab.oge.utils.OGEMeshRenderData;
import org.cakelab.oge.utils.SingleProgramRendererBase;
import org.cakelab.oge.texture.GPUTexture;
import org.cakelab.soapbox.model.Mesh;
import org.cakelab.soapbox.model.TriangleMesh;
import org.cakelab.soapbox.testscene.SBM_KTX.TextureCheckerBoard;
import org.cakelab.soapbox.testscene.hud.resources.HudResources;
import org.lwjgl.opengl.GL11;



public class HudRenderer extends SingleProgramRendererBase {
	private GPUTexture texture;
	private OGEMeshRenderData assets;

	public HudRenderer() throws GLException, IOException {
		loadShaders();
		boolean png = true;
		if (png) {
			int pixelFormat = GL11.GL_RGBA;
			boolean flipped = true;
			boolean forceAlpha = false;
			texture = new TextureImageIO(HudResources.asImage(HudResources.TEST_TEXTURE_UNCOMPRESSED), 
					pixelFormat, flipped, forceAlpha, GL11.GL_NEAREST, GL11.GL_NEAREST);
		} else {
			texture = new TextureCheckerBoard();
		}
//		float scale = texture.getWidth();
		float scale = 1.0f;
		TriangleMesh mesh = new TriangleMesh(Mesh.FrontFaceVertexOrder.Clockwise, 6, new float[] {
	            0f, 0f, 0f, 1f, 0f*scale , 0f*scale,
	            0f, 1f, 0f, 1f, 0f*scale , 1f*scale,
	            1f, 1f, 0f, 1f, 1f*scale , 1f*scale,
	           
	            1f, 1f, 0f, 1f, 1f*scale , 1f*scale,
	            1f, 0f, 0f, 1f, 1f*scale , 0f*scale,
	            0f, 0f, 0f, 1f, 0f*scale , 0f*scale,
		}, -1, -1);
		
		int attrIdxTexCoord = 4;
		int elemsPerVector = 4;
		int startIndex = 2;
		assets = new OGEMeshRenderData(new MeshVertexArray(mesh, 0, Usage.STATIC_DRAW), mesh.getGlDrawingMethod(), mesh.getNumVertices());
		assets.getVertexArrayObject().declareVertexAttribute(attrIdxTexCoord, elemsPerVector, startIndex);
	}


	private void loadShaders() throws GLException, IOException{
		VertexShader vs = new VertexShader(HudResources.VERTEX_SHADER, HudResources.asInputStream(HudResources.VERTEX_SHADER));
		FragmentShader fs = new FragmentShader(HudResources.FRAGMENT_SHADER, HudResources.asInputStream(HudResources.FRAGMENT_SHADER));

		this.setShaderProgram(new Program("Hud Shader Program", vs, fs));
		
		vs.delete();
		fs.delete();
		
	}

	public void delete() {
		assets.delete();
		super.delete();
	}

	
	@Override
	public void prepareRenderPass(ApplicationContext context, double currentTime) {
		assets.bind();
		texture.bind();
	}

	@Override
	public void draw(double currentTime, VisualEntity vobj) {
		glDrawArrays(assets.getDrawingMethod(), 0, assets.getNumVertices());
	}


	@Override
	public boolean needsNormals() {
		return false;
	}

}
