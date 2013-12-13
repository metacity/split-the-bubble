package fi.metacity.splitthebubble;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class Assets {
	
	static Texture background;
	
	static TextureAtlas atlas;
	
	static TextureRegion bob;
	static TextureRegion bubbleSmall;
	static TextureRegion bubbleMedium;
	static TextureRegion bubbleBig;
	static TextureRegion rope;
	
	static BitmapFont font;
	
	public static void load() {
		atlas = new TextureAtlas(Gdx.files.internal("texturepack.atlas"));
		for (Texture texture : atlas.getTextures()) {
			texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		}
		
		bob = atlas.findRegion("bob");
		bubbleSmall = atlas.findRegion("bubble_small");
		bubbleMedium = atlas.findRegion("bubble_med");
		bubbleBig = atlas.findRegion("bubble_big");
		rope = atlas.findRegion("rope");
		
		background = new Texture(Gdx.files.internal("background.png"));
		
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Roboto-Regular.ttf"));
		font = generator.generateFont(20);
		generator.dispose();
	}
	
	public static void dispose() {
		background.dispose();
		atlas.dispose();
		font.dispose();
	}
}
