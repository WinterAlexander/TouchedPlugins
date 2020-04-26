package me.winterguardian.test.components;

import me.winterguardian.core.message.ErrorMessage;
import me.winterguardian.core.util.ActionBarUtil;
import me.winterguardian.test.TestComponent;
import me.winterguardian.test.TestingCenter;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class ActionBar extends TestComponent
{
	public ActionBar()
	{
		super("actionbar", TestingCenter.TEST_LEVEL3, "/test actionbar <player> <normal|continuous|clear> [message]");
	}
	
	@Override
	public boolean onSubCommand(CommandSender sender, String label, String[] args)
	{
		if(args.length <= 0)
		{
			ErrorMessage.COMMAND_INVALID_ARGUMENT.say(sender);
			return false;
		}
		
		Player p = Bukkit.getPlayer(args[0]);
		if(p == null)
		{
			ErrorMessage.COMMAND_INVALID_PLAYER.say(sender);
			return false;
		}
		
		String message = "";
		
		for(int i = 2; i < args.length; i++)
		{
			message += args[i];
			if(i + 1 < args.length)
				message += " ";
		}
		
		if(args[1].equalsIgnoreCase("normal"))
			ActionBarUtil.sendActionMessage(p, message.replaceAll("&", "§"));
		else if(args[1].equalsIgnoreCase("continuous"))
			ActionBarUtil.sendActionMessageContinuously(p, message.replaceAll("&", "§"), TestingCenter.getInstance());
		else if(args[1].equalsIgnoreCase("clear"))
			ActionBarUtil.clear(p);
		else
		{
			ErrorMessage.COMMAND_INVALID_ARGUMENT.say(sender);
			return false;
		}
		
		return true;
	}

	@Override
	public List<String> onSubTabComplete(CommandSender arg0, String arg1, String[] arg2)
	{
		return null;
	}
}
