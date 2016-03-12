package me.winterguardian.commandcenter.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.winterguardian.core.Core;
import me.winterguardian.core.command.AutoRegistrationCommand;
import me.winterguardian.core.message.CoreMessage;
import me.winterguardian.core.message.ErrorMessage;
import me.winterguardian.core.userdata.UserData;
import me.winterguardian.core.util.TextUtil;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

public class UserDataCommand extends AutoRegistrationCommand
{
	@SuppressWarnings("unchecked")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if(args.length < 2)
		{
			ErrorMessage.COMMAND_INVALID_ARGUMENT.say(sender);
			return false;
		}
		
		UserData data = Core.getUserData(args[1]);
		
		if(data == null)
		{
			ErrorMessage.COMMAND_INVALID_PLAYER.say(sender);
			return false;
		}
		
		if(args[0].equalsIgnoreCase("reload"))
		{
			if(!data.getPlayer().isOnline())
			{
				CoreMessage.USERDATA_USELESSRELOAD.say(sender);
				return true;
			}
			Core.removeUserData(data);
			UserData reloadedUserData = new UserData(data.getPlayer());
			reloadedUserData.load();
			Core.addUserData(reloadedUserData);
			CoreMessage.USERDATA_RELOAD.say(sender);
			return true;
		}
		else if(args[0].equalsIgnoreCase("get") && args.length == 3)
		{
			sender.sendMessage(data.getContent().get(args[2]).toString());
			return true;
		}
		else if(args[0].equalsIgnoreCase("set") && args.length >= 4)
		{
			String stringValue = args[3];
			for(int i = 4; i < args.length; i++)
				stringValue += " " + args[i];
			Object value = null;
			do
			{
				if(stringValue.equalsIgnoreCase("null"))
				{
					break;
				}
				
				try
				{
					value = Double.parseDouble(stringValue.replaceAll(" ", ""));
					break;
				}
				catch(Exception e) {}
				
				try
				{
					value = Integer.parseInt(stringValue.replaceAll(" ", ""));
					break;
				}
				catch(Exception e) {}
				
				if(stringValue.startsWith("[") && stringValue.endsWith("]"))
				{
					value = new ArrayList<String>();
					
					for(String element : stringValue.substring(1, stringValue.length() - 1).split(", "))
						((List<String>)value).add(element);
					break;
				}
				
				value = stringValue;
			}
			while(false);
			
			try
			{
				data.getContent().set(args[2], value);
				data.save(true);
				CoreMessage.USERDATA_SETDATA.say(sender);
			}
			catch(Exception e)
			{
				CoreMessage.USERDATA_SETDATA_ERROR.say(sender);
			}
			return true;
		}
		else
		{
			ErrorMessage.COMMAND_INVALID_ARGUMENT.say(sender);
			return false;
		}
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args)
	{
		if(args.length == 1)
		{
			return TextUtil.getStringsThatStartWith(args[0], Arrays.asList("reload", "set", "get"));
		}
		else if(args.length == 3 && (args[0].equalsIgnoreCase("set") || args[0].equalsIgnoreCase("get")))
		{
			if(Bukkit.getPlayer(args[1]) == null)
				return null;
			
			UserData data = Core.getUserData(args[1]);
			
			if(data != null)
				return TextUtil.getStringsThatStartWith(args[2], new ArrayList<String>(data.getContent().getKeys(true)));
		}
		return null;
	}

	@Override
	public String getName()
	{
		return "userdata";
	}

	@Override
	public Permission getPermission()
	{
		return new Permission("CommandCenter.userdata", getDescription(), PermissionDefault.OP);
	}

	@Override
	public String getPermissionMessage()
	{
		return ErrorMessage.COMMAND_INVALID_PERMISSION.toString();
	}

	@Override
	public List<String> getAliases()
	{
		return Arrays.asList("userdat", "userd");
	}

	@Override
	public String getUsage()
	{
		return "/userdata <reload|set|get> <player> [data]";
	}

	@Override
	public String getDescription()
	{
		return "Permet d'éditer les userdata.";
	}
}
