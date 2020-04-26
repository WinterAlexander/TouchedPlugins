package me.winterguardian.commandcenter.commands;

import me.winterguardian.commandcenter.CommandCenterMessage;
import me.winterguardian.core.command.AutoRegistrationCommand;
import me.winterguardian.core.message.ErrorMessage;
import me.winterguardian.core.util.TextUtil;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Jukebox;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.util.Arrays;
import java.util.List;

public class SetMusicDiscCommand extends AutoRegistrationCommand
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
		
		Block block;
		
		if(sender instanceof Player)
			block = ((Player)sender).getWorld().getBlockAt(x, y, z);

		else if(sender instanceof BlockCommandSender)
			block = ((BlockCommandSender)sender).getBlock().getWorld().getBlockAt(x, y, z);
		
		else
		{
			ErrorMessage.COMMAND_INVALID_SENDER.say(sender);
			return true;
		}
		
		if(block.getState() instanceof Jukebox)
		{
			((Jukebox)block.getState()).setPlaying(record);
			if(record != null)
				CommandCenterMessage.JUKEBOXDISC_SET.say(sender);
			else
				CommandCenterMessage.JUKEBOXDISC_REMOVE.say(sender);
			return true;
		}
		else
		{
			ErrorMessage.WORLD_INVALID_BLOCK.say(sender);
			return true;
		}
		
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
		return "setmusicdisc";
	}

	@Override
	public Permission getPermission()
	{
		return new Permission("CommandCenter.set-jukebox-disc", "/setmusicdisc", PermissionDefault.OP);
	}

	@Override
	public List<String> getAliases()
	{
		return Arrays.asList("setjukeboxmusic", "setrecord");
	}

	@Override
	public String getUsage()
	{
		return "§cSyntaxe: §f/setmusicdisc <x> <y> <z> <disc id>";
	}

	@Override
	public String getDescription()
	{
		return "Permet de mettre un disque dans un jukebox.";
	}

}
