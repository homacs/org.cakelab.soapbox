package org.cakelab.oge;

import org.joml.Matrix4f;
import org.joml.Quaternionf;

public class Camera extends Pose {

	
	private Matrix4f viewTransform = new Matrix4f();
	private Matrix4f orientationTransform = new Matrix4f();
	private float fov;
	
	
	public Camera(float x, float y, float z, float pitch, float yaw, float roll) {
		super(x, y, z, pitch, yaw, roll);
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

	public Matrix4f getOrientationTransform() {
		applyModifications();
		return orientationTransform;
	}
	
	public Matrix4f getViewTransform() {
		applyModifications();
		return viewTransform;
	}

	private void applyModifications() {
		if (isPoseModified()) {
			Quaternionf qRotate = getRotationQuaternion();
			
			orientationTransform.identity()
				.rotate(qRotate);
			viewTransform.identity()
				.translate(getX(), getY(), getZ())
				.rotate(qRotate)
				.invert()
				;
			
			setPoseModified(false);
		}
	}


	
}
