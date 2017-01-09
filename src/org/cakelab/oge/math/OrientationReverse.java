package org.cakelab.oge.math;

import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector3fc;

/**
 * This class implements the interface Orientation in a more efficient way for 
 * entities that rotate often. 
 * 
 * @author homac
 *
 */
public class OrientationReverse implements Orientation {
	/*
	 * This class uses a quaternion which gets the rotations applied in reversed 
	 * direction. This allows to multiply subsequent rotations to the already 
	 * existing rotation without using temporary quaternions. Inverting a quanterion 
	 * to revert its rotation, is just a matter of toggling the sign bit of the 
	 * imaginary components - thus, no effort compared to using temporary quaternions. 
	 * 
	 * Another advantage of this approach is, that the coordinate system vectors 
	 * of the quaternion are always aligned with the forward and up vectors of the
	 * entity.
	 * 
	 * To comply with the interface contract, the getRotation() method returns the
	 * inverted quaternion, which is then the rotation needed to turn a vector
	 * of the entity into its intended orientation.
	 */
	private Quaternionf reverseRotation = new Quaternionf();

	protected OrientationReverse(boolean noinit) {
	}
	
	public OrientationReverse() {
		this(DEFAULT_FORWARD, DEFAULT_UP);
	}
	
	public OrientationReverse(Orientation that) {
		set(that);
	}
	
	public OrientationReverse(Vector3fc forward, Vector3fc up) {
		setRotationFrom(forward,up);
	}

	private void setRotationFrom(Vector3fc forward, Vector3fc up) {
		reverseRotation.identity();

		// Rotations are the inverted rotations we want 
		// to achieve for our forward and up vectors, because it
		// rotates the coordinate system around the vectors and not
		// the vectors inside the coordinate system.
		
		
		// rotate to match forward vector
		reverseRotation.rotateTo(forward, Orientation.DEFAULT_FORWARD);

		// get the rotated up vector
		Vector3f rotatedUp = new Vector3f();
		reverseRotation.positiveY(rotatedUp);
		
		// add rotation to match the up vector
		reverseRotation.rotateTo(up, rotatedUp);
	}


	
	public Vector3f getForward(Vector3f v) {
		reverseRotation.normalizedPositiveZ(v);
		return v;
	}

	public Vector3f getUp(Vector3f v) {
		reverseRotation.normalizedPositiveY(v);
		return v;
	}

	public void set(Vector3fc forward, Vector3fc up) {
		setRotationFrom(forward, up);
	}

	public void set(Orientation that) {
		that.getRotation(reverseRotation).conjugate();
	}

	public void apply(Quaternionf rotation) {
		reverseRotation.mul(rotation.conjugate());
		rotation.conjugate();
	}

	public Vector3f getLocalXAxis(Vector3f v) {
		reverseRotation.normalizedPositiveX(v);
		return v;
	}
	
	public Vector3f getLocalYAxis(Vector3f v) {
		return getUp(v);
	}

	public Vector3f getLocalZAxis(Vector3f v) {
		return getForward(v);
	}

	public void addLocalEulerZXY(float xAngle, float yAngle, float zAngle) {
		reverseRotation
			.rotateLocalZ(-zAngle)
			.rotateLocalX(-xAngle)
			.rotateLocalY(-yAngle)
		;
		
	}

	public void addEulerZXY(float xAngle, float yAngle, float zAngle) {
		reverseRotation
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
		return quat.set(reverseRotation).conjugate();
	}

	public void getEulerAnglesYXZ(Vector3f outEuler) {
		reverseRotation.conjugate().getEulerAnglesXYZ(outEuler);
		reverseRotation.conjugate();
	}

}
