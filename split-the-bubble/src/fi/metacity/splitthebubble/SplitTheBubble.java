package fi.metacity.splitthebubble;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SplitTheBubble extends Game {
	
	SpriteBatch batch;
	
	@Override
	public void create() {
		Texture.setEnforcePotImages(false);
		Gdx.graphics.setVSync(true);
		
		Assets.load();
		batch = new SpriteBatch();
		setScreen(new GameScreen(this));
	}

	@Override
	public void render() {
		super.render();
	}
	
	@Override
	public void dispose() {
		super.dispose();
		batch.dispose();
		Assets.dispose();
		getScreen().dispose();
	}
	
}
