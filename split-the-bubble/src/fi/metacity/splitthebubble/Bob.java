package fi.metacity.splitthebubble;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Rectangle;

@SuppressWarnings("serial")
public class Bob extends Rectangle {
	static final float WIDTH = 80;
	static final float HEIGHT = 80;
	static final int MOVEMENT_PER_SECOND = 400;

	public Bob(float x, float y) {
		super(x - WIDTH/2, y, WIDTH, HEIGHT);
	}

	public void update(float delta) {
		float movementPerSecond = 0;

		if (Gdx.app.getType() == ApplicationType.Android) {
			movementPerSecond = Gdx.input.getAccelerometerY() * 175;
		} else if (Gdx.input.isKeyPressed(Keys.LEFT)) {
			movementPerSecond = -MOVEMENT_PER_SECOND;
		} else if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
			movementPerSecond = MOVEMENT_PER_SECOND;
		}
		x += (movementPerSecond * delta);

		preventOffscreen();	
	}

	private void preventOffscreen() {
		if (x < 0) {
			x = 0;
		} else if (x > World.WORLD_WIDTH - width) {
			x = World.WORLD_WIDTH - width;
		}
	}

}
