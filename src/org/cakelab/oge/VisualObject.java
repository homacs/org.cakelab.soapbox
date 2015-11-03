package org.cakelab.oge;

import org.joml.Matrix4f;
import org.joml.Quaternionf;

public abstract class VisualObject extends Pose {

	Matrix4f worldTransform = new Matrix4f();
	private Matrix4f modelTransform;

	public VisualObject() {}
	
	public VisualObject(float x, float y, float z) {
		super(x,y,z);
	}

	protected Matrix4f getWorldTransform() {
		applyModifications();
		return worldTransform;
	}

	private void applyModifications() {
		if (isPoseModified()) {
			// TODO: issue with roll in model view transformation
			// maybe use lookat matrix instead.
			Quaternionf qRotate = getRotationQuaternion();
			worldTransform
				.identity()
				.translate(getX(), getY(), getZ())
				.rotate(qRotate)
			;
			
//			worldTransform
//				.identity()
//				.translate(getX(), getY(), getZ())
//				.rotate(getYaw(), 0.0f, 1.0f, 0.0f)
//				.rotate(getPitch(), 1.0f, 0.0f, 0.0f)
//				.rotate(getRoll(), 0.0f, 0.0f, 1.0f)
//			;
			if (modelTransform != null) {
				worldTransform.mul(modelTransform);
			}
			setPoseModified(false);
		}
	}

	/**
	 * This allows a transformation of a common model before application of 
	 * of a model world transformation. This is for example useful if the
	 * local center of a model is different to that required in your application.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @param yaw
	 * @param pitch
	 * @param roll
	 */
	public void setModelTransform(float x, float y, float z, float yaw, float pitch,
			float roll) {
		modelTransform = new Matrix4f().rotate(new Quaternionf().rotate(pitch, yaw, roll)).translate(x,y,z);
	}
	
}
