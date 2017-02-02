package org.cakelab.oge.app;


import org.cakelab.oge.shader.GLException;
import org.lwjgl.glfw.*;
import org.lwjgl.system.Platform;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*; // for constants like GL_TRUE
import static org.lwjgl.system.MemoryUtil.*;




public abstract class ApplicationBase extends AbstractAplicationBase {
	

	
	public ApplicationBase(String windowTitle) throws GLException {
		if (!glfwInit()) {
			throw new GLException("Failed to initialize GLFW\n");
		}

		info.title = windowTitle;
		info.setWindowWidth(800);
		info.setWindowHeight(600);
		if (Platform.get()== Platform.MACOSX) {
			info.majorVersion = 3;
			info.minorVersion = 2;
		} else {
			info.majorVersion = 4;
			info.minorVersion = 3;
		}
		info.settings.samples = 0;
		info.settings.cursor = true;
		info.settings.fullscreen = false;
		info.settings.stereo = false;
		info.settings.vsync = false;
		if (DEBUG) {
			info.settings.debug = true;
		}

	}
	
	
	
	@Override
	public void createWindow() {

		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, info.majorVersion);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, info.minorVersion);
		
		if (DEBUG) {
			glfwWindowHint(GLFW_OPENGL_DEBUG_CONTEXT, GL_TRUE);
		}
		
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
		glfwWindowHint(GLFW_SAMPLES, info.settings.samples);
		glfwWindowHint(GLFW_STEREO, info.settings.stereo ? GL_TRUE : GL_FALSE);

		glfwWindowHint(GLFW_ALPHA_BITS, 16);
		glfwWindowHint(GLFW_DEPTH_BITS, 32);
		glfwWindowHint(GLFW_STENCIL_BITS, 0);
		long monitor = 0;
		if (info.settings.fullscreen) {
			monitor = glfwGetPrimaryMonitor();
			GLFWVidMode mode = glfwGetVideoMode(monitor);

			info.setWindowWidth(mode.width());
			info.setWindowHeight(mode.height());
			glfwWindowHint(GLFW_RED_BITS, mode.redBits());
			glfwWindowHint(GLFW_GREEN_BITS, mode.greenBits());
			glfwWindowHint(GLFW_BLUE_BITS, mode.blueBits());
		} else {
			glfwWindowHint(GLFW_RED_BITS, 8);
			glfwWindowHint(GLFW_GREEN_BITS, 8);
			glfwWindowHint(GLFW_BLUE_BITS, 8);
		}
		
		glfwWindowHint(GLFW_STEREO, info.settings.stereo?1:0);

		window = glfwCreateWindow(info.getWindowWidth(), info.getWindowHeight(),
				info.title, monitor, 0);

		if (window == NULL) {
			glfwTerminate();
			throw new RuntimeException("Failed to open window\n");
		}
	}




}
