package me.winterguardian.commandcenter.commands;

import me.winterguardian.core.command.AutoRegistrationCommand;
import me.winterguardian.core.message.ErrorMessage;
import me.winterguardian.core.util.TextUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;

public class ItemInfoCommand extends AutoRegistrationCommand
{
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (!(sender instanceof Player))
		{
			ErrorMessage.COMMAND_INVALID_SENDER.say(sender);
			return true;
		}
		
		ItemStack stack = ((Player)sender).getItemInHand();
		
		if(stack == null || stack.getItemMeta() == null)
		{
			ErrorMessage.WORLD_INVALID_ITEM.say(sender);
			return true;
		}
		
		sender.sendMessage("§eNom custom: §f" + (stack.getItemMeta().getDisplayName() != null ? stack.getItemMeta().getDisplayName() : "Aucun"));
		sender.sendMessage("§eType: §f" + stack.getType().name());
		sender.sendMessage("§eQuantité: §f" + stack.getAmount());
		sender.sendMessage("§eDurabilité: §f" + stack.getDurability());
		sender.sendMessage("§eLore: §f" + (stack.getItemMeta().getLore() != null ? TextUtil.toString(stack.getItemMeta().getLore(), "§r, ") : "Aucun"));
		sender.sendMessage("§eIncassable: §f" + stack.getItemMeta().spigot().isUnbreakable());
		
		String itemFlags = "Aucun";
		
		if(stack.getItemMeta().getItemFlags().size() != 0)
		{
			itemFlags = "";
			for(ItemFlag flag : stack.getItemMeta().getItemFlags())
				itemFlags += flag.name() + ", ";
			
			itemFlags.substring(0, itemFlags.length() - 2);
		}
		
		sender.sendMessage("§eItem Flags: §f" + itemFlags);
		
		
		String enchants = "Aucun";
		
		
		if(stack.getEnchantments().size() != 0)
		{
			enchants = "";
			for(Entry<Enchantment, Integer> enchant : stack.getEnchantments().entrySet())
				enchants += enchant.getKey().getName() + " " + TextUtil.arabicToRomanNumbers(enchant.getValue()) + ", ";
			
			enchants.substring(0, enchants.length() - 2);
		}
		
		sender.sendMessage("§eEnchantements: §f" + enchants);
		
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
		return Arrays.asList("infoitem", "itemdb");
	}

	@Override
	public String getDescription()
	{
		return "Permet d'obtenir toutes les informations sur un item.";
	}

	@Override
	public String getName()
	{
		return "iteminfo";
	}

	@Override
	public Permission getPermission()
	{
		return new Permission("CommandCenter.item-info", "/iteminfo", PermissionDefault.OP);
	}

	@Override
	public String getUsage()
	{
		return "§cSyntaxe: §f/iteminfo";
	}

}
