package me.winterguardian.pvp.command;

import me.winterguardian.core.command.SubCommand;
import me.winterguardian.core.message.ErrorMessage;
import me.winterguardian.core.util.TextUtil;
import me.winterguardian.pvp.PvPPlugin;
import me.winterguardian.pvp.purchase.kit.Kit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

/**
 * /pvp stuff help
 * /pvp stuff list
 * /pvp stuff set name
 * /pvp stuff get name
 * 
 * @author Alexander Winter
 *
 */
public class KitSubCommand extends SubCommand
{
	public KitSubCommand()
	{
		super("kit", Arrays.asList("shopkit", "kitshop"), PvPPlugin.ALL, ErrorMessage.COMMAND_INVALID_PERMISSION.toString(), "/pvp stuff");
	}

	@Override
	public boolean onSubCommand(CommandSender sender, String label, String[] args)
	{
		if(args.length == 0)
			args = new String[]{"help"};
		
		if(args[0].equalsIgnoreCase("help"))
		{
			sender.sendMessage("/pvp kit <help/list>");
			sender.sendMessage("/pvp kit <set/get/delete> <name>");
			return true;
		}
		
		if(args[0].equalsIgnoreCase("list"))
		{
			for(String stuff : Kit.listNames())
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
			new Kit(args[1]).delete();
			return true;
		}
		
		if(!(sender instanceof Player))
		{
			ErrorMessage.COMMAND_INVALID_SENDER.say(sender);
			return true;
		}
		
		if(args[0].equalsIgnoreCase("set"))
		{
			Kit stuff = new Kit(args[1]);
			stuff.setContent(((Player)sender).getInventory());
			stuff.save();
			sender.sendMessage("Kit " + args[1] + " saved.");
			return true;
		}
		
		if(args[0].equalsIgnoreCase("get"))
		{
			Kit stuff = new Kit(args[1]);
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
			return TextUtil.getStringsThatStartWith(args[0], Kit.listNames());
		
		return null;
	}
}
