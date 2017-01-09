package org.cakelab.oge.scene;

import org.cakelab.oge.app.GlobalClock;
import org.cakelab.oge.math.Orientation;
import org.cakelab.oge.math.OrientationReverse;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector3fc;



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

	private Orientation orientation = new OrientationReverse();

	public Pose() {	
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
		this.orientation.set(dirForward,dirUp);
	}

	public void setPoseModified() {
		lastModified = GlobalClock.getCurrentTime();
	}
	
	public void set(Pose pose) {
		this.pos.set(pose.pos);
		this.orientation.set(pose.getOrientation());
		setPoseModified();
	}
	
	public void setPosition(Vector3f position) {
		this.pos.set(position);
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
		orientation.apply(rotation);
	}
	
	private Vector3f getPitchAxis(Vector3f v) {
		return orientation.getLocalXAxis(v);
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
		orientation.addLocalEulerZXY(pitch, yaw, roll);
		setPoseModified();
	}	
	
	public void addRotation(float xAngle, float yAngle, float zAngle) {
		orientation.addEulerZXY(xAngle, yAngle, zAngle);
		setPoseModified();
	}

	public void setRotation(float pitch, float yaw, float roll) {
		orientation.setEulerZXY(pitch, yaw, roll);
		setPoseModified();
	}
	
	public boolean isPoseModified(double since) {
		return lastModified > since;
	}

	public Vector3f getUpDirection(Vector3f v) {
		return orientation.getLocalYAxis(v);
	}

	public Vector3f getForwardDirection(Vector3f v) {
		return orientation.getLocalZAxis(v);
	}

	public void setOrientation(Vector3fc defaultForward, Vector3fc defaultUp) {
		this.orientation.set(defaultForward, defaultUp);
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
		orientation.apply(rotation);
	}

	public Pose getReferenceSystem() {
		return referenceSystem;
	}

	public void setReferenceSystem(Pose referenceSystem) {
		this.referenceSystem = referenceSystem;
	}

	public Orientation getOrientation() {
		return orientation;
	}

	public void setOrientation(Orientation orientation) {
		this.orientation = orientation;
	}

	public boolean isWorldPoseModified(double since) {
		return isPoseModified(since) || (referenceSystem != null && referenceSystem.isWorldPoseModified(since));
	}

}
