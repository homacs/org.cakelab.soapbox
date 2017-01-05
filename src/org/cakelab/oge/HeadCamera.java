package org.cakelab.oge;

import org.cakelab.oge.utils.HeadCameraMatrices;
import org.joml.Quaternionf;
import org.joml.Vector3f;


/**
 * Head camera is suppose to act like the user is turning its 
 * head, which means it is limited in angles and changes 
 * in yaw are always independent of the current orientation 
 * of the head (as if the user would turn his body instead).
 * 
 * @author homac
 */
public class HeadCamera extends Camera {
	// TODO head camera is nonsense! 
	// CHANGE: Player moves and camera movement is 
	// restricted by the player class, not by the camera itself.
	
	private float yaw;
	private float pitch;
	private float roll;
	private int recursion;
	
	
	public HeadCamera() {
		super(0,0,0,0,0,0);
		matrices = new HeadCameraMatrices(this);
	}

	public HeadCamera(float x, float y, float z, float pitch, float yaw, float roll) {
		super(x, y, z, pitch, yaw, roll);
		matrices = new HeadCameraMatrices(this);
	}

	public HeadCamera(float x, float y, float z, Vector3f forward, Vector3f up) {
		super(x, y, z, forward, up);
		matrices = new HeadCameraMatrices(this);
	}

	public void set(Camera that) {
		super.set(that);
		Quaternionf rotation = new Quaternionf().lookRotate(that.getForwardDirection().negate(), that.getUpDirection());
		Vector3f euler = new Vector3f();
		rotation.invert();
		rotation.getEulerAnglesXYZ(euler);
		this.yaw = euler.y;
		this.roll = euler.z;
		this.pitch = euler.x;
		
		setPoseModified();
	}

	public void addPitch(float degree) {
		addLocalPitch(degree);
	}

	@Override
	public void addLocalPitch(float degree) {
		recursion++;
		float newPitch = this.pitch + degree;
		if (newPitch > 90) {
			degree -= newPitch -  90;
		} else if (newPitch < -90) {
			degree -= newPitch - -90;
		}
		if (degree != 0) { 
			this.pitch += degree;
			super.addLocalPitch(degree);
		}
		recursion--;
	}


	public void addRoll(float degree) {
		addLocalRoll(degree);
	}
	
	@Override
	public void addLocalRoll(float degree) {
		final float maxRoll = 45;
		recursion++;
		float newRoll = this.roll + degree;
		if (newRoll > maxRoll) {
			degree -= newRoll -  maxRoll;
		} else if (newRoll < -maxRoll) {
			degree -= newRoll - -maxRoll ;
		}
		if (degree != 0) { 
			this.roll += degree;
			super.addLocalRoll(degree);
		}
		recursion--;
	}

	public void addLocalYaw(float degree) {
		addYaw(degree);
	}
	
	public void addYaw(float degree) {
		recursion++;
		yaw += degree;
		super.addYaw(degree);
		recursion--;
	}
	
	
	@Override
	public void addLocalRotation(float pitch, float yaw, float roll) {
		if (recursion > 0) super.addLocalRotation(pitch, yaw, roll);
		else {
			addLocalPitch(pitch);
			addYaw(yaw);
			addLocalRoll(roll);
		}
	}

	@Override
	public void addRotation(float pitch, float yaw, float roll) {
		addLocalRotation(pitch, yaw, roll);
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
