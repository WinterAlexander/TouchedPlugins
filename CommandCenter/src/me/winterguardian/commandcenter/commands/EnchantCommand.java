package me.winterguardian.commandcenter.commands;


import java.util.Arrays;
import java.util.List;

import me.winterguardian.commandcenter.CommandCenterMessage;
import me.winterguardian.core.command.AutoRegistrationCommand;
import me.winterguardian.core.message.ErrorMessage;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

public class EnchantCommand extends AutoRegistrationCommand
{
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) 
	{
		if(args.length == 0)
		{
			String msg = CommandCenterMessage.ENCHANTS.toString();
			for(Enchantment ench : Enchantment.values())
			{
				msg += "§f" + ench.getId() + ": §e" + ench.getName().toLowerCase() + ", ";
			}
			sender.sendMessage(msg);
		}
		else if(args.length == 2 || args.length == 3)
		{
			Player player = null;
			
			if(args.length == 3)
				player = Bukkit.getPlayer(args[2]);
			else if(sender instanceof Player)
			{
				player = (Player) sender;
			}
			else
			{
				ErrorMessage.COMMAND_INVALID_SENDER.say(sender);
				return false;
			}
			
			try 
			{
				int level = 0;
				
				if(args[1].equalsIgnoreCase("max"))
					level = 32767;
				else if(args[1].equalsIgnoreCase("off"))
					level = 0;
				else
					level = Integer.parseInt(args[1]);
				
				Enchantment enchant = Enchantment.getByName(args[0]);
				
				if(enchant == null)
					enchant = Enchantment.getById(Integer.parseInt(args[0]));
				
				if(level != 0)
					player.getItemInHand().addUnsafeEnchantment(enchant, level);
				else
					player.getItemInHand().removeEnchantment(enchant);
				
				CommandCenterMessage.ENCHANT_COMPLETE.say(sender);
				return true;
			}
			catch(Exception e)
			{
				ErrorMessage.COMMAND_INVALID_ARGUMENT.say(sender);
				return false;
			}
		}
		else
		{
			ErrorMessage.COMMAND_INVALID_ARGUMENT.say(sender);
		}
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender arg0, Command arg1, String arg2, String[] arg3)
	{
		return null;
	}

	@Override
	public List<String> getAliases()
	{
		return Arrays.asList("ench", "nolimitenchant", "nle");
	}

	@Override
	public String getDescription()
	{
		return "Permet d'enchanter un item sans limites. (sauf 32767)";
	}

	@Override
	public String getName()
	{
		return "enchant";
	}

	@Override
	public Permission getPermission()
	{
		return new Permission("CommandCenter.enchant", "Permet d'enchanter ses propres items et ceux des autres.", PermissionDefault.OP);
	}

	@Override
	public String getUsage()
	{
		return "§cSyntaxe: §f/enchant <enchantId|enchantName> <level|max|off> [player]";
	}
}
