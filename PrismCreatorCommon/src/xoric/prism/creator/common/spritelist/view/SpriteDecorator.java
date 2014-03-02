package xoric.prism.creator.common.spritelist.view;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import xoric.prism.data.types.IFloatPoint_r;

/**
 * @author XoricLee
 * @since 28.02.2014, 19:37:11
 */
abstract class SpriteDecorator
{
	public static void decorateSprite(BufferedImage sprite, HotspotList list)
	{
		if (list != null)
		{
			Graphics2D g = sprite.createGraphics();

			// hotspot
			g.setColor(Color.red);
			g.drawRect((int) list.hotSpot.x - 1, (int) list.hotSpot.y - 1, 3, 3);

			// action points
			final float steps = 600.0f;
			g.setColor(Color.blue);
			for (ActionPoint p : list.actionPoints)
			{
				IFloatPoint_r c = p.angle.getComponents();
				float tx = p.down.x + c.getX() * steps;
				float ty = p.down.y + c.getY() * steps;

				g.drawRect((int) p.down.x - 1, (int) p.down.y - 1, 3, 3);
				g.drawLine((int) p.down.x, (int) p.down.y, (int) tx, (int) ty);
			}
			g.dispose();
		}
	}
}
