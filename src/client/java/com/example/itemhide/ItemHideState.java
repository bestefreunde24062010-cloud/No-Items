package com.example.itemhide;

import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/** Hält den An/Aus-Zustand (wird in config/itemhide.txt gespeichert). */
public final class ItemHideState {
	// volatile, weil das Netzwerk-Thread-Paket-Handling den Wert mitliest
	private static volatile boolean enabled = false;

	private ItemHideState() {}

	private static Path file() {
		return FabricLoader.getInstance().getConfigDir().resolve("itemhide.txt");
	}

	public static boolean isEnabled() {
		return enabled;
	}

	public static void setEnabled(boolean value) {
		enabled = value;
		try {
			Files.writeString(file(), Boolean.toString(value));
		} catch (IOException ignored) {
		}
	}

	public static void load() {
		try {
			Path f = file();
			if (Files.exists(f)) {
				enabled = Boolean.parseBoolean(Files.readString(f).trim());
			}
		} catch (IOException ignored) {
		}
	}
}
