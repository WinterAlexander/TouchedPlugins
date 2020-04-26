package me.winterguardian.commandcenter.commands;

import me.winterguardian.core.command.AutoRegistrationCommand;
import me.winterguardian.core.message.ErrorMessage;
import me.winterguardian.core.util.TextUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.util.Arrays;
import java.util.List;

public class ColorCommand extends AutoRegistrationCommand
{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if(args.length == 0)
			args = new String[]{"bukkit"};
		
		if(args[0].equalsIgnoreCase("bukkit"))
		{
			return false;
		}
		
		if(args[0].equalsIgnoreCase("json"))
		{
			return false;
		}
		
		if(args[0].equalsIgnoreCase("firework"))
		{
			return false;
		}
		
		ErrorMessage.COMMAND_INVALID_ARGUMENT.say(sender);
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args)
	{
		if(args.length == 1)
			return TextUtil.getStringsThatStartWith(args[1], Arrays.asList("bukkit", "json", "firework"));
		return null;
	}

	@Override
	public String getName()
	{
		return "color";
	}

	@Override
	public Permission getPermission()
	{
		return new Permission("CommandCenter.Color", "Affiche la liste des couleurs au joueur", PermissionDefault.TRUE);
	}
	
	@Override
	public List<Permission> getOtherPermissions()
	{
		return Arrays.asList(new Permission("CommandCenter.Color.Format", "Affiche aussi les formats dans la liste des couleurs", PermissionDefault.OP));
	}

	@Override
	public List<String> getAliases()
	{
		return Arrays.asList("couleur", "mccolor", "aide-couleur", "aide_couleur");
	}

	@Override
	public String getUsage()
	{
		return "C'est à Winter qu'il faut se plaindre puisqu'il fait toujours ses commandes à moitié.";
	}

	@Override
	public String getDescription()
	{
		return "Affiche la liste des couleurs et des formats disponibles sur minecraft.";
	}

}
