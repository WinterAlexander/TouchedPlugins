package me.winterguardian.commandcenter.commands;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import me.winterguardian.commandcenter.CommandCenterMessage;
import me.winterguardian.core.Core;
import me.winterguardian.core.command.AsyncCommand;
import me.winterguardian.core.message.ErrorMessage;
import me.winterguardian.core.playerstats.MappedData;
import me.winterguardian.core.playerstats.PlayerStats;
import me.winterguardian.core.util.TextUtil;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.Plugin;

public class PointCommand extends AsyncCommand
{
	private static final Permission POINT = new Permission("CommandCenter.point", "Permet de consulter ses points.", PermissionDefault.OP);
	private static final Permission POINT_GIVE = new Permission("CommandCenter.point.give", "Permet de donner ses points à un autre joueur.", PermissionDefault.OP);
	private static final Permission POINT_EDIT = new Permission("CommandCenter.point.edit", "Permet d'éditer les stats d'un joueur", PermissionDefault.OP);

	public PointCommand(Plugin plugin)
	{
		super(plugin);
	}

	@Override
	public List<String> getAliases()
	{
		return Arrays.asList("points", "pts", "money", "$", "€", "£", "balance");
	}

	@Override
	public String getDescription()
	{
		return "Permet de consulter, de donner et d'éditer son nombre de points.";
	}

	@Override
	public String getName()
	{
		return "point";
	}

	@Override
	public Permission getPermission()
	{
		return POINT;
	}

	@Override
	public String getUsage()
	{
		return "§cSyntaxe: §f/point [player|give|add|remove|set] [player] [value]";
	}
	
	@Override
	public Collection<Permission> getOtherPermissions()
	{
		return Arrays.asList(POINT_GIVE, POINT_EDIT);
	}

	@Override
	protected void onAsyncCommand(CommandSender sender, String label, String[] args)
	{
		if(sender.hasPermission(POINT_EDIT) && args.length == 3)
		{
			PlayerStats data;
			int value;

            Player player;
            UUID id;
            if((player = Bukkit.getPlayer(args[1])) != null)
            {
                data = new PlayerStats(Core.getUserDatasManager().getUserData(player));
                id = player.getUniqueId();
            }
            else
            {
                Entry<UUID, MappedData> entry = Core.getUserDatasManager().getFirstByValue("core.name", args[1]);

                if (entry == null)
                {
                    ErrorMessage.COMMAND_INVALID_PLAYER.say(sender);
                    sender.sendMessage(getUsage());
                    return;
                }

                id = entry.getKey();
                data = new PlayerStats(entry.getValue());
            }


			
			try
			{
				value = Integer.parseInt(args[2]);
			}
			catch(Exception e)
			{
				ErrorMessage.COMMAND_INVALID_ARGUMENT.say(sender);
				sender.sendMessage(getUsage());
				return;
			}
			
			if(args[0].equalsIgnoreCase("add"))
			{
				data.addPoints(value);
				CommandCenterMessage.POINT_ADD.say(sender, "#", TextUtil.toString(value), "<player>", data.getName());
                Core.getUserDatasManager().saveUserData(id, data.getContent());
				return;
			}
			else if(args[0].equalsIgnoreCase("remove"))
			{
				data.removePoints(value);
				CommandCenterMessage.POINT_REMOVE.say(sender, "#", TextUtil.toString(value), "<player>", data.getName());
                Core.getUserDatasManager().saveUserData(id, data.getContent());
				return;
			}
			else if(args[0].equalsIgnoreCase("set"))
			{
				data.setPoints(value);
				CommandCenterMessage.POINT_SET.say(sender, "#", TextUtil.toString(value), "<player>", data.getName());
                Core.getUserDatasManager().saveUserData(id, data.getContent());
				return;
			}
		}
		
		if(sender.hasPermission(POINT_GIVE) && args.length == 3)
		{
			if(args[0].equalsIgnoreCase("give") || args[0].equalsIgnoreCase("donner"))
			{
				if(!(sender instanceof Player))
				{
					ErrorMessage.COMMAND_INVALID_SENDER.say(sender);
					return;
				}	
				
				PlayerStats giver = new PlayerStats(Core.getUserDatasManager().getUserData((Player)sender));
				PlayerStats receiver = null;

                UUID receiverId = null;

                Player player;
                if((player = Bukkit.getPlayer(args[1])) != null)
                {
                    receiver = new PlayerStats(Core.getUserDatasManager().getUserData(player));
                    receiverId = player.getUniqueId();
                }
                else
                {
                    Entry<UUID, MappedData> entry = Core.getUserDatasManager().getFirstByValue("core.name", args[1]);

                    if(entry != null)
                    {
                        receiverId = entry.getKey();
                        receiver = new PlayerStats(entry.getValue());
                    }
                }


				
				if(receiver == null || ((Player)sender).getUniqueId() == receiverId)
				{
					ErrorMessage.COMMAND_INVALID_PLAYER.say(sender);
					sender.sendMessage(getUsage());
					return;
				}

				try
				{
					int amount = Integer.parseInt(args[2]);
					if(giver.getPoints() < amount)
					{
						CommandCenterMessage.POINT_GIVE_NOTENOUGH.say(sender);
						return;
					}

					if(amount <= 0)
					{
						ErrorMessage.COMMAND_INVALID_ARGUMENT.say(sender);
						sender.sendMessage(getUsage());
						return;
					}
					
					giver.setPoints(giver.getPoints() - amount);
					receiver.addPoints(amount);

                    Core.getUserDatasManager().saveUserData(((Player)sender).getUniqueId(), giver.getContent());
                    Core.getUserDatasManager().saveUserData(receiverId, receiver.getContent());

					CommandCenterMessage.POINT_GIVE_DONE_SENDER.say(sender, "#", TextUtil.toString(amount), "<player>", receiver.getName());
                    CommandCenterMessage.POINT_GIVE_DONE_RECEIVER.sayIfOnline(Bukkit.getOfflinePlayer(receiverId), "#", TextUtil.toString(amount), "<player>", giver.getName());
				}
				catch(Exception e)
				{
					ErrorMessage.COMMAND_INVALID_ARGUMENT.say(sender);
					sender.sendMessage(getUsage());
					return;
				}
			}
		}
	
		
		PlayerStats data = null;
        String playerMessage = null;
		
		if(sender instanceof Player && args.length == 0)
		{
			data = new PlayerStats(Core.getUserDatasManager().getUserData((Player) sender));
            playerMessage = "Vous avez";
		}
		else if(args.length >= 1)
		{
            Player player;
            if((player = Bukkit.getPlayer(args[0])) != null)
            {
	            data = new PlayerStats(Core.getUserDatasManager().getUserData(player));
	            playerMessage = data.getName() + " a";
            }
            else
            {
                Entry<UUID, MappedData> entry = Core.getUserDatasManager().getFirstByValue("core.name", args[0]);

                if(entry != null)
                {
                    data = new PlayerStats(entry.getValue());
                    playerMessage = data.getName() + " a";
                }
            }
		}
		else
		{
			ErrorMessage.COMMAND_INVALID_SENDER.say(sender);
			return;
		}
		
		if(data == null)
		{
			ErrorMessage.COMMAND_INVALID_PLAYER.say(sender);
			sender.sendMessage(getUsage());
			return;
		}
		
		CommandCenterMessage.POINT_SHOW.say(sender, "<player>", playerMessage, "#", TextUtil.toString(data.getPoints()));
	}
}
