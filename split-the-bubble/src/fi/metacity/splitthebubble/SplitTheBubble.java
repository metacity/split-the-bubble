package fi.metacity.splitthebubble;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SplitTheBubble extends Game {
	
	SpriteBatch batch;
	BitmapFont font;
	
	@Override
	public void create() {
		Texture.setEnforcePotImages(false);
		Gdx.graphics.setVSync(true);
		
		batch = new SpriteBatch();
		font = new BitmapFont();
		setScreen(new GameScreen(this));
	}

	@Override
	public void render() {
		super.render();
	}
	
	@Override
	public void dispose() {
		batch.dispose();
		font.dispose();
	}
	
}
