package org.cakelab.oge.scene;

/**
 * Entities implementing DynamicEntity and inserted in the Scene 
 * will get their {@link #update(double)} method called on each frame.
 * To be visible, they have to inherit VisualEntity too.
 * @author homac
 *
 */
public interface DynamicEntity {
	public abstract void update(double currentTime);
}
