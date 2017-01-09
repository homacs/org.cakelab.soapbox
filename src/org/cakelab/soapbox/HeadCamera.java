package org.cakelab.soapbox;

import org.cakelab.oge.Camera;
import org.cakelab.oge.math.Orientation;
import org.cakelab.oge.utils.HeadCameraMatrices;
import org.joml.Vector3f;
import org.joml.Vector3fc;


/**
 * Head camera is suppose to act like the user is turning its 
 * head, which means it is limited in angles and changes 
 * in yaw are always independent of the current orientation 
 * of the head (as if the user would turn his body instead).
 * 
 * @author homac
 */
public class HeadCamera extends Camera {
	private static final float DEGREES_90 = (float) Math.toRadians(90);
	private static final float DEGREES_45 = (float) Math.toRadians(45);
	
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
		updateRotationAngles();
		
		setPoseModified();
	}

	
	private void updateRotationAngles() {
		Orientation rotation = getOrientation();
		Vector3f euler = new Vector3f();
		rotation.getEulerAnglesYXZ(euler);
		
		this.yaw = euler.y;
		this.roll = euler.z;
		this.pitch = euler.x;
		
		
		System.out.println(Math.toDegrees(yaw) + " " + Math.toDegrees(pitch) + " " + Math.toDegrees(roll));
	}
	
	
	public void setOrientation(Vector3fc forward, Vector3fc up) {
		super.setOrientation(forward, up);
		updateRotationAngles();
		setPoseModified();
	}

	public void addPitch(float degree) {
		addLocalPitch(degree);
	}

	@Override
	public void addLocalPitch(float degree) {
		
		recursion++;
		float newPitch = this.pitch + degree;
		if (newPitch > DEGREES_90) {
			degree -= newPitch -  DEGREES_90;
		} else if (newPitch < -DEGREES_90) {
			degree -= newPitch - -DEGREES_90;
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
		final float maxRoll = DEGREES_45;
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
