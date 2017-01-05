package org.cakelab.soapbox;

import org.cakelab.oge.scene.DynamicEntity;
import org.cakelab.oge.scene.Pose;

public interface MovementAdapter extends DynamicEntity {

	void moveForward(float amount);
	void moveBackward(float amount);
	void moveUp(float amount);
	void moveDown(float amount);
	void moveLeft(float amount);
	void moveRight(float amount);
	void addRotation(float pitch, float yaw, float roll);
	
	void setVelocityMultiplyier(float f);
	void addTranslationVelocity(float x, float y, float z);
	void addRotationVelocity(float yaw, float pitch, float roll);
	
	void init(Pose pose);
}
