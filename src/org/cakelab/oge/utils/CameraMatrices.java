package org.cakelab.oge.utils;

import org.cakelab.oge.app.GlobalClock;
import org.cakelab.oge.scene.Pose;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class CameraMatrices {
	protected double lastUpdate = 0;
	
	protected Matrix4f viewTransform = new Matrix4f();
	protected Matrix4f orientationTransform = new Matrix4f();

	private Quaternionf tempQuat = new Quaternionf();

	protected Pose pose;

	public CameraMatrices(Pose pose) {
		this.pose = pose;
	}
	
	protected CameraMatrices() {
	}

	public Matrix4f getOrientationTransform() {
		update();
		return orientationTransform;
	}
	
	public Matrix4f getViewTransform() {
		update();
		return viewTransform;
	}

	public void update() {
		if (pose.isPoseModified(lastUpdate) 
				|| pose.getReferenceSystem() != null && pose.getReferenceSystem().isPoseModified(lastUpdate)) {
			applyModifications();
			lastUpdate = GlobalClock.getCurrentTime();
		}			
	}
	
	protected void applyModifications() {
		Pose reference = pose.getReferenceSystem();

		if (reference == null) {

			orientationTransform.identity()
				.lookAlong(pose.getForwardDirection(), pose.getUpDirection()).invert();
			
			viewTransform.identity()
				.translate(pose.getPosition())
				.mul(orientationTransform)
				.invert()
				;
		
		} else {
			// with reference system:
			// 1. rotate locally
			// 2. move to position from 0,0
			// 3. rotate according to reference system
			// 4. translate according to reference system

			orientationTransform.identity()
				.lookAlong(pose.getForwardDirection(), pose.getUpDirection())
				.invert()
				;
		
			System.out.println("cam: " + pose.getPosition() + pose.getForwardDirection() + pose.getUpDirection());
			System.out.println("ref: " + reference.getPosition() + reference.getForwardDirection() + reference.getUpDirection());
			
			Matrix4f referenceOrientation = new Matrix4f()
					.lookAlong(reference.getForwardDirection(), reference.getUpDirection())
					.invert();
			
			viewTransform.identity()
				.translate(reference.getPosition())
				.mul(referenceOrientation)
				.translate(pose.getPosition())
				.mul(orientationTransform)
				.invert()
				;
		}
	}

	public Quaternionf getRotationQuaternion() {
		Vector3f eye = new Vector3f(pose.getForwardDirection());
		Vector3f up = new Vector3f(pose.getUpDirection());
		return tempQuat.identity().lookRotate(eye.negate(), up).rotate(0,180,0);
	}

}
