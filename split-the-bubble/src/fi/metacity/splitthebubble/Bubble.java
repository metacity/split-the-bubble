package fi.metacity.splitthebubble;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Bubble extends Sprite {
	
	private static Texture sBubbleBig;
	private static Texture sBubbleMed;
	private static Texture sBubbleSmall;

	public int yDirection = 1;
	public int xDirection = 1;
	public Bubble.Type type;
	
	private Bubble(Texture texture, Bubble.Type type) {
		super(texture);
		this.type = type;
		
		int xRnd = (int)(Math.random() * 100);
		if (xRnd > 50) flipXDirection();
		
		int yRnd = (int)(Math.random() * 100);
		if (yRnd > 50) flipYDirection();
	}

	public void flipYDirection() {
		yDirection *= -1;
	}

	public void flipXDirection() {
		xDirection *= -1;
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
