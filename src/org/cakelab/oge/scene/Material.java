package org.cakelab.oge.scene;

import org.joml.Vector4f;

public class Material {

	private Vector4f color;
	public void setColor(Vector4f color) {
		this.color = color;
	}

	public void setColorTexture(TextureImage colorTexture) {
		this.colorTexture = colorTexture;
	}

	private TextureImage colorTexture;
	private float lightIntensity;

	public Material(Vector4f color, TextureImage colorTexture, float lightIntensity) {
		this.color = color;
		this.colorTexture = colorTexture;
		this.lightIntensity = lightIntensity;
	}

	public Material(Vector4f color) {
		this.color = color;
	}

	public Material() {
		color = new Vector4f(0.5f,0.5f,0.5f,1f);
	}

	public boolean hasTextures() {
		return colorTexture != null;
	}

	public boolean isLightEmitting() {
		return lightIntensity > 0.00001;
	}

	public float getEmitterIntensity() {
		return lightIntensity;
	}
	
	public void setEmitterIntensity(float intensity) {
		lightIntensity = intensity;
	}
	
	public Vector4f getColor() {
		return color;
	}

	public TextureImage getColorTexture() {
		return colorTexture;
	}

}
