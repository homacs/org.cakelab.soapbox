package org.cakelab.oge.texture;

import static org.lwjgl.opengl.GL11.GL_RGB;
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
import org.lwjgl.opengl.GL13;


// TODO work on texture import
//      turn this class in a tool and don't derive from GPUTexture
//      allow setting of all the different texture parameters
//      maybe have a common oge specific internal format, which allows conversion into other formats
public class TextureFactory extends GPUTexture {
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
     * @param image
     * @param requiredPixelFormat 	Pixel format needed.
     * @param flip          		True if we should flip the image on the y-axis while loading
     * @param forceAlpha    		Forces to create an alpha channel if none available in image.
     * @param transparent   		Color to be transparent (default: null -> black)
     * @param minFilter
     * @param magFilter
     */
	public TextureFactory(BufferedImage image, int requiredPixelFormat, boolean flip, boolean forceAlpha, int minFilter, int magFilter) {
        // TODO: textures: Not every texture needs to be converted
		//       This constructor considers a lot of cases where the image is
		//       not suitable for a OpenGL compatible texture. There should
		//       be a way to prevent all this.
		
		imageData = null; 
        WritableRaster raster;
        BufferedImage texImage;
        
        this.width = image.getWidth();
        this.height = image.getHeight();

        
        //
        // Conversion:
        // 1. Resize image to have dimensions to be power of 2.
        // 2. Convert between RGBA8 (32bit) and RGB8 (24bit).
        //

        // calc new width and height
        texWidth = 2;
        while (texWidth < width) texWidth *= 2;
        texHeight = 2;
        while (texHeight < height) texHeight *= 2;

        // configure image codec and buffer
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
        
        // transfer (and convert) source image into new buffer
        Graphics2D g = (Graphics2D) texImage.getGraphics();
        if (hasAlpha) {
            // only need to blank the image for mac 
        	// compatibility if we're using alpha
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
		
        if (flip) {
        	sy *= -1.0;
        }

        if (stretch || flip) {
        	// add scale transformation (no conversion yet)
        	g.scale(sx,sy);
        }
        
        // apply conversion
        if (flip) {
        	g.drawImage(image,0,-height,null);
        } else {
        	g.drawImage(image,0,0,null);
        }
        

        //
        // Transfer converted data to OpenGL
        //
        
        
        // transfer converted texture image into byte buffer (native)
        byte[] data = ((DataBufferByte) texImage.getRaster().getDataBuffer()).getData(); 
        imageData = ByteBuffer.allocateDirect(data.length); 
        imageData.order(ByteOrder.nativeOrder()); 
        imageData.put(data, 0, data.length);
        // TODO: a reset should do it here, since length is already set as required
        imageData.flip();
        // TODO: dispose can be earlier I think
        g.dispose();

        
        
        // transfer texture in byte buffer to opengl
        int srcPixelFormat = hasAlpha ? GL_RGBA : GL_RGB;
    	target = GL_TEXTURE_2D;
    	textureObject = glGenTextures();
        bind();
        float color[] = { 0.0f, 0.0f, 0.0f, 0.0f };
        GL11.glTexParameterfv(GL_TEXTURE_2D, GL11.GL_TEXTURE_BORDER_COLOR, BufferUtilsHelper.createFloatBuffer(color));
        
        glTexParameteri(target, GL11.GL_TEXTURE_WRAP_S, GL13.GL_CLAMP_TO_BORDER);
        glTexParameteri(target, GL11.GL_TEXTURE_WRAP_T, GL13.GL_CLAMP_TO_BORDER);
        
        glTexParameteri(target, GL_TEXTURE_MIN_FILTER, minFilter);
        glTexParameteri(target, GL_TEXTURE_MAG_FILTER, magFilter);
        
        // produce a texture from the byte buffer
        GL11.glTexImage2D(target, 
                      0, 
                      requiredPixelFormat, 
                      texWidth, 
                      texHeight, 
                      0, 
                      srcPixelFormat, 
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
