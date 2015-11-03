package org.cakelab.oge;

import java.util.HashMap;


public class Registry {

	private static HashMap<Class<? extends VisualObject>, Renderer> renderers = new HashMap<Class<? extends VisualObject>, Renderer>();
	
	public static void registerRenderer(Class<? extends VisualObject> modelClass, Renderer renderer) {
		renderers.put(modelClass,  renderer);
	}
	
	public static Renderer getRenderer(Class<? extends VisualObject> modelClass) {
		Renderer renderer = null;
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
		for (Renderer renderer : renderers.values()) {
			renderer.delete();
		}
	}
	
	

}
