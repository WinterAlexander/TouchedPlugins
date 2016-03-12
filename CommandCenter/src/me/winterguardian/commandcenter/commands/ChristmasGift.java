package me.winterguardian.commandcenter.commands;

import me.winterguardian.core.Core;
import me.winterguardian.core.command.AutoRegistrationCommand;
import me.winterguardian.core.message.ErrorMessage;
import me.winterguardian.core.playerstats.PlayerStats;
import me.winterguardian.core.util.SoundEffect;
import me.winterguardian.core.world.LocationUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 *
 * Created by Alexander Winter on 2015-12-24.
 */
public class ChristmasGift extends AutoRegistrationCommand implements Listener
{
	private List<UUID> takens;
	private Location giftLoc;


	@Override
	public void register(Plugin plugin)
	{
		super.register(plugin);

		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	@Override
	public void unregister()
	{
		HandlerList.unregisterAll(this);
		super.unregister();
	}


	public ChristmasGift()
	{
		this.takens = new ArrayList<>();
		this.giftLoc = null;
	}

	@Override
	public String getName()
	{
		return "christmasgift";
	}

	@Override
	public Permission getPermission()
	{
		return new Permission("CommandCenter.christmasgift", getDescription(), PermissionDefault.OP);
	}

	@Override
	public String getUsage()
	{
		return "Si tu ne sais pas utiliser cette commande, ne l'utilises pas.";
	}

	@Override
	public String getDescription()
	{
		return "Place le cadeau de noël à cette position.";
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if(!(sender instanceof Player))
		{
			ErrorMessage.COMMAND_INVALID_SENDER.say(sender);
			return true;
		}

		this.giftLoc = ((Player)sender).getLocation();
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings)
	{
		return null;
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerInteract(PlayerInteractEvent event)
	{
		if(event.getClickedBlock() == null || giftLoc == null)
			return;

		if(!LocationUtil.equalsAsBlock(event.getClickedBlock().getLocation(), giftLoc))
			return;

		event.setCancelled(true);

		for(UUID uuid : this.takens)
			if(uuid.equals(event.getPlayer().getUniqueId()))
				return;

		this.takens.add(event.getPlayer().getUniqueId());
		PlayerStats stats = new PlayerStats(Core.getUserDatasManager().getUserData(event.getPlayer()));
		stats.addPoints(25000);
		event.getPlayer().sendMessage("§aPoints +25 000");
		event.getPlayer().sendMessage("§f§lJ§c§lo§f§ly§c§le§f§lu§c§lx §f§lN§c§lo§f§lë§c§ll §f§l!!!");
		new SoundEffect(Sound.ENDERDRAGON_DEATH, 1.0f, 1.0f).play(event.getPlayer());
	}
}
