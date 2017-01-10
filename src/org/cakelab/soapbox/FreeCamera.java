package org.cakelab.soapbox;

import org.cakelab.oge.Camera;
import org.cakelab.oge.scene.DynamicEntity;
import org.cakelab.oge.scene.Pose;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class FreeCamera extends Camera implements DynamicEntity, MovementAdapter {
	private Vector3f translationVelocity = new Vector3f();
	private Vector3f rotationVelocity = new Vector3f();
	private double lastTime = -1;
	private float velocityMultiplier = 1.0f;
	private Vector3f tmpV = new Vector3f();
	private Quaternionf tmpQuat = new Quaternionf();
	
	public FreeCamera() {
		super(0f, 1.75f, +6f, 0f, 0f, 0f);
	}
	
	public void init(Camera camera) {
		set(camera);
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
		Vector3f direction = tmpV.set(x, y, z);
		Quaternionf orientation = getOrientation().getRotation(tmpQuat );
		
		orientation.transform(direction);
		
		Vector3f pos = direction.add(getPosition());
		setPosition(pos);
		
	}


	/**
	 * +degree: turn left
	 * -degree: turn right
	 * @param degree
	 */
	public void addYaw(float degree) {
		addLocalYaw((float) Math.toRadians(degree));
	}
	
	
	/**
	 * +degree: turn up
	 * -degree: turn down
	 * @param degree
	 */
	@Override
	public void addPitch(float degree) {
		addLocalPitch((float) Math.toRadians(degree));
	}
	
	/**
	 * +degree: roll counterclockwise
	 * -degree: roll clockwise
	 * @param degree
	 */
	@Override
	public void addRoll(float degree) {
		addLocalRoll((float) Math.toRadians(degree));
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
		addLocalRotation((float) Math.toRadians(pitch), (float) Math.toRadians(yaw), (float) Math.toRadians(roll));
	}

	@Override
	public void setVelocityMultiplyier(float f) {
		velocityMultiplier = f;
	}

}
