package org.cakelab.soapbox.testscene.blenderRaw;


import org.cakelab.oge.module.Module;
import org.cakelab.oge.opengl.BufferObject.Usage;
import org.cakelab.oge.opengl.MeshVertexArray;
import org.cakelab.oge.scene.Material;
import org.cakelab.oge.scene.VisualMeshEntity;
import org.cakelab.oge.shader.GLException;
import org.cakelab.oge.utils.OGEMeshRenderData;
import org.cakelab.soapbox.model.Mesh;

public abstract class BlenderObject extends VisualMeshEntity {


	public BlenderObject(Module module, Mesh mesh) throws GLException {
		this(module, mesh, 0,0,0);
	}

	public BlenderObject(Module module, Mesh mesh, float x, float y, float z) throws GLException {
		super(mesh, new Material(), x,y,z);
		OGEMeshRenderData renderData = new OGEMeshRenderData(new MeshVertexArray(mesh, 0, Usage.STATIC_DRAW), mesh.getGlDrawingMethod(), mesh.getNumVertices());
		super.setModuleData(module.getModuleId(), renderData);
	}

}
