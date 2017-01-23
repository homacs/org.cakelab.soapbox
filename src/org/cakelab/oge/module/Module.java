package org.cakelab.oge.module;


/** A module is a 'large' component of an application. 
 * <p>
 * Examples of modules are 
 * <ul>
 * <li> the render engine, </li>
 * <li> the physics engine, </li>
 * <li> the application logic engine, </li>
 * </ul>
 * </p><p>
 * <b>For performance reasons, the overall number of 
 * modules should be low!</b>
 * </p><p>
 * Modules are allowed to attach {@link ModuleData} to 
 * {@link ModuleDataContainer}s such as {@link Entity}s of the scene. 
 * The more modules attach data to entities the less performance you 
 * will get.
 * </p><p>
 * Modules have to register in the {@link ModuleRegistry} before using 
 * methods of {@link ModuleDataContainer}s.
 * </p>
 * */
public interface Module {
	/** This method has to return the module id, which was issued to 
	 * the module by call to {@link ModuleRegistry#registerModule(Module)}.
	 * @return
	 */
	int getModuleId();
	
}
