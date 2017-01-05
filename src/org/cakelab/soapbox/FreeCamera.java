package org.cakelab.soapbox;

import org.cakelab.oge.Camera;
import org.cakelab.oge.scene.DynamicEntity;
import org.cakelab.oge.scene.Pose;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class FreeCamera extends Camera implements DynamicEntity, MovementAdapter {
	Vector3f translationVelocity = new Vector3f();
	Vector3f rotationVelocity = new Vector3f();
	private double lastTime = -1;
	private float velocityMultiplier = 1.0f;
	
	public FreeCamera() {
		super(0f, 1.75f, +6f, 0f, 0f, 0f);
	}
	
	@Override
	public void init(Pose pose) {
		set(pose);
	}

	@Override
	public void moveForward(float amount) {
		moveAlong(0, 0, -amount);
	}

	@Override
	public void moveBackward(float amount) {
		moveAlong(0, 0, +amount);
	}

	@Override
	public void moveUp(float amount) {
		moveAlong(0, +amount, 0);
	}

	@Override
	public void moveDown(float amount) {
		moveAlong(0, -amount, 0);
	}

	@Override
	public void moveLeft(float amount) {
		moveAlong(-amount, 0, 0);
	}

	@Override
	public void moveRight(float amount) {
		moveAlong(+amount, 0, 0);
	}

	private void moveAlong(float x, float y, float z) {

		Vector4f direction = new Vector4f(x, y, z, 1);
		
		Matrix4f orientation = matrices.getOrientationTransform();
		
		direction.mul(orientation);
		
		Vector4f pos = new Vector4f(getPosition(), 1).add(direction);
		setX(pos.x);
		setY(pos.y);
		setZ(pos.z);
		
	}


	/**
	 * +degree: turn left
	 * -degree: turn right
	 * @param degree
	 */
	public void addYaw(float degree) {
		addLocalYaw(degree);
	}
	
	
	/**
	 * +degree: turn up
	 * -degree: turn down
	 * @param degree
	 */
	@Override
	public void addPitch(float degree) {
		addLocalPitch(degree);
	}
	
	/**
	 * +degree: roll counterclockwise
	 * -degree: roll clockwise
	 * @param degree
	 */
	@Override
	public void addRoll(float degree) {
		addLocalRoll(degree);
	}
	
	
	@Override
	public void addTranslationVelocity(float x, float y, float z) {
		translationVelocity.add(x, y, z);
	}

	@Override
	public void addRotationVelocity(float yaw, float pitch, float roll) {
		rotationVelocity.add(yaw, pitch, roll);
	}

	@Override
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

	@Override
	public void addRotation(float pitch, float yaw, float roll) {
		addLocalRotation(pitch, yaw, roll);
	}

	@Override
	public void setVelocityMultiplyier(float f) {
		velocityMultiplier = f;
	}

}
