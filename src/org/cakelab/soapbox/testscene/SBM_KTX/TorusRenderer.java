package org.cakelab.soapbox.testscene.SBM_KTX;

import java.io.IOException;

import org.cakelab.oge.GraphicContext;
import org.cakelab.oge.Renderer;
import org.cakelab.oge.VisualObject;
import org.cakelab.oge.shader.FragmentShader;
import org.cakelab.oge.shader.GLCompilerException;
import org.cakelab.oge.shader.GLException;
import org.cakelab.oge.shader.GLLinkerException;
import org.cakelab.oge.shader.Program;
import org.cakelab.oge.shader.VertexShader;
import org.cakelab.oge.texture.Texture;
import org.cakelab.oge.utils.ktx.KTX;
import org.cakelab.oge.utils.sbm.SBMObject;
import org.cakelab.soapbox.testscene.SBM_KTX.resources.Resources;



public class TorusRenderer extends Renderer {

	/** texture objects */
	private Texture[] textures = new Texture[2];
	
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
	
	private void loadShaders() throws GLCompilerException, IOException, GLLinkerException {
		VertexShader vs = new VertexShader(Resources.VERTEX_SHADER, Resources.asInputStream(Resources.VERTEX_SHADER));
		FragmentShader fs = new FragmentShader(Resources.FRAGMENT_SHADER, Resources.asInputStream(Resources.FRAGMENT_SHADER));

		this.setShaderProgram(new Program("Torus Shader Program", vs, fs));
		
		vs.delete();
		fs.delete();
		
	}

	
	
	
	@Override
	public void prepare(GraphicContext context, double currentTime) {
        
		super.prepare(context, currentTime);
	}

	@Override
	protected void draw(double currentTime, VisualObject vobj) {
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

	

}
