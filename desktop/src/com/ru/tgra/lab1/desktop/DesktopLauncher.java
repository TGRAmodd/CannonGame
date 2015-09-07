package com.ru.tgra.lab1.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.ru.tgra.lab1.Lab1Game;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.title = "Lab1"; // or whatever you like
		config.width = 1024;  //experiment with
		config.height = 768;  //the window size

		new LwjglApplication(new Lab1Game(), config);
	}
}
