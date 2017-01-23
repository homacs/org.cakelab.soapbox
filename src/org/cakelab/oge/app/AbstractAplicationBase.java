package org.cakelab.oge.app;

import static org.lwjgl.glfw.GLFW.GLFW_CURSOR;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR_DISABLED;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR_HIDDEN;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR_NORMAL;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPosCallback;
import static org.lwjgl.glfw.GLFW.glfwSetInputMode;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetMouseButtonCallback;
import static org.lwjgl.glfw.GLFW.glfwSetScrollCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.glfw.GLFW.glfwSetWindowSizeCallback;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.AMDDebugOutput.GL_DEBUG_CATEGORY_API_ERROR_AMD;
import static org.lwjgl.opengl.AMDDebugOutput.GL_DEBUG_CATEGORY_DEPRECATION_AMD;
import static org.lwjgl.opengl.AMDDebugOutput.GL_DEBUG_CATEGORY_UNDEFINED_BEHAVIOR_AMD;
import static org.lwjgl.opengl.AMDDebugOutput.GL_DEBUG_CATEGORY_WINDOW_SYSTEM_AMD;
import static org.lwjgl.opengl.GL11.GL_RENDERER;
import static org.lwjgl.opengl.GL11.GL_VENDOR;
import static org.lwjgl.opengl.GL11.GL_VERSION;
import static org.lwjgl.opengl.GL11.glGetString;
import static org.lwjgl.opengl.GL43.GL_DEBUG_SEVERITY_HIGH;
import static org.lwjgl.opengl.GL43.GL_DEBUG_SEVERITY_LOW;
import static org.lwjgl.opengl.GL43.GL_DEBUG_SEVERITY_MEDIUM;
import static org.lwjgl.opengl.GL43.GL_DEBUG_SEVERITY_NOTIFICATION;
import static org.lwjgl.opengl.GL43.GL_DEBUG_TYPE_DEPRECATED_BEHAVIOR;
import static org.lwjgl.opengl.GL43.GL_DEBUG_TYPE_ERROR;
import static org.lwjgl.opengl.GL43.GL_DEBUG_TYPE_PORTABILITY;
import static org.lwjgl.opengl.GL43.GL_DEBUG_TYPE_UNDEFINED_BEHAVIOR;

import org.cakelab.appbase.log.Log;
import org.cakelab.lwjgl.GLFWException;
import org.cakelab.oge.shader.GLException;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWErrorCallbackI;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLCapabilities;
import org.lwjgl.system.MemoryUtil;

public abstract class AbstractAplicationBase {
	public static final boolean DEBUG = false;

	protected Info info = new Info();
	protected DebugMessageHandler debugMessageHandler;
	protected int exitStatus;
	protected GLFWWindowSizeCallback windowSizeCB;
	protected GLFWKeyCallback keyCB;
	protected GLFWMouseButtonCallback mouseButtonCB;
	protected GLFWCursorPosCallback cursorPosCB;
	protected GLFWScrollCallback scrollCB;

	protected ApplicationContext context;

	private boolean running;

	private SwapControl swapControl;
	protected static long window;

	/** Cached mouse pointer position */
	private static Cursor cursor = new Cursor(0, 0);

	
	
	protected void init() throws GLException {
		GLFW.glfwSetErrorCallback(new GLFWErrorCallbackI(){
			@Override
			public void invoke(int error, long description) {
				String text = "GLFW error " + error;
				try {
					// try to get a description
					String s = MemoryUtil.memUTF8(description);
					if (s != null && s.length() != 0) text += ": " + s;
				} catch (Throwable t) {}
				throw new GLFWException(text);
			}
		});

		if (!glfwInit()) {
			throw new GLException("Failed to initialize GLFW\n");
		}

		createWindow();

		windowSizeCB = new GLFWWindowSizeCallback() {
			@Override
			public void invoke(long window, int width, int height) {
				try {
					onResize(width, height);
				} catch (Throwable e) {
					e.printStackTrace();
					requestExit(-1);
				}
			}
		};
		glfwSetWindowSizeCallback(window, windowSizeCB);
		
		keyCB = new GLFWKeyCallback() {
			@Override
			public void invoke(long window, int key, int scancode, int action,
					int mods) {
				try {
					onKey(key, scancode, action, mods);
				} catch (Throwable e) {
					e.printStackTrace();
					requestExit(-1);
				}
			}
		};
		glfwSetKeyCallback(window, keyCB);
		mouseButtonCB = new GLFWMouseButtonCallback() {
			@Override
			public void invoke(long window, int button, int action, int mods) {
				onMouseButton(button, action);
			}

		};
		glfwSetMouseButtonCallback(window, mouseButtonCB);
		
		cursorPosCB = new GLFWCursorPosCallback() {
			double xpos_last;
			double ypos_last;
			boolean init = true;
			@Override
			public void invoke(long window, double xpos, double ypos) {
				// TODO: consider large values in xpos and ypos and reset mouse cursor
				if(init) {
					xpos_last = xpos;
					ypos_last = ypos;
					init = false;
				}
				onMouseMove(xpos, ypos, xpos-xpos_last, ypos-ypos_last);
				xpos_last = xpos;
				ypos_last = ypos;
			}
		};
		glfwSetCursorPosCallback(window, cursorPosCB);

		scrollCB = new GLFWScrollCallback() {
			@Override
			public void invoke(long window, double xoffset, double yoffset) {
				onMouseWheel(yoffset);
			}

		};
		glfwSetScrollCallback(window, scrollCB);
		if (info.settings.cursor)
			glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
		else
			glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_HIDDEN);


		glfwMakeContextCurrent(window);


        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the ContextCapabilities instance and makes the OpenGL
        // bindings available for use.
		
        GLCapabilities capabilities = GL.createCapabilities();
        context = new ApplicationContext(window, capabilities, info);
		
        swapControl = SwapControl.select(context, info.settings.vsync, info.settings.fps, info.settings.softwareThrottle);
        // swapControl.setDiagnostics(new LogForwardReceiverImpl());
        
		// loading of extensions is done automatically by the lwjgl library,
		// thus, we dont need w3g.

		if (DEBUG) {
			info("VENDOR: " + glGetString(GL_VENDOR));
			info("VERSION: " + glGetString(GL_VERSION));
			info("RENDERER: " + glGetString(GL_RENDERER));
		}
		// Creates a debug message callback for the context
		// which supports OpenGL43, GL_KHR_debug, GL_ARB_debug_output
		// and GL_AMD_debug_output if supported by the drivers.
		debugMessageHandler = new DebugMessageHandler(GL.getCapabilities(), this);
		

	}
	

	protected void onResize(int w, int h) throws Throwable {
		info.setWindowWidth(w);
		info.setWindowHeight(h);
	}

	/**
	 * Will be called when a key is pressed, repeated or released.
	 *
	 * @param window   the window that received the event
	 * @param key      the keyboard key that was pressed or released
	 * @param scancode the system-specific scancode of the key
	 * @param action   the key action. One of:<br>{@link GLFW#GLFW_PRESS}, {@link GLFW#GLFW_RELEASE}, {@link GLFW#GLFW_REPEAT}
	 * @param mods     bitfield describing which modifiers keys were held down. One of: 
	 * <br>{@link GLFW#GLFW_MOD_SHIFT}, {@link GLFW#GLFW_MOD_CONTROL}, {@link GLFW#GLFW_MOD_ALT}, {@link GLFW#GLFW_MOD_SUPER}
	 */
	protected void onKey(int key, int scancode, int action, int mods) throws Throwable {

	}

	protected void onMouseButton(int button, int action) {

	}

	protected void onMouseMove(double xpos, double ypos, double xmov, double ymov) {

	}

	protected void onMouseWheel(double yoffset) {

	}

	protected void requestExit(int status) {
		exitStatus = status;
		glfwSetWindowShouldClose(window, true);
		running = false;
	}
	
	public void setVirtualCursor(boolean enabled) {
		glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
	}


	
	/** Immediate program exit */
	protected void exit (int status) {
		if (debugMessageHandler != null) debugMessageHandler.release();
		glfwTerminate();
		System.exit(status);
	}


	void onDebugMessage(int source, int typeOrCategory, int id, int severity,
			int length, String message) {
		switch (typeOrCategory) {
		case GL_DEBUG_TYPE_ERROR:
		case GL_DEBUG_CATEGORY_API_ERROR_AMD:
			fatal(message);
			break;
		case GL_DEBUG_TYPE_DEPRECATED_BEHAVIOR:
		case GL_DEBUG_TYPE_UNDEFINED_BEHAVIOR:
		case GL_DEBUG_TYPE_PORTABILITY:
		case GL_DEBUG_CATEGORY_WINDOW_SYSTEM_AMD:
		case GL_DEBUG_CATEGORY_DEPRECATION_AMD:
		case GL_DEBUG_CATEGORY_UNDEFINED_BEHAVIOR_AMD:
			error(message);
			break;
		default:
			switch(severity) {
			case GL_DEBUG_SEVERITY_HIGH:
				// same as:
				/* GL_DEBUG_SEVERITY_HIGH_ARB */
				/* GL_DEBUG_SEVERITY_HIGH_AMD */
				fatal(message);
				break;
			case GL_DEBUG_SEVERITY_MEDIUM:
				// same as:
				/* GL_DEBUG_SEVERITY_MEDIUM_ARB */
				/* GL_DEBUG_SEVERITY_MEDIUM_AMD */
				error(message);
				break;
			case GL_DEBUG_SEVERITY_LOW:
				// same as:
				/* GL_DEBUG_SEVERITY_LOW_ARB */
				warn(message);
				break;
			case GL_DEBUG_SEVERITY_NOTIFICATION:
				// same as:
				/* GL_DEBUG_SEVERITY_MEDIUM_AMD */
				/* GL_DEBUG_SEVERITY_LOW_AMD */
				if(DEBUG) info(message);
				break;
			default:
				warn(message);
			}
		}
	}

	static Cursor getMousePosition() {
		cursor.update(window);
		return cursor;
	}

	/**
	 * Print error message and exit abnormal
	 * @param errmsg
	 */
	protected void fatal(String errmsg) {
		Log.fatal(errmsg);
		throw new Error("FATAL: " + errmsg);
	}

	/** 
	 * Logs an error message on stderr.
	 * @param errmsg
	 */
	protected void error(String errmsg) {
		Log.error(errmsg);
	}

	/**
	 * Logs warnmsg on stdout
	 * @param warnmsg
	 */
	protected void warn(String warnmsg) {
		Log.warn(warnmsg);
	}

	/**
	 * Logs infomsg on stdout.
	 * @param infomsg
	 */
	protected void info(String infomsg) {
		Log.info(infomsg);
	}
	
	public void setVerticalSync(boolean enable) {
        info.settings.vsync = enable;
        if (swapControl != null) swapControl.stop();
        swapControl = SwapControl.select(context, info.settings.vsync, info.settings.fps, info.settings.softwareThrottle);
	}

	public final void run() throws GLException {

		exitStatus = 0;
		try {

			init();
			
			startup();
	
			
			running = true;
			

			while (running) {
				// we swap buffers first to be in sync
				GlobalClock.frameTime = swapControl.syncAndSwapBuffers();
				
	        	process(GlobalClock.frameTime, context);

		        /* Poll for and process events */
		        glfwPollEvents();

				if (glfwWindowShouldClose(window)) {
					running = false;
				}
				
			}
	
			shutdown();

		} catch (Throwable t) {
			t.printStackTrace();
			exitStatus = -1;
		}

		exit(exitStatus);
	}

	public abstract void createWindow();
	protected abstract void startup() throws Throwable;
	protected abstract void shutdown() throws Throwable;
	protected abstract void process(double currentTime, ApplicationContext context) throws Throwable;
	
}
