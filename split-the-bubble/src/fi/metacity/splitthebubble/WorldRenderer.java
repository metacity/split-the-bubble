package fi.metacity.splitthebubble;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class WorldRenderer {
	
	final SpriteBatch batch;
	final World world;
	final OrthographicCamera camera; 

	public WorldRenderer(SpriteBatch batch, World world) {
		this.batch = batch;
		this.world = world;
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, World.WORLD_WIDTH, World.WORLD_HEIGHT);
	}
	
	public void render() {
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		renderBackground();
		renderObjects();
		Assets.font.draw(batch, "Score: " + world.score, 20, World.WORLD_HEIGHT - 20);
		batch.end();
	}
	
	private void renderBackground() {
		batch.disableBlending();
		batch.draw(Assets.background, 0, 0);
	}
	
	private void renderObjects() {
		batch.enableBlending();
		renderRope();
		renderBob();
		renderBubbles();
	}
	
	private void renderBob() {
		batch.draw(Assets.bob, world.bob.x, world.bob.y);
	}
	
	private void renderBubbles() {
		int len = world.activeBubbles.size;
		for (int i = 0; i < len; ++i) {
			Bubble bubble = world.activeBubbles.get(i);
			batch.draw(bubble.getTextureRegion(), bubble.x - bubble.radius, bubble.y - bubble.radius);
		}
	}
	
	private void renderRope() {
		batch.draw(Assets.rope, world.rope.x, world.rope.y, world.rope.width, world.rope.height);
	}
}