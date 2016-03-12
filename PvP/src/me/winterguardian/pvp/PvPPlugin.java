package me.winterguardian.pvp;

import me.winterguardian.core.Core;
import me.winterguardian.pvp.stats.PvPStats;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class PvPPlugin extends JavaPlugin
{
	public static Permission DOUBLE_VOTE = new Permission("PvP.double-vote", "Double vote", PermissionDefault.OP);
	public static Permission STAFF = new Permission("PvP.staff", "Accès staff en pvp", PermissionDefault.OP);
	public static Permission ALL = new Permission("PvP.*", "Toutes les permissions pvp", PermissionDefault.OP);
	
	private static PvP game;
	
	@Override
	public void onEnable()
	{
		game = new PvP(this);
		game.onEnable();
		game.setOpen(true);

		Core.getUserDatasManager().enableDB(this, null, null, null, null, PvPStats.getTables());

		DOUBLE_VOTE.addParent(STAFF, true);
		STAFF.addParent(ALL, true);
	}
	
	@Override
	public void onDisable()
	{
		game.onDisable();
		game = null;

		Core.disable(this);
		HandlerList.unregisterAll(this);
		Bukkit.getScheduler().cancelTasks(this);
	}
	
	public static PvP getGame()
	{
		return game;
	}

	public static Plugin getPlugin()
	{
		return game.getPlugin();
	}
}
