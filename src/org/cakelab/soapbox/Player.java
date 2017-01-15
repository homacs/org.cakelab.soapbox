package org.cakelab.soapbox;

import org.cakelab.oge.Camera;
import org.cakelab.oge.math.OrientationC;
import org.cakelab.oge.scene.Entity;
import org.cakelab.oge.scene.Pose;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class Player extends Entity implements MovementAdapter {
	private HeadCamera camera;
	Vector3f translationVelocity = new Vector3f();
	Vector3f rotationVelocity = new Vector3f();
	
	Vector3f tmpVect = new Vector3f();
	Quaternionf tmpQuat = new Quaternionf();
	
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

	public void moveAlong(float x, float y, float z) {
		// TODO: join movement code from headcam and player somewhere?
		
		if (isSignificant(x) || isSignificant(y) || isSignificant(z) ) {
			Vector3f direction = tmpVect.set(x, y, z);

			Quaternionf orientation = getOrientation().getRotation(tmpQuat);
			orientation.transform(direction);
			Vector3f pos = direction.add(getPosition());
			setPosition(pos);
		}

		

	}


	private boolean isSignificant(float f) {
		return Math.abs(f) > 0.0000001;
	}

	/**
	 * +degree: turn left
	 * -degree: turn right
	 * @param degree
	 */
	public void addYaw(float degree) {
		this.addLocalYaw((float)Math.toRadians(degree));
	}
	
	
	/**
	 * +degree: turn up
	 * -degree: turn down
	 * @param degree
	 */
	public void addPitch(float degree) {
		camera.addLocalPitch((float)Math.toRadians(degree));
	}
	
	/**
	 * +degree: roll counterclockwise
	 * -degree: roll clockwise
	 * @param degree
	 */
	public void addRoll(float degree) {
		camera.addLocalRoll((float)Math.toRadians(degree));
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

	
	public void init(Camera camera) {
		set(this.camera);
		init((Pose)camera);
	}

	@Override
	public void init(Pose that) {

		// We want the player body to be upright
		Vector3f up = new Vector3f(0,1,0);
		
		// TODO move calculation to math package
		
		// Now calc an appropriate forward direction in x-z plane
		// 
		// We do this by considering the intersection of the plane
		// spanned by forward (u) and up (v) with the x-z plane
		// as a line and search for a point on that line. To do that
		// we search for a point V=(x,0,z) in the x-z plane which is equal to
		// a point in the u-v plane. This is can be done by solving 
		// the following equation:
		//     V = s*U + t*V
		// This results in a system with 3 equations and 4 unknown 
		// variables x,y,s and t. For now, we just want the orientation
		// of the vector V and not which way is pointing forwards and
		// the length doesn't matter either. Thus we can choose any value 
		// for x (respective z) and calculate the other value z (respective x).
		//
		// We also need to consider the special case, in which the u-v plane 
		// is equal to the x-z plane. In this case the actual forward direction
		// of the player doesn't matter, because we can rotate his head in 
		// with roll and/or pitch to get into the x-z plane.
		Vector3f u = that.getForwardDirection(new Vector3f());
		Vector3f v = that.getUpDirection(new Vector3f());

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
				forward = new Vector3f(OrientationC.DEFAULT_FORWARD);
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
		// 1. Calc the difference between both rotations
		// 2. Set the difference as camera rotation
		
		// remove the rotation of the player from the camera orientation
		Quaternionf thisRotation = getOrientation().getRotation(new Quaternionf());
		Quaternionf camRotation = that.getOrientation().getRotation(new Quaternionf());
		Quaternionf diff = thisRotation.difference(camRotation, new Quaternionf());

		forward = camera.getForwardDirection(forward);
		up = camera.getUpDirection(up);
		
		diff.transform(forward);
		diff.transform(up);
		
		camera.setPosition(0, 0, 0);
		camera.setOrientation(forward, up);
		
		
		setPoseModified();
	}


}
