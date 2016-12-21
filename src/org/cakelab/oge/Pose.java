package org.cakelab.oge;

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
	
	/**
	 * Axis to apply Yaw.
	 * It's the local Y axis.
	 * Also the Up axis.
	 */
	private Vector3f dirUp = new Vector3f();
	/**
	 * Axis to apply roll.
	 * It's the local Z axis.
	 * Inverse to eye.
	 */
	private Vector3f dirForward = new Vector3f();

	private Quaternionf tempQuat = new Quaternionf();

	public Pose() {	
		resetRotation();
	}
	
	public Pose(float x, float y, float z) {
		this();
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Pose(float x, float y, float z, float pitch, float yaw, float roll) {
		this(x,y,z);
		setRotation(pitch, yaw, roll);
	}

	public void set(Pose pose) {
		this.x = pose.x;
		this.y = pose.y;
		this.z = pose.z;
		this.dirUp.set(pose.dirUp);
		this.dirForward.set(pose.dirForward);
		this.modified = true;
	}
	
	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
		modified = true;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
		modified = true;
	}

	public float getZ() {
		return z;
	}

	public void setZ(float z) {
		this.z = z;
		modified = true;
	}

	

	public void apply(Quaternionf rotation) {
		rotation.transform(dirUp);
		rotation.transform(dirForward);
	}
	
	private Vector3f getPitchAxis() {
		return new Vector3f(dirForward).cross(dirUp);
	}

	public void addPitch(float pitch) {
		Quaternionf rotation = tempQuat.identity().rotateAxis(pitch, getPitchAxis());
		apply(rotation);
		modified = true;
	}

	public void addYaw(float yaw) {
		Quaternionf rotation = tempQuat.identity().rotateAxis(yaw, dirUp);
		apply(rotation);
		modified = true;
	}
	
	
	public void addRoll(float roll) {
		Quaternionf rotation = tempQuat.identity().rotateAxis(roll, dirForward);
		apply(rotation);
		modified = true;
	}
	
	public void addRotation(float pitch, float yaw, float roll) {
		Quaternionf rotation = tempQuat.identity()
				.rotateAxis(yaw, dirUp)
				.rotateAxis(pitch, getPitchAxis())
				.rotateAxis(roll, dirForward)
				;
		apply(rotation);
		modified = true;
	}
	
	public void setRotation(float pitch, float yaw, float roll) {
		resetRotation();
		addRotation(pitch, yaw, roll);
		modified = true;
	}
	
	public void resetRotation() {
		dirUp.set(0, 1, 0);
		dirForward.set(0, 0, 1);
		modified = true;
	}

	public boolean isPoseModified() {
		return modified;
	}
	
	public void setPoseModified(boolean modified) {
		this.modified = modified;
	}

	public Quaternionf getRotationQuaternion() {
		return tempQuat.identity().lookRotate(dirForward, dirUp).invert();
	}
}
