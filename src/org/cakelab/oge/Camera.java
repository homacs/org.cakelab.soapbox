package org.cakelab.oge;

import org.cakelab.oge.scene.Pose;
import org.cakelab.oge.utils.CameraMatrices;

public class Camera extends Pose {
	
	public CameraMatrices matrices;
	
	private float fov;
	
	
	public Camera(float x, float y, float z, float pitch, float yaw, float roll) {
		super(x, y, z, pitch, yaw, roll);
		matrices = new CameraMatrices(this);
	}

	public void setFoV(float fov) {
		this.fov = fov;
	}

	public float getFoV() {
		return fov;
	}

	public void set(Camera that) {
		super.set(that);
		this.fov = that.fov;
	}

	
}
