package me.winterguardian.duel.command;

import java.util.Arrays;
import java.util.List;

import me.winterguardian.core.command.SubCommand;
import me.winterguardian.core.game.GameManager;
import me.winterguardian.core.message.ErrorMessage;
import me.winterguardian.core.util.PlayerUtil;
import me.winterguardian.duel.Duel;
import me.winterguardian.duel.DuelMessage;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WarpSubCommand extends SubCommand
{
	public WarpSubCommand()
	{
		super("warp", Arrays.asList("teleport", "téléporter", "tp", "go"), null, null, "/duel warp [player]");
	}

	@Override
	public boolean onSubCommand(CommandSender sender, String label, String[] args)
	{
		if(!Duel.getInstance().getSettings().isReady())
		{
			DuelMessage.DUEL_NOTREADY.say(sender);
			return true;
		}
		
		Player p;
		if(args.length == 1)
		{
			if(!sender.hasPermission(Duel.ADMINISTRATION))
			{
				ErrorMessage.COMMAND_INVALID_PERMISSION.say(sender);
				return false;
			}
			
			p = Bukkit.getPlayer(args[0]);
			if(p == null)
			{
				ErrorMessage.COMMAND_INVALID_PLAYER.say(sender);
				return false;
			}
		}
		else if(sender instanceof Player)
		{
			p = (Player)sender;
			
		}
		else
		{
			ErrorMessage.COMMAND_INVALID_SENDER.say(sender);
			return false;
		}
		
		if(Duel.getInstance().gameContains(p))
		{
			if(sender == p)
				DuelMessage.DUEL_ALREADYINGAME.say(sender);
			else
				DuelMessage.DUEL_PLAYERALREADYINGAME.say(sender);
			return true;
		}
		
		GameManager.leaveAll(p);
		PlayerUtil.clearInventory(p);
		PlayerUtil.heal(p);
		PlayerUtil.prepare(p);
		PlayerUtil.clearBoard(p);
		p.teleport(Duel.getInstance().getSettings().getLobby());
		return true;
	}

	@Override
	public List<String> onSubTabComplete(CommandSender sender, String label, String[] args)
	{
		return null;
	}

}
