package me.winterguardian.duel.command;

import me.winterguardian.core.command.SubCommand;
import me.winterguardian.core.message.ErrorMessage;
import me.winterguardian.duel.Duel;
import me.winterguardian.duel.DuelMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class InfoSubCommand extends SubCommand
{

	public InfoSubCommand()
	{
		super("info", Arrays.asList("infos", "information", "informations", "aide", "help"), null, ErrorMessage.COMMAND_INVALID_PERMISSION.toString(), "/duel info");
	}

	@Override
	public boolean onSubCommand(CommandSender sender, String label, String[] args)
	{
		DuelMessage.DUEL_INFO.say(sender);
		if(Duel.getInstance().getGame().getPlayer1() != null && Duel.getInstance().getGame().getPlayer2() != null)
			sender.sendMessage(DuelMessage.COMMAND_INFO_TWOPLAYERS.toString().replace("<player1>", Duel.getInstance().getGame().getPlayer1().getName()).replace("<player2>", Duel.getInstance().getGame().getPlayer2().getName()));
		else if(Duel.getInstance().getGame().getPlayer1() != null && Duel.getInstance().getGame().getPlayer2() == null)
			sender.sendMessage(DuelMessage.COMMAND_INFO_ONEPLAYER.toString().replace("<player1>", Duel.getInstance().getGame().getPlayer1().getName()));
		else
			sender.sendMessage(DuelMessage.COMMAND_INFO_NOBODYINGAME.toString());
		if(Duel.getInstance().getGame().getWaiting().size() > 0)
			sender.sendMessage(DuelMessage.COMMAND_INFO_WAITINGLIST.toString());
		int i = 0;
		for(Player p : Duel.getInstance().getGame().getWaiting())
		{
			i++;
			sender.sendMessage("§e" + i + ": " + p.getDisplayName());
		}
		return true;
	}

	@Override
	public List<String> onSubTabComplete(CommandSender sender, String label, String[] args)
	{
		return null;
	}

}
