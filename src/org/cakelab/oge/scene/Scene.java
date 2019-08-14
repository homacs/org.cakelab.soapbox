package org.cakelab.oge.scene;

import java.util.ArrayList;


/**
 * <h4>Entites</h4>
 * Entities may have different properties in regards of rendering 
 * and physics assigned by implementing at least one of these interfaces: 
 * <ul>
 * <li>{@link VisualEntity} refers to entities that can be displayed.</li>
 * <li>{@link DynamicEntity} refers to entities that have time dependent properties, such as changing position, rotation etc..</li>
 * 
 * @author homac
 *
 */
public class Scene {

	/**
	 * All entities in the scene.
	 */
	ArrayList<Entity> entities = new ArrayList<Entity>();

	/**
	 * All visual objects.
	 */
	ArrayList<VisualEntity> visuals = new ArrayList<VisualEntity>();

	
	/**
	 * Subset of entites which are dynamic objects.
	 */
	ArrayList<DynamicEntity> dynamics = new ArrayList<DynamicEntity>();
	
	/**
	 * Subset of light sources.
	 */
	ArrayList<LightSource> lights = new ArrayList<LightSource>();
	
	public void add(Entity entity) {
		assert (entity instanceof VisualEntity || entity instanceof DynamicEntity || entity instanceof LightSource);
		entities.add(entity);
		if (entity instanceof VisualEntity) visuals.add((VisualEntity) entity);
		if (entity instanceof DynamicEntity) dynamics.add((DynamicEntity) entity);
		if (entity instanceof LightSource) lights.add((LightSource)entity);
	}

	
	public ArrayList<Entity> getAllEntities() {
		return entities;
	}

	public ArrayList<VisualEntity> getVisualEntities() {
		return visuals;
	}

	public ArrayList<DynamicEntity> getDynamicEntities() {
		return dynamics;
	}

	public ArrayList<LightSource> getLightSources() {
		return lights;
	}

	@SuppressWarnings("unlikely-arg-type")
	public void remove(Entity entity) {
		assert (entity instanceof VisualEntity || entity instanceof DynamicEntity || entity instanceof LightSource);
		entities.remove(entity);
		if(entity instanceof VisualEntity) visuals.remove(entity);
		if (entity instanceof DynamicEntity) dynamics.remove(entity);
		if (entity instanceof LightSource) dynamics.remove(entity);
	}
	

}
