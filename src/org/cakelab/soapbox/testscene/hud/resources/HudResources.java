package org.cakelab.soapbox.testscene.hud.resources;


import org.cakelab.appbase.gui.GUIResourcesUtils;

public class HudResources extends GUIResourcesUtils {
	private static final String RESOURCES_PATH = HudResources.class.getPackage().getName().replace('.', '/') + '/';
	public static final String TEST_TEXTURE_UNCOMPRESSED = RESOURCES_PATH + "test-16x16-uncompressed.png";
	public static final String VERTEX_SHADER = RESOURCES_PATH + "render.vs.glsl";
	public static final String FRAGMENT_SHADER = RESOURCES_PATH + "render.fs.glsl";

}
