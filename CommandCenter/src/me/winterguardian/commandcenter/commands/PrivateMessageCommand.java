package me.winterguardian.commandcenter.commands;

import me.winterguardian.commandcenter.CommandCenterMessage;
import me.winterguardian.core.Core;
import me.winterguardian.core.command.AutoRegistrationCommand;
import me.winterguardian.core.message.ErrorMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 *
 * Created by Alexander Winter on 2015-12-21.
 */
public class PrivateMessageCommand extends AutoRegistrationCommand
{
	private HashMap<UUID, String> replyRecipients;

	public PrivateMessageCommand()
	{
		this.replyRecipients = new HashMap<>();
	}

	public void register(Plugin plugin)
	{
		super.register(plugin);
		new ResendMessageCommand().register(plugin);
	}

	@Override
	public String getName()
	{
		return "privatemessage";
	}

	@Override
	public Permission getPermission()
	{
		return new Permission("CommandCenter.private-message", getDescription(), PermissionDefault.TRUE);
	}

	@Override
	public String getUsage()
	{
		return "§eEnvoyer un message: §f/msg <joueur> <message...>";
	}

	@Override
	public String getDescription()
	{
		return "Permet d'envoyer des messages privés";
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if(args.length < 2)
			return false;

		Player player = Bukkit.getPlayer(args[0]);

		String playerName = player == null ? args[0] : player.getName();

		if(sender instanceof Player)
			this.replyRecipients.put(((Player)sender).getUniqueId(), playerName);

		String message = "§e§l" + sender.getName() + " §f§l-> §e§l" + playerName + " §f" + args[1];

		for(int i = 2; i < args.length; i++)
			message += " " + args[i];

		sender.sendMessage(message.replaceFirst(sender.getName(), "§c§lmoi"));
		if(player == null)
			Core.getBungeeMessager().sendMessage(playerName, message.replaceFirst(playerName, "§c§lmoi"));
		else
			player.sendMessage(message.replaceFirst(playerName, "§c§lmoi"));

		for(Player staff : Bukkit.getOnlinePlayers())
			if(staff.hasPermission(getOtherPermissions().get(0)) && staff != sender && staff != player)
				staff.sendMessage(message);

		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args)
	{
		return null;
	}

	@Override
	public List<String> getAliases()
	{
		return Arrays.asList("pm", "msg", "message", "m", "tell", "whisper", "w", "t");
	}

	@Override
	public List<Permission> getOtherPermissions()
	{
		return Collections.singletonList(new Permission("CommandCenter.private-message.spy", "Permet de voir tout les messages privés.", PermissionDefault.OP));
	}

	public class ResendMessageCommand extends AutoRegistrationCommand
	{
		@Override
		public String getName()
		{
			return "renvoyer";
		}

		@Override
		public Permission getPermission()
		{
			return PrivateMessageCommand.this.getPermission();
		}

		@Override
		public String getUsage()
		{
			return "§eRenvoyer un message: §f/renvoyer <message ...>";
		}

		@Override
		public String getDescription()
		{
			return "Permet renvoyer ses messages privés au précédent destinataire";
		}

		@Override
		public List<String> getAliases()
		{
			return Arrays.asList("r", "reply", "réponse", "répondre", "reply", "answer", "reponse", "repondre");
		}

		@Override
		public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
		{
			if(!(sender instanceof Player))
			{
				ErrorMessage.COMMAND_INVALID_SENDER.say(sender);
				return true;
			}

			if(!replyRecipients.containsKey(((Player)sender).getUniqueId()))
			{
				CommandCenterMessage.RESENDMESSAGE_NORECIPIENT.say(sender);
				return true;
			}

			if(args.length < 1)
				return false;

			String message = args[0];

			for(int i = 1; i < args.length; i++)
				message += " " + args[i];

			((Player)sender).performCommand("privatemessage " + replyRecipients.get(((Player)sender).getUniqueId()) + " " + message);

			return true;
		}

		@Override
		public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args)
		{
			return null;
		}
	}
}
