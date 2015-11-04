package org.cakelab.soapbox.testscene.hud;

import java.io.IOException;

import org.cakelab.oge.Registry;
import org.cakelab.oge.Scene;
import org.cakelab.oge.shader.GLException;

public class HudScene extends Scene {
	public HudScene() throws GLException, IOException {
		HudRenderer cubeRenderer = new HudRenderer();
		Registry.registerRenderer(HudObject.class, cubeRenderer);
		
		add(new HudObject());
	}

	
}
