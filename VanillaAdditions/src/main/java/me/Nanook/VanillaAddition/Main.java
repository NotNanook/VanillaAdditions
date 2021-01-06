package me.Nanook.VanillaAddition;

import java.lang.reflect.Field;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

@Mod(modid = Main.MODID, name = Main.NAME, version = Main.VERSION)
public class Main {

	public static final String MODID = "VA";
	public static final String VERSION = "1.3";
	public static final String NAME = "Vanilla Addition";
	public static boolean freecam_pa_enabled = false;
	public static boolean playerInvis = false;
	public static boolean spleefHack = false;
	public static boolean fastPlace = false;

	public static KeyBinding kFreecam = new KeyBinding("ProjectileAimer toggle", Keyboard.KEY_F, "Vanilla Additions");
	public static KeyBinding kPlayerVis = new KeyBinding("Toggle player visibility", Keyboard.KEY_G, "Vanilla Additions");
	public static KeyBinding kDistance = new KeyBinding("Get Distance", Keyboard.KEY_V, "Vanilla Additions");
	public static KeyBinding kFastPlace = new KeyBinding("Toggle Fastplace", Keyboard.KEY_P, "Vanilla Additions");
	
	public static Field fastField;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(new onInteract());

		// Key bind register
		ClientRegistry.registerKeyBinding(kFreecam);
		ClientRegistry.registerKeyBinding(kPlayerVis);
		ClientRegistry.registerKeyBinding(kDistance);
		ClientRegistry.registerKeyBinding(kFastPlace);
		MinecraftForge.EVENT_BUS.register(new keyHandler());

		MinecraftForge.EVENT_BUS.register(new onUpdate());

		MinecraftForge.EVENT_BUS.register(new ChannelHandlerInput());
		MinecraftForge.EVENT_BUS.register(new PacketHandler());
	}

	@EventHandler
	public void postinit(FMLPostInitializationEvent event) {
	}
}
