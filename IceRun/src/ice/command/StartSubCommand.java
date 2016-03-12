package ice.command;

import ice.IceRunMessage;
import ice.IceRun;
import ice.IceRunGame;
import ice.WaitingSession;

import java.util.Arrays;
import java.util.List;

import me.winterguardian.core.command.SubCommand;
import me.winterguardian.core.message.ErrorMessage;

import org.bukkit.command.CommandSender;

public class StartSubCommand extends SubCommand
{

	public StartSubCommand()
	{
		super("start", Arrays.asList("demarre", "go"), IceRun.STAFF, ErrorMessage.COMMAND_INVALID_PERMISSION.toString(), "/icerun start");
	}

	@Override
	public boolean onSubCommand(CommandSender sender, String label, String[] args)
	{
		if (IceRun.status instanceof WaitingSession)
		{
			IceRun.status.end();
			IceRun.status = new IceRunGame();
			IceRun.status.start();
		}
		else
			IceRunMessage.GAME_CANT_START.say(sender);
		
		return true;
	}

	@Override
	public List<String> onSubTabComplete(CommandSender sender, String label, String[] args)
	{
		return null;
	}
	
}