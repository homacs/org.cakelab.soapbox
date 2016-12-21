package org.cakelab.oge;

import org.cakelab.oge.utils.HeadCameraMatrices;

public class HeadCamera extends Camera {
	// TODO head camera is weird .. needs work
	// pitch, yaw and roll actually part of pose?
	
	private float yaw;
	private float pitch;
	private float roll;
	
	
	public HeadCamera(float x, float y, float z, float pitch, float yaw, float roll) {
		super(x, y, z, pitch, yaw, roll);
		matrices = new HeadCameraMatrices(this);
	}



	@Override
	public void addPitch(float pitch) {
		float newPitch = Math.max(Math.min(this.pitch + pitch, 90), -90);
		if (newPitch != this.pitch) { 
			setPoseModified();
			this.pitch = newPitch;
		}
	}


	@Override
	public void addYaw(float yaw) {
		setPoseModified();
		this.yaw += yaw;
	}


	@Override
	public void addRoll(float roll) {
		float newRoll = Math.max(Math.min(this.roll + roll, 90), -90);
		if (newRoll != this.roll) {
			setPoseModified();
			this.roll = newRoll;
		}
		
	}


	@Override
	public void addRotation(float pitch, float yaw, float roll) {
		addPitch(pitch);
		addYaw(yaw);
		addRoll(roll);
	}

	public float getYaw() {
		return yaw;
	}

	public float getPitch() {
		return pitch;
	}

	public float getRoll() {
		return roll;
	}
	
}
