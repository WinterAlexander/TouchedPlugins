package me.winterguardian.commandcenter.commands;

import me.winterguardian.core.command.AutoRegistrationCommand;
import me.winterguardian.core.message.ErrorMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.util.Arrays;
import java.util.List;

public class RenameItemCommand extends AutoRegistrationCommand
{
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if(sender instanceof Player)
		{
			Player p = (Player) sender;
			if(args.length == 0)
			{
				sender.sendMessage("§eUtilisez le signe & pour les couleurs et _ pour les espaces. Utilisez ~ pour représenter la valeur précédente.");
				return false;
			}
			else if(args.length == 1)
			{
				if(p.getItemInHand() != null)
				{
					String name = args[0].replaceAll("&", "§").replaceAll("_", " ");
					ItemMeta meta = p.getItemInHand().getItemMeta();
					name.replaceAll("~", meta.getDisplayName());
					meta.setDisplayName(name);
					p.getItemInHand().setItemMeta(meta);
					return true;
				}
				else
				{
					ErrorMessage.WORLD_INVALID_ITEM.say(sender);
					return true;
				}
			}
			else if(args.length == 2)
			{
				if(p.getItemInHand() != null)
				{
					String name = args[0].replaceAll("&", "§").replaceAll("_", " ");
					String lore = args[1].replaceAll("&", "§").replaceAll("_", " ");
					ItemMeta meta = p.getItemInHand().getItemMeta();
					name = name.replaceAll("~", meta.getDisplayName());
					meta.setDisplayName(name);
					if(lore.contains("~"))
					{
						String lastLore = "";
						for(String string : meta.getLore())
							lastLore += "/" + string;
						lore = lore.replaceAll("~", lastLore.replaceFirst("/", ""));
					}
					meta.setLore(Arrays.asList(lore.split("/")));
					p.getItemInHand().setItemMeta(meta);
					return true;
				}
				else
				{
					ErrorMessage.WORLD_INVALID_ITEM.say(sender);
					return true;
				}
			}
			else
			{
				ErrorMessage.COMMAND_INVALID_ARGUMENT.say(sender);
				return false;
			}
		}
		else
		{
			ErrorMessage.COMMAND_INVALID_SENDER.say(sender);
		}
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
		return Arrays.asList("rename", "renommeitem", "itemnameedit", "itemloreedit", "setitemname");
	}

	@Override
	public String getDescription()
	{
		return "Permet de modifier le nom et le lore d'un item.";
	}

	@Override
	public String getName()
	{
		return "renameitem";
	}

	@Override
	public Permission getPermission()
	{
		return new Permission("CommandCenter.rename-item", "Commande /renameitem", PermissionDefault.OP);
	}

	@Override
	public String getUsage()
	{
		return "§cSyntaxe: §f/rename <nom> [lore]";
	}
}
