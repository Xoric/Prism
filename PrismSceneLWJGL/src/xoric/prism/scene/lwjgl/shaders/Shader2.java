package xoric.prism.scene.lwjgl.shaders;

import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;

import xoric.prism.data.types.PrismColor;
import xoric.prism.scene.shaders.IShader2;
import xoric.prism.scene.textures.ITexture;

public class Shader2 implements IShader2
{
	private final int programID;
	private PrismColor color;

	public Shader2(int program)
	{
		this.programID = program;
	}

	@Override
	public void activate()
	{
		ARBShaderObjects.glUseProgramObjectARB(programID);
	}

	@Override
	public void setTexture(ITexture texture)
	{
		// 		gl2.glActiveTexture(GL.GL_TEXTURE0);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);

		//		gl2.glBindTexture(GL.GL_TEXTURE_2D, textureObject); 
		int programID = texture.getTextureID();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, programID);

		//		int uniformLocation = gl2.glGetUniformLocationARB(program, "tex");
		int uniformLocation = GL20.glGetUniformLocation(programID, "tex");

		//		gl2.glUniform1iARB(uniformLocation, 0);
		GL20.glUniform1i(uniformLocation, 0);
	}

	@Override
	public void setColor(PrismColor color)
	{
		this.color = color;

		//		int uniformLocation = gl2.glGetUniformLocationARB(program, "color");
		int uniformLocation = GL20.glGetUniformLocation(programID, "color");

		//				gl2.glUniform4f(uniformLocation, color.getR(), color.getG(), color.getB(), color.getA()); 
		float[] rgba = color.getRGBA();
		GL20.glUniform4f(uniformLocation, rgba[0], rgba[1], rgba[2], rgba[3]);
	}
}
