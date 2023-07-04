package com.tohant.om2d;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.tohant.om2d.OfficeManager2D;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.setTitle("Office Manager 2D");
		config.setWindowedMode(Lwjgl3ApplicationConfiguration.getDisplayMode().width, Lwjgl3ApplicationConfiguration.getDisplayMode().height);
		config.setResizable(false);
		String os = System.getProperty("os.name");
		if (os.contains("inu")) {
			config.setWindowIcon("icon/employee32x32.png");
		} else if (os.contains("ind")) {
			try {
				config.setWindowIcon("icon/employee32x32.png");
			} catch (Exception e) {
				config.setWindowIcon("icon/employee16x16.png");
			}
		} else if (os.contains("ac")) {
			config.setWindowIcon("icon/employee128x128.png");
		} else {
			config.setWindowIcon("icon/employee16x16.png");
		}
		new Lwjgl3Application(new OfficeManager2D(), config);
	}
}
