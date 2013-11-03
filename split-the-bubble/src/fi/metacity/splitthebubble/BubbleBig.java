package fi.metacity.splitthebubble;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;

@SuppressWarnings("serial")
public class BubbleBig extends Bubble {
	static final float RADIUS = 64;
	private static final Pool<BubbleBig> pool = Pools.get(BubbleBig.class);

	public static Bubble newInstance(float x, float y) {
		Bubble bubble = pool.obtain();
		bubble.set(x, y, RADIUS);
		return bubble;
	}

	public BubbleBig() {
		super(0, 0, RADIUS);
	}

	@Override
	public TextureRegion getTextureRegion() {
		return Assets.bubbleBig;
	}

	@Override
	public Bubble getSplitBubble(float x, float y) {
		return BubbleMedium.newInstance(x, y);
	}

	@Override
	public void free() {
		pool.free(this);
	}
}
