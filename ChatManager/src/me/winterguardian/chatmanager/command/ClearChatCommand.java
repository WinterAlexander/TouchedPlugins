package me.winterguardian.chatmanager.command;

import me.winterguardian.core.command.AutoRegistrationCommand;
import me.winterguardian.core.message.ErrorMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class ClearChatCommand extends AutoRegistrationCommand
{
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		for(Player player : Bukkit.getOnlinePlayers())
		{
			if(!player.hasPermission((Permission)getOtherPermissions().iterator().next()))
			{
				for(int i = 0; i < 100; i++)
				{
					player.sendMessage("");
				}
			}
			else
			{
				player.sendMessage("§eLe chat a été nettoyé par " + sender.getName() + ".");
			}
		}
		return true;
	}

	public List<String> onTabComplete(CommandSender arg0, Command arg1, String arg2, String[] arg3)
	{
		return null;
	}

	public List<String> getAliases()
	{
		return Arrays.asList("pub", "cc");
	}

	public String getDescription()
	{
		return "Permet de nettoyer le chat.";
	}

	public String getName()
	{
		return "clearchat";
	}

	public Permission getPermission()
	{
		return new Permission("ChatManager.chat-clear", getDescription(), PermissionDefault.OP);
	}

	public String getPermissionMessage()
	{
		return ErrorMessage.COMMAND_INVALID_PERMISSION.toString();
	}

	public String getUsage()
	{
		return "/clearchat";
	}

	public Collection<Permission> getOtherPermissions()
	{
		return Arrays.asList(new Permission("ChatManager.chat-clear.bypass", "Permet de contourner les nettoyage de chat.", PermissionDefault.OP));
	}
}
