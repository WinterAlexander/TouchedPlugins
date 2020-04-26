package ice.command;

import ice.IceRun;
import ice.IceRunMessage;
import me.winterguardian.core.command.SubCommand;
import me.winterguardian.core.message.ErrorMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class LeaveSubCommand extends SubCommand
{

	public LeaveSubCommand()
	{
		super("quitter", Arrays.asList("leave", "quit", "gtfo", "kick"), null, ErrorMessage.COMMAND_INVALID_PERMISSION.toString(), "/icerun quitter [joueur]");
	}

	@Override
	public boolean onSubCommand(CommandSender sender, String label, String[] args)
	{
		if(args.length >= 1 && sender.hasPermission(IceRun.STAFF))
		{
			Player p = Bukkit.getPlayer(args[0]);
			if(p == null)
			{
				ErrorMessage.COMMAND_INVALID_PLAYER.say(sender);
				return false;
			}
			
			if(!IceRun.getPlugin().contains(p))
			{
				IceRunMessage.LEAVE_PLAYERNOTINGAME.say(sender);
				return true;
			}
			
			IceRun.getPlugin().leave(p);
			return true;
		}
		
		if(!(sender instanceof Player))
		{
			ErrorMessage.COMMAND_INVALID_SENDER.say(sender);
			return false;
		}
		
		if(!IceRun.getPlugin().contains((Player) sender))
		{
			IceRunMessage.LEAVE_NOTINGAME.say(sender);
			return true;
		}
		
		IceRun.getPlugin().leave((Player) sender);
		return true;
	}

	@Override
	public List<String> onSubTabComplete(CommandSender arg0, String arg1, String[] arg2)
	{
		return null;
	}

}
