package me.Nanook.VanillaAddition;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class onInteract {

	@SubscribeEvent
	public void onRightClick(PlayerInteractEvent event) {
		setAngle(event);
	}
	
	public static void setAngle(PlayerInteractEvent event)
	{
		BlockPos pos = event.pos;
		World world = event.entityPlayer.getEntityWorld();
		if (Main.freecam_pa_enabled && !(pos == null) && !world.isAirBlock(pos)) {

			Minecraft mc = Minecraft.getMinecraft();
			Main.freecam_pa_enabled = false;
			
			mc.thePlayer.addChatComponentMessage(new ChatComponentText("[" + EnumChatFormatting.RED + "ProjectileAimer" + EnumChatFormatting.WHITE + "]: Freecam disabled"));

			mc.thePlayer.setPositionAndRotation(keyHandler.oldX, keyHandler.oldY, keyHandler.oldZ,
												mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch);
			mc.theWorld.removeEntityFromWorld(keyHandler.fakePlayer.getEntityId());
			keyHandler.fakePlayer = null;
			mc.thePlayer.motionX = 0;
			mc.thePlayer.motionY = 0;
			mc.thePlayer.motionZ = 0;

			double dx = keyHandler.oldX - pos.getX();
			double dz = keyHandler.oldZ - pos.getZ();
			
			// calculate yaw with trigonometry
			double nYaw = 0;
			
			if (dx <= 0 && dz <= 0) {
				nYaw = -Math.toDegrees(Math.atan(dx / dz));
			} else if (dx >= 0 && dz <= 0) {
				nYaw = Math.toDegrees(-Math.atan(dx / dz));
			} else if (dx <= 0 && dz >= 0) {
				nYaw = Math.toDegrees(Math.atan(dz / dx)) - 90;
			} else if (dx >= 0 && dz >= 0) {
				nYaw = Math.toDegrees(Math.atan(dz / dx)) + 90;
			}
			
			// set play yaw
			mc.thePlayer.rotationYaw = (float) nYaw;
			
			// if couldnt find pitch, then write that to the player
			if (calculatePitch(mc, nYaw, pos)) {
				mc.thePlayer.addChatComponentMessage(new ChatComponentText("[" + EnumChatFormatting.GREEN + "ProjectileAimer" + EnumChatFormatting.WHITE + "]: Found angle "
																			+ EnumChatFormatting.GRAY + "(" + String.valueOf(mc.thePlayer.rotationYaw) + " " + String.valueOf(mc.thePlayer.rotationPitch) + ")"));
			}
			else
			{
				mc.thePlayer.addChatComponentMessage(new ChatComponentText("[" + EnumChatFormatting.RED + "ProjectileAimer" + EnumChatFormatting.WHITE + "]: No valid angle found"));
			}
		}
	}

	public static boolean calculatePitch(Minecraft mc, double nYaw, BlockPos pos) {
	      
		EntityPlayerSP player = mc.thePlayer;
		
		// fully charged bow
		double powerMod = 3;
		float yaw = (float) nYaw;
		
		List<Vector3d> vlist = new ArrayList<Vector3d>();

		for (double pitch = -90.0; pitch <= 90.0; pitch += 0.1) {
			
			pitch = Math.round(pitch * 10) / 10.0;
			
			double posX = mc.thePlayer.posX;
			double posY = mc.thePlayer.eyeHeight + mc.thePlayer.posY;
			double posZ = mc.thePlayer.posZ;

			posX = posX - Math.cos(yaw / 180.0F * Math.PI) * 0.16F;
			posY = posY - 0.1000000014901161F;
			posZ = posZ - Math.sin(yaw / 180.0F * Math.PI) * 0.16F;


			double motX = (-Math.sin(yaw / 180.0F * Math.PI) * Math.cos(pitch / 180.0F * Math.PI));
			double motZ = (Math.cos(yaw / 180.0F * Math.PI) * Math.cos(pitch / 180.0F * Math.PI));
			double motY = (-Math.sin(pitch / 180.0F * Math.PI));

			double f2 = Math.sqrt(motX * motX + motY * motY + motZ * motZ);
			motX /= f2;
			motY /= f2;
			motZ /= f2;

			motX *= powerMod;
			motY *= powerMod;
			motZ *= powerMod;

			double y = posY + motY;
			double totalDistance = 0.0;
			while (y > 0 && totalDistance < 400) {
				motY *= 0.99D;
				motX *= 0.99D;
				motZ *= 0.99D;
				motY -= 0.05F;
				
				Vector3d vec = new Vector3d();
				vec.x = motX;
				vec.y = motY;
				vec.z = motZ;
				
				vlist.add(vec);
				y += motY;
				totalDistance += Math.sqrt(Math.pow(motX, 2) + Math.pow(motY, 2) + Math.pow(motZ, 2));
			}
			
			// get all vectors and add them to player look vector to see if no block are in the way
			for (Vector3d vector : vlist) {
				posX += vector.x;
				posY += vector.y;
				posZ += vector.z;
				
				BlockPos loc = new BlockPos(posX, posY, posZ);
				
				// check if arrow landed in the area
				if ((loc.getX() == pos.getX() || loc.getX() == pos.getX()-1 || loc.getX() == pos.getX()+1) && 
					(loc.getZ() == pos.getZ() || loc.getZ() == pos.getZ()-1 || loc.getZ() == pos.getZ()+1) && 
				    (loc.getY() == pos.getY() || loc.getY() == pos.getY()-1 || loc.getY() == pos.getY()+1)) 
				{
					// set the player pitch
					mc.thePlayer.rotationPitch = (float) pitch;
					return true;
				}
				else if(mc.theWorld.getBlockState(loc).getBlock() != Blocks.air)
				{
					break;
				}
			}
			vlist.clear();

		}
		return false;
	}
}
