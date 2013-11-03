package fi.metacity.splitthebubble;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;

@SuppressWarnings("serial")
public class BubbleSmall extends Bubble {
	static final float RADIUS = 12;
	private static final Pool<BubbleSmall> pool = Pools.get(BubbleSmall.class);

	public static Bubble newInstance(float x, float y) {
		Bubble bubble = pool.obtain();
		bubble.set(x, y, RADIUS);
		return bubble;
	}

	public BubbleSmall() {
		super(0, 0, RADIUS);
	}

	@Override
	public TextureRegion getTextureRegion() {
		return Assets.bubbleSmall;
	}

	@Override
	public Bubble getSplitBubble(float x, float y) {
		return null;
	}

	@Override
	public void free() {
		pool.free(this);
	}
}
