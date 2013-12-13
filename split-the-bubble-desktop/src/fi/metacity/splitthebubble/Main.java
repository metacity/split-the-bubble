package fi.metacity.splitthebubble;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.imagepacker.TexturePacker2;
import com.badlogic.gdx.tools.imagepacker.TexturePacker2.Settings;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Split the Bubble";
		cfg.useGL20 = false;
		cfg.width = 1280;
		cfg.height = 720;
		
		Settings settings = new Settings();
//        settings.maxWidth = 512;
//        settings.maxHeight = 512;
        TexturePacker2.process(settings, "../images", "../split-the-bubble-android/assets", "texturepack");

		
		new LwjglApplication(new SplitTheBubble(), cfg);
	}
}
