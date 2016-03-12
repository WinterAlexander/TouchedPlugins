package me.winterguardian.duel.command;

import java.util.Arrays;
import java.util.List;

import me.winterguardian.core.command.SubCommand;
import me.winterguardian.core.message.ErrorMessage;
import me.winterguardian.duel.Duel;
import me.winterguardian.duel.DuelMessage;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RestartSubCommand extends SubCommand
{

	public RestartSubCommand()
	{
		super("restart", Arrays.asList("stop", "arret", "arrêt", "debug"), Duel.MODERATION, ErrorMessage.COMMAND_INVALID_PERMISSION.toString(), "/duel restart");
	}

	@Override
	public boolean onSubCommand(CommandSender sender, String label, String[] args)
	{
		for(Player p : Bukkit.getOnlinePlayers())
			if(p.hasPermission(Duel.MODERATION))
				p.sendMessage("§cArrêt de §6§lDuel §cen cours par §e" + sender.getName() + "§c.");
		if(Duel.getInstance().getGame().stop(false))
			for(Player p : Bukkit.getOnlinePlayers())
				p.sendMessage(DuelMessage.DUEL_STOPCOMPLETE.toString());
		else
			DuelMessage.DUEL_STOPERROR.say(sender);
		return true;
	}

	@Override
	public List<String> onSubTabComplete(CommandSender arg0, String arg1, String[] arg2)
	{
		return null;
	}

}
