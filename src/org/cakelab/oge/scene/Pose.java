package org.cakelab.oge.scene;

import org.cakelab.oge.app.GlobalClock;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class Pose {
	// TODO xyz should be a vector
	
	
	private double lastModified = 0;
	
	
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
		this.x = pose.x;
		this.y = pose.y;
		this.z = pose.z;
		this.dirUp.set(pose.dirUp);
		this.dirForward.set(pose.dirForward);
		setPoseModified();
	}
	
	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
		setPoseModified();
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
		setPoseModified();
	}

	public float getZ() {
		return z;
	}

	public void setZ(float z) {
		this.z = z;
		setPoseModified();
	}

	

	public void apply(Quaternionf rotation) {
		rotation.transform(dirUp);
		rotation.transform(dirForward);
	}
	
	private Vector3f getPitchAxis() {
		// TODO this might be the reason why camera flips around if turned upside down
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
	
}
