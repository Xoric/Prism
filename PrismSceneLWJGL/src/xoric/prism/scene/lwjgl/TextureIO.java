package xoric.prism.scene.lwjgl;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.IntBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class TextureIO
{
	private final IntBuffer texture;
	private final int width;
	private final int height;

	private int id;

	public TextureIO(final InputStream inputStream) throws IOException
	{
		BufferedImage image = ImageIO.read(inputStream);

		width = image.getWidth();
		height = image.getHeight();

		final AffineTransform tx = AffineTransform.getScaleInstance(1, -1);
		tx.translate(0, -height);

		final AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		image = op.filter(image, null);

		final int[] pixels = image.getRGB(0, 0, width, height, null, 0, width);
		texture = BufferUtils.createIntBuffer(pixels.length);
		texture.put(pixels);
		texture.rewind();
	}

	public void init()
	{
		GL11.glEnable(GL11.GL_TEXTURE_2D);

		final IntBuffer buffer = BufferUtils.createIntBuffer(1);
		GL11.glGenTextures(buffer);
		id = buffer.get(0);

		GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);

		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, width, height, 0, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, texture);

		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
	}

	public int getProgramID()
	{
		return id;
	}
}
