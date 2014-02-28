package xoric.prism.world.client.map2;

import xoric.prism.data.time.IUpdateListener;
import xoric.prism.data.types.IFloatRect_r;
import xoric.prism.scene.materials.art.AllArt;
import xoric.prism.world.map2.GroundType2;

/**
 * @author XoricLee
 * @since 25.02.2014, 18:48:11
 */
class AnimatedGround implements IUpdateListener
{
	private final int interval;
	private final int fullDuration;
	private IFloatRect_r[] texRects;

	private int currentTime;
	private IFloatRect_r currentTexRect;

	public AnimatedGround(GroundType2 groundType)
	{
		this.interval = groundType.getAnimationInterval();

		int n = groundType.getAnimationCount();
		if (n > 1)
		{
			texRects = new IFloatRect_r[n];

			int j = 0;
			for (int i = groundType.getAnimationStart(); i < groundType.getAnimationStart() + n; ++i)
				texRects[j++] = AllArt.env0.getMeta().getRect(i);

			currentTexRect = texRects[0];
		}
		else
		{
			currentTexRect = AllArt.env0.getMeta().getRect(groundType.getAnimationStart());
		}
		this.fullDuration = interval * n;
	}

	// IUpdateListener:
	@Override
	public boolean update(int passedMs)
	{
		if (texRects != null)
		{
			currentTime = (currentTime + passedMs) % fullDuration;
			currentTexRect = texRects[currentTime / interval];
		}
		return true;
	}

	public IFloatRect_r getCurrentTexRect()
	{
		return currentTexRect;
	}
}
