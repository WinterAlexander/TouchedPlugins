package me.winterguardian.commandcenter.commands;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import me.winterguardian.core.command.AutoRegistrationCommand;
import me.winterguardian.core.message.ErrorMessage;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.Plugin;

public class SignEditCommand extends AutoRegistrationCommand implements Listener
{
	@Override
	public void register(Plugin plugin)
	{
		super.register(plugin);
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	@Override
	public void unregister()
	{
		HandlerList.unregisterAll(this);
		super.unregister();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if(args.length == 0)
		{
			ErrorMessage.COMMAND_INVALID_ARGUMENT.say(sender);
			return true;
		}

		if(!(sender instanceof Player))
		{
			ErrorMessage.COMMAND_INVALID_SENDER.say(sender);
			return true;
		}

		Player player = (Player)sender;
		HashSet<Material> transparent = new HashSet<>();
		transparent.add(Material.AIR);

		if(!(player.getTargetBlock(transparent, 5).getState() instanceof Sign))
		{
			ErrorMessage.WORLD_INVALID_BLOCK.say(sender);
			return true;
		}

		Sign sign = (Sign)player.getTargetBlock(transparent, 5).getState();

		try
		{
			int line = Integer.parseInt(args[0]) - 1;
			if(line >= 0 && line < 4)
			{
				String content = "";
				for(int i = 1; i < args.length; i++)
				{
					content += args[i];
					if(i + 1 < args.length)
					{
						content += " ";
					}
				}

				if(sender.hasPermission(getOtherPermissions().get(0)))
					content = ChatColor.translateAlternateColorCodes('&', content);
				sign.setLine(line, content);
				sign.update();
			}
			else
			{
				ErrorMessage.COMMAND_INVALID_ARGUMENT.say(sender);
			}
		}
		catch(Exception e)
		{
			ErrorMessage.COMMAND_INVALID_ARGUMENT.say(sender);
		}

		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender arg0, Command arg1, String arg2, String[] arg3)
	{
		return null;
	}

	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void onSignChange(SignChangeEvent event)
	{
		if(!event.getPlayer().hasPermission(getOtherPermissions().get(0)))
			return;

		for(int i = 0; i < 4; i++)
			event.setLine(i, ChatColor.translateAlternateColorCodes('&', event.getLine(i)));
	}

	@Override
	public List<String> getAliases()
	{
		return Arrays.asList("signmodify", "modifierpanneau", "panneaumodif");
	}

	@Override
	public String getDescription()
	{
		return "Permet de modifier un panneau sans le casser.";
	}

	@Override
	public Permission getPermission()
	{
		return new Permission("CommandCenter.sign.edit", getDescription(), PermissionDefault.OP);
	}

	@Override
	public String getUsage()
	{
		return "§cSyntaxe: §f/signedit <# de ligne> [contenu]";
	}

	@Override
	public String getName()
	{
		return "signedit";
	}

	@Override
	public List<Permission> getOtherPermissions()
	{
		return Collections.singletonList(new Permission("CommandCenter.sign.color", "Permet de mettre de la couleur sur un panneau.", PermissionDefault.OP));
	}
}