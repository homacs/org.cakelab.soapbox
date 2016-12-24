package org.cakelab.oge.utils;

import org.cakelab.oge.app.GlobalClock;
import org.cakelab.oge.scene.VisualObject;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

public class ObjectMatrices {

	private VisualObject visualObject;
	private Matrix4f worldTransform = new Matrix4f();
	private Matrix4f modelTransform = new Matrix4f();
	private Quaternionf tempQuat = new Quaternionf();

	private double lastUpdate = 0;
	
	public ObjectMatrices(VisualObject visualObject) {
		this.visualObject = visualObject;
	}

	

	public Matrix4f getWorldTransform() {
		applyModifications();
		return worldTransform;
	}

	private void applyModifications() {
		if (visualObject.isPoseModified(lastUpdate)) {
			Quaternionf qRotate = getRotationQuaternion();
			worldTransform
				.identity()
				.translate(visualObject.getPosition())
				.rotate(qRotate)
			;
			modelTransform.identity().scale(visualObject.getScale());
			worldTransform.mul(modelTransform);
			lastUpdate = GlobalClock.getCurrentTime();
		}
	}

	/**
	 * 
	 * TODO add relationship to parent objects
	 * 
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
		modelTransform = new Matrix4f().rotate(tempQuat.identity().rotate(pitch, yaw, roll)).translate(x,y,z);
	}
	

	public Quaternionf getRotationQuaternion() {
		return tempQuat.identity().lookRotate(visualObject.getForwardDirection(), visualObject.getUpDirection()).invert();
	}


}
