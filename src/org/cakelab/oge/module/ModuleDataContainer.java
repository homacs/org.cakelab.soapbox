package org.cakelab.oge.module;

/**
 * Objects implementing {@link ModuleDataContainer} allow {@link Module}s 
 * to attach {@link ModuleData} to it using the 
 * {@link #setModuleData(int, ModuleData)} method.
 * The data is associated with the module id and can only retrieved via
 * {@link #getModuleData(int)} when using the same id.
 * 
 * @author homac
 *
 */
public interface ModuleDataContainer {
	/** 
	 * Retrieve the data once set with the method {@link #setModuleData(int, ModuleData)}.
	 * @param moduleId Id of the module, which originally stored the data.
	 * @return data stored in this container for the given module.
	 */
	ModuleData getModuleData(int moduleId);
	/**
	 * Store data of a module in this container.
	 * @param moduleId Id of the module, which wants to store data.
	 * @param data Data to be stored.
	 */
	void setModuleData(int moduleId, ModuleData data);
}
