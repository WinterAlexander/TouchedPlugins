package me.winterguardian.commandcenter.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.winterguardian.core.command.AutoRegistrationCommand;
import me.winterguardian.core.message.ErrorMessage;
import me.winterguardian.core.util.ColorUtil;
import me.winterguardian.core.util.TextUtil;

import org.apache.commons.lang.WordUtils;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.potion.PotionEffectType;

public class BowEffectCommand extends AutoRegistrationCommand
{
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if(args.length == 3)
		{
			try
			{
				if(sender instanceof Player)
				{
					Player p = (Player) sender;
					if(p.getItemInHand() != null && p.getItemInHand().getType() == Material.BOW)
					{
						if(PotionEffectType.getByName(args[0].toUpperCase()) == null)
						{
							ErrorMessage.COMMAND_INVALID_ARGUMENT.say(sender);
							return true;
						}
						
						ItemMeta bowMeta = p.getItemInHand().getItemMeta();
						
						String effectName = WordUtils.capitalize(args[0].toLowerCase().replaceAll("_", " "));
						String amplifier;
						try
						{
							amplifier = TextUtil.arabicToRomanNumbers(Integer.parseInt(args[1]));
						}
						catch(Exception e)
						{
							amplifier = args[1];
						}
						
						String time;
						
						if(args[2].contains(":"))
							time = "(" + args[2] + ")";
						else if(args[2].length() >= 2)
							time = "(0:" + args[2] + ")";
						else
							time = "(0:0" + args[2] + ")";
							
						List<String> lore = new ArrayList<>();
						if(bowMeta.hasLore())
							lore = bowMeta.getLore();
						lore.add(ColorUtil.getColorCodeFromPotionEffectName(args[0].toUpperCase()) + effectName + " " + amplifier + " " + time);
						bowMeta.setLore(lore);
						
						p.getItemInHand().setItemMeta(bowMeta);
						
						sender.sendMessage("§aL'effet a été ajouté sur votre arc.");
					}
					else
					{
						sender.sendMessage("§cVous devez avoir un arc dans votre main.");
					}
				}
				else
				{
					ErrorMessage.COMMAND_INVALID_SENDER.say(sender);
				}
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
			return false;
		}
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args)
	{
		if(args.length <= 1)
		{
			List<String> list = new ArrayList<>();
			for(PotionEffectType type : PotionEffectType.values())
				if(type.getName() != null && type.getName().startsWith(args[0]))
					list.add(type.getName());
			return list;
		}

		if(args.length == 2)
		{
			return TextUtil.getStringsThatStartWith(args[1], Arrays.asList("I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X"));
		}
		
		if(args.length == 3)
		{
			return TextUtil.getStringsThatStartWith(args[2], Arrays.asList("0:05", "0:10", "0:15", "0:20", "0:30", "0:45", "1:00", "1:30"));
		}
		
		return null;
	}

	@Override
	public List<String> getAliases()
	{
		return Arrays.asList("arceffet", "effectbow");
	}

	@Override
	public String getDescription()
	{
		return "Ajouter un effet de potion à un arc.";
	}

	@Override
	public String getName()
	{
		return "boweffect";
	}

	@Override
	public Permission getPermission()
	{
		return new Permission("CommandCenter.boweffect", getDescription(), PermissionDefault.OP);
	}

	@Override
	public String getPermissionMessage()
	{
		return ErrorMessage.COMMAND_INVALID_PERMISSION.toString();
	}

	@Override
	public String getUsage()
	{
		return "/boweffect <effect> <amplifier> <time>";
	}

}
