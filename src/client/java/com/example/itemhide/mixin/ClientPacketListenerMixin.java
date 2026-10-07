package com.example.itemhide.mixin;

import com.example.itemhide.ItemHideState;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.world.entity.EntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public class ClientPacketListenerMixin {
	/**
	 * Bricht das Spawn-Paket für Item-Entities ab, bevor das Entity überhaupt
	 * erzeugt wird. Dadurch gibt es weder Rendering noch Hitbox noch Tracking.
	 */
	@Inject(method = "handleAddEntity", at = @At("HEAD"), cancellable = true)
	private void itemhide$skipItemSpawn(ClientboundAddEntityPacket packet, CallbackInfo ci) {
		if (ItemHideState.isEnabled() && packet.getType() == EntityType.ITEM) {
			ci.cancel();
		}
	}
}
