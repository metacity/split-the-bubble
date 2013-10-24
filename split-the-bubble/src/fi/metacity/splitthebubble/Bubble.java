package fi.metacity.splitthebubble;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool.Poolable;

@SuppressWarnings("serial")
public class Bubble extends Circle implements Poolable {

	Bubble.Type type;
	final Vector2 velocity;
	
	boolean alive;
	
	public Bubble() {
		super();
		
		this.velocity = new Vector2((float)Math.random() + 0.75f, 2.5f);
		this.alive = false;
	}
	
	public void init(Bubble.Type type, float radius, float x, float y) {
		super.radius = radius;
		this.type = type;
		this.x = x;
		this.y = y;
		
		alive = true;
	}
	
	public void flipXDirection() {
		velocity.x *= -1;
	}
	
	public void flipYDirection() {
		velocity.y *= -1;
	}
	
	public void update(float delta) {
		// Apply gravity
		velocity.y -= (4 * delta);
		
		// Move the bubble
		x += velocity.x * 200 * delta;
		y += velocity.y * 200 * delta;;
	}
	
	@Override
	public void reset() {
		type = null;
		x = 0;
		y = 0;
		radius = 0;
		alive = false;
	}
	
	public static enum Type {
		BIG,
		MEDIUM,
		SMALL
	}
}
