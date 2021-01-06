package me.Nanook.VanillaAddition;

/*
 * By TheAlphaEpsilon
 * 28JAN2020
 * 
 */

import io.netty.channel.ChannelPipeline;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientDisconnectionFromServerEvent;

public class ChannelHandlerInput {

	public static Minecraft mc = Minecraft.getMinecraft();

	public static boolean firstConnection = true;
	
	public static ChannelPipeline pipeline = null;
	
	public static String name = "listener";

	@SubscribeEvent
	public void init(ClientConnectedToServerEvent event) {

		if (firstConnection) {
			
			firstConnection = false;

			pipeline = event.manager.channel().pipeline();
			
			pipeline.addBefore("packet_handler", name, new PacketListener());

		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onDisconnect(ClientDisconnectionFromServerEvent event) {
		firstConnection = true;
		pipeline = null;
		
	}
}
