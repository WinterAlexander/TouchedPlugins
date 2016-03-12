package me.winterguardian.jsonconverter;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;


public class JsonMessageSender
{
	private JsonMessageSender() {}

	private static String getVersion()
	{
		Class<?> bukkitServerClass = Bukkit.getServer().getClass();
		String[] pas = bukkitServerClass.getName().split("\\.");
		return pas[3];
	}
	
	private static void sendPacket(Player player, Object packet) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException, ClassNotFoundException
	{
		Object entityPlayer = player.getClass().getMethod("getHandle").invoke(player);
		
		Object playerConnection = entityPlayer.getClass().getField("playerConnection").get(entityPlayer);
		
		playerConnection.getClass().getMethod("sendPacket", new Class<?>[]{Class.forName("net.minecraft.server." + getVersion() + ".Packet")}).invoke(playerConnection, packet);
	}
	
	private static Object getSerialized(String string) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException
	{
		return Class.forName("net.minecraft.server." + getVersion() + ".IChatBaseComponent$ChatSerializer").getMethod("a", new Class<?>[]{String.class}).invoke(null, string);
	}
	
	public static void sendJsonChatMessage(Player player, String jsonMessage)
	{
		if(player == null || jsonMessage == null)
			return;
		
		try
		{
			Object packet = Class.forName("net.minecraft.server." + getVersion() + ".PacketPlayOutChat").newInstance();
			
			Field content = packet.getClass().getDeclaredField("a");
			if(!content.isAccessible())
				content.setAccessible(true);
			content.set(packet, getSerialized(jsonMessage));

			Field data = packet.getClass().getDeclaredField("b");
			if(!data.isAccessible())
				data.setAccessible(true);
			data.set(packet, (byte)1);
					
			sendPacket(player, packet);
		}
		catch (Exception e)
		{
			Bukkit.getLogger().warning("Error with method sendJsonChatMessage into class JsonMessageSender, using method sendSafeJsonChatMessage instead.");
			e.printStackTrace();
			sendSafeJsonChatMessage(player, jsonMessage);
		}
	}
	
	public static void sendSafeJsonChatMessage(Player player, String jsonMessage)
	{
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tellraw " + player.getName() + " " + jsonMessage);
	}
	
	public static void sendJsonTabHeaderAndFooter(Player player, String jsonHeader, String jsonFooter)
	{
		try
		{
			Object packet = Class.forName("net.minecraft.server." + getVersion() + ".PacketPlayOutPlayerListHeaderFooter").newInstance();

			Field headerField = packet.getClass().getDeclaredField("a");
			if(!headerField.isAccessible())
				headerField.setAccessible(true);
			headerField.set(packet, getSerialized(jsonHeader));

			Field footerField = packet.getClass().getDeclaredField("b");
			if(!footerField.isAccessible())
				footerField.setAccessible(true);
			footerField.set(packet, getSerialized(jsonFooter));

			sendPacket(player, packet);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void sendJsonTitle(Player p, String jsonTitle, String jsonSubTitle, int fadeIn, int delay, int fadeOut)
	{
		if(p != null)
		{
			try
			{
				Object times = Class.forName("net.minecraft.server." + getVersion() + ".PacketPlayOutTitle").getConstructor(new Class<?>[]{int.class, int.class, int.class}).newInstance(fadeIn, delay, fadeOut);
				
				Object subTitle = Class.forName("net.minecraft.server." + getVersion() + ".PacketPlayOutTitle").newInstance();
				
				Field subTitleAction = subTitle.getClass().getDeclaredField("a");
				if(!subTitleAction.isAccessible())
					subTitleAction.setAccessible(true);
				subTitleAction.set(subTitle, Class.forName("net.minecraft.server." + getVersion() + ".PacketPlayOutTitle$EnumTitleAction").getField("SUBTITLE").get(null));

				Field subTitleContent = subTitle.getClass().getDeclaredField("b");
				if(!subTitleContent.isAccessible())
					subTitleContent.setAccessible(true);
				subTitleContent.set(subTitle, getSerialized(jsonSubTitle));
				
				Object title = Class.forName("net.minecraft.server." + getVersion() + ".PacketPlayOutTitle").newInstance();
				
				Field titleAction = title.getClass().getDeclaredField("a");
				if(!titleAction.isAccessible())
					titleAction.setAccessible(true);
				
				titleAction.set(title, Class.forName("net.minecraft.server." + getVersion() + ".PacketPlayOutTitle$EnumTitleAction").getField("TITLE").get(null));

				Field titleContent = title.getClass().getDeclaredField("b");
				if(!titleContent.isAccessible())
					titleContent.setAccessible(true);
				titleContent.set(title, getSerialized(jsonTitle));
				
				sendPacket(p, times);
				sendPacket(p, subTitle);
				sendPacket(p, title);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
}
