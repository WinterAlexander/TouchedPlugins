package me.winterguardian.blockfarmers.command;

import me.winterguardian.blockfarmers.BlockFarmersMessage;
import me.winterguardian.blockfarmers.BlockFarmersPlugin;
import me.winterguardian.blockfarmers.BlockFarmersSetup;
import me.winterguardian.core.Core;
import me.winterguardian.core.command.SubCommand;
import me.winterguardian.core.util.TextUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class SetupSubCommand extends SubCommand
{
	private BlockFarmersSetup setup;
	
	public SetupSubCommand(BlockFarmersSetup setup)
	{
		super("setup", Arrays.asList("config", "settings", "option", "options", "réglages", "reglages", "paramètres", "parametres"), BlockFarmersPlugin.STAFF, BlockFarmersMessage.COMMAND_INVALID_PERMISSION.toString(), BlockFarmersMessage.COMMAND_CONFIG_USAGE.toString());
		this.setup = setup;
	}

	@Override
	public boolean onSubCommand(CommandSender sender, String label, String[] args)
	{
		
		if(args.length == 0)
		{
			BlockFarmersMessage.COMMAND_CONFIG_MENU_TITLE.say(sender);
			
			BlockFarmersMessage.COMMAND_CONFIG_MENU_SPAWN.say(sender, "<loc>", setup.getSpawn() != null ? BlockFarmersMessage.COMMAND_CONFIG_SET.toString() : BlockFarmersMessage.COMMAND_CONFIG_UNSET.toString());
			BlockFarmersMessage.COMMAND_CONFIG_MENU_LOBBY.say(sender, "<loc>", setup.getLobby() != null ? BlockFarmersMessage.COMMAND_CONFIG_SET.toString() : BlockFarmersMessage.COMMAND_CONFIG_UNSET.toString());
			BlockFarmersMessage.COMMAND_CONFIG_MENU_EXIT.say(sender, "<loc>", setup.getExit() != null ? BlockFarmersMessage.COMMAND_CONFIG_SET.toString() : BlockFarmersMessage.COMMAND_CONFIG_UNSET.toString());
			BlockFarmersMessage.COMMAND_CONFIG_MENU_REGION.say(sender, "<region>", setup.getRegion() != null ? BlockFarmersMessage.COMMAND_CONFIG_SET.toString() : BlockFarmersMessage.COMMAND_CONFIG_UNSET.toString());
			
			if(setup.getRegion() != null && setup.getExit() != null && setup.getRegion().contains(setup.getExit()))
				BlockFarmersMessage.COMMAND_CONFIG_MENU_WARNING_EXITINREGION.say(sender);
			
			if(setup.getRegion() != null && setup.getSpawn() != null && !setup.getRegion().contains(setup.getSpawn()))
				BlockFarmersMessage.COMMAND_CONFIG_MENU_WARNING_SPAWNNOTINREGION.say(sender);
			
			if(setup.getRegion() != null && setup.getLobby() != null && !setup.getRegion().contains(setup.getLobby()))
				BlockFarmersMessage.COMMAND_CONFIG_MENU_WARNING_LOBBYNOTINREGION.say(sender);
			
			return true;
		}
		else if(args[0].equalsIgnoreCase("setspawn"))
		{
			if(!(sender instanceof Player))
			{
				BlockFarmersMessage.COMMAND_INVALID_SENDER.say(sender);
				return true;
			}
			
			setup.setSpawn(((Player)sender).getLocation());
			BlockFarmersMessage.COMMAND_CONFIG_SPAWNSET.say(sender);
			return true;
		}
		else if(args[0].equalsIgnoreCase("setlobby"))
		{
			if(!(sender instanceof Player))
			{
				BlockFarmersMessage.COMMAND_INVALID_SENDER.say(sender);
				return true;
			}
			
			setup.setLobby(((Player)sender).getLocation());
			BlockFarmersMessage.COMMAND_CONFIG_LOBBYSET.say(sender);
			return true;
		}
		else if(args[0].equalsIgnoreCase("setexit"))
		{
			if(!(sender instanceof Player))
			{
				BlockFarmersMessage.COMMAND_INVALID_SENDER.say(sender);
				return true;
			}
			
			setup.setExit(((Player)sender).getLocation());
			BlockFarmersMessage.COMMAND_CONFIG_EXITSET.say(sender);
			return true;
		}
		else if(args[0].equalsIgnoreCase("setregion"))
		{
			if(!(sender instanceof Player))
			{
				BlockFarmersMessage.COMMAND_INVALID_SENDER.say(sender);
				return true;
			}
			
			setup.setRegion(Core.getWand().getRegion((Player) sender));
			BlockFarmersMessage.COMMAND_CONFIG_REGIONSET.say(sender);
			return true;
		}
		else if(args[0].equalsIgnoreCase("reload"))
		{
			setup.load();
			BlockFarmersMessage.COMMAND_CONFIG_RELOADED.say(sender);
			return true;
		}
		else if(args[0].equalsIgnoreCase("save"))
		{
		
			setup.save();
			BlockFarmersMessage.COMMAND_CONFIG_SAVED.say(sender);
			return true;
		}
		else if(args[0].equalsIgnoreCase("reset"))
		{
			try
			{
				setup.getFile().delete();
				setup.load();
				BlockFarmersMessage.COMMAND_CONFIG_RESET.say(sender);
			}
			catch (Exception e)
			{
				BlockFarmersMessage.COMMAND_CONFIG_RESET_ERROR.say(sender);
				e.printStackTrace();
			}
			
			return true;
		}		
		BlockFarmersMessage.COMMAND_INVALID_ARGUMENT.say(sender);
		return false;
	}

	@Override
	public List<String> onSubTabComplete(CommandSender arg0, String arg1, String[] args)
	{
		if(args.length == 1)
		{
			return TextUtil.getStringsThatStartWith(args[0], Arrays.asList("setspawn", "setlobby", "setexit", "setregion", "reload", "save", "reset"));
		}
		return null;
	}

}
