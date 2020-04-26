package me.winterguardian.commandcenter.commands;

import me.winterguardian.core.command.AutoRegistrationCommand;
import me.winterguardian.core.message.ErrorMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.List;

public class SpoingCommand extends AutoRegistrationCommand
{
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		switch(args.length)
		{
		case 3:
			if(sender instanceof Player)
			{
				try
				{
					((Player)sender).setVelocity(new Vector(Double.parseDouble(args[0]), Double.parseDouble(args[1]), Double.parseDouble(args[2])));
					return true;
				}
				catch(Exception e)
				{
					ErrorMessage.COMMAND_INVALID_ARGUMENT.say(sender);
					return false;
				}
			}
			
			ErrorMessage.COMMAND_INVALID_SENDER.say(sender);
			return false;
				
		case 4:
			for(Player player : Bukkit.getOnlinePlayers())
			{
				if(player.getName().equalsIgnoreCase(args[0]))
				{
					player.setVelocity(new Vector(Double.parseDouble(args[1]), Double.parseDouble(args[2]), Double.parseDouble(args[3])));
					return true;
				}
			}
			ErrorMessage.COMMAND_INVALID_PLAYER.say(sender);
			return false;
				
		default:
			ErrorMessage.COMMAND_INVALID_ARGUMENT.say(sender);
			break;
		}
		return true;
	}

	@Override
	public String getName()
	{
		return "spoing";
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args)
	{
		return null;
	}
	@Override
	public List<String> getAliases()
	{
		return Arrays.asList("shoot", "boing", "throw", "setvelocity");
	}
	
	@Override
	public String getDescription()
	{
		return "Permet de modifier le vecteur d'un joueur.";
	}
	
	@Override
	public Permission getPermission()
	{
		return new Permission("CommandCenter.spoing", "Commande /spoing", PermissionDefault.OP);
	}
	
	@Override
	public String getUsage()
	{
		return "§cSyntaxe: §f/spoing [player] <x> <y> <z>";
	}

}
