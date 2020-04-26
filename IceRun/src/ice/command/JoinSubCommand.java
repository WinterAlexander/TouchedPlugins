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

public class JoinSubCommand extends SubCommand
{

	public JoinSubCommand()
	{
		super("joindre", Arrays.asList("join", "rejoindre", "rejoin", "entrer"), null, ErrorMessage.COMMAND_INVALID_PERMISSION.toString(), "§bUsage: §f/icerun joindre [joueur]");
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
			
			if(IceRun.getPlugin().contains(p))
			{
				IceRunMessage.JOIN_PLAYERALREADYINGAME.say(sender);
				return true;
			}
			
			IceRun.getPlugin().join(p);
			return true;
		}
		
		if(!(sender instanceof Player))
		{
			ErrorMessage.COMMAND_INVALID_SENDER.say(sender);
			return false;
		}
		
		if(IceRun.getPlugin().contains((Player) sender))
		{
			IceRunMessage.JOIN_ALREADYINGAME.say(sender);
			return true;
		}
		
		IceRun.getPlugin().join((Player) sender);
		return true;
	}

	@Override
	public List<String> onSubTabComplete(CommandSender sender, String label, String[] args)
	{
		return null;
	}

}
