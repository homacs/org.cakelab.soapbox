package org.cakelab.soapbox.testscene.blenderRaw;

import java.io.IOException;

import org.cakelab.oge.Registry;
import org.cakelab.oge.module.Module;
import org.cakelab.oge.module.ModuleRegistry;
import org.cakelab.oge.scene.Scene;
import org.cakelab.oge.shader.GLException;
import org.cakelab.oge.utils.blender.BlenderRaw;
import org.cakelab.soapbox.model.TriangleMesh;
import org.cakelab.soapbox.testscene.blenderRaw.resources.BlenderResources;

public class BlenderScene extends Scene implements Module {


	private final int moduleId;

	public BlenderScene () throws GLException, IOException {
		moduleId = ModuleRegistry.registerModule(this);
		BlenderMeshRenderer renderer = new BlenderMeshRenderer();
		Registry.registerRenderer(BlenderObject.class, renderer);

		BlenderRaw rawCubeMesh = new BlenderRaw(BlenderRaw.Format.TRIANGLES, BlenderResources.asInputStream(BlenderResources.CUBE_TRIANGLES));
		TriangleMesh cubeMesh = rawCubeMesh.getTriangleMesh();
		
		BlenderObject bobj = new BlenderCube(this, cubeMesh);
		
		add(bobj);
	}

	@Override
	public int getModuleId() {
		return moduleId;
	}

}
