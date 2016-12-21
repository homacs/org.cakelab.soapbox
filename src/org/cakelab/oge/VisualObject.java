package org.cakelab.oge;

import org.joml.Matrix4f;
import org.joml.Quaternionf;

public abstract class VisualObject extends Pose {
	// TODO transformation matrices are renderer specific!
	Matrix4f worldTransform = new Matrix4f();
	private Matrix4f modelTransform;
	private RenderData renderData;

	public VisualObject() {}
	
	public VisualObject(float x, float y, float z) {
		super(x,y,z);
	}

	protected Matrix4f getWorldTransform() {
		applyMatrixModifications();
		return worldTransform;
	}

	private void applyMatrixModifications() {
		if (isPoseModified()) {
			Quaternionf qRotate = getRotationQuaternion();
			worldTransform
				.identity()
				.translate(getX(), getY(), getZ())
				.rotate(qRotate)
			;
			
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

	/**
	 * Retrieve render engine specific data.
	 */
	public RenderData getRenderData() {
		return renderData;
	}

	/**
	 * Set render engine specific data.
	 */
	public void setRenderData(RenderData renderData) {
		this.renderData = renderData ;
	}

}
