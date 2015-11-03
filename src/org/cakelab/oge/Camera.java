package org.cakelab.oge;

import org.joml.Matrix4f;
import org.joml.Quaternionf;

public class Camera extends Pose {

	
	private Matrix4f viewTransform = new Matrix4f();
	private Matrix4f orientationTransform = new Matrix4f();
	
	
	public Camera(float x, float y, float z, float yaw, float pitch, float roll) {
		super();
		this.setX(x);
		this.setY(y);
		this.setZ(z);
		this.addYaw(yaw);
		this.addPitch(pitch);
		this.addRoll(roll);
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
//			viewTransform.identity()
//				.rotate(qRotate.invert())
//				.translate(-getX(), -getY(), -getZ())
//			;
			viewTransform.identity()
				.translate(getX(), getY(), getZ())
				.rotate(qRotate)
				.invert()
				;
			
//			orientationTransform.identity()
//				.rotate(getPitch(), 1f, 0f, 0f)
//				.rotate(getYaw(), 0f, 1f, 0f)
//				.rotate(getRoll(), 0f, 0f, 1f)
//			;
			setPoseModified(false);
		}
	}


	
}
