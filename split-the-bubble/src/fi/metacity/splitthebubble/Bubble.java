package fi.metacity.splitthebubble;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool.Poolable;

@SuppressWarnings("serial")
public abstract class Bubble extends Circle implements Poolable {
	
	static final int MOVEMENT_PER_SECOND = 200;

	final Vector2 velocity;
	
	protected Bubble(float x, float y, float radius) {
		super.x = x;
		super.y = y;
		super.radius = radius;
		
		velocity = new Vector2();
		randomizeVelocity();
	}
	
	public void flipXDirection() {
		velocity.x *= -1;
	}
	
	public void flipYDirection() {
		velocity.y *= -1;
	}
	
	public void update(float delta) {
		// Apply gravity
		velocity.y -= (World.GRAVITY * delta);
		
		// Move the bubble
		x += velocity.x * MOVEMENT_PER_SECOND * delta;
		y += velocity.y * MOVEMENT_PER_SECOND * delta;;
		
		preventOffscreenX();
		preventOffscreenY();
	}
	
	private void preventOffscreenX() {
		if (x > World.WORLD_WIDTH - radius) {
			x = World.WORLD_WIDTH - radius;
			flipXDirection();
		} else if (x - radius < 0) {
			x = radius;
			flipXDirection();
		}
	}
	
	private void preventOffscreenY() {
		if (y > World.WORLD_HEIGHT - radius) {
			y = World.WORLD_HEIGHT - radius;
			flipYDirection();
			velocity.y *= 0.98; // And lose some momentum
		} else if (y - radius < 0) {
			y = radius;
			// Apply ground friction to X
			velocity.x *= 0.999;
			flipYDirection();
		}
	}
	
	private void randomizeVelocity() {
		velocity.set((float)Math.random()/2 + 1, (float)Math.random()/2 + 2);
		if (Math.random() > 0.5) {
			velocity.x *= -1;
		}
		
	}
	
	@Override
	public void reset() {
		x = 0;
		y = 0;
		radius = 0;
		randomizeVelocity();
	}
	
	/**
	 * Returns a texture region used to render the bubble.
	 */
	public abstract TextureRegion getTextureRegion();
	
	/**
	 * Returns an instance of a smaller bubble after a split, or null if no no splitting happens
	 */
	public abstract Bubble getSplitBubble(float x, float y);
	
	/**
	 * Frees the object from an internal object pool.
	 */
	public abstract void free();
}
