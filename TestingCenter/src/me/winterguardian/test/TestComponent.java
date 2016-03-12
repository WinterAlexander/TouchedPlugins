package me.winterguardian.test;

import java.util.List;

import me.winterguardian.core.command.SubCommand;
import me.winterguardian.core.message.ErrorMessage;

import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.permissions.Permission;

public abstract class TestComponent extends SubCommand implements Listener
{
	public TestComponent(String name, Permission permission, String usage)
	{
		super(name, null, permission, ErrorMessage.COMMAND_INVALID_PERMISSION.toString(), usage);
	}
	
	/**¸
	 * Executed when typing /test <your component>
	 * Default send unknown command if your component is only a listener.
	 * 
	 * 
	 */
	@Override
	public boolean onSubCommand(CommandSender sender, String label, String[] args)
	{
		ErrorMessage.COMMAND_INVALID_SUBCOMMAND.say(sender);
		return true;
	}
	
	@Override
	public List<String> onSubTabComplete(CommandSender arg0, String arg1, String[] arg2)
	{
		return null;
	}
}
