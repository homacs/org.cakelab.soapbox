package org.cakelab.oge.shader;

import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_INFO_LOG_LENGTH;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glDeleteShader;
import static org.lwjgl.opengl.GL20.glGetShaderiv;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glShaderSource;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.charset.Charset;
import java.util.StringTokenizer;

import org.cakelab.appbase.fs.FileSystem;
import org.lwjgl.BufferUtils;
import org.lwjgl.system.MemoryUtil;


public class Shader {

	public static final int INVALID_SHADER_ID = -1;
	
	protected int shaderId;
	protected int shaderType;
	protected String shaderName;

	/**
	 * @param type GL_VERTEX_SHADER, GL_FRAGMENT_SHADER, GL_GEOMETRY_SHADER, 
	 * 				GL_TESS_CONTROL_SHADER, GL_TESS_EVALUATION_SHADER
	 * @param name User given name
	 */
	protected Shader(int type, String name) {
		this.shaderType = type;
		this.shaderName = name;
		this.shaderId = INVALID_SHADER_ID;
	}
	
	protected Shader(int shaderType, String name, String glslSource) throws GLCompilerException {
		this(shaderType, name);
		compile(glslSource);
	}
	
	protected Shader(int shaderType, String name, File glslSource) throws GLCompilerException, IOException {
		this(shaderType, name);
		compile(glslSource);
	}
	
	protected Shader(int shaderType, File glslSource) throws GLCompilerException, IOException {
		this(shaderType, glslSource.getName(), glslSource);
	}
	
	public Shader(int type, String name, InputStream glslSource) throws GLCompilerException, IOException {
		this(type, name);
		compile(glslSource);
	}

	public void delete() {
		if (shaderId != INVALID_SHADER_ID) {
			glDeleteShader(shaderId);
			shaderId = INVALID_SHADER_ID;
		}
	}
	
	
	public void compile(File file) throws IOException, GLCompilerException {
		  compile(new FileInputStream(file));
	}
	
	private void compile(InputStream glslSource) throws IOException, GLCompilerException {
		  String code = FileSystem.readText(glslSource, Charset.forName("UTF-8"));
		  compile(code);
	}
	
	public void compile(String sourceCode) throws GLCompilerException {
		checkASCII(sourceCode);
		
		// Create and compile vertex shader
		shaderId = glCreateShader(shaderType);
		glShaderSource(shaderId, sourceCode);
		glCompileShader(shaderId);
		
		// check result
		IntBuffer status = BufferUtils.createIntBuffer(1);
		glGetShaderiv(shaderId, GL_COMPILE_STATUS, status);

		if (status.get(0) != GL_TRUE) {
			throwCompilerErrors();
		}
	}

	private void checkASCII(String sourceCode) throws GLCompilerException {
		int line = 0;
		int column = 0;
		for(char c : sourceCode.toCharArray()) {
			if (c < 0 || c > 128) {
				throw new GLCompilerException(compilerErrorLine(line, column, "non-ascii character '" + c + "'."));
			}
			column++;
			if (c == '\n') {
				line++;
				column = 0;
			}
		}
	}


	void throwCompilerErrors() throws GLCompilerException {
		IntBuffer bufSize = BufferUtils.createIntBuffer(1);
		glGetShaderiv(shaderId, GL_INFO_LOG_LENGTH, bufSize);
		ByteBuffer infoLog = BufferUtils.createByteBuffer(Character.SIZE/8 * bufSize.get(0));
		glGetShaderInfoLog(shaderId, bufSize, infoLog);

		StringTokenizer tokenizer = new StringTokenizer(MemoryUtil.memUTF8(infoLog,bufSize.get(0)), "\n");

		StringBuffer errorMessage = new StringBuffer();
		while (tokenizer.hasMoreTokens()) {
			String line = tokenizer.nextToken();
			int messageStart = line.indexOf(')');
			int lineNo = Integer.parseInt(line.substring(line.indexOf('(')+1, messageStart));		
			int columnNo = 0; // currently unknown
			errorMessage.append(compilerErrorLine(lineNo, columnNo, line.substring(messageStart+1)));
			errorMessage.append('\n');
		}
		
		throw new GLCompilerException("Errors in shader code:\n" + errorMessage.toString());

	}

	private String compilerErrorLine(int line, int column, String error) {
		return '\t' + shaderName + ':' + line + ':' + column + ':' + error;
	}
	


}
