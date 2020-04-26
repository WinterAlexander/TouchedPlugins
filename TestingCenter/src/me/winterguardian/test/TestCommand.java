package me.winterguardian.test;

import me.winterguardian.core.command.CommandSplitter;
import me.winterguardian.core.command.SubCommand;
import me.winterguardian.core.message.ErrorMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;


public class TestCommand extends CommandSplitter
{

	@Override
	public boolean index(CommandSender sender, Command cmd, String label)
	{
		for(SubCommand verifySub : this.getSubCommands())
			if(sender.hasPermission(verifySub.getPermission()))
			{
				sender.sendMessage(TestingCenter.getInstance().getName() + " §aComposants de tests");
				for(SubCommand sub : this.getSubCommands())
					if(sender.hasPermission(sub.getPermission()))
						sender.sendMessage("  -" + sub.getName() + " : " + sub.getPermission().getDescription());
				return true;
			}
		ErrorMessage.COMMAND_INVALID_PERMISSION.say(sender);
		return true;
	}

	@Override
	public List<String> getAliases()
	{
		return Arrays.asList("test", "testcenter");
	}

	@Override
	public String getDescription()
	{
		return "Commande principale pour lister et utiliser les composants.";
	}

	@Override
	public String getName()
	{
		return "testingcenter";
	}

	@Override
	public Permission getPermission()
	{
		return null;
	}

	@Override
	public String getPermissionMessage()
	{
		return ErrorMessage.COMMAND_INVALID_PERMISSION.toString();
	}

	@Override
	public String getUsage()
	{
		return "/testingcenter";
	}
	
	@Override
	public Collection<Permission> getOtherPermissions()
	{
		return Arrays.asList(TestingCenter.TEST_LEVEL1, TestingCenter.TEST_LEVEL2, TestingCenter.TEST_LEVEL3, TestingCenter.TEST_LEVEL4);
	}
}
