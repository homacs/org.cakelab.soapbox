package org.cakelab.oge.module;


/**
 * An implementation of {@link ModuleDataContainer}.
 * <p>
 * Just a lightweight array list which adapts its size 
 * to the number of registered modules. Can be used to 
 * implement the methods of {@link ModuleDataContainer}.
 * </p><p>
 * Also provides static methods to implement 
 * {@link ModuleDataContainer} on an array. Using those 
 * removes another level of indirection (faster).
 * </p>
 * @author homac
 *
 */
public class ModuleDataContainerImpl implements ModuleDataContainer {
	private ModuleData[] store = new ModuleData[ModuleRegistry.INITIAL_SIZE];
	
	@Override
	public void setModuleData(final int moduleId, ModuleData data) {
		store = checkSize(store);
		setModuleData(store, moduleId, data);
	}
	
	@Override
	public ModuleData getModuleData(final int moduleId) {
		store = checkSize(store);
		return getModuleData(store, moduleId);
	}


	/**
	 * Adapts the size of the given array to the number 
	 * of registered modules and returns the possibly 
	 * adapted array with the copied content.
	 * @param store array to be checked and possibly resized
	 * @return copy of the resized array
	 */
	public static ModuleData[] checkSize(ModuleData[] store) {
		int numModules = ModuleRegistry.getNumModules();
		if (numModules > store.length) {
			ModuleData[] tmp = new ModuleData[store.length<<1];
			System.arraycopy(store, 0, tmp, 0, store.length);
			return tmp;
		}
		return store;
	}

	/**
	 * Static method to implement {@link ModuleDataContainer#getModuleData(int)} 
	 * on an array. No resizing (see {@link #checkSize(ModuleData[])}).
	 * @see #checkSize(ModuleData[])
	 * @param store Array to retrieve data from.
	 * @param moduleId
	 * @return Module data found
	 */
	public static ModuleData getModuleData(ModuleData[] store, int moduleId) {
		assert (ModuleRegistry.isRegisteredModuleId(moduleId));
		return store[moduleId];
	}
	
	/**
	 * Static method to implement {@link ModuleDataContainer#setModuleData(int, ModuleData)} 
	 * on an array. No resizing (see {@link #checkSize(ModuleData[])}).
	 * @see #checkSize(ModuleData[])
	 * @param store Array to store data in.
	 * @param moduleId
	 */
	public static void setModuleData(ModuleData[] list, int moduleId, ModuleData data) {
		assert (ModuleRegistry.isRegisteredModuleId(moduleId));
		list[moduleId] = data;
	}

	
}
