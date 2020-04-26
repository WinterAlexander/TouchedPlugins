package me.winterguardian.commandcenter.commands;

import me.winterguardian.commandcenter.CommandCenterMessage;
import me.winterguardian.core.command.AutoRegistrationCommand;
import me.winterguardian.core.message.ErrorMessage;
import me.winterguardian.core.util.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class ClearInventoryCommand extends AutoRegistrationCommand
{
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if(args.length == 0)
		{
			if(sender instanceof Player)
			{
				PlayerUtil.clearInventory(((Player)sender));
				CommandCenterMessage.INVENTORYCLEAR.say(sender);
			}
			else
				ErrorMessage.COMMAND_INVALID_SENDER.say(sender);
			return true;
		}
		else if(args.length >= 1)
		{
			if(sender.hasPermission(getOtherPermissions().iterator().next()))
			{
				Player player = Bukkit.getPlayer(args[0]);
				
				if(player != null)
				{
					PlayerUtil.clearInventory(player);
					if(args.length == 2 && (args[1].equalsIgnoreCase("silent") || args[1].equalsIgnoreCase("silencieux") || args[1].equalsIgnoreCase("-s")))
					{
						CommandCenterMessage.INVENTORYCLEAR_OTHER_SILENTLY.say(sender);
						return true;
						
					}
					CommandCenterMessage.INVENTORYCLEAR.say(player);
					CommandCenterMessage.INVENTORYCLEAR_OTHER.say(sender);
				}
				else
					ErrorMessage.COMMAND_INVALID_PLAYER.say(sender);
			}
			else
				ErrorMessage.COMMAND_INVALID_PERMISSION.say(sender);
		}
		else
			ErrorMessage.COMMAND_INVALID_ARGUMENT.toString();
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args)
	{
		return null;
	}

	@Override
	public List<String> getAliases()
	{
		return Arrays.asList("clearinventory", "clearall", "clearinv", "ca", "ci", "clear");
	}

	@Override
	public String getDescription()
	{
		return "Permet de vider l'inventaire de quelqu'un y compris son armure et l'item sur son curseur.";
	}

	@Override
	public String getName()
	{
		return "clearinventory";
	}

	@Override
	public Permission getPermission()
	{
		return new Permission("CommandCenter.clear-inventory", "Permet de nettoyer son inventaire.", PermissionDefault.OP);
	}

	@Override
	public String getUsage()
	{
		return "§cSyntaxe: §f/clearinventory [player] [-s]";
	}
	
	@Override
	public Collection<Permission> getOtherPermissions()
	{
		return Arrays.asList(new Permission("CommandCenter.clear-inventory.others", "Permet de nettoyer l'inventaire de n'importe qui d'autres.", PermissionDefault.OP));
	}
}
