package xoric.prism.scene.lwjgl;

import org.lwjgl.opengl.ARBShaderObjects;

import xoric.prism.scene.shader.IShader2;

public class Shader2 implements IShader2
{
	private final int program;

	public Shader2(int program)
	{
		this.program = program;
	}

	@Override
	public void activate()
	{
		ARBShaderObjects.glUseProgramObjectARB(program);
	}
}
