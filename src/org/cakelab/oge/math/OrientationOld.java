package org.cakelab.oge.math;

import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector3fc;

public class OrientationOld implements Orientation {

	
	// TODO thread local memory management
	private Quaternionf tempQuat = new Quaternionf();
	
	protected Vector3f up = new Vector3f();
	protected Vector3f forward = new Vector3f();

	
	protected OrientationOld(boolean noinit) {
		
	}
	
	public OrientationOld() {
		this(DEFAULT_FORWARD, DEFAULT_UP);
	}
	
	public OrientationOld(Orientation that) {
		set(that);
	}
	
	public OrientationOld(Vector3fc forward, Vector3fc up) {
		set(forward,up);
	}

	public Vector3f getForward(Vector3f v) {
		return v.set(forward);
	}

	protected void setForward(Vector3fc forward) {
		this.forward.set(forward);
	}

	public Vector3f getUp(Vector3f v) {
		return v.set(up);
	}

	protected void setUp(Vector3fc up) {
		this.up.set(up);
	}

	public void set(Vector3fc forward, Vector3fc up) {
		setForward(forward);
		setUp(up);
	}

	public void set(Orientation that) {
		that.getForward(forward);
		that.getUp(up);
	}

	/**
	 * Adds the given rotation to the current orientation
	 * @param rotation
	 */
	public void apply(Quaternionf rotation) {
		rotation.transform(up);
		rotation.transform(forward);
	}

	public Vector3f getLocalXAxis(Vector3f v) {
		return v.set(up).cross(forward);
	}

	
	public Vector3f getLocalYAxis(Vector3f v) {
		return v.set(up);
	}

	public Vector3f getLocalZAxis(Vector3f v) {
		return v.set(forward);
	}

	public void addLocalEulerZXY(float xAngle, float yAngle, float zAngle) {
		// FIXME euler
		Vector3f xAxis = getLocalXAxis(new Vector3f());
		Quaternionf rotation = tempQuat.identity()
				.rotateAxis(yAngle, up)
				.rotateAxis(xAngle, xAxis)
				.rotateAxis(zAngle, forward)
				;
			apply(rotation);
	}

	public void addEulerZXY(float xAngle, float yAngle, float zAngle) {
		// FIXME euler
		Quaternionf rotation = tempQuat.identity()
				.rotateY(yAngle)
				.rotateX(xAngle)
				.rotateZ(zAngle)
				;
			apply(rotation);
	}

	public void setEulerZXY(float pitch, float yaw, float roll) {
		reset();
		addEulerZXY(pitch, yaw, roll);
	}

	private void reset() {
		set(DEFAULT);
	}

	/** Returns the rotation of this orientation relative 
	 *  to the DEFAULT orientation. */
	public Quaternionf getRotation(Quaternionf quat) {
		quat.identity();

		//
		// Determine a rotation which maps the default forward vector 
		// to our forward vector.
		//
		Quaternionf fRot = tempQuat;
		fRot.rotateTo(DEFAULT.forward, forward);
		
		//
		// To determine the required rotation to map the default 
		// up vector to our current up vector, we have to respect the
		// rotation which will be applied to map the forward vector.
		// Thus, we need to determine the rotation required to map
		// the default up vector, when it was already rotated by
		// the rotation to map the forward vector.
		//
		
		// rotate the default up vector by the rotation for the forward vector
		Vector3f up = new Vector3f(DEFAULT.up);
		fRot.transform(up);

		//
		// Now combine both rotation in the order 
		//    1. rotate to map the forward vector
		//    2. rotate to map the up vector
		// (i.e. multiply both rotations in reverse order)
		quat.rotateTo(up, this.up)
			.mul(fRot);
		
		return quat;
	}

	public void getEulerAnglesYXZ(Vector3f outEuler) {
		// TODO: euler YXZ instead of XYZ
		getRotation(tempQuat).getEulerAnglesXYZ(outEuler);
	}

}
