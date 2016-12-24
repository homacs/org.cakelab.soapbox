package org.cakelab.oge.scene;

import org.cakelab.oge.app.GlobalClock;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class Pose {
	
	
	private double lastModified = 0;
	
	/**
	 * Position vector
	 */
	private Vector3f pos = new Vector3f();
	
	/** 
	 * Scaling in x y and z direction.
	 */
	private Vector3f scale = new Vector3f(1,1,1);
	
	/**
	 * Axis to apply Yaw.
	 * It's the local Y axis.
	 * Also the Up axis.
	 */
	private Vector3f dirUp = new Vector3f(0,1,0);
	/**
	 * Axis to apply roll.
	 * It's the local Z axis.
	 * Inverse to eye.
	 */
	private Vector3f dirForward = new Vector3f(0,0,1);

	private Quaternionf tempQuat = new Quaternionf();

	public Pose() {	
		resetRotation();
	}
	
	public Pose(float x, float y, float z) {
		this();
		this.pos.set(x,y,z);
		setPoseModified();
	}

	public Pose(float x, float y, float z, float pitch, float yaw, float roll) {
		this(x,y,z);
		setRotation(pitch, yaw, roll);
	}

	public void setPoseModified() {
		lastModified = GlobalClock.getCurrentTime();
	}
	
	public void set(Pose pose) {
		this.pos.set(pose.pos);
		this.dirUp.set(pose.dirUp);
		this.dirForward.set(pose.dirForward);
		this.scale.set(pose.scale);
		setPoseModified();
	}
	
	public float getX() {
		return pos.x;
	}

	public void setX(float x) {
		this.pos.x = x;
		setPoseModified();
	}

	public float getY() {
		return pos.y;
	}

	public void setY(float y) {
		this.pos.y = y;
		setPoseModified();
	}

	public float getZ() {
		return pos.z;
	}

	public void setZ(float z) {
		this.pos.z = z;
		setPoseModified();
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
		setPoseModified();
	}

	public void addYaw(float yaw) {
		Quaternionf rotation = tempQuat.identity().rotateAxis(yaw, dirUp);
		apply(rotation);
		setPoseModified();
	}
	
	
	public void addRoll(float roll) {
		Quaternionf rotation = tempQuat.identity().rotateAxis(roll, dirForward);
		apply(rotation);
		setPoseModified();
	}
	
	public void addRotation(float pitch, float yaw, float roll) {
		Quaternionf rotation = tempQuat.identity()
				.rotateAxis(yaw, dirUp)
				.rotateAxis(pitch, getPitchAxis())
				.rotateAxis(roll, dirForward)
				;
		apply(rotation);
		setPoseModified();
	}
	
	public void setRotation(float pitch, float yaw, float roll) {
		resetRotation();
		addRotation(pitch, yaw, roll);
		setPoseModified();
	}
	
	public void resetRotation() {
		dirUp.set(0, 1, 0);
		dirForward.set(0, 0, 1);
		setPoseModified();
	}

	public boolean isPoseModified(double lastUpdate) {
		return lastModified > lastUpdate;
	}

	public Vector3f getUpDirection() {
		return dirUp;
	}

	public Vector3f getForwardDirection() {
		return dirForward;
	}

	public Vector3f getPosition() {
		return pos;
	}

	public void setScale(float x, float y, float z) {
		scale.set(x,y,z);
	}
	public Vector3f getScale() {
		return scale;
	}

	
}
