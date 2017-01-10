package org.cakelab.oge.scene;

import org.cakelab.oge.app.GlobalClock;
import org.cakelab.oge.math.OrientationC;
import org.cakelab.oge.math.Orientation;
import org.joml.Quaternionfc;
import org.joml.Vector3f;
import org.joml.Vector3fc;



/**
 * <p>
 * A Pose holds a position and an orientation (of an entity). 
 * Both are relative to a reference system, which is by 
 * default the global coordinate system (world coordinates).
 * </p><p>
 * Position and orientation are always local. The position of 
 * a Pose is its relative position from the centre (0,0,0) of its
 * local coordinate system and the orientation defines a rotation 
 * relative to the default orientation (see {@link OrientationC#DEFAULT}) 
 * around its position (origin). 
 * </p><p>
 * A Pose can have a reference system other then the world coordinate 
 * system (see {@link Pose#setReferenceSystem(Pose)}). E.g. tires of 
 * a car have the car as reference system. The global position and 
 * orientation of the pose, then is relative to its reference system, 
 * such as the tires will always stick to the car. Thus, translations 
 * and rotations of parenting reference systems have to be considered 
 * when calculating the global position and rotation of an entity. 
 * </p>
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

	private Orientation orientation = new Orientation();

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
	
	public Pose(float x, float y, float z, Vector3fc dirForward, Vector3fc dirUp) {
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
	
	public void setPosition(Vector3fc position) {
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

	public void apply(Quaternionfc rotation) {
		orientation.apply(rotation);
	}
	
	public Vector3f getPitchAxis(Vector3f v) {
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

	public Vector3f getUpDirection(Vector3f result) {
		return orientation.getLocalYAxis(result);
	}

	public Vector3f getForwardDirection(Vector3f result) {
		return orientation.getLocalZAxis(result);
	}

	public void setOrientation(Vector3fc forward, Vector3fc up) {
		this.orientation.set(forward, up);
		setPoseModified();
	}

	public Vector3fc getPosition() {
		return pos;
	}

	public void setPosition(int x, int y, int z) {
		pos.x = x;
		pos.y = y;
		pos.z = z;
		setPoseModified();
	}

	public void setRotation(Quaternionfc rotation) {
		orientation.set(Orientation.DEFAULT_FORWARD, Orientation.DEFAULT_UP);
		orientation.apply(rotation);
	}

	/**
	 * Returns the reference coordinate system, which was set for 
	 * this pose.
	 * @return
	 */
	public Pose getReferenceSystem() {
		return referenceSystem;
	}

	/**
	 * Set a reference coordinate system for this pose. Position and 
	 * orientation are from now on to be considered relative to the
	 * reference system.
	 * 
	 * @param referenceSystem
	 */
	public void setReferenceSystem(Pose referenceSystem) {
		this.referenceSystem = referenceSystem;
	}

	/**
	 * Returns the orientation of this pose.
	 */
	public OrientationC getOrientation() {
		return orientation;
	}

	public void setOrientation(Orientation orientation) {
		this.orientation = orientation;
	}

	public boolean isWorldPoseModified(double since) {
		return isPoseModified(since) || (referenceSystem != null && referenceSystem.isWorldPoseModified(since));
	}

}
