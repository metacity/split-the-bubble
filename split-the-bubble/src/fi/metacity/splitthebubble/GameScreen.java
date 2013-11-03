package fi.metacity.splitthebubble;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class GameScreen implements Screen {
	
	final SplitTheBubble game;
	final World world;
	final WorldRenderer renderer;
	
	public GameScreen(SplitTheBubble game) {
		this.game = game;
		
		world = new World();
		renderer = new WorldRenderer(game.batch, world);
	}
	
	public void setLevel(Stage level) {
		
	}

	private void update(float delta) {
		world.update(delta);
	}
	
	private void draw() {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		renderer.render();
	}
	
	@Override
	public void render(float delta) {
		update(delta);
		draw();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
	}

}
