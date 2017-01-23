package org.cakelab.oge.texture;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDeleteTextures;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL11.glTexSubImage2D;
import static org.lwjgl.opengl.GL42.glTexStorage2D;

import java.nio.ByteBuffer;

import org.cakelab.oge.module.ModuleData;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL31;

public class GPUTexture {

	protected int textureObject = -1;
	protected int target = -1;
	private int width;
	private int height;
	
	private ModuleData renderData;

	
	protected GPUTexture() {}
	
	/**
	 *
	 * @param target         the target of the operation. One of:<br>{@link GL11#GL_TEXTURE_2D TEXTURE_2D}, {@link GL30#GL_TEXTURE_1D_ARRAY TEXTURE_1D_ARRAY}, {@link GL31#GL_TEXTURE_RECTANGLE TEXTURE_RECTANGLE}, {@link GL13#GL_TEXTURE_CUBE_MAP TEXTURE_CUBE_MAP}, {@link GL11#GL_PROXY_TEXTURE_2D PROXY_TEXTURE_2D}, {@link GL30#GL_PROXY_TEXTURE_1D_ARRAY PROXY_TEXTURE_1D_ARRAY}, {@link GL31#GL_PROXY_TEXTURE_RECTANGLE PROXY_TEXTURE_RECTANGLE}, {@link GL13#GL_PROXY_TEXTURE_CUBE_MAP PROXY_TEXTURE_CUBE_MAP}
	 * @param levels         the number of texture (mipmap) levels
	 * @param internalFormat the sized internal format to be used to store texture image data (e.g. GL_RGB8 etc.)
	 * @param pixelFormat    the pixel data format. One of:<br>{@link #GL_STENCIL_INDEX STENCIL_INDEX}, {@link #GL_DEPTH_COMPONENT DEPTH_COMPONENT}, {@link GL30#GL_DEPTH_STENCIL DEPTH_STENCIL}, {@link #GL_RED RED}, {@link #GL_GREEN GREEN}, {@link #GL_BLUE BLUE}, {@link #GL_ALPHA ALPHA}, {@link GL30#GL_RG RG}, {@link #GL_RGB RGB}, {@link #GL_RGBA RGBA}, {@link GL12#GL_BGR BGR}, {@link GL12#GL_BGRA BGRA}, {@link #GL_LUMINANCE LUMINANCE}, {@link #GL_LUMINANCE_ALPHA LUMINANCE_ALPHA}, {@link GL30#GL_RED_INTEGER RED_INTEGER}, {@link GL30#GL_GREEN_INTEGER GREEN_INTEGER}, {@link GL30#GL_BLUE_INTEGER BLUE_INTEGER}, {@link GL30#GL_ALPHA_INTEGER ALPHA_INTEGER}, {@link GL30#GL_RG_INTEGER RG_INTEGER}, {@link GL30#GL_RGB_INTEGER RGB_INTEGER}, {@link GL30#GL_RGBA_INTEGER RGBA_INTEGER}, {@link GL30#GL_BGR_INTEGER BGR_INTEGER}, {@link GL30#GL_BGRA_INTEGER BGRA_INTEGER}
	 * @param type    		 the pixel data type. One of:<br>{@link #GL_UNSIGNED_BYTE UNSIGNED_BYTE}, {@link #GL_BYTE BYTE}, {@link #GL_UNSIGNED_SHORT UNSIGNED_SHORT}, {@link #GL_SHORT SHORT}, {@link #GL_UNSIGNED_INT UNSIGNED_INT}, {@link #GL_INT INT}, {@link GL30#GL_HALF_FLOAT HALF_FLOAT}, {@link #GL_FLOAT FLOAT}, {@link GL12#GL_UNSIGNED_BYTE_3_3_2 UNSIGNED_BYTE_3_3_2}, {@link GL12#GL_UNSIGNED_BYTE_2_3_3_REV UNSIGNED_BYTE_2_3_3_REV}, {@link GL12#GL_UNSIGNED_SHORT_5_6_5 UNSIGNED_SHORT_5_6_5}, {@link GL12#GL_UNSIGNED_SHORT_5_6_5_REV UNSIGNED_SHORT_5_6_5_REV}, {@link GL12#GL_UNSIGNED_SHORT_4_4_4_4 UNSIGNED_SHORT_4_4_4_4}, {@link GL12#GL_UNSIGNED_SHORT_4_4_4_4_REV UNSIGNED_SHORT_4_4_4_4_REV}, {@link GL12#GL_UNSIGNED_SHORT_5_5_5_1 UNSIGNED_SHORT_5_5_5_1}, {@link GL12#GL_UNSIGNED_SHORT_1_5_5_5_REV UNSIGNED_SHORT_1_5_5_5_REV}, {@link GL12#GL_UNSIGNED_INT_8_8_8_8 UNSIGNED_INT_8_8_8_8}, {@link GL12#GL_UNSIGNED_INT_8_8_8_8_REV UNSIGNED_INT_8_8_8_8_REV}, {@link GL12#GL_UNSIGNED_INT_10_10_10_2 UNSIGNED_INT_10_10_10_2}, {@link GL12#GL_UNSIGNED_INT_2_10_10_10_REV UNSIGNED_INT_2_10_10_10_REV}, {@link GL30#GL_UNSIGNED_INT_24_8 UNSIGNED_INT_24_8}, {@link GL30#GL_UNSIGNED_INT_10F_11F_11F_REV UNSIGNED_INT_10F_11F_11F_REV}, {@link GL30#GL_UNSIGNED_INT_5_9_9_9_REV UNSIGNED_INT_5_9_9_9_REV}, {@link GL30#GL_FLOAT_32_UNSIGNED_INT_24_8_REV FLOAT_32_UNSIGNED_INT_24_8_REV}, {@link #GL_BITMAP BITMAP}
	 * @param width          the width of the texture, in texels
	 * @param height         the height of the texture, in texels
	 * @param minFilter      GL_TEXTURE_MIN_FILTER {@link GL_NEAREST}, {@link GL_LINEAR}
	 * @param magFilter      GL_TEXTURE_MAG_FILTER {@link GL_NEAREST}, {@link GL_LINEAR}
	 * @param data			 the actual texture data
	 */
	public GPUTexture(int target, int levels, int internalFormat, 
			int pixelFormat, int dataType, 
			int width, int height, 
			int minFilter, int magFilter, 
			ByteBuffer data) 
	{
		
        this.target = target;
		this.width = width;
		this.height = height;
        
		// register a new texture object on the graphics card
    	textureObject = glGenTextures();


    	// bind it to the current opengl context
    	bind();
        
        // allocate appropriate memory
        glTexStorage2D(target, levels, internalFormat, width, height);
        
        // submit its data
        glTexSubImage2D(target, 0, 0, 0, width, height, pixelFormat, dataType, data);
        
        // The following two parameters define parameters of the default 
        // sampler object for this texture. In particular these two parameters 
        // specify how a fragments colour is determined if the corresponding texture 
        // coordinate does not map exactly to a specific pixel in the texture.
        // GL_LINEAR is the default value which does not work without mipmaps.
        glTexParameteri(target, GL_TEXTURE_MIN_FILTER, minFilter);
        glTexParameteri(target, GL_TEXTURE_MAG_FILTER, magFilter);
	}
	
	public GPUTexture(int target, int textureName) {
		this.target = target;
		this.textureObject = textureName;
	}
	
	public void bind() {
        glBindTexture(target, textureObject);
	}
	
	public void delete() {
		glDeleteTextures(textureObject);
	}

	public float getWidth() {
		return width;
	}
	
	public float getHeight() {
		return height;
	}
	
	public void setRenderData(ModuleData renderData) {
		this.renderData = renderData;
	}
	public ModuleData getRenderData() {
		return renderData;
	}
	
}
