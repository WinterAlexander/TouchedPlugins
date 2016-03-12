package me.winterguardian.duel.command;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import me.winterguardian.core.command.CommandSplitter;
import me.winterguardian.core.message.ErrorMessage;
import me.winterguardian.duel.Duel;
import me.winterguardian.duel.DuelMessage;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;


public class DuelCommand extends CommandSplitter
{
	public DuelCommand(Duel game)
	{
		super();
		this.getSubCommands().add(new InfoSubCommand());
		this.getSubCommands().add(new JoinSubCommand());
		this.getSubCommands().add(new ManageSubCommand());
		this.getSubCommands().add(new QuitSubCommand());
		this.getSubCommands().add(new RestartSubCommand());
		this.getSubCommands().add(new StatsSubCommand(game));
		this.getSubCommands().add(new WarpSubCommand());
	}
	
	@Override
	public List<String> getAliases()
	{
		return Arrays.asList("1vs1", "1versus1");
	}

	@Override
	public String getDescription()
	{
		return "Main command.";
	}

	@Override
	public String getName()
	{
		return "duel";
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
		return DuelMessage.COMMAND_USAGE.toString() + " §f" + super.getUsage();
	}

	@Override
	public boolean index(CommandSender sender, Command cmd, String label)
	{
		if(sender instanceof Player)
		{
			if(!Duel.getInstance().gameContains((Player) sender))
				if(Duel.getInstance().contains((Player) sender))
					return this.onCommand(sender, cmd, label, new String[]{"joindre"});
				else
					return this.onCommand(sender, cmd, label, new String[]{"warp"});
			else
				return this.onCommand(sender, cmd, label, new String[]{"quitter"});
		}
		return this.onCommand(sender, cmd, label, new String[]{"info"});
	}
	
	
	@Override
	public Collection<Permission> getOtherPermissions()
	{
		return Arrays.asList(Duel.MODERATION, Duel.ADMINISTRATION);
	}
}
