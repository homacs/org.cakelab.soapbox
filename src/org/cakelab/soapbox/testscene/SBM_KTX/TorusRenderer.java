package org.cakelab.soapbox.testscene.SBM_KTX;

import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_RGB8;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDeleteTextures;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL11.glTexSubImage2D;
import static org.lwjgl.opengl.GL42.glTexStorage2D;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.cakelab.oge.GraphicContext;
import org.cakelab.oge.Renderer;
import org.cakelab.oge.VisualObject;
import org.cakelab.oge.shader.FragmentShader;
import org.cakelab.oge.shader.GLCompilerException;
import org.cakelab.oge.shader.GLException;
import org.cakelab.oge.shader.GLLinkerException;
import org.cakelab.oge.shader.Program;
import org.cakelab.oge.shader.VertexShader;
import org.cakelab.oge.utils.ktx.KTX;
import org.cakelab.oge.utils.sbm.SBMObject;
import org.cakelab.soapbox.testscene.SBM_KTX.resources.Resources;
import org.lwjgl.BufferUtils;



public class TorusRenderer extends Renderer {

	/** ids of texture objects */
	private int[] tex_object = new int[2];
	
    /** vertices of the torus */
    SBMObject     object = new SBMObject();



	public TorusRenderer() throws GLException, IOException {

		/*
		 * Instantiate 2 alternate textures. The first is just a black 
		 * and white checkerboard pattern image. The second is a more 
		 * organic black and white pattern.
		 */
		final byte[] B = {0x00, 0x00, 0x00, 0x00};
		final byte[] W = {(byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF};
		// Note: this should be actually a 1D array but for
		//       readability we did it here in 2D and convert it later
        final byte[][] tex_data = {
            B, W, B, W, B, W, B, W, B, W, B, W, B, W, B, W,
            W, B, W, B, W, B, W, B, W, B, W, B, W, B, W, B,
            B, W, B, W, B, W, B, W, B, W, B, W, B, W, B, W,
            W, B, W, B, W, B, W, B, W, B, W, B, W, B, W, B,
            B, W, B, W, B, W, B, W, B, W, B, W, B, W, B, W,
            W, B, W, B, W, B, W, B, W, B, W, B, W, B, W, B,
            B, W, B, W, B, W, B, W, B, W, B, W, B, W, B, W,
            W, B, W, B, W, B, W, B, W, B, W, B, W, B, W, B,
            B, W, B, W, B, W, B, W, B, W, B, W, B, W, B, W,
            W, B, W, B, W, B, W, B, W, B, W, B, W, B, W, B,
            B, W, B, W, B, W, B, W, B, W, B, W, B, W, B, W,
            W, B, W, B, W, B, W, B, W, B, W, B, W, B, W, B,
            B, W, B, W, B, W, B, W, B, W, B, W, B, W, B, W,
            W, B, W, B, W, B, W, B, W, B, W, B, W, B, W, B,
            B, W, B, W, B, W, B, W, B, W, B, W, B, W, B, W,
            W, B, W, B, W, B, W, B, W, B, W, B, W, B, W, B,
        };
        // register a new texture object on the graphics card
    	tex_object[0] = glGenTextures();
    	// bind it to the current opengl context
        glBindTexture(GL_TEXTURE_2D, tex_object[0]);
        // allocate appropriate memory
        glTexStorage2D(GL_TEXTURE_2D, 1, GL_RGB8, 16, 16);
        // submit its data
        glTexSubImage2D(GL_TEXTURE_2D, 0, 0, 0, 16, 16, GL_RGBA, GL_UNSIGNED_BYTE, toUByteBuffer(tex_data));
        
        // The following two parameters define parameters of the default 
        // sampler object for this texture. In particular these two parameters 
        // specify how a fragments colour is determined if the corresponding texture 
        // coordinate does not map exactly to a specific pixel in the texture.
        // GL_LINEAR is the default value which does not work without mipmaps.
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);


        // load the image for the second texture.
        tex_object[1] = KTX.load(Resources.asInputStream(Resources.TEXTURE_PATTERN1));


        
        // Load the vertices of the 3D object (torus) to be displayed.
        // and the corresponding texture coordinates into buffer objects
        // and register vertex attributes accordingly.
        // Positions are referenced by vertex attribute 0
        // and texture coordinates are referenced by vertex attribute 4
        object.load(Resources.asInputStream(Resources.TORUS_VERTICES_SBM));
        
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
		glBindTexture(GL_TEXTURE_2D, tex_object[torus.tex_index]);
		object.render();
	}

	
	private ByteBuffer toUByteBuffer(byte[][] tex_data) {
		ByteBuffer b = BufferUtils.createByteBuffer(tex_data.length * tex_data[0].length);
		for (int i = 0; i < tex_data.length; i++) {
			b.put(tex_data[i]);
		}
		b.rewind();
		return b;
	}

	@Override
	public void delete() {
        glDeleteTextures(tex_object[0]);
        glDeleteTextures(tex_object[1]);
        object.free();

		super.delete();
	}

	

}
