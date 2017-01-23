package org.cakelab.oge.scene;

import org.cakelab.oge.module.ModuleData;
import org.joml.Vector3f;

// TODO lights may be dynamic objects too
// TODO consider associating light source and light object
public class LightSource extends Pose {

	private Vector3f color;
	private ModuleData renderData;

	public LightSource(Vector3f color) {
		this(0, 0, 0, color);
	}

	public LightSource(int x, int y, int z, Vector3f color) {
		super(x,y,z);
		this.color = color;
	}

	public Vector3f getColor() {
		return color;
	}

	public void setColor(Vector3f color) {
		this.color = color;
	}


	public void setRenderData(ModuleData renderData) {
		this.renderData = renderData;
	}

	public ModuleData getRenderData() {
		return renderData;
	}
}
