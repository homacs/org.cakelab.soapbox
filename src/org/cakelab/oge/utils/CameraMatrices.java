package org.cakelab.oge.utils;

import org.cakelab.oge.app.GlobalClock;
import org.cakelab.oge.scene.Pose;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

public class CameraMatrices {
	protected double lastUpdate = 0;
	
	protected Matrix4f viewTransform = new Matrix4f();
	protected Matrix4f orientationTransform = new Matrix4f();

	private Quaternionf tempQuat = new Quaternionf();

	protected Pose pose;

	public CameraMatrices(Pose pose) {
		this.pose = pose;
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
		if (pose.isPoseModified(lastUpdate)) {
			applyModifications();
			lastUpdate = GlobalClock.getCurrentTime();
		}			
	}
	
	
	protected void applyModifications() {
		Quaternionf qRotate = getRotationQuaternion();
		
		orientationTransform.identity()
			.rotate(qRotate);
		viewTransform.identity()
			.translate(pose.getPosition())
			.rotate(qRotate)
			.invert()
			;
			
	}

	public Quaternionf getRotationQuaternion() {
		return tempQuat.identity().lookRotate(pose.getForwardDirection(), pose.getUpDirection()).invert();
	}

}
