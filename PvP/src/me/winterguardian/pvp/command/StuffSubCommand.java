package me.winterguardian.pvp.command;

import java.util.Arrays;
import java.util.List;

import me.winterguardian.core.command.SubCommand;
import me.winterguardian.core.message.ErrorMessage;
import me.winterguardian.core.util.TextUtil;
import me.winterguardian.pvp.GameStuff;
import me.winterguardian.pvp.PvPPlugin;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
/**
 * /pvp stuff help
 * /pvp stuff list
 * /pvp stuff set name
 * /pvp stuff get name
 * 
 * @author Alexander Winter
 *
 */
public class StuffSubCommand extends SubCommand
{
	public StuffSubCommand()
	{
		super("stuff", Arrays.asList("class", "classe"), PvPPlugin.ALL, ErrorMessage.COMMAND_INVALID_PERMISSION.toString(), "/pvp stuff");
	}

	@Override
	public boolean onSubCommand(CommandSender sender, String label, String[] args)
	{
		if(args.length == 0)
			args = new String[]{"help"};
		
		if(args[0].equalsIgnoreCase("help"))
		{
			sender.sendMessage("/pvp stuff <help/list>");
			sender.sendMessage("/pvp stuff <set/get/delete> <name>");
			return true;
		}
		
		if(args[0].equalsIgnoreCase("list"))
		{
			for(String stuff : GameStuff.listNames())
				sender.sendMessage(stuff);
			return true;
		}
		
		if(args.length != 2)
		{
			ErrorMessage.COMMAND_INVALID_ARGUMENT.say(sender);
			return true;
		}
		
		args[1] = args[1].replace("/", "").replace("\\", "");
		
		if(args[0].equalsIgnoreCase("delete"))
		{
			new GameStuff(args[1]).delete();
			return true;
		}
		
		if(!(sender instanceof Player))
		{
			ErrorMessage.COMMAND_INVALID_SENDER.say(sender);
			return true;
		}
		
		if(args[0].equalsIgnoreCase("set"))
		{
			GameStuff stuff = new GameStuff(args[1]);
			stuff.setContent(((Player)sender).getInventory());
			stuff.save();
			sender.sendMessage("Stuff " + args[1] + " saved.");
			return true;
		}
		
		if(args[0].equalsIgnoreCase("get"))
		{
			GameStuff stuff = new GameStuff(args[1]);
			stuff.load();
			stuff.give((Player)sender);
			return true;
		}
		
		ErrorMessage.COMMAND_INVALID_ARGUMENT.say(sender);
		return true;
	}
	
	@Override
	public List<String> onSubTabComplete(CommandSender sender, String label, String[] args)
	{
		if(args.length == 1)
			return TextUtil.getStringsThatStartWith(args[0], GameStuff.listNames());
		
		return null;
	}
}
