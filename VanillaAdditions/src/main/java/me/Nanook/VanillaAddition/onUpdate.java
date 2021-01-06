package me.Nanook.VanillaAddition;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class onUpdate {

	public static Minecraft mc = Minecraft.getMinecraft();

	@SubscribeEvent
	public void onLivingUpdateEvent(LivingUpdateEvent event) {
		
		//mc.thePlayer.addChatComponentMessage(new ChatComponentText(ChannelHandlerInput.pipeline.toString()));
		
		// do something to player every update tick:
		if (event.entity instanceof EntityPlayer && Main.freecam_pa_enabled) 
		{
			mc.thePlayer.noClip = true;
			mc.thePlayer.fallDistance = 0;

			mc.thePlayer.motionY = 0;
			mc.thePlayer.motionX = 0;
			mc.thePlayer.motionZ = 0;
			if (mc.gameSettings.keyBindJump.isKeyDown())
				mc.thePlayer.motionY += 0.8;
			if (mc.gameSettings.keyBindSneak.isKeyDown())
				mc.thePlayer.motionY -= 0.8;

			if (!(mc.thePlayer != null
					&& (mc.thePlayer.movementInput.moveForward != 0F || mc.thePlayer.movementInput.moveStrafe != 0F))) {
				return;
			}

			final double yaw = getDirection();
			mc.thePlayer.motionX = -Math.sin(yaw) * 0.8;
			mc.thePlayer.motionZ = Math.cos(yaw) * 0.8;
		}
		
		if (Main.fastPlace)
		{
			try {
				Main.fastField.set(mc, 1);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}

	public static double getDirection() {
		float rotationYaw = mc.thePlayer.rotationYaw;

		if (mc.thePlayer.moveForward < 0F)
			rotationYaw += 180F;

		float forward = 1F;
		if (mc.thePlayer.moveForward < 0F)
			forward = -0.5F;
		else if (mc.thePlayer.moveForward > 0F)
			forward = 0.5F;

		if (mc.thePlayer.moveStrafing > 0F)
			rotationYaw -= 90F * forward;

		if (mc.thePlayer.moveStrafing < 0F)
			rotationYaw += 90F * forward;

		return Math.toRadians(rotationYaw);
	}
}
