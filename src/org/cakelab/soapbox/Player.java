package org.cakelab.soapbox;

import org.cakelab.oge.Camera;
import org.cakelab.oge.HeadCamera;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

// TODO player and its camera would be the first example of a group object
public class Player {
	private Camera camera;
	Vector3f translationVelocity = new Vector3f();
	Vector3f rotationVelocity = new Vector3f();
	private double lastTime = -1;
	private float velocityMultiplier = 1.0f;
	
	public Player() {
		setCamera(new HeadCamera(0f, 1.75f, +6f, 0f, 0f, 0f));
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
		
		Matrix4f orientation = getCamera().matrices.getOrientationTransform();
		
		direction.mul(orientation);
		
		Vector4f pos = new Vector4f(getCamera().getX(), getCamera().getY(), getCamera().getZ(), 1).add(direction);
		getCamera().setX(pos.x);
		getCamera().setY(pos.y);
		getCamera().setZ(pos.z);
		
	}


	/**
	 * +degree: turn left
	 * -degree: turn right
	 * @param degree
	 */
	public void addYaw(float degree) {
		getCamera().addYaw(degree);
	}
	
	
	/**
	 * +degree: turn up
	 * -degree: turn down
	 * @param degree
	 */
	public void addPitch(float degree) {
		getCamera().addPitch(degree);
	}
	
	/**
	 * +degree: roll counterclockwise
	 * -degree: roll clockwise
	 * @param degree
	 */
	public void addRoll(float degree) {
		getCamera().addRoll(degree);
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

	public void addRotation(float pitch, float yaw, int roll) {
		getCamera().addRotation(pitch, yaw, roll);
	}

	public void setVelocityMultiplyier(float f) {
		velocityMultiplier = f;
	}

	public Camera getCamera() {
		return camera;
	}

	public void setCamera(Camera camera) {
		this.camera = camera;
	}



}
