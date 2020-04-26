package ice.command;

import ice.IceRun;
import ice.IceRunMessage;
import ice.Standby;
import me.winterguardian.core.command.SubCommand;
import me.winterguardian.core.message.ErrorMessage;
import me.winterguardian.core.util.PlayerUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StopSubCommand extends SubCommand
{

	public StopSubCommand()
	{
		super("stop", Arrays.asList("arret", "fermer", "stopthatnigga"), IceRun.STAFF, ErrorMessage.COMMAND_INVALID_PERMISSION.toString(), "/icerun stop [debug]");
	}

	@Override
	public boolean onSubCommand(CommandSender sender, String label, String[] args)
	{
		if (args.length >= 1)
		{
			if (args[0].equalsIgnoreCase("debug"))
			{
				IceRunMessage.GAME_STOP.say(IceRun.players);
				if(IceRun.getSettings().isGameReady())
					for (Player p : IceRun.players)
						if (p != null && p.isOnline())
						{
							p.teleport(IceRun.getSettings().getExit().getLocation());
							PlayerUtil.prepare(p);
							PlayerUtil.clearInventory(p);
							PlayerUtil.clearBoard(p);
							PlayerUtil.heal(p);
						}
				IceRun.players = new ArrayList<Player>();
				try
				{
					IceRun.status.end();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				IceRun.status = new Standby();
			}
			else
			{
				ErrorMessage.COMMAND_INVALID_ARGUMENT.say(sender);
			}
		}
		else
		{
			IceRunMessage.GAME_STOP.say(IceRun.players);
			for (int i = 0; i < IceRun.players.size(); i++)
				IceRun.getPlugin().leave(IceRun.players.get(i));
		}
		return true;
	}

	@Override
	public List<String> onSubTabComplete(CommandSender sender, String label, String[] args)
	{
		if(args.length == 1)
			return Arrays.asList("debug");
		return null;
	}
	
}