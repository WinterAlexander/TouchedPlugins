package me.winterguardian.commandcenter.commands;

import me.winterguardian.core.Core;
import me.winterguardian.core.command.AsyncCommand;
import me.winterguardian.core.playerstats.MappedData;
import me.winterguardian.core.playerstats.PlayerStats;
import me.winterguardian.core.util.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

public class RconVoteCommand extends AsyncCommand
{
	public RconVoteCommand(Plugin plugin)
	{
		super(plugin);
	}

	@Override
	public void register(Plugin plugin)
	{
		Core.getUserDatasManager().enableDB(plugin, null, null, null, null, RconVoteCommand.getTables());
		super.register(plugin);
	}

	@Override
	public List<String> getAliases()
	{
		return null;
	}

	@Override
	public String getDescription()
	{
		return null;
	}

	@Override
	public String getName()
	{
		return "rconvote";
	}

	@Override
	public Permission getPermission()
	{
		return new Permission("CommandCenter.rcon-vote", "Rcon command for votes.", PermissionDefault.OP);
	}

	@Override
	public String getUsage()
	{
		return null;
	}

	@Override
	protected void onAsyncCommand(CommandSender sender, String label, String[] args)
	{
		if(args.length != 1)
			return;

		Player player = Bukkit.getPlayer(args[0]);

		PlayerStats stats;
		UUID id;

		if(player != null)
		{
			id = player.getUniqueId();
			stats = new PlayerStats(Core.getUserDatasManager().getUserData(player));
		}
		else
		{

			Entry<UUID, MappedData> entry = Core.getUserDatasManager().getFirstByValue("core.name", args[0]);

			if(entry == null)
			{
				entry = TextUtil.newEntry(Bukkit.getOfflinePlayer(args[0]).getUniqueId(), new MappedData());
			}

			id = entry.getKey();
			stats = new PlayerStats(entry.getValue());
		}

		int points = 1000;


		stats.getContent().set("vote.total", stats.getContent().getInt("vote.total") + 1);

		if(stats.getContent().getLong("vote.lastvote", 0) + 36 * 60 * 60 * 1000 > System.currentTimeMillis())
		{
			stats.getContent().set("vote.streak", stats.getContent().getInt("vote.streak") + 1);
			points += 50 * stats.getContent().getInt("vote.streak");
		}
		else
			stats.getContent().set("vote.streak", 0);

		stats.getContent().set("vote.lastvote", System.currentTimeMillis());

		stats.addPoints(points);
		stats.getContent().set("vote.score", stats.getContent().getInt("vote.score") + points);
		String message = "§6§lVote §f§l> §b" + args[0] + " §aobtient " + points + " points en votant ! §fSérie: " + stats.getContent().getInt("vote.streak");


		for(Player p : Bukkit.getOnlinePlayers())
			p.sendMessage(message);
		Core.getBungeeMessager().executeEverywhereElse("sayall " + message);

		if(Bukkit.getPlayer(args[0]) == null)
			Core.getUserDatasManager().getLoader().save(id, stats.getContent());
	}

	public static HashMap<String, String> getTables()
	{
		return TextUtil.map("vote->total INTEGER, lastvote BIGINT, streak INTEGER, score INTEGER");
	}

}
