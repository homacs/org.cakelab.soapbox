package org.cakelab.soapbox.testscene.SBM_KTX.resources;

import org.cakelab.appbase.gui.GUIResourcesUtils;

public class Resources extends GUIResourcesUtils {
	private static final String RESOURCES_PATH = Resources.class.getPackage().getName().replace('.', '/') + '/';

	public static final String VERTEX_SHADER = RESOURCES_PATH + "render.vs.glsl";
	public static final String FRAGMENT_SHADER = RESOURCES_PATH + "render.fs.glsl";

	public static final String TEXTURE_PATTERN1 = RESOURCES_PATH + "pattern1.ktx";

	public static final String TORUS_VERTICES_SBM = RESOURCES_PATH + "torus_nrms_tc.sbm";
}
