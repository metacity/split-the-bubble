package fi.metacity.splitthebubble;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

@SuppressWarnings("serial")
public class Bubble extends Rectangle {
	
	private static Texture sBubbleBig;
	private static Texture sBubbleMed;
	private static Texture sBubbleSmall;

	
	private final Texture mTexture;
	
	public final Bubble.Type type;
	public float yVelocity = 2.5f;
	public float xVelocity;
	
	private Bubble(Texture texture, Bubble.Type type) {
		super();
		mTexture = texture;
		this.type = type;
		
		width = texture.getWidth();
		height = texture.getHeight();
		
		xVelocity = (float)Math.random() * 4 - 2;
	}

	public void flipYDirection() {
		yVelocity *= -1;
	}

	public void flipXDirection() {
		xVelocity *= -1;
	}
	
	public void draw(SpriteBatch batch) {
		batch.draw(mTexture, x, y);
	}
	
	public static Bubble newInstance(Bubble.Type type) {
		switch (type) {
			case BIG:
				return new Bubble(sBubbleBig, Bubble.Type.BIG);
				
			case MEDIUM:
				return new Bubble(sBubbleMed, Bubble.Type.MEDIUM);
				
			case SMALL:
				return new Bubble(sBubbleSmall, Bubble.Type.SMALL);
				
			default:
				throw new IllegalArgumentException("Invalid type");
		}
	}
	
	public static void loadAssets() {
		sBubbleBig = new Texture(Gdx.files.internal("bubble_big.png"));
		sBubbleMed = new Texture(Gdx.files.internal("bubble_med.png"));
		sBubbleSmall = new Texture(Gdx.files.internal("bubble_small.png"));
	}
	
	public static void dispose() {
		sBubbleBig.dispose();
		sBubbleMed.dispose();
		sBubbleSmall.dispose();
	}
	
	public static enum Type {
		BIG,
		MEDIUM,
		SMALL
	}
}
