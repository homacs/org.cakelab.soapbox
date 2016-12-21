package org.cakelab.oge.scene;

import org.joml.Vector3f;

// TODO refactor: rename to Light
// TODO lights may be dynamic objects too
// TODO consider associating light source and light object
public class LightSource extends Pose {

	private Vector3f color;
	private ModuleData renderData;

	public Vector3f getColor() {
		return color;
	}

	public void setColor(Vector3f color) {
		this.color = color;
	}

	public LightSource(Vector3f color) {
		this.color = color;
	}

	public void setRenderData(ModuleData renderData) {
		this.renderData = renderData;
	}

	public ModuleData getRenderData() {
		return renderData;
	}
}
