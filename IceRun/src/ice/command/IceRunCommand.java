package ice.command;

import ice.IceRun;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import me.winterguardian.core.command.CommandSplitter;
import me.winterguardian.core.command.JoinGameSubCommand;
import me.winterguardian.core.command.LeaveGameSubCommand;
import me.winterguardian.core.message.ErrorMessage;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

public class IceRunCommand extends CommandSplitter
{
	
	public IceRunCommand(IceRun icerun)
	{
		super();
		this.getSubCommands().add(new JoinGameSubCommand(icerun));
		this.getSubCommands().add(new LeaveGameSubCommand(icerun));
		this.getSubCommands().add(new InfoSubCommand());
		this.getSubCommands().add(new ManageSubCommand());
		this.getSubCommands().add(new StartSubCommand());
		this.getSubCommands().add(new StatsSubCommand(icerun));
		this.getSubCommands().add(new StopSubCommand());
		
	}

	@Override
	public List<String> getAliases()
	{
		return Arrays.asList("ir", "ice-run", "icer");
	}

	@Override
	public String getDescription()
	{
		return "Commande principale servant à jouer et configurer le jeu.";
	}

	@Override
	public String getName()
	{
		return "icerun";
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
		return "§bSyntaxe: §f" + super.getUsage();
	}

	@Override
	public boolean index(CommandSender sender, Command cmd, String label)
	{
		if(!(sender instanceof Player) || IceRun.getPlugin().contains((Player)sender))
			return this.onCommand(sender, cmd, label, new String[]{"info"});

		return this.onCommand(sender, cmd, label, new String[]{"joindre"});
	}
	
	@Override
	public Collection<Permission> getOtherPermissions()
	{
		return Arrays.asList(IceRun.VIP, IceRun.STAFF);
	}
}