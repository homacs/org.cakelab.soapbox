package org.cakelab.oge;

import org.cakelab.oge.app.ApplicationContext;
import org.cakelab.oge.scene.VisualEntity;

public interface Renderer {
	void delete();
	
	void prepare(ApplicationContext context, double frameTime);
	void render(ApplicationContext context, double frameTime, VisualEntity o);
	
	boolean needsNormals();
	boolean needsUv();
}
