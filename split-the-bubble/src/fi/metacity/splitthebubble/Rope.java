package fi.metacity.splitthebubble;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Rectangle;

@SuppressWarnings("serial")
public class Rope extends Rectangle {
	static final float WIDTH = 18;
	static final int MOVEMENT_PER_SECOND = 400;
	
	final Bob bob;
	boolean isVisible = false;
	
	public Rope(Bob bob) {
		super(0, 0, WIDTH, 0); // Only the width is relevant, position is relative to Bob's position
		this.bob = bob;
	}

	public void update(float delta) {
		if (isVisible) {
			height += MOVEMENT_PER_SECOND * delta;
			if (height > World.WORLD_HEIGHT) {
				clear();
			}
		} else {
			trySpawning();
		}
	}
	
	public void trySpawning() {
		if (Gdx.input.isKeyPressed(Keys.SPACE) || Gdx.input.isKeyPressed(Keys.MENU) || Gdx.input.isTouched()) {
			isVisible = true;
			x = bob.x + bob.width/2 - width/2;
			height = 0;
		}
	}
	
	public void clear() {
		isVisible = false;
		height = 0;
	}
}
