package org.cakelab.soapbox;

import org.cakelab.oge.Camera;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class Player {
	Camera camera;
	Vector3f translationVelocity = new Vector3f();
	Vector3f rotationVelocity = new Vector3f();
	private double lastTime = -1;
	
	public Player() {
		camera = new Camera(0f, 0f, +6f, 0f, 0f, 0f);
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
		System.out.println("move: " + x + ", " + y + ", " + z);

		Vector4f direction = new Vector4f(x, y, z, 1);
		
		Matrix4f orientation = new Matrix4f(camera.getOrientationTransform());
		
		direction.mul(orientation);
		
		System.out.println("dir: " + direction.x + ", " + direction.y + ", " + direction.z);
		Vector4f pos = new Vector4f(camera.getX(), camera.getY(), camera.getZ(), 1).add(direction);
		camera.setX(pos.x);
		camera.setY(pos.y);
		camera.setZ(pos.z);
		
		dumpCamera();
	}


	private void dumpCamera() {
		System.out.println("cam.pos: " + camera.getX() + ", " + camera.getY() + ", " + camera.getZ());
//		System.out.println("cam.ori: " + camera.getYaw() + "°, " + camera.getPitch() + "°, " + camera.getRoll() + "°");
	}

	/**
	 * +degree: turn left
	 * -degree: turn right
	 * @param degree
	 */
	public void addYaw(float degree) {
		camera.addYaw(degree);
		dumpCamera();
	}
	
	
	/**
	 * +degree: turn up
	 * -degree: turn down
	 * @param degree
	 */
	public void addPitch(float degree) {
		camera.addPitch(degree);
		dumpCamera();
	}
	
//	public void setRoll(float degree) {
//		camera.setRoll(degree);
//	}

	/**
	 * +degree: roll counterclockwise
	 * -degree: roll clockwise
	 * @param degree
	 */
	public void addRoll(float degree) {
		camera.addRoll(degree);
		dumpCamera();
	}
	
//	private float addDegree(float degree, float amount) {
//		degree += amount;
//		degree = degree%360;
//		return degree;
//	}

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
		moveAlong(translationVelocity.x*time, translationVelocity.y*time, translationVelocity.z*time);
		addYaw(rotationVelocity.x);
		addPitch(rotationVelocity.y);
		addRoll(rotationVelocity.z);
	}

	public void addRotation(float pitch, float yaw, int roll) {
		camera.addRotation(pitch, yaw, roll);
	}

	public void addPitchAbsolute(float pitch) {
		camera.addPitchAbsolute(pitch);
	}

	public void addYawAbsolute(float yaw) {
		camera.addYawAbsolute(yaw);
	}


}
