package org.cakelab.oge;

import org.joml.Vector3f;

// TODO refactor: rename to Light
// TODO lights may be dynamic object to
// TODO consider associating light source and light object
public class Lamp extends Pose {

	private Vector3f color;
	private LampRenderData renderData;

	public Vector3f getColor() {
		return color;
	}

	public void setColor(Vector3f color) {
		this.color = color;
	}

	public Lamp(Vector3f color) {
		this.color = color;
	}

	public void setRenderData(LampRenderData renderData) {
		this.renderData = renderData;
	}

	public LampRenderData getRenderData() {
		return renderData;
	}
}
