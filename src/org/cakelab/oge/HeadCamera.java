package org.cakelab.oge;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class HeadCamera extends Camera {

	
	private Matrix4f viewTransform = new Matrix4f();
	private Matrix4f orientationTransform = new Matrix4f();
	private float yaw;
	private float pitch;
	private float roll;
	
	
	public HeadCamera(float x, float y, float z, float pitch, float yaw, float roll) {
		super(x, y, z, pitch, yaw, roll);
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


	@Override
	public void addPitch(float pitch) {
		setPoseModified(true);
		this.pitch += pitch;
	}


	@Override
	public void addYaw(float yaw) {
		setPoseModified(true);
		this.yaw += yaw;
	}


	@Override
	public void addRoll(float roll) {
		setPoseModified(true);
		this.roll += roll;
	}


	@Override
	public void addRotation(float pitch, float yaw, float roll) {
		addPitch(pitch);
		addYaw(yaw);
		addRoll(roll);
	}


	@Override
	public Quaternionf getRotationQuaternion() {
		// roll axis (Z)
		Vector3f eye = new Vector3f(0, 0, 1);
		// yaw axis (Y)
		Vector3f up = new Vector3f(0, 1, 0);
		Quaternionf rotation = new Quaternionf();
		
		rotation.identity().rotateZ(roll);
		rotation.transform(eye);
		rotation.transform(up);

		rotation.identity().rotateX(-pitch);
		rotation.transform(eye);
		rotation.transform(up);
		
		rotation.identity().rotateY(yaw);
		rotation.transform(eye);
		rotation.transform(up);

		
		return rotation.identity().lookRotate(eye, up).invert();
	}

	
	
}
