package fi.metacity.splitthebubble;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;

@SuppressWarnings("serial")
public class BubbleMedium extends Bubble {
	static final float RADIUS = 32;
	private static final Pool<BubbleMedium> pool = Pools.get(BubbleMedium.class);

	public static Bubble newInstance(float x, float y) {
		Bubble bubble = pool.obtain();
		bubble.set(x, y, RADIUS);
		return bubble;
	}

	public BubbleMedium() {
		super(0, 0, RADIUS);
	}

	@Override
	public TextureRegion getTextureRegion() {
		return Assets.bubbleMedium;
	}

	@Override
	public Bubble getSplitBubble(float x, float y) {
		return BubbleSmall.newInstance(x, y);
	}

	@Override
	public void free() {
		pool.free(this);
	}
}
