package org.cakelab.soapbox;

import org.cakelab.oge.Camera;
import org.cakelab.oge.HeadCamera;
import org.cakelab.oge.scene.Entity;
import org.cakelab.oge.scene.Pose;
import org.joml.AxisAngle4f;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector4f;

// TODO Player class needs to maintain its own Pose as parent for its camera.
// TODO player and its camera would be the first example of a group object
public class Player extends Entity implements MovementAdapter {
	private HeadCamera camera;
	Vector3f translationVelocity = new Vector3f();
	Vector3f rotationVelocity = new Vector3f();
	private double lastTime = -1;
	private float velocityMultiplier = 1.0f;
	
	public Player() {
		super(0f, 1.75f, +6f);
		// init head cam with some random position
		camera = new HeadCamera();
		camera.setReferenceSystem(this);
	}

	public void moveForward(float amount) {
		moveAlong(0, 0, -amount);
	}

	public void moveBackward(float amount) {
		moveAlong(0, 0, +amount);
	}

	public void moveUp(float amount) {
		moveAlong(0, +amount, 0);
	}

	public void moveDown(float amount) {
		moveAlong(0, -amount, 0);
	}

	public void moveLeft(float amount) {
		moveAlong(-amount, 0, 0);
	}

	public void moveRight(float amount) {
		moveAlong(+amount, 0, 0);
	}

	private void moveAlong(float x, float y, float z) {

		Vector4f direction = new Vector4f(x, y, z, 1);
		
		Matrix4f orientation = getOrientationTransform();
		
		direction.mul(orientation);
		
		Vector4f pos = new Vector4f(getPosition(), 1).add(direction);
		setX(pos.x);
		setY(pos.y);
		setZ(pos.z);
		
	}


	private Matrix4f getOrientationTransform() {
		Matrix4f orientationTransform = new Matrix4f()
				.lookAlong(getForwardDirection(), getUpDirection()).invert();
		return orientationTransform;
	}

	/**
	 * +degree: turn left
	 * -degree: turn right
	 * @param degree
	 */
	public void addYaw(float degree) {
		this.addLocalYaw(degree);
	}
	
	
	/**
	 * +degree: turn up
	 * -degree: turn down
	 * @param degree
	 */
	public void addPitch(float degree) {
		camera.addLocalPitch(degree);
	}
	
	/**
	 * +degree: roll counterclockwise
	 * -degree: roll clockwise
	 * @param degree
	 */
	public void addRoll(float degree) {
		camera.addLocalRoll(degree);
	}
	
	
	public void addTranslationVelocity(float x, float y, float z) {
		translationVelocity.add(x, y, z);
	}

	public void addRotationVelocity(float yaw, float pitch, float roll) {
		rotationVelocity.add(yaw, pitch, roll);
	}

	public void update(double currentTime) {
		if (lastTime < 0) lastTime = currentTime;
		float time = (float) (currentTime - lastTime);
		lastTime  = currentTime;
		moveAlong(translationVelocity.x * time * velocityMultiplier, 
				translationVelocity.y * time * velocityMultiplier, 
				translationVelocity.z * time * velocityMultiplier);
		addYaw(rotationVelocity.x * time * velocityMultiplier * 3);
		addPitch(rotationVelocity.y * time * velocityMultiplier * 3);
		addRoll(rotationVelocity.z * time * velocityMultiplier * 3);
	}

	public void addRotation(float pitch, float yaw, float roll) {
		addYaw(yaw);
		addPitch(pitch);
		addRoll(roll);
	}

	public void setVelocityMultiplyier(float f) {
		velocityMultiplier = f;
	}

	public Camera getCamera() {
		return camera;
	}

	@Override
	public void init(Pose that) {
		

		// We want the player body to be upright
		Vector3f up = new Vector3f(0,1,0);
		
		// Now calc an appropriate forward direction in x-z plane
		// 
		Vector3f u = that.getForwardDirection();
		Vector3f v = that.getUpDirection();
//		Vector3f u = new Vector3f(0,0,-1); // testing
//		Vector3f v = new Vector3f(1,0,0);  // testing
		Vector3f forward;
		// try with (x,0,1)
		float x = (v.x - (v.y*u.x)/u.y) / (v.z - (v.y * u.z)/u.y);
		if (Float.isFinite(x)) {
			forward = new Vector3f(x,0,1).normalize();
		} else {
			// no intersection with (x,0,1)
			// try (1,0,z)
			float z = (v.z - (v.y*u.z)/u.y) / (v.x - (v.y * u.x)/u.y);
			if (Float.isFinite(z)) {
				forward = new Vector3f(1,0,z).normalize();
			} else {
				// no intersection with (1,0,z) either, which means
				// u-v plane equals x-z plane
				// -> use standard orientation
				forward = new Vector3f(0,0,-1);
			}
		}
		// now we do have a vector in x-z plane, but we don't know if it
		// is pointing forward in the sense of the given camera orientation 
		// without pitch and roll
		
		// The angle between the pitch axes of both orientations has to be less 
		// than +/-90 degree, otherwise the forward vector points in the 
		// opposite direction.
		Vector3f camPitchAxis = new Vector3f(u).cross(v);
		Vector3f playerPitchAxis = new Vector3f(forward).cross(up);
		float cos = camPitchAxis.dot(playerPitchAxis);
		if (cos < 0) {
			forward.negate();
		}
		
		setPosition(that.getPosition());
		setOrientation(forward, up);

		// now determine the camera orientation relative to the player's orientation
		forward = new Vector3f(that.getForwardDirection());
		up = new Vector3f(that.getUpDirection());
		Vector3f camForward = new Vector3f(0,0,1);
		Vector3f camUp = new Vector3f(0,1,0);
		Vector3f euler = new Vector3f();
		
		// remove the rotation of the player from the camera orientation
		Quaternionf thisRotation = new Quaternionf().lookRotate(new Vector3f(this.getForwardDirection()), this.getUpDirection());
		thisRotation.getEulerAnglesXYZ(euler);
		Quaternionf inverse = new Quaternionf(thisRotation).invert();
		inverse.getEulerAnglesXYZ(euler);
		
		inverse.transform(forward);
		inverse.transform(up);
		
		Quaternionf rotation = new Quaternionf().lookRotate(new Vector3f(forward), up);
		rotation.getEulerAnglesXYZ(euler);
		rotation.transform(camForward);
		rotation.transform(camUp);

		Vector3f testForward = new Vector3f(camForward);
		Vector3f testUp = new Vector3f(camUp);
		thisRotation.transform(testForward);
		thisRotation.transform(testUp);
		
		
		rotation = new Quaternionf().lookRotate(new Vector3f(forward).negate(), up).invert();
		rotation.getEulerAnglesXYZ(euler);
		camera.setPosition(0, 0, 0);
		camera.setOrientation(new Vector3f(0,0,-1), new Vector3f(0,1,0));
		camera.addRotation(euler.x, 0, euler.z);
		setPoseModified();
	}


}
