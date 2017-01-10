package org.cakelab.oge.math;

import org.joml.Quaternionf;
import org.joml.Quaternionfc;
import org.joml.Vector3f;
import org.joml.Vector3fc;

/**
 * This class implements methods to deal with the orientation of an entity or camera.
 * An Orientation is defined by a forward vector, which describes the forward
 * direction of an entity (e.g. the front of a car) and an up vector, defining 
 * which way of the entity is up (e.g. the top of the car). Rotating an entity 
 * means altering its orientation, which will be reflected in this class.
 * 
 * The orientation translates into a rotation relative to the 
 * {@link OrientationC#DEFAULT} orientation. A rotation consists of a vector defining 
 * the rotation axis and an angle in clockwise order when looking along the vector.
 * 
 * @author homac
 *
 */
public class Orientation implements OrientationC {
	/*
	 * This class uses a quaternion which gets the rotations applied in reversed 
	 * direction. This allows to multiply subsequent rotations to the already 
	 * existing rotation without using temporary quaternions. Inverting a quaternion 
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

	protected Orientation(boolean noinit) {
	}
	
	public Orientation() {
		this(DEFAULT_FORWARD, DEFAULT_UP);
	}
	
	public Orientation(OrientationC that) {
		set(that);
	}
	
	/**
	 * Create an orientation which has the given forward and up direction.
	 * 
	 * @param forward Unit vector pointing in forward direction in the sense of the entity.
	 * @param up Unit vector pointing upwards from the entity.
	 */
	public Orientation(Vector3fc forward, Vector3fc up) {
		set(forward,up);
	}

	/**
	 * Set this orientation to have the given forward and up direction.
	 * 
	 * @param forward Unit vector pointing in forward direction in the sense of the entity.
	 * @param up Unit vector pointing upwards from the entity.
	 */
	public void set(Vector3fc forward, Vector3fc up) {
		reverseRotation.identity();

		// Rotations are the inverted rotations we want 
		// to achieve for our forward and up vectors, because it
		// rotates the coordinate system around the vectors and not
		// the vectors inside the coordinate system.
		
		
		// rotate to match forward vector
		reverseRotation.rotateTo(forward, OrientationC.DEFAULT_FORWARD);

		// get the rotated up vector
		Vector3f rotatedUp = new Vector3f();
		reverseRotation.positiveY(rotatedUp);
		
		// add rotation to match the up vector
		reverseRotation.rotateTo(up, rotatedUp);
	}

	/** 
	 * Set this orientation to have the given forward and up direction of the given orientation.
	 * 
	 * @param that Orientation to be copied by this..
	 */
	public void set(OrientationC that) {
		that.getRotation(reverseRotation).conjugate();
	}


	/**
	 * Add the given Euler rotation to this orientation (changing forward and/or up)
	 * considering the current orientation.
	 * 
	 * The axis order of this Euler rotation is: Z -> X -> Y.
	 * 
	 * @param xAngle Radians of the angle to rotate around the local X axis (aka pitch)
	 * @param yAngle Radians of the angle to rotate around the local Y axis (aka yaw)
	 * @param zAngle Radians of the angle to rotate around the local X axis (aka roll)
	 */
	public void addLocalEulerZXY(float xAngle, float yAngle, float zAngle) {
		// Since we maintain a reverse rotation, we add the angles in 
		// reverse order and negate the angles (inverted euler rotation).
		
		// Also, since we maintain the inverted quaternion, the local axes match the axes of
		// the entity for pitch (forward), yaw (up), roll.
		reverseRotation
			.rotateLocalZ(-zAngle)
			.rotateLocalX(-xAngle)
			.rotateLocalY(-yAngle)
		;
		
	}

	/**
	 * Add the given Euler rotation to this orientation (changing forward and/or up),
	 * applying the rotations to the axes of the Cartesian space (not the local coordinate 
	 * system of the entity).
	 * 
	 * The axis order of this Euler rotation is: Z -> X -> Y.
	 * 
	 * @param xAngle Radians of the angle to rotate around the local X axis (aka pitch)
	 * @param yAngle Radians of the angle to rotate around the local Y axis (aka yaw)
	 * @param zAngle Radians of the angle to rotate around the local X axis (aka roll)
	 */
	public void addEulerZXY(float xAngle, float yAngle, float zAngle) {
		// Since we maintain a reverse rotation, we add the angles in 
		// reverse order and negated angles.
		reverseRotation
				.rotateZ(-zAngle)
				.rotateX(-xAngle)
				.rotateY(-yAngle)
				;
	}

	/**
	 * Set this orientation to reflect the given Euler rotation (changing forward and/or up)
	 * relative to the {@link OrientationC#DEFAULT} orientation,
	 * applying the rotations to the axes of the Cartesian space (not the local coordinate 
	 * system of the entity).
	 * 
	 * The axis order of this Euler rotation is: Z -> X -> Y.
	 * 
	 * @param xAngle Radians of the angle to rotate around the local X axis (aka pitch)
	 * @param yAngle Radians of the angle to rotate around the local Y axis (aka yaw)
	 * @param zAngle Radians of the angle to rotate around the local X axis (aka roll)
	 */
	public void setEulerZXY(float pitch, float yaw, float roll) {
		reset();
		addEulerZXY(pitch, yaw, roll);
	}

	/**
	 * Reset the orientation to {@link OrientationC#DEFAULT}.
	 * Same as {@link #set}({@link OrientationC#DEFAULT}).
	 */
	private void reset() {
		set(DEFAULT);
	}

	/**
	 * Adds the given rotation to the current orientation
	 * @param rotation
	 */
	public void apply(Quaternionfc rotation) {
		assert (rotation instanceof Quaternionf);
		Quaternionf rot = (Quaternionf)rotation;
		reverseRotation.mul(rot.conjugate());
		rot.conjugate();
	}

	@Override
	public Vector3f getForward(Vector3f result) {
		reverseRotation.normalizedPositiveZ(result);
		return result;
	}

	@Override
	public Vector3f getUp(Vector3f v) {
		reverseRotation.normalizedPositiveY(v);
		return v;
	}

	@Override
	public Vector3f getLocalXAxis(Vector3f v) {
		reverseRotation.normalizedPositiveX(v);
		return v;
	}

	@Override
	public Vector3f getLocalYAxis(Vector3f v) {
		return getUp(v);
	}

	@Override
	public Vector3f getLocalZAxis(Vector3f v) {
		return getForward(v);
	}

	@Override
	public Quaternionf getRotation(Quaternionf q) {
		return q.set(reverseRotation).conjugate();
	}

	@Override
	public Quaternionfc getReverseRotation() {
		return reverseRotation;
	}
	
	@Override
	public Vector3f getEulerAnglesXYZ(Vector3f result) {
		reverseRotation.conjugate().getEulerAnglesXYZ(result);
		reverseRotation.conjugate();
		return result;
	}
	

}
