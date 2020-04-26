package me.winterguardian.core.util;

import me.winterguardian.core.json.JsonUtil;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;

public class TabUtil
{
	@Deprecated
	public static final String DEFAULT_HEADER = JsonUtil.toJson(" §c✦ §9✦ §f§lTouched§3§lCraft §e✦ §a✦ ");
	@Deprecated
	public static final String DEFAULT_FOOTER = JsonUtil.toJson("§9§nplay.touchedcraft.fr");
	
	public static void sendInfos(Player player, String jsonHeader, String jsonFooter)
	{
		if(player == null)
			return;
		
		try
		{
			Object packet = Class.forName("net.minecraft.server." + ReflectionUtil.getVersion() + ".PacketPlayOutPlayerListHeaderFooter").newInstance();

			Field headerField = packet.getClass().getDeclaredField("a");
			if(!headerField.isAccessible())
				headerField.setAccessible(true);
			headerField.set(packet, ReflectionUtil.getSerialized(jsonHeader));

			Field footerField = packet.getClass().getDeclaredField("b");
			if(!footerField.isAccessible())
				footerField.setAccessible(true);
			footerField.set(packet, ReflectionUtil.getSerialized(jsonFooter));

			ReflectionUtil.sendPacket(player, packet);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void resetTab(Player p)
	{
		sendInfos(p, JsonUtil.toJson(" "), JsonUtil.toJson(" "));
	}
}
