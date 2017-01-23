package org.cakelab.oge.module;

/**
 * The registry for {@link Module}s.
 * 
 * @author homac
 *
 */
public class ModuleRegistry {
	// have 0 as invalid id to prevent unregistered modules from errors.
	public static final int INVALID_MODULE_ID = 0;

	/** 
	 * This is a reasonable initial size for module data containers 
	 * assuming a set of modules consisting of render engine, 
	 * physics engine and application logic engine.
	 */
	public static final int INITIAL_SIZE = 4;

	/** Maximum amount of modules that can be registered.
	 * <p><b>Note to developers</b> Increasing the number
	 * of registered modules will have notable impact on 
	 * performance since every entity will have to adapt 
	 * its module data container size to the number of modules.
	 * </p> */
	public static final int MAXIMUM_SIZE = 32;
	
	private static Module[] registeredModules;
	private static int size;
	
	
	static {
		registeredModules = new Module[MAXIMUM_SIZE];
		// first place is reserved for INVALID_MODULE_ID
		size = 1;
	}
	
	

	/**
	 * Registers a module to use data stored in module data containers.
	 * It returns the id issued for this module. Best practice is to 
	 * store the id in a final variable.
	 * @param module Module to be registered
	 * @return Issued id for the registered module (final)
	 * @throws IndexOutOfBoundException if registered number of modules exceeds {@link #MAXIMUM_SIZE}
	 */
	public static int registerModule(Module module) {
		int id = module.getModuleId();
		if (id <= INVALID_MODULE_ID) {
			id = size++;
			registeredModules[id] = module;
		}
		return id;
	}
	
	/**
	 * For debugging purposes (asserts).
	 * 
	 * Checks whether the given module has a valid module id and 
	 * is registered with this id.
	 * 
	 * @param module Module to be checked
	 * @return 
	 */
	public static boolean isRegisteredModule(Module module) {
		int id = module.getModuleId();
		if (isRegisteredModuleId(id)) {
			return false;
		} else {
			Module registered = registeredModules[id];
			return module.equals(registered);
		}
	}

	/**
	 * For debugging purposes (asserts).
	 * Checks whether the given module id is valid.
	 * 
	 * @param id
	 * @return
	 */
	public static boolean isRegisteredModuleId(int id) {
		return id > INVALID_MODULE_ID && id < MAXIMUM_SIZE;
	}

	/**
	 * @return Returns the number of modules currently registered.
	 */
	public static int getNumModules() {
		return size;
	}
}
