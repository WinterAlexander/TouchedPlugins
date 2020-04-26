package me.winterguardian.commandcenter.commands;

import me.winterguardian.commandcenter.CommandCenterMessage;
import me.winterguardian.core.command.AutoRegistrationCommand;
import me.winterguardian.core.message.ErrorMessage;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.util.Arrays;
import java.util.List;

public class SkullCommand extends AutoRegistrationCommand
{
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if(!(sender instanceof Player))
		{
			ErrorMessage.COMMAND_INVALID_SENDER.say(sender);
			return true;
		}

		if(args.length != 1)
		{
			ErrorMessage.COMMAND_INVALID_ARGUMENT.say(sender);
			return false;
		}
		
		
		ItemStack item = new ItemStack(Material.SKULL_ITEM, 1);
		item.setDurability((short) 3);
		
		SkullMeta skull = (SkullMeta)item.getItemMeta();
		skull.setOwner(args[0]);
		
		item.setItemMeta(skull);
		
		((Player) sender).getInventory().addItem(item);
		((Player) sender).updateInventory();
		CommandCenterMessage.SKULL_GIVED.say(sender, "<skull>", args[0]);

		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender arg0, Command arg1,
			String arg2, String[] arg3)
	{
		return null;
	}

	@Override
	public List<String> getAliases()
	{
		return Arrays.asList("tete", "crane", "tête", "crâne", "head", "playerhead", "playerskull");
	}

	@Override
	public String getDescription()
	{
		return "Permet de recevoir le crâne d'un joueur.";
	}

	@Override
	public String getName() {
		return "skull";
	}

	@Override
	public Permission getPermission()
	{
		return new Permission("CommandCenter.skull", "Commande /skull", PermissionDefault.OP);
	}

	@Override
	public String getUsage()
	{
		return "§cSyntaxe: §f/skull <pseudo>";
	}

}
