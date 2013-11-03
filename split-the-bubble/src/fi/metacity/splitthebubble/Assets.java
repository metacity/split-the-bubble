package fi.metacity.splitthebubble;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Assets {
	static final int WORLD_WIDTH = 800;
	static final int WORLD_HEIGHT = 480;
	
	static TextureAtlas atlas;
	
	static TextureRegion bob;
	static TextureRegion background;
	static TextureRegion bubbleSmall;
	static TextureRegion bubbleMedium;
	static TextureRegion bubbleBig;
	static TextureRegion rope;
	
	static BitmapFont font;
	
	public static void load() {
		atlas = new TextureAtlas(Gdx.files.internal("texturepack.atlas"));
		bob = atlas.findRegion("bob");
		background = atlas.findRegion("background");
		bubbleSmall = atlas.findRegion("bubble_small");
		bubbleMedium = atlas.findRegion("bubble_med");
		bubbleBig = atlas.findRegion("bubble_big");
		rope = atlas.findRegion("rope");
		
		font = new BitmapFont();
	}
	
	public static void dispose() {
		atlas.dispose();
		font.dispose();
	}
}
