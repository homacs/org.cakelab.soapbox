package org.cakelab.oge;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class Pose {

	private boolean modified = true;
	
	/**
	 * translation along x (left/right)
	 */
	private float x = 0;
	
	/**
	 * translation along y (up/down)
	 */
	private float y = 0;
	
	/**
	 * translation along z (forward/backward)
	 */
	private float z = 0;
	
//	/** 
//	 * counter clockwise rotation [deg] around y
//	 */
//	private float yaw = 0;
//	/** 
//	 * counter clockwise rotation [deg] around x
//	 */
//	private float pitch = 0;
//	/** 
//	 * counter clockwise rotation [deg] around z
//	 */
//	private float roll = 0;

	private Quaternionf quaternion = new Quaternionf();

	/**
	 * Axis to apply pitch.
	 * It's the local X axis.
	 * Orthogonally to eye and up
	 */
	private Vector3f pitchAxis = new Vector3f(1, 0, 0);
	/**
	 * Axis to apply Yaw.
	 * It's the local Y axis.
	 * Also the Up axis.
	 */
	private Vector3f yawAxis = new Vector3f(0, 1, 0);
	/**
	 * Axis to apply roll.
	 * It's the local Z axis.
	 * Inverse to eye.
	 */
	private Vector3f rollAxis = new Vector3f(0, 0, 1);


	private Quaternionf tmpQuaternion = new Quaternionf();
	private Matrix4f tmpMatrix = new Matrix4f();

	
	public Pose() {	}
	
	public Pose(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		modified = true;
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		modified = true;
		this.y = y;
	}

	public float getZ() {
		return z;
	}

	public void setZ(float z) {
		modified = true;
		this.z = z;
	}

	
	
	
	public void addPitch(float pitch) {
		modified = true;
		Quaternionf rotation = tmpQuaternion .identity().rotateAxis(pitch, pitchAxis);
		rotation.transform(yawAxis);
		rotation.transform(pitchAxis);
		rotation.transform(rollAxis);
	}
	
	public void addPitchAbsolute(float pitch) {
		Quaternionf rotation = this.getRotationQuaternion();
		Quaternionf inverse = this.getRotationQuaternion().invert();
//		rotation.rotateX(-pitch).mul(inverse);
		rotation = inverse.rotateX(-pitch).mul(rotation);
		rotation.transform(yawAxis);
		rotation.transform(pitchAxis);
		rotation.transform(rollAxis);
	}

	public void addYawAbsolute(float yaw) {
		Quaternionf rotation = this.getRotationQuaternion();
		Quaternionf inverse = this.getRotationQuaternion().invert();
//		rotation.rotateY(yaw).mul(inverse);
		rotation = inverse.rotateY(yaw).mul(rotation);
		rotation.transform(yawAxis);
		rotation.transform(pitchAxis);
		rotation.transform(rollAxis);
	}


	public void addYaw(float yaw) {
		modified = true;
		Quaternionf rotation = tmpQuaternion .identity().rotateAxis(yaw, yawAxis);
		rotation.transform(yawAxis);
		rotation.transform(pitchAxis);
		rotation.transform(rollAxis);
	}
	
	public void addRoll(float roll) {
		modified = true;
		Quaternionf rotation = tmpQuaternion .identity().rotateAxis(roll, rollAxis);
		rotation.transform(yawAxis);
		rotation.transform(pitchAxis);
		rotation.transform(rollAxis);
	}
	
	public void addRotation(float pitch, float yaw, float roll) {
		modified = true;
		Quaternionf rotation = tmpQuaternion.identity()
				.rotateAxis(yaw, yawAxis)
				.rotateAxis(pitch, pitchAxis)
				.rotateAxis(roll, rollAxis)
				;
		rotation.transform(yawAxis);
		rotation.transform(pitchAxis);
		rotation.transform(rollAxis);
	}
	
//	public float getYaw() {
//		return yaw;
//	}
//
//	public void setYaw(float yaw) {
//		modified = true;
//		this.yaw = yaw;
//	}

//	public float getPitch() {
//		return pitch;
//	}
//
//	public void setPitch(float pitch) {
//		modified = true;
//		this.pitch = pitch;
//	}
//
//	public float getRoll() {
//		return roll;
//	}
//
//	public void setRoll(float roll) {
//		modified = true;
//		this.roll = roll;
//	}

	public boolean isPoseModified() {
		return modified;
	}
	
	public void setPoseModified(boolean modified) {
		this.modified = modified;
	}

	public Quaternionf getRotationQuaternion() {
		return quaternion.identity().lookRotate(rollAxis, yawAxis).invert();
	}
}
