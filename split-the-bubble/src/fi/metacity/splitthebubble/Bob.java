package fi.metacity.splitthebubble;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

@SuppressWarnings("serial")
public class Bob extends Rectangle {
	static final float WIDTH = 50;
	static final float HEIGHT = 100;
	static final int MOVEMENT_PER_SECOND = 400;
	
	final Array<Rope> ropes;
	boolean facingLeft;

	public Bob(float x, float y) {
		super(x - WIDTH/2, y, WIDTH, HEIGHT);
		ropes = new Array<Rope>(false, 1);
		ropes.add(new Rope(this));
	}

	public void update(float delta) {
		updatePosition(delta);
		updateRopes(delta);
	}
	
	private void updatePosition(float delta) {
		float movementPerSecond = 0;

		if (Gdx.app.getType() == ApplicationType.Android) {
			movementPerSecond = Gdx.input.getAccelerometerY() * 175;
		} else if (Gdx.input.isKeyPressed(Keys.LEFT)) {
			movementPerSecond = -MOVEMENT_PER_SECOND;
		} else if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
			movementPerSecond = MOVEMENT_PER_SECOND;
		}
		
		if (movementPerSecond != 0) {
			facingLeft = (movementPerSecond < 0);
			x += (movementPerSecond * delta);
			preventOffscreen();	
		}
	}

	private void preventOffscreen() {
		if (x < 0) {
			x = 0;
		} else if (x > World.WORLD_WIDTH - width) {
			x = World.WORLD_WIDTH - width;
		}
	}
	
	private void updateRopes(float delta) {
		for (int i = 0; i < ropes.size; ++i) {
			ropes.get(i).update(delta);
		}
	}

}
