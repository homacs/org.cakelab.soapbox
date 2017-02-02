package org.cakelab.oge.scene;

import java.awt.image.BufferedImage;

import org.cakelab.oge.module.ModuleData;



public class TextureImage {

	private BufferedImage image;
	private int pixelFormat;
	private boolean flipped;
	private ModuleData renderData;

	public TextureImage(BufferedImage image, int pixelFormat, boolean flipped) {
		this.image = image;
		this.pixelFormat = pixelFormat;
		this.flipped = flipped;
	}
	
	public BufferedImage getImage() {
		return image;
	}

	public int getPixelFormat() {
		return pixelFormat;
	}

	public boolean isFlipped() {
		return flipped;
	}

	public void setRenderData(ModuleData renderData) {
		this.renderData = renderData;
	}
	
	public ModuleData getRenderData() {
		return renderData;
	}



}
