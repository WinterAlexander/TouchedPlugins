package me.winterguardian.duel.command;

import java.util.Arrays;
import java.util.List;

import me.winterguardian.core.Core;
import me.winterguardian.core.command.SubCommand;
import me.winterguardian.core.message.ErrorMessage;
import me.winterguardian.core.util.TextUtil;
import me.winterguardian.duel.Duel;
import me.winterguardian.duel.DuelMessage;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ManageSubCommand extends SubCommand {

	public ManageSubCommand()
	{
		super("manage", Arrays.asList("admin", "setup", "settings", "config"), Duel.ADMINISTRATION, ErrorMessage.COMMAND_INVALID_PERMISSION.toString(), "§eUsage: §f/duel manage <setlobby|setspawn1|setspawn2|setstuff|getstuff|setregion>");
	}

	@Override
	public boolean onSubCommand(CommandSender sender, String label, String[] args)
	{
		
		if(args.length <= 0)
		{
			sender.sendMessage("§f----- [§6§lDuel §7- §ePanel Admin§f] -----");
			sender.sendMessage("§eLobby: " + (Duel.getInstance().getSettings().getLobby() != null ? "§aDéfini" : "§cIndéfini"));
			sender.sendMessage("§eSpawn §cRouge§e: " + (Duel.getInstance().getSettings().getSpawn1() != null ? "§aDéfini" : "§cIndéfini"));
			sender.sendMessage("§eSpawn §9Bleu§e: " + (Duel.getInstance().getSettings().getSpawn2() != null ? "§aDéfini" : "§cIndéfini"));
			sender.sendMessage("§eRégion: " + (Duel.getInstance().getSettings().isRegionSet() ? "§aDéfinie" : "§cIndéfinie"));
			sender.sendMessage("§eNombre de commandes allouées en jeu: §f" + Duel.getInstance().getSettings().getAllowedCommands().size());
			sender.sendMessage("§eNombre d'items: §f" + Duel.getInstance().getSettings().getNumberOfItems());
			sender.sendMessage("");
			sender.sendMessage("§ePermission Modo: §f" + Duel.MODERATION.getName());
			sender.sendMessage("§ePermission Admin: §f" + Duel.ADMINISTRATION.getName());
			return false;
		}
		
		if(!(sender instanceof Player))
		{
			ErrorMessage.COMMAND_INVALID_SENDER.say(sender);
			return true;
		}
		
		if(args.length == 1)
		{
			if(args[0].equalsIgnoreCase("setlobby"))
			{
				Duel.getInstance().getSettings().setLobby(((Player)sender).getLocation());
				if(Duel.getInstance().getSettings().save())
					DuelMessage.DUEL_LOBBYSET.say(sender);
				else
					DuelMessage.DUEL_SAVEFAILED.say(sender);
				return true;
			}
			else if(args[0].equalsIgnoreCase("setspawn1"))
			{
				Duel.getInstance().getSettings().setSpawn1(((Player)sender).getLocation());
				if(Duel.getInstance().getSettings().save())
					DuelMessage.DUEL_SPAWN1SET.say(sender);
				else
					DuelMessage.DUEL_SAVEFAILED.say(sender);
				return true;
			}
			else if(args[0].equalsIgnoreCase("setspawn2"))
			{
				Duel.getInstance().getSettings().setSpawn2(((Player)sender).getLocation());
				if(Duel.getInstance().getSettings().save())
					DuelMessage.DUEL_SPAWN2SET.say(sender);
				else
					DuelMessage.DUEL_SAVEFAILED.say(sender);
				return true;
			}
			else if(args[0].equalsIgnoreCase("setstuff"))
			{
				Duel.getInstance().getSettings().setStuff(((Player)sender).getInventory().getArmorContents(), ((Player)sender).getInventory().getContents());
				if(Duel.getInstance().getSettings().save())
					DuelMessage.DUEL_STUFFSET.say(sender);
				else
					DuelMessage.DUEL_SAVEFAILED.say(sender);
				return true;
			}
			else if(args[0].equalsIgnoreCase("getstuff"))
			{
				Duel.getInstance().getSettings().giveStuff((Player)sender);
				return true;
			}
			else if(args[0].equalsIgnoreCase("setregion"))
			{
				Duel.getInstance().getSettings().setRegion(Core.getWand().getRegion((Player) sender));
				if(Duel.getInstance().getSettings().save())
					DuelMessage.DUEL_REGIONSET.say(sender);
				else
					DuelMessage.DUEL_SAVEFAILED.say(sender);
				return true;
			}
		}
		
		ErrorMessage.COMMAND_INVALID_ARGUMENT.say(sender);
		return false;
	}

	@Override
	public List<String> onSubTabComplete(CommandSender sender, String label, String[] args)
	{
		if(args.length == 1)
			return TextUtil.getStringsThatStartWith(args[0], Arrays.asList("setlobby", "setspawn1", "setspawn2", "setstuff", "getstuff", "setregion"));
		
		return null;
	}

}
