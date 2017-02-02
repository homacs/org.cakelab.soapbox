package org.cakelab.soapbox.testscene.SBM_KTX;

import java.io.IOException;

import org.cakelab.oge.app.ApplicationContext;
import org.cakelab.oge.scene.VisualEntity;
import org.cakelab.oge.shader.FragmentShader;
import org.cakelab.oge.shader.GLException;
import org.cakelab.oge.shader.Program;
import org.cakelab.oge.shader.VertexShader;
import org.cakelab.oge.texture.GPUTexture;
import org.cakelab.oge.utils.SingleProgramRendererBase;
import org.cakelab.oge.utils.ktx.KTX;
import org.cakelab.oge.utils.sbm.SBMObject;
import org.cakelab.soapbox.testscene.SBM_KTX.resources.Resources;



public class TorusRenderer extends SingleProgramRendererBase {

	/** texture objects */
	private GPUTexture[] textures = new GPUTexture[2];
	
    /** vertices of the torus */
    SBMObject     sbmObject = new SBMObject();



	public TorusRenderer() throws GLException, IOException {

        textures[0] = new TextureCheckerBoard();
        
        // load the image for the second texture.
        textures[1] = KTX.load(Resources.asInputStream(Resources.TEXTURE_PATTERN1));

        // Load the vertices of the 3D object (torus) to be displayed.
        // and the corresponding texture coordinates into buffer objects
        // and register vertex attributes accordingly.
        // Positions are referenced by vertex attribute 0
        // and texture coordinates are referenced by vertex attribute 4
        sbmObject.load(Resources.asInputStream(Resources.TORUS_VERTICES_SBM));
        
        loadShaders();
		
		
	}
	
	private void loadShaders() throws GLException, IOException {
		VertexShader vs = new VertexShader(Resources.VERTEX_SHADER, Resources.asInputStream(Resources.VERTEX_SHADER));
		FragmentShader fs = new FragmentShader(Resources.FRAGMENT_SHADER, Resources.asInputStream(Resources.FRAGMENT_SHADER));

		this.setShaderProgram(new Program("Torus Shader Program", vs, fs));
		
		vs.delete();
		fs.delete();
		
	}

	
	
	
	@Override
	public void prepareRenderPass(ApplicationContext context, double currentTime) {
	}

	@Override
	public void draw(double currentTime, VisualEntity vobj) {
		Torus torus = (Torus) vobj;
	    // activate the texture we want to use for the torus
		textures[torus.tex_index].bind();
		sbmObject.render();
	}


	@Override
	public void delete() {
        textures[0].delete();
        textures[1].delete();
        sbmObject.free();

		super.delete();
	}

	@Override
	public boolean needsNormals() {
		return false;
	}

	@Override
	public boolean needsUv() {
		return true;
	}

	

}
