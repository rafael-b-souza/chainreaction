package de.freewarepoint.cr;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.ServiceLoader;

import de.freewarepoint.cr.ai.AI;
import de.freewarepoint.cr.exceptions.ConfigUnreadableException;

public class SettingsLoader {

	private static final String REACTION_DELAY = "reaction.delay";
	private static final String ANIM_MODE          = "anim.mode";
	private static final String ANIM_FRAMES        = "anim.explodeFrames";
	private static final String ANIM_SHRINK_END    = "anim.shrinkEnd";

	/**
	 * Loads the settings stored in the user's home directory.
	 * If there are no settings, the default values are stores.
	 *
	 * @return
	 *  	the loaded settings
	 *
	 * @throws
	 *  	ConfigUnreadableException if there was an error while reading the settings
	 */
	public static Settings loadSettings() {
		Properties properties = new Properties();
		try {
			checkConfigReadability();
			String configFileName = getConfigurationFileLocation().toString();
			BufferedInputStream stream = new BufferedInputStream(new FileInputStream(configFileName));
			properties.load(stream);
			int delay = Integer.valueOf(properties.getProperty(REACTION_DELAY));

			Settings s = new Settings(delay);
			AnimSettings a = new AnimSettings();
			a.setMode(AnimSettings.Mode.valueOf(
					properties.getProperty(ANIM_MODE, a.getMode().name())));
			a.setExplodeFrames(Integer.parseInt(
					properties.getProperty(ANIM_FRAMES,
							String.valueOf(a.getExplodeFrames()))));
			a.setShrinkEnd(Boolean.parseBoolean(
					properties.getProperty(ANIM_SHRINK_END,
							String.valueOf(a.isShrinkEnd()))));
			s.setAnim(a);
			return s;
		} catch (ConfigUnreadableException e) {
			Settings settingsWithDefaultValues = new Settings();
			storeSettings(settingsWithDefaultValues);
			return settingsWithDefaultValues;
		} catch (IOException e) {
			System.err.println("Unable to load properties: " + e.getMessage());
			return new Settings();
		}
	}

	public static void storeSettings(Settings settings) {
		String configFileName = getConfigurationFileLocation().toString();
		Properties properties = new Properties();
		properties.setProperty(REACTION_DELAY, String.valueOf(settings.getReactionDelay()));
		AnimSettings a = settings.getAnim();
		properties.setProperty(ANIM_MODE,       a.getMode().name());
		properties.setProperty(ANIM_FRAMES,     String.valueOf(a.getExplodeFrames()));
		properties.setProperty(ANIM_SHRINK_END, String.valueOf(a.isShrinkEnd()));
		try {
			BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(configFileName));
			properties.store(stream, "Settings for Chain Reaction");
		} catch (IOException | ConfigUnreadableException e) {
			System.err.println("Unable to store properties: " + e.getMessage());
		}
	}

	private static Path getConfigurationStorageLocation() {
		String homeDir = System.getProperties().getProperty("user.home");
		String configDir = ".cr";
		return FileSystems.getDefault().getPath(homeDir, configDir);
	}

	private static Path getConfigurationFileLocation() {
		String filename = "settings.properties";
		return FileSystems.getDefault().getPath(getConfigurationStorageLocation().toString(), filename);
	}

	private static void checkConfigReadability() {
		Path path = getConfigurationStorageLocation();

		if (!Files.exists(path)) {
			try {
				Files.createDirectory(path);
			} catch (IOException e) {
				throw new ConfigUnreadableException(e);
			}
		}
		else if (!Files.isDirectory(path)) {
			System.err.println("Unable to read settings, " + path + " is not a directory!");
			throw new ConfigUnreadableException();
		}

		path = getConfigurationFileLocation();

		if (!Files.exists(path)) {
			throw new ConfigUnreadableException();
		}

		if (!Files.isRegularFile(path)) {
			System.err.println("Unable to read settings from " + path + ", is not a regular file");
			throw new ConfigUnreadableException();
		}
	}

	public static List<AI> loadAIs() {
		
		Path aiDir = getAIPath();

		if (!Files.exists(aiDir)) {
			return Collections.emptyList();
		}

		File[] files = aiDir.toFile().listFiles();

		if (files == null) {
			return Collections.emptyList();
		}

		List<URL> urls = new LinkedList<>();

		for (File file : files) {
			if (file.isFile() && file.getName().endsWith(".jar")) {
				try {
					urls.add(file.toURI().toURL());
				} catch (MalformedURLException e) {
					System.err.println("Unable to load " + file.getName() + ": " + e.getMessage());
				}
			}
		}

		URL[] urlArray = urls.toArray(new URL[urls.size()]);	
		
		for(URL u: urlArray) {
			System.out.println("Found "+u);
		}
		
		URLClassLoader loader = new URLClassLoader(urlArray, SettingsLoader.class.getClassLoader());
		Thread.currentThread().setContextClassLoader(loader);
		ServiceLoader<AI> serviceLoader = ServiceLoader.load(AI.class, loader);
		
		List<AI> ais = new ArrayList<>(urls.size());
		Iterator<AI> iterator = serviceLoader.iterator();
		
		while (iterator.hasNext()) {
			ais.add(iterator.next());
		}

		return ais;
	}

	public static Path getAIPath() {
		Path configDir = getConfigurationStorageLocation();
		return FileSystems.getDefault().getPath(configDir.toString(), "AIs");
	}

}
