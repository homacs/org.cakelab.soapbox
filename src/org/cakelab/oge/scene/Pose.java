package org.cakelab.oge.scene;

import org.cakelab.oge.app.GlobalClock;
import org.joml.Quaternionf;
import org.joml.Vector3f;



/**
 * A Pose describes position and orientation of an object. 
 * 
 * @author homac
 *
 */
public class Pose {
	
	
	private double lastModified = 0;
	
	
	private Pose referenceSystem;
	
	/**
	 * Position vector
	 */
	private Vector3f pos = new Vector3f();
	
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
	
	public Pose(float x, float y, float z, Vector3f dirForward, Vector3f dirUp) {
		this(x,y,z);
		this.dirForward = dirForward;
		this.dirUp = dirUp;
	}

	public void setPoseModified() {
		lastModified = GlobalClock.getCurrentTime();
	}
	
	public void set(Pose pose) {
		this.pos.set(pose.pos);
		this.dirUp.set(pose.dirUp);
		this.dirForward.set(pose.dirForward);
		setPoseModified();
	}
	
	public void setPosition(Vector3f position) {
		this.pos = position;
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
		addRotation(pitch,0,0);
	}

	public void addLocalPitch(float pitch) {
		addLocalRotation(pitch,0,0);
	}

	public void addYaw(float yaw) {
		addRotation(0,yaw,0);
	}
	
	public void addLocalYaw(float yaw) {
		addLocalRotation(0,yaw,0);
	}
	
	public void addRoll(float roll) {
		addRotation(0, 0, roll);
	}
	
	public void addLocalRoll(float roll) {
		addLocalRotation(0, 0, roll);
	}
	
	public void addLocalRotation(float pitch, float yaw, float roll) {
		Quaternionf rotation = tempQuat.identity()
			.rotateAxis(yaw, dirUp)
			.rotateAxis(pitch, getPitchAxis())
			.rotateAxis(roll, dirForward)
			;
		apply(rotation);
		
		setPoseModified();
	}	
	
	public void addRotation(float xAngle, float yAngle, float zAngle) {
		Quaternionf rotation = tempQuat.identity()
			.rotateY(yAngle)
			.rotateX(xAngle)
			.rotateZ(zAngle)
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
		// FIXME [0] this is dangerous in case the user expects another default orientation (set by setOrientation)
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

	public void setOrientation(Vector3f forward, Vector3f up) {
		this.dirForward = forward;
		this.dirUp = up;
		setPoseModified();
	}

	public Vector3f getPosition() {
		return pos;
	}

	public void setPosition(int x, int y, int z) {
		pos.x = x;
		pos.y = y;
		pos.z = z;
		setPoseModified();
	}

	public void setRotation(Quaternionf rotation) {
		apply(rotation);
	}

	public Pose getReferenceSystem() {
		return referenceSystem;
	}

	public void setReferenceSystem(Pose referenceSystem) {
		this.referenceSystem = referenceSystem;
	}

	
}
