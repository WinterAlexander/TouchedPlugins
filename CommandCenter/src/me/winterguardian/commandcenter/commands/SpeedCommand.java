package me.winterguardian.commandcenter.commands;

import me.winterguardian.commandcenter.CommandCenterMessage;
import me.winterguardian.core.command.AutoRegistrationCommand;
import me.winterguardian.core.message.ErrorMessage;
import me.winterguardian.core.util.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.util.Arrays;
import java.util.List;

public class SpeedCommand extends AutoRegistrationCommand
{
	private static final Permission SPEED_OTHERS = new Permission("CommandCenter.speed.others", "Permet de changer la vitesse de d'autres joueurs", PermissionDefault.OP);
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		Player player;
		
		if(args.length == 2)
		{
			if(!sender.hasPermission(SPEED_OTHERS))
			{
				ErrorMessage.COMMAND_INVALID_PERMISSION.say(sender);
				return false;
			}
			
			player = Bukkit.getPlayer(args[1]);
		}
		else if(args.length == 1)
		{
			if(!(sender instanceof Player))
			{
				ErrorMessage.COMMAND_INVALID_SENDER.say(sender);
				return false;
			}
			
			player = (Player) sender;
		}
		else
		{
			ErrorMessage.COMMAND_INVALID_ARGUMENT.say(sender);
			return false;
		}
		
		if(player == null)
		{
			ErrorMessage.COMMAND_INVALID_PLAYER.say(sender);
			return false;
		}
		
		float walkSpeed, flySpeed;
		
		try
		{
			walkSpeed = Float.parseFloat(args[0]) * 0.2f;
			flySpeed = Float.parseFloat(args[0]) * 0.1f;
			if(walkSpeed > 1)
				walkSpeed = 1;
			
			if(walkSpeed < -1)
				walkSpeed = -1;
		
			if(flySpeed > 1)
				flySpeed = 1;
			
			if(flySpeed < -1)
				flySpeed = -1;
		}
		catch(Exception e)
		{
			ErrorMessage.COMMAND_INVALID_ARGUMENT.say(sender);
			return false;
		}
		
		
		player.setWalkSpeed(walkSpeed);
		player.setFlySpeed(flySpeed);
		
		if(player == sender)
			CommandCenterMessage.SPEED_CHANGED.say(sender, "<speed>", args[0]);
		else
			CommandCenterMessage.SPEED_CHANGED_OTHER.say(sender, "<speed>", args[0], "<player>", player.getName());
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args)
	{
		if(args.length == 1)
			return TextUtil.getStringsThatStartWith(args[0], Arrays.asList("2", "1.5", "0.5", "1"));
		return null;
	}

	@Override
	public List<String> getAliases()
	{
		return Arrays.asList("vitesse", "vroum", "rapidité", "yaniggagottarun");
	}

	@Override
	public String getDescription()
	{
		return "Permet de changer sa vitesse ou celle d'un autre";
	}

	@Override
	public String getName()
	{
		return "speed";
	}

	@Override
	public Permission getPermission()
	{
		return new Permission("CommandCenter.speed", "Permet de changer sa vitesse", PermissionDefault.OP);
	}

	@Override
	public String getUsage()
	{
		return "§cSyntaxe: §f/speed <speed> [player]";
	}

}
