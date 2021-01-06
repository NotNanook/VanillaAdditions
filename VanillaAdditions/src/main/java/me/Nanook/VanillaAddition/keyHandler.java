package me.Nanook.VanillaAddition;

import java.lang.reflect.Field;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class keyHandler {

	public static EntityOtherPlayerMP fakePlayer = null;
	public static double oldX;
	public static double oldY;
	public static double oldZ;

	@SubscribeEvent
	public void onKeyPressed(KeyInputEvent event) {
		// Projectile Aimer
		if(Main.kFreecam.isKeyDown() && !Main.freecam_pa_enabled) {
			// For enabling
			Minecraft mc = Minecraft.getMinecraft();
			mc.thePlayer.addChatComponentMessage(new ChatComponentText("[" + EnumChatFormatting.GREEN + "ProjectileAimer" + EnumChatFormatting.WHITE + "]: Freecam enabled"));
			Main.freecam_pa_enabled = true;

			oldX = mc.thePlayer.posX;
			oldY = mc.thePlayer.posY;
			oldZ = mc.thePlayer.posZ;

			fakePlayer = new EntityOtherPlayerMP(mc.theWorld, mc.thePlayer.getGameProfile());
			fakePlayer.clonePlayer(mc.thePlayer, true);

			fakePlayer.rotationYawHead = mc.thePlayer.rotationYawHead;
			fakePlayer.copyLocationAndAnglesFrom(mc.thePlayer);

			mc.theWorld.addEntityToWorld(-1000, fakePlayer);

			mc.thePlayer.noClip = true;
		} else if(Main.kFreecam.isKeyDown() && Main.freecam_pa_enabled) {
			// For disabling
			Minecraft mc = Minecraft.getMinecraft();
			mc.thePlayer.addChatComponentMessage(new ChatComponentText("[" + EnumChatFormatting.RED + "ProjectileAimer" + EnumChatFormatting.WHITE + "]: Freecam disabled"));

			mc.thePlayer.setPositionAndRotation(oldX, oldY, oldZ, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch);
			mc.theWorld.removeEntityFromWorld(keyHandler.fakePlayer.getEntityId());
			keyHandler.fakePlayer = null;
			mc.thePlayer.motionX = 0;
			mc.thePlayer.motionY = 0;
			mc.thePlayer.motionZ = 0;
			Main.freecam_pa_enabled = false;

		}
		
		// Makes all players invisible
		if(Main.kPlayerVis.isKeyDown() && !Main.playerInvis)
		{
			Minecraft mc = Minecraft.getMinecraft();
			mc.thePlayer.addChatComponentMessage(new ChatComponentText("[" + EnumChatFormatting.GREEN + "PlayerInvis" + EnumChatFormatting.WHITE + "]: All players are now invisible"));
			Main.playerInvis = true;
			
		} else if(Main.kPlayerVis.isKeyDown() && Main.playerInvis)
		{
			Minecraft mc = Minecraft.getMinecraft();
			mc.thePlayer.addChatComponentMessage(new ChatComponentText("[" + EnumChatFormatting.GREEN + "PlayerInvis" + EnumChatFormatting.WHITE + "]: All players are now visible again"));
			Main.playerInvis = false;
		}
		
		// Gives you the distance to a certain block
		if(Main.kDistance.isKeyDown())
		{
			Minecraft mc = Minecraft.getMinecraft();
			MovingObjectPosition lookingAt = mc.getRenderViewEntity().rayTrace(100, 1);
			
			if (lookingAt != null && lookingAt.typeOfHit == MovingObjectType.BLOCK) {
			    BlockPos block_pos = lookingAt.getBlockPos();
			    BlockPos player_pos = mc.thePlayer.getPosition();
			    mc.thePlayer.addChatComponentMessage(new ChatComponentText("[" + EnumChatFormatting.GREEN + "Distance" + EnumChatFormatting.WHITE + "]: " + String.valueOf(Math.round(Math.sqrt(player_pos.distanceSq(block_pos))))));
			    System.out.println(mc.theWorld.getBlockState(block_pos));
			}
		}
		
		if(Main.kFastPlace.isKeyDown() && Main.fastPlace == false)
		{
			Main.fastField = ReflectionHelper.findField(Minecraft.class, "rightClickDelayTimer", "field_71467_ac", "ap");
			Main.fastPlace = true;
		} else if(Main.kFastPlace.isKeyDown() && Main.fastPlace == true)
		{
			Main.fastPlace = false;
		}
	}
}
