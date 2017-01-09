package org.cakelab.oge.math;

import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector3fc;

public interface Orientation {

	static final Vector3fc DEFAULT_FORWARD = new Vector3f(0,0,1);
	static final Vector3fc DEFAULT_UP = new Vector3f(0,1,0);
	static final Vector3fc DEFAULT_X_AXIS = new Vector3f(1,0,0);

	static final ImmutableOrientation DEFAULT = new ImmutableOrientation(DEFAULT_FORWARD, DEFAULT_UP);

	
	
	public Vector3f getForward(Vector3f v);
	public Vector3f getUp(Vector3f v);
	public void set(Vector3fc forward, Vector3fc up);

	public void set(Orientation that);

	/**
	 * Adds the given rotation to the current orientation
	 * @param rotation
	 */
	public void apply(Quaternionf rotation);

	public Vector3f getLocalXAxis(Vector3f v);

	
	public Vector3f getLocalYAxis(Vector3f v);

	public Vector3f getLocalZAxis(Vector3f v);

	public void addLocalEulerZXY(float xAngle, float yAngle, float zAngle);

	public void addEulerZXY(float xAngle, float yAngle, float zAngle);

	public void setEulerZXY(float pitch, float yaw, float roll);

	public void getEulerAnglesYXZ(Vector3f outEuler);

	/** Returns the rotation of this orientation relative 
	 *  to the DEFAULT orientation. */
	public Quaternionf getRotation(Quaternionf quat);

}
