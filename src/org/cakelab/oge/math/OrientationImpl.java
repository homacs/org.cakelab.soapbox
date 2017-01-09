package org.cakelab.oge.math;

import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector3fc;

public class OrientationImpl implements Orientation {

	private Quaternionf myRotation = new Quaternionf();
	private Quaternionf tmpQuat = new Quaternionf();
	private Vector3f tmpV = new Vector3f();
	

	
	protected OrientationImpl(boolean noinit) {
		
	}
	
	public OrientationImpl() {
		this(DEFAULT_FORWARD, DEFAULT_UP);
	}
	
	public OrientationImpl(Orientation that) {
		that.getRotation(myRotation);
	}
	
	public OrientationImpl(Vector3fc forward, Vector3fc up) {
		setRotationFrom(forward,up);
	}

	private void setRotationFrom(Vector3fc _forward, Vector3fc _up) {
		myRotation.identity();

		//
		// Determine a rotation which maps the default forward vector 
		// to our forward vector.
		//
		Quaternionf fRot = new Quaternionf();
		fRot.rotateTo(DEFAULT_FORWARD, _forward);
		
		//
		// To determine the required rotation to map the default 
		// up vector to our current up vector, we have to respect the
		// rotation which will be applied to map the forward vector.
		// Thus, we need to determine the rotation required to map
		// the default up vector, when it was already rotated by
		// the rotation to map the forward vector.
		//
		
		// rotate the default up vector by the rotation for the forward vector
		Vector3f up = new Vector3f(DEFAULT_UP);
		fRot.transform(up);

		//
		// Now combine both rotation in the order 
		//    1. rotate to map the forward vector
		//    2. rotate to map the up vector
		// (i.e. multiply both rotations in reverse order)
		myRotation.rotateTo(up, _up)
			.mul(fRot);
	}


	
	public Vector3f getForward(Vector3f v) {
		v.set(DEFAULT_FORWARD);
		myRotation.transform(v);
		return v;
	}

	public Vector3f getUp(Vector3f v) {
		v.set(DEFAULT_UP);
		myRotation.transform(v);
		return v;
	}

	public void set(Vector3fc forward, Vector3fc up) {
		setRotationFrom(forward, up);
	}

	public void set(Orientation that) {
		that.getRotation(myRotation);
	}

	public void apply(Quaternionf rotation) {
		myRotation.mul(rotation);
	}

	public Vector3f getLocalXAxis(Vector3f v) {
		v.set(DEFAULT_X_AXIS);
		myRotation.transform(v);
		return v;
	}

	
	public Vector3f getLocalYAxis(Vector3f v) {
		return getUp(v);
	}

	public Vector3f getLocalZAxis(Vector3f v) {
		return getForward(v);
	}

	public void addLocalEulerZXY(float xAngle, float yAngle, float zAngle) {
		// FIXME euler

		
		//
		// This is the straight forward approach:
		//
		// 1. save current rotation in R1
		// 2. set old rotation to requested angles to be added
		// 3. combine both in R = NEW * R1
		Quaternionf NEW = tmpQuat.identity()
				.rotateAxis(yAngle, getUp(tmpV))
				.rotateAxis(xAngle, getLocalXAxis(tmpV))
				.rotateAxis(zAngle, getForward(tmpV))
				.mul(myRotation);
		myRotation.set(NEW);

		
		
	}

	public void addEulerZXY(float xAngle, float yAngle, float zAngle) {
		// FIXME euler
		myRotation
				.rotateY(yAngle)
				.rotateX(xAngle)
				.rotateZ(zAngle)
				;
	}

	public void setEulerZXY(float pitch, float yaw, float roll) {
		reset();
		addEulerZXY(pitch, yaw, roll);
	}

	private void reset() {
		set(DEFAULT);
	}

	public Quaternionf getRotation(Quaternionf quat) {
		return quat.set(myRotation);
	}

	public void getEulerAnglesYXZ(Vector3f outEuler) {
		// TODO: euler YXZ instead of XYZ
		myRotation.getEulerAnglesXYZ(outEuler);
	}

}
