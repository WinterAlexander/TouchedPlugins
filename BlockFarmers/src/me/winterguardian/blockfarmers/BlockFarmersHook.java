package me.winterguardian.blockfarmers;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderHook;
import me.winterguardian.core.Core;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class BlockFarmersHook extends PlaceholderHook
{
	@Override
	public String onPlaceholderRequest(Player player, String identifier)
	{
		BlockFarmersGame game;
		if(player == null || (game = BlockFarmersPlugin.getGame()) == null)
			return "";
		
		if(identifier.equalsIgnoreCase("minplayers"))
			return "" + game.getMinPlayers();
		
		if(identifier.equalsIgnoreCase("maxplayers"))
			return "" + game.getMaxPlayers();
		
		if(identifier.equalsIgnoreCase("players"))
			return "" + game.getPlayers().size();
		
		if(identifier.equalsIgnoreCase("status"))
			return "" + game.getStatus();
		
		if(identifier.equalsIgnoreCase("open"))
			return "" + game.isOpen();
		
		if(!((BlockFarmersConfig)game.getConfig()).enableStats())
			return "";
	
		FarmersStats stats = new FarmersStats(player, Core.getUserDatasManager().getUserData(player));
		
		if(identifier.equalsIgnoreCase("stats_victories"))
			return "" + stats.getVictories();
		
		if(identifier.equalsIgnoreCase("stats_score"))
			return "" + stats.getScore();
		
		if(identifier.equalsIgnoreCase("stats_gamesplayed"))
			return "" + stats.getGamesPlayed();
		
		if(identifier.equalsIgnoreCase("stats_tilesfarmed"))
			return "" + stats.getTilesFarmed();
		
		return "";
	}

	public boolean register(Plugin plugin)
	{
	    return PlaceholderAPI.registerPlaceholderHook(plugin, this);
	}
	  
	public boolean unregister(Plugin plugin)
	{
	    return PlaceholderAPI.unregisterPlaceholderHook(plugin);
	}
}
