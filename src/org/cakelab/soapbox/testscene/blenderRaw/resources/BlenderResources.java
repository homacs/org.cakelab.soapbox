package org.cakelab.soapbox.testscene.blenderRaw.resources;


import org.cakelab.appbase.gui.GUIResourcesUtils;

public class BlenderResources extends GUIResourcesUtils {
	private static final String RESOURCES_PATH = BlenderResources.class.getPackage().getName().replace('.', '/') + '/';
	public static final String CUBE_TRIANGLES = RESOURCES_PATH + "cube.raw";
	public static final String CUBE_TEXTURE = RESOURCES_PATH + "cube-texture.png";

}
