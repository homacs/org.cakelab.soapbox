package org.cakelab.oge;

import java.util.HashMap;

import org.cakelab.oge.scene.VisualEntity;
import org.cakelab.oge.utils.SingleProgramRendererBase;


public class Registry {

	private static HashMap<Class<? extends VisualEntity>, SingleProgramRendererBase> renderers = new HashMap<Class<? extends VisualEntity>, SingleProgramRendererBase>();
	
	public static void registerRenderer(Class<? extends VisualEntity> modelClass, SingleProgramRendererBase renderer) {
		renderers.put(modelClass,  renderer);
	}
	
	public static SingleProgramRendererBase getRenderer(Class<? extends VisualEntity> modelClass) {
		SingleProgramRendererBase renderer = null;
		Class<?> clazz = modelClass;
		while(renderer == null && !clazz.equals(Object.class)) {
			renderer = renderers.get(clazz);
			if (renderer == null) {
				clazz = clazz.getSuperclass();
			}
		}
		return renderer;
	}

	public static void delete() {
		for (SingleProgramRendererBase renderer : renderers.values()) {
			renderer.delete();
		}
	}
	
	

}
