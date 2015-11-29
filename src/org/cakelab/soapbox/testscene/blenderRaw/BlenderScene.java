package org.cakelab.soapbox.testscene.blenderRaw;

import java.io.IOException;

import org.cakelab.oge.Registry;
import org.cakelab.oge.RenderAssets;
import org.cakelab.oge.Scene;
import org.cakelab.oge.shader.GLException;
import org.cakelab.oge.utils.blender.BlenderRaw;
import org.cakelab.soapbox.model.TriangleMesh;
import org.cakelab.soapbox.testscene.blenderRaw.resources.BlenderResources;

public class BlenderScene extends Scene {

	public BlenderScene () throws GLException, IOException {
		
		BlenderMeshRenderer renderer = new BlenderMeshRenderer();
		Registry.registerRenderer(BlenderObject.class, renderer);

		BlenderRaw rawCubeMesh = new BlenderRaw(BlenderRaw.Format.TRIANGLES, BlenderResources.asInputStream(BlenderResources.CUBE_TRIANGLES));
		TriangleMesh cubeMesh = rawCubeMesh.getTriangleMesh();
		RenderAssets cubeAssets = new RenderAssets(cubeMesh);
		
		BlenderObject bobj = new BlenderCube(cubeAssets);
		
		add(bobj);
	}
}
