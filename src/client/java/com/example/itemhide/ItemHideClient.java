package com.example.itemhide;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ItemHideClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ItemHideState.load();

		// Fängt Chat-Nachrichten ab, bevor sie zum Server gehen.
		ClientSendMessageEvents.ALLOW_CHAT.register(message -> {
			String[] parts = message.trim().split("\\s+");
			if (!parts[0].equalsIgnoreCase(".item")) {
				return true; // normale Chatnachricht
			}

			String arg = parts.length > 1 ? parts[1].toLowerCase(Locale.ROOT) : "";
			switch (arg) {
				case "enable" -> {
					ItemHideState.setEnabled(true);
					removeExistingItems();
					say("§aItems sind jetzt unsichtbar (nichts wird geladen).");
				}
				case "disable" -> {
					ItemHideState.setEnabled(false);
					say("§eItems werden wieder normal geladen. Bereits versteckte Items erscheinen nach Rejoin/Chunk-Reload.");
				}
				default -> say("§cBenutzung: .item enable | .item disable");
			}
			return false; // Nachricht NICHT an den Server senden
		});
	}

	private static void say(String text) {
		Minecraft mc = Minecraft.getInstance();
		if (mc.player != null) {
			mc.player.displayClientMessage(Component.literal(text), false);
		}
	}

	/** Entfernt bereits geladene Item-Entities sofort aus der Client-Welt. */
	private static void removeExistingItems() {
		ClientLevel level = Minecraft.getInstance().level;
		if (level == null) return;

		List<Integer> ids = new ArrayList<>();
		for (Entity entity : level.entitiesForRendering()) {
			if (entity instanceof ItemEntity) {
				ids.add(entity.getId());
			}
		}
		for (int id : ids) {
			level.removeEntity(id, Entity.RemovalReason.DISCARDED);
		}
	}
      }
