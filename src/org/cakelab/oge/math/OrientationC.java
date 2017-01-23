package org.cakelab.oge.math;

import org.joml.Quaternionf;
import org.joml.Quaternionfc;
import org.joml.Vector3f;
import org.joml.Vector3fc;

/** 
 * This is an interface for <em>immutable</em> {@link Orientation}s (e.g. to define constants).
 * 
 * @author homac
 *
 */
public interface OrientationC {
	/**
	 * A unit vector pointing along positive X (1,0,0).
	 */
	public static final Vector3fc DEFAULT_X_AXIS = new Vector3f(1,0,0);
	/**
	 * A unit vector pointing along positive Y (0,1,0).
	 */
	public static final Vector3fc DEFAULT_Y_AXIS = new Vector3f(0,1,0);
	/**
	 * A unit vector pointing along positive Z (0,0,1).
	 */
	public static final Vector3fc DEFAULT_Z_AXIS = new Vector3f(0,0,1);

	/**
	 * A unit vector pointing in default forward direction. In our 
	 * system this is equal to {@link #DEFAULT_Z_AXIS}.
	 */
	public static final Vector3fc DEFAULT_FORWARD = DEFAULT_Z_AXIS;
	/**
	 * A unit vector pointing in default up direction. In our 
	 * system this is equal to {@link #DEFAULT_Y_AXIS}.
	 */
	public static final Vector3fc DEFAULT_UP      = DEFAULT_Y_AXIS;

	/**
	 * The default orientation having the default forward ({@link #DEFAULT_FORWARD}) 
	 * and default up ({@link #DEFAULT_UP}) vector.
	 */
	public static final OrientationC DEFAULT = new Orientation(DEFAULT_FORWARD, DEFAULT_UP);

	/**
	 * Set v to the unit vector which is pointing forward in respect to the 
	 * current local rotation.
	 * @param v out parameter.
	 * @return v
	 */
	public Vector3f getForward(Vector3f v);

	/**
	 * Set v to the unit vector which is pointing up in respect to the 
	 * current local rotation.
	 * @param v out parameter.
	 * @return v
	 */
	public Vector3f getUp(Vector3f v);

	/**
	 * Set v to the unit vector which is pointing along positive X 
	 * in respect to the current local rotation.
	 * @param v out parameter.
	 * @return v
	 */
	public Vector3f getLocalXAxis(Vector3f v);

	/**
	 * Set v to the unit vector which is pointing along positive Y 
	 * in respect to the current local rotation.
	 * @param v out parameter.
	 * @return v
	 */
	public Vector3f getLocalYAxis(Vector3f v);

	/**
	 * Set v to the unit vector which is pointing along positive Z
	 * in respect to the current local rotation.
	 * @param v out parameter.
	 * @return v
	 */
	public Vector3f getLocalZAxis(Vector3f v);

	/**
	 * Set the vector angles in degree according to rotation applied
	 * in Y->X->Z order.
	 * 
	 * @param result
	 * @return result
	 */
	public Vector3f getEulerAnglesXYZ(Vector3f result);

	/**
	 * Sets the given quaternion q to the rotation required to turn a vector of 
	 * this entity in the intended orientation. The returned rotation is relative
	 * to the {@link #DEFAULT} orientation.
	 * 
	 * @param q Quaternion which will receive the result
	 * @return Returns q
	 */
	public Quaternionf getRotation(Quaternionf quat);

	
	/**
	 * Returns the reversed rotation relative to the {@link #DEFAULT} orientation, 
	 * which is the same as {@link #getRotation(Quaternionf)}.invert()
	 * @return reverse rotation.
	 */
	public Quaternionfc getReverseRotation();

}
