package me.winterguardian.commandcenter.commands;

import me.winterguardian.commandcenter.CommandCenter;
import me.winterguardian.commandcenter.CommandCenterMessage;
import me.winterguardian.core.Core;
import me.winterguardian.core.command.AutoRegistrationCommand;
import me.winterguardian.core.message.ErrorMessage;
import me.winterguardian.core.portal.BungeeDestination;
import me.winterguardian.core.portal.CommandDestination;
import me.winterguardian.core.portal.ConsoleCommandDestination;
import me.winterguardian.core.portal.LocationDestination;
import me.winterguardian.core.portal.SerializableDestination;
import me.winterguardian.core.portal.SerializablePortal;
import me.winterguardian.core.world.SerializableRegion;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class PortalCommand extends AutoRegistrationCommand
{
	private HashMap<String, SerializablePortal> portals = new HashMap<>();
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if(args.length == 0)
			args = new String[]{"help"};
		
		if(args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("aide"))
		{
			sender.sendMessage("  /portal list");
			sender.sendMessage("  /portal remove <name>");
			sender.sendMessage("  /portal add <name> here");
			sender.sendMessage("  /portal add <name> there [world] <x> <y> <z> [yaw] [pitch]");
			sender.sendMessage("  /portal add <name> playercmd <command ...>");
			sender.sendMessage("  /portal add <name> consolecmd <command ...>");
			sender.sendMessage("  /portal add <name> server <server>");
		}
		
		if(args[0].equalsIgnoreCase("list") || args[0].equalsIgnoreCase("liste"))
		{
			if(portals.size() == 0)
			{
				CommandCenterMessage.PORTAL_LIST_EMPTY.say(sender);
				return true;
			}
			 
			CommandCenterMessage.PORTAL_LIST_HEADER.say(sender);
			for(String name : portals.keySet())
			{
				String type = "inconnu";
				 
				if(portals.get(name).getDestination() instanceof CommandDestination)
					type = "commande";
				 
				if(portals.get(name).getDestination() instanceof LocationDestination)
					type = "téléportation";
				 
				if(portals.get(name).getDestination() instanceof BungeeDestination)
					type = "bungee";
				CommandCenterMessage.PORTAL_LIST_PORTAL.say(sender, "<name>", name, "<type>", type);
			}
			return true;
		}
		 
		if((args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("del")) && args.length == 2)
		{
			if(!portals.containsKey(args[1]))
			{
				CommandCenterMessage.PORTAL_REMOVE_NOTINLIST.say(sender);
				return false;
			}
			 
			portals.get(args[1]).unregister();
			portals.remove(args[1]);
			CommandCenterMessage.PORTAL_REMOVE_DONE.say(sender);
				 
			return true;
		}
		 
		if(args[0].equalsIgnoreCase("add") && args.length > 2)
		{
			if(!(sender instanceof Player))
			{
				ErrorMessage.COMMAND_INVALID_SENDER.say(sender);
				return false;
			}
			 
			SerializableRegion region = Core.getWand().getRegion((Player)sender);
			 
			if(region == null)
			{
				ErrorMessage.WORLD_INVALID_REGION.say(sender);
				return true;
			}
			 
			SerializableDestination dest = null;
			 
			if(args[2].equalsIgnoreCase("here"))
				dest = new LocationDestination(((Player)sender).getLocation());
			
			if(args[2].equalsIgnoreCase("server") && args.length == 4)
				dest = new BungeeDestination(args[3]);
			
			if(args[2].equalsIgnoreCase("there") && args.length == 6)
			{
				try
				{
					dest = new LocationDestination(new Location(((Player)sender).getWorld(), 
							Double.parseDouble(args[3]), 
							Double.parseDouble(args[4]), 
							Double.parseDouble(args[5])));
				}
				catch(Exception e)
				{
					dest = null;
				}
			}
			
			if(args[2].equalsIgnoreCase("there") && args.length == 7)
			{
				try
				{
					dest = new LocationDestination(new Location(Bukkit.getWorld(args[3]), 
							Double.parseDouble(args[4]), 
							Double.parseDouble(args[5]), 
							Double.parseDouble(args[6])));
				}
				catch(Exception e)
				{
					dest = null;
				}
			}
			
			if(args[2].equalsIgnoreCase("there") && args.length == 8)
			{
				try
				{
					dest = new LocationDestination(new Location(((Player)sender).getWorld(), 
							Double.parseDouble(args[3]), 
							Double.parseDouble(args[4]), 
							Double.parseDouble(args[5]),
							Float.parseFloat(args[6]),
							Float.parseFloat(args[7])));
				}
				catch(Exception e)
				{
					dest = null;
				}
			}
			
			if(args[2].equalsIgnoreCase("there") && args.length == 9)
			{
				try
				{
					dest = new LocationDestination(new Location(Bukkit.getWorld(args[3]), 
							Double.parseDouble(args[4]), 
							Double.parseDouble(args[5]), 
							Double.parseDouble(args[6]),
							Float.parseFloat(args[7]),
							Float.parseFloat(args[8])));
				}
				catch(Exception e)
				{
					dest = null;
				}
			}
			
			if(args[2].equalsIgnoreCase("playercmd") && args.length > 3 && sender.isOp())
			{
				String command = args[3];
				
				for(int i = 4; i < args.length; i++)
					command += " " + args[i];
				
				dest = new CommandDestination(command);
			}
			
			if(args[2].equalsIgnoreCase("consolecmd") && args.length > 3 && sender.isOp())
			{
				String command = args[3];
				
				for(int i = 4; i < args.length; i++)
					command += " " + args[i];
				
				dest = new ConsoleCommandDestination(command);
			}
			 
			if(args[2].equalsIgnoreCase("bungee") && args.length == 4 && sender.isOp())
				dest = new BungeeDestination(args[3]);
				 
			if(dest == null)
			{
				ErrorMessage.COMMAND_INVALID_ARGUMENT.say(sender);
				return false;
			}
			 
			SerializablePortal portal = new SerializablePortal(region, dest);
			portal.register(CommandCenter.getPlugin());
			 
			portals.put(args[1], portal);
			 
			CommandCenterMessage.PORTAL_ADD_DONE.say(sender, "<name>", args[1]);
			return true;
		}
		 
		ErrorMessage.COMMAND_INVALID_ARGUMENT.say(sender);
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
		return Arrays.asList("touchedportal", "touchedportail", "skp", "mvp", "touchedp", "portail", "portals", "portails");
	}

	@Override
	public String getDescription()
	{
		return "Commandes de gestion de portails";
	}

	@Override
	public String getName()
	{
		return "portal";
	}

	@Override
	public Permission getPermission()
	{
		return new Permission("CommandCenter.portal", getDescription(), PermissionDefault.OP);
	}

	@Override
	public String getUsage()
	{
		return "§cSyntaxe: §f/portal help";
	}

	@Override
	public void register(Plugin plugin)
	{
		super.register(plugin);
		YamlConfiguration config = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "portals.yml"));
		for(String name : config.getKeys(false))
		{
			try
			{
				SerializablePortal portal = SerializablePortal.fromString(config.getString(name));
				portals.put(name, portal);
				portal.register(plugin);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	@Override
	public void unregister()
	{
		YamlConfiguration config = new YamlConfiguration();
		for(String name : new ArrayList<>(portals.keySet()))
		{
			config.set(name, portals.get(name).toString());
			portals.get(name).unregister();
			portals.remove(name);
		}
		try
		{
			config.save(new File(getPlugin().getDataFolder(), "portals.yml"));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		super.unregister();
	}
}
