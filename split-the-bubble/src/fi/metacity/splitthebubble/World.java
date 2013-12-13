package fi.metacity.splitthebubble;

import java.util.Iterator;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public class World {
	static final int WORLD_WIDTH = 1280;
	static final int WORLD_HEIGHT = 720;
	static final float GRAVITY = 3;
	static final long BUBBLE_SPAWN_INTERVAL_NS = 12500000000L; 
	
	final Bob bob;
	final Array<Bubble> activeBubbles;
	
	int score = 0;
	long lastBubbleSpawnTime;
	
	public World() {
		bob = new Bob(World.WORLD_WIDTH / 2, 60);
		activeBubbles = new Array<Bubble>(false, 16);
	}
	
	public void update(float delta) {
		bob.update(delta);
		
		// Process bubbles
		Iterator<Bubble> iter = activeBubbles.iterator();
		while (iter.hasNext()) {
			Bubble bubble = iter.next();
			bubble.update(delta);
			// Split when overlapping with a rope
			for (int i = 0; i < bob.ropes.size; ++i) {
				Rope rope = bob.ropes.get(i);
				if (rope.isVisible && Intersector.overlaps(bubble, rope)) {
					for (int j = 0; j < 2; ++j) { // 2 bubbles per split
						Bubble splitBubble = bubble.getSplitBubble(bubble.x, bubble.y);
						if (splitBubble != null) {
							activeBubbles.add(splitBubble);
						}
					}
					score++;
					rope.clear();
					iter.remove();
					bubble.free();
				}
			}
				
		}
		
		if (TimeUtils.nanoTime() - lastBubbleSpawnTime > BUBBLE_SPAWN_INTERVAL_NS) {
			Bubble spawnedBubble = BubbleBig.newInstance((float)Math.random() * 600, (float)Math.random() * 100 + 200);
			activeBubbles.add(spawnedBubble);
			lastBubbleSpawnTime = TimeUtils.nanoTime();
		}
	}
}
