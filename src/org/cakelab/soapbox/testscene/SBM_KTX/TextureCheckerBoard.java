package org.cakelab.soapbox.testscene.SBM_KTX;

import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_RGB8;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;

import java.nio.ByteBuffer;

import org.cakelab.oge.texture.Texture;
import org.lwjgl.BufferUtils;

public class TextureCheckerBoard extends Texture {

	/*
	 * Instantiate 2 alternate textures. The first is just a black 
	 * and white checkerboard pattern image. The second is a more 
	 * organic black and white pattern.
	 */
	final static byte[] B = {0x00, 0x00, 0x00, 0x00};
	final static byte[] W = {(byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF};
	// Note: this should be actually a 1D array but for
	//       readability we did it here in 2D and convert it later
    final static byte[][] tex_data = {
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
    
    public TextureCheckerBoard() {
    	super(GL_TEXTURE_2D, 1, GL_RGB8,  GL_RGBA, GL_UNSIGNED_BYTE, 
        		16, 16, GL_NEAREST, GL_NEAREST, toUByteBuffer(tex_data));
    }
    
    

	private static ByteBuffer toUByteBuffer(byte[][] tex_data) {
		ByteBuffer b = BufferUtils.createByteBuffer(tex_data.length * tex_data[0].length);
		for (int i = 0; i < tex_data.length; i++) {
			b.put(tex_data[i]);
		}
		b.rewind();
		return b;
	}
}
