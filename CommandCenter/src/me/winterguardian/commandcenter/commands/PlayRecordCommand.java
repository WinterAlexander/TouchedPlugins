package me.winterguardian.commandcenter.commands;

import java.util.Arrays;
import java.util.List;

import me.winterguardian.commandcenter.CommandCenterMessage;
import me.winterguardian.core.command.AutoRegistrationCommand;
import me.winterguardian.core.message.ErrorMessage;
import me.winterguardian.core.util.RecordUtil;
import me.winterguardian.core.util.TextUtil;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

public class PlayRecordCommand extends AutoRegistrationCommand
{
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if(args.length != 4)
		{
			ErrorMessage.COMMAND_INVALID_ARGUMENT.say(sender);
			return false;
		}
		
		int x, y, z;
		
		try
		{
			x = Integer.parseInt(args[0]);
			y = Integer.parseInt(args[1]);
			z = Integer.parseInt(args[2]);
		}
		catch(Exception e)
		{
			ErrorMessage.COMMAND_INVALID_ARGUMENT.say(sender);
			return false;
		}
		
		Material record = Material.matchMaterial(args[3].toUpperCase());
		
		World world;
		
		if(sender instanceof Player)
			world = ((Player)sender).getWorld();

		else if(sender instanceof BlockCommandSender)
			world = ((BlockCommandSender)sender).getBlock().getWorld();
		
		else
		{
			ErrorMessage.COMMAND_INVALID_SENDER.say(sender);
			return true;
		}
		
		
		if(record != null)
		{
			for(Player p : world.getPlayers())
				RecordUtil.playRecord(p, new Location(world, x, y, z), record);
			CommandCenterMessage.MUSICPLAYED.say(sender);
		}
		else
		{
			for(Player p : world.getPlayers())
				RecordUtil.stopRecord(p, new Location(world, x, y, z));
			CommandCenterMessage.MUSICSTOPPED.say(sender);
		}
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args)
	{
		if(args.length == 4)
		{
			return TextUtil.getStringsThatStartWith(args[3], Arrays.asList(
					Material.GOLD_RECORD.name().toLowerCase(),
					Material.GREEN_RECORD.name().toLowerCase(),
					Material.RECORD_3.name().toLowerCase(),
					Material.RECORD_4.name().toLowerCase(),
					Material.RECORD_5.name().toLowerCase(),
					Material.RECORD_6.name().toLowerCase(),
					Material.RECORD_7.name().toLowerCase(),
					Material.RECORD_8.name().toLowerCase(),
					Material.RECORD_9.name().toLowerCase(),
					Material.RECORD_10.name().toLowerCase(),
					Material.RECORD_11.name().toLowerCase(),
					Material.RECORD_12.name().toLowerCase(),
					"null"));
		}
		return null;
	}

	@Override
	public String getName()
	{
		return "playrecord";
	}

	@Override
	public Permission getPermission()
	{
		return new Permission("CommandCenter.play-record", "/playrecord", PermissionDefault.OP);
	}

	@Override
	public List<String> getAliases()
	{
		return Arrays.asList("playmusic", "playdisc");
	}

	@Override
	public String getUsage()
	{
		return "§cSyntaxe: §f/playrecord <x> <y> <z> <disc id>";
	}

	@Override
	public String getDescription()
	{
		return "Permet de faire jouer un disque à une position donnée.";
	}

}
