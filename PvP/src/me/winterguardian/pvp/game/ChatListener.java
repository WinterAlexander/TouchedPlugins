package me.winterguardian.pvp.game;

import me.winterguardian.pvp.PvP;
import me.winterguardian.pvp.PvPPlugin;
import me.winterguardian.pvp.stats.PvPStats;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.regex.Pattern;

/**
 *
 * Created by Alexander Winter on 2015-12-17.
 */
public class ChatListener implements Listener
{
	private PvP game;

	public ChatListener(PvP game)
	{
		this.game = game;
	}

	@EventHandler(priority  = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPlayerChat(AsyncPlayerChatEvent event)
	{
		if(!game.getPlayers().contains(event.getPlayer()))
			return;

		String pvpName;

		if(game.getState() instanceof PvPMatch)
			pvpName = ((PvPMatch)game.getState()).getPlayerData(event.getPlayer()).getPvPName();
		else
			pvpName = PvPStats.get(event.getPlayer().getUniqueId()).getPvPName();

		String format = event.getFormat().replaceFirst(Pattern.quote("%s"), Pattern.quote("§§"));
		event.setFormat(pvpName.replace(event.getPlayer().getName(), "%s") + format.split(Pattern.quote("§§"))[1].substring(1));
	}
}
