package me.winterguardian.commandcenter.commands;

import me.winterguardian.commandcenter.CommandCenterMessage;
import me.winterguardian.core.command.AutoRegistrationCommand;
import me.winterguardian.core.message.ErrorMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.util.List;

/**
 *
 * Created by Alexander Winter on 2016-01-05.
 */
public class ItemFlagCommand extends AutoRegistrationCommand
{
	public ItemFlagCommand()
	{
		super();
	}

	@Override
	public String getName()
	{
		return "itemflag";
	}

	@Override
	public Permission getPermission()
	{
		return new Permission("CommandCenter.item-flag", getDescription(), PermissionDefault.OP);
	}

	@Override
	public String getUsage()
	{
		return "§cSyntaxe: §f/itemflag <flag>";
	}

	@Override
	public String getDescription()
	{
		return null;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if(args.length == 0)
		{
			CommandCenterMessage.ITEMFLAG_FLAGS.say(sender);
			String itemflags = "§a" + ItemFlag.values()[0].name().toLowerCase().replace('_', '-');
			for(int i = 1; i < ItemFlag.values().length; i++)
				itemflags += "§f, §a" + ItemFlag.values()[i].name().toLowerCase().replace('_', '-');

			sender.sendMessage(itemflags);
			return true;
		}

		if(!(sender instanceof Player))
		{
			ErrorMessage.COMMAND_INVALID_SENDER.say(sender);
			return true;
		}

		Player player = (Player)sender;

		if(args.length != 2)
		{
			ErrorMessage.COMMAND_INVALID_ARGUMENT.say(sender);
			return true;
		}

		if(player.getItemInHand() == null)
		{
			ErrorMessage.WORLD_INVALID_ITEM.say(sender);
			return true;
		}

		ItemStack item = player.getItemInHand();
		ItemMeta meta = item.getItemMeta();

		String flagName = args[1].toUpperCase().replace('-', '_');

		ItemFlag flag = null;

		try
		{
			flag = ItemFlag.valueOf(flagName);
		}
		catch(Exception e)
		{
			try
			{
				flag = ItemFlag.valueOf("HIDE_" + flagName);
			}
			catch(Exception e2)
			{
				ErrorMessage.COMMAND_INVALID_ARGUMENT.say(sender);
				return true;
			}
		}

		if(args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("hide"))
		{
			meta.addItemFlags(flag);
			item.setItemMeta(meta);
			CommandCenterMessage.ITEMFLAG_ADD.say(sender, "<flag>", flag.name().toLowerCase().replace('_', '-'));
			return true;
		}
		else if(args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("show"))
		{
			meta.removeItemFlags(flag);
			item.setItemMeta(meta);
			CommandCenterMessage.ITEMFLAG_REMOVE.say(sender, "<flag>", flag.name().toLowerCase().replace('_', '-'));
			return true;
		}

		ErrorMessage.COMMAND_INVALID_ARGUMENT.say(sender);
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings)
	{
		return null;
	}
}
