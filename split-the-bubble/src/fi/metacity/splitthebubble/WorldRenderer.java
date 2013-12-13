package fi.metacity.splitthebubble;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

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
		Assets.font.draw(batch, "Score: " + world.score, 15, World.WORLD_HEIGHT - 15);
		batch.end();
	}
	
	private void renderBackground() {
		batch.disableBlending();
		batch.draw(Assets.background, 0, 0);
	}
	
	private void renderObjects() {
		batch.enableBlending();
		renderBob();
		renderRopes();
		renderBubbles();
	}
	
	private void renderBob() {
		TextureRegion bobTexture = Assets.bob;
		// Flip texture if direction changed
		if ((world.bob.facingLeft && !bobTexture.isFlipX()) ||
			(!world.bob.facingLeft && bobTexture.isFlipX())) {
			bobTexture.flip(true, false);
		}
		batch.draw(Assets.bob, world.bob.x, world.bob.y);
	}
	
	private void renderBubbles() {
		int len = world.activeBubbles.size;
		for (int i = 0; i < len; ++i) {
			Bubble bubble = world.activeBubbles.get(i);
			batch.draw(bubble.getTextureRegion(), bubble.x - bubble.radius, bubble.y - bubble.radius);
		}
	}
	
	private void renderRopes() {
		for (int i = 0; i < world.bob.ropes.size; ++i) {
			Rope rope = world.bob.ropes.get(i);
			batch.draw(Assets.rope, rope.x, rope.y, rope.width, rope.height);
		}
		
	}
}
