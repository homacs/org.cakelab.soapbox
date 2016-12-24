package org.cakelab.oge.scene;

import org.cakelab.oge.utils.ObjectMatrices;

public abstract class VisualObject extends Pose {
	// TODO object groups
	
	private ModuleData renderData;

	private ObjectMatrices matrices;

	private Material material;

	public VisualObject(Material material) {
		this(material,0,0,0);
	}
	
	public VisualObject(Material material, float x, float y, float z) {
		super(x,y,z);
		this.material = material;
		matrices = new ObjectMatrices(this);
	}

	public Material getMaterial() {
		return material;
	}

	/**
	 * Retrieve render engine specific data.
	 */
	public ModuleData getRenderData() {
		return renderData;
	}

	/**
	 * Set render engine specific data.
	 */
	public void setRenderData(ModuleData renderData) {
		this.renderData = renderData ;
	}

	public ObjectMatrices getMatrices() {
		return matrices;
	}

}
