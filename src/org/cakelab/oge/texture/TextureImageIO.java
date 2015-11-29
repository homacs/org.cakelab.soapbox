package org.cakelab.oge.texture;

import static org.lwjgl.opengl.GL11.GL_RGB;
import static org.lwjgl.opengl.GL11.GL_RGB8;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexParameteri;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Hashtable;

import org.cakelab.oge.utils.BufferUtilsHelper;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;


public class TextureImageIO extends Texture {
	/** The colour model including alpha for the GL image */
    private static final ColorModel glAlphaColorModel = 
    		new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB),
	            new int[] {8,8,8,8},
	            true,
	            false,
	            ComponentColorModel.TRANSLUCENT,
	            DataBuffer.TYPE_BYTE);
    
    /** The colour model for the GL image */
    private static final  ColorModel glColorModel =
    		new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB),
                new int[] {8,8,8,0},
                false,
                false,
                ComponentColorModel.OPAQUE,
                DataBuffer.TYPE_BYTE);
    /** The image data */
    private ByteBuffer imageData;
    /** The bit depth of the image */
    private int depth;
    /** The height of the image */
    private int height;
    /** The width of the image */
    private int width;
    /** The width of the texture that should be created for the image */
    private int texWidth;
    /** The height of the texture that should be created for the image */
    private int texHeight;

    /**
     * 
     * @param image
     * @param pixelFormat
     * @param flipped       True if we should flip the image on the y-axis while loading
     * @param forceAlpha    Forces to create an alpha channel if none available in image.
     * @param transparent   Color to be transparent (default: null -> black)
     * @param minFilter
     * @param magFilter
     */
	public TextureImageIO(BufferedImage image, int pixelFormat, boolean flipped, boolean forceAlpha, int minFilter, int magFilter) {
		imageData = null; 
        WritableRaster raster;
        BufferedImage texImage;
        
        this.width = image.getWidth();
        this.height = image.getHeight();
        
        
        // find the closest power of 2 for the width and height
        // of the produced texture
        texWidth = 2;
        while (texWidth < width) texWidth *= 2;
        texHeight = 2;
        while (texHeight < height) texHeight *= 2;
        
        // create a raster that can be used by OpenGL as a source
        // for a texture
        boolean hasAlpha = image.getColorModel().hasAlpha() || forceAlpha; 

        if (hasAlpha) {
        	depth = 32;
            raster = Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE,texWidth,texHeight,4,null);
            texImage = new BufferedImage(glAlphaColorModel,raster,false,new Hashtable<String, Object>());
        } else {
        	depth = 24;
            raster = Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE,texWidth,texHeight,3,null);
            texImage = new BufferedImage(glColorModel,raster,false,new Hashtable<String, Object>());
        }
        
        
        // copy the source image into the produced image
        Graphics2D g = (Graphics2D) texImage.getGraphics();
        
        // only need to blank the image for mac compatibility if we're using alpha
        if (hasAlpha) {
	        g.setColor(new Color(0f,0f,0f,0f));
	        g.fillRect(0,0,texWidth,texHeight);
        }

		double sx = 1.0;
		double sy = 1.0;
		boolean stretch = true;
		if (stretch) {
    		sx = ((double)texWidth)/width;
			sy = ((double)texHeight)/height;
		}
		
        if (flipped) {
        	sy *= -1.0;
        }
        
        if (flipped) {
        	g.scale(sx,sy);
        	g.drawImage(image,0,-height,null);
        } else {
        	g.drawImage(image,0,0,null);
        }
        
        
        // build a byte buffer from the temporary image 
        // that be used by OpenGL to produce a texture.
        byte[] data = ((DataBufferByte) texImage.getRaster().getDataBuffer()).getData(); 
        
        imageData = ByteBuffer.allocateDirect(data.length); 
        imageData.order(ByteOrder.nativeOrder()); 
        imageData.put(data, 0, data.length); 
        imageData.flip();
        g.dispose();



        int srcPixelFormat = hasAlpha ? GL_RGBA : GL_RGB;
//        int componentCount = hasAlpha ? 4 : 3;
	
    	target = GL_TEXTURE_2D;
    	textureObject = glGenTextures();
        bind();
        float color[] = { 0.0f, 0.0f, 0.0f, 0.0f };
        GL11.glTexParameter(GL_TEXTURE_2D, GL11.GL_TEXTURE_BORDER_COLOR, BufferUtilsHelper.createFloatBuffer(color));
        
        glTexParameteri(target, GL11.GL_TEXTURE_WRAP_S, GL13.GL_CLAMP_TO_BORDER);
        glTexParameteri(target, GL11.GL_TEXTURE_WRAP_T, GL13.GL_CLAMP_TO_BORDER);
        
        glTexParameteri(target, GL_TEXTURE_MIN_FILTER, minFilter);
        glTexParameteri(target, GL_TEXTURE_MAG_FILTER, magFilter);
        
       
        // produce a texture from the byte buffer
        GL11.glTexImage2D(target, 
                      0, 
                      srcPixelFormat, 
                      texWidth, 
                      texHeight, 
                      0, 
                      pixelFormat, 
                      GL11.GL_UNSIGNED_BYTE, 
                      imageData); 

	}

	public int getDepth() {
		return depth;
	}

	public float getWidth() {
		return width;
	}
}
