package me.Nanook.VanillaAddition;

import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PacketHandler {
	
	@SubscribeEvent
	public void outgoing(PacketEvent.Outgoing event) {
		Packet<?> packet = event.getPacket();

		if((packet instanceof C03PacketPlayer || packet instanceof C0BPacketEntityAction) && Main.freecam_pa_enabled) 
		{
			event.setCanceled(true);
			//Minecraft.getMinecraft().thePlayer.addChatComponentMessage(new ChatComponentText("Packet canceled"));
		}
	}
}
