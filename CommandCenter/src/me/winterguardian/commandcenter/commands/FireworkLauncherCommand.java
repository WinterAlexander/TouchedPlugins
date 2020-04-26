package me.winterguardian.commandcenter.commands;

import me.winterguardian.commandcenter.CommandCenterMessage;
import me.winterguardian.core.command.AutoRegistrationCommand;
import me.winterguardian.core.message.ErrorMessage;
import me.winterguardian.core.util.ColorUtil;
import me.winterguardian.core.util.FireworkUtil;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.util.Arrays;
import java.util.List;
import java.util.Random;


public class FireworkLauncherCommand extends AutoRegistrationCommand
{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if(args.length == 0)
		{
			Location loc = null;
			if(sender instanceof Player)
			{
				loc = ((Player)sender).getLocation();
			}
			else if(sender instanceof BlockCommandSender)
			{
				
				loc = ((BlockCommandSender)sender).getBlock().getLocation();
			}
			
			if(loc != null)
			{
				Random r = new Random();
				FireworkUtil.generate(loc, 
						Type.values()[r.nextInt(Type.values().length)], 
						Color.fromRGB(r.nextInt(256), r.nextInt(256), r.nextInt(256)), 
						Color.fromRGB(r.nextInt(256), r.nextInt(256), r.nextInt(256)),  
						r.nextBoolean(), 
						r.nextBoolean(),  
						r.nextInt(3));
				sender.sendMessage(CommandCenterMessage.FIREWORKSHOOT.toString());
			}
			else
			{
				ErrorMessage.COMMAND_INVALID_ARGUMENT.say(sender);
			}
		}
		else if(args.length == 1)
		{
			Player player = Bukkit.getPlayer(args[0]);
			
			if(player != null)
			{
				Random r = new Random();
				FireworkUtil.generate(player.getLocation(), 
						Type.values()[r.nextInt(Type.values().length)], 
						Color.fromRGB(r.nextInt(256), r.nextInt(256), r.nextInt(256)), 
						Color.fromRGB(r.nextInt(256), r.nextInt(256), r.nextInt(256)),  
						r.nextBoolean(), 
						r.nextBoolean(),  
						r.nextInt(3));
				sender.sendMessage(CommandCenterMessage.FIREWORKSHOOT.toString());
			}
			else
			{
				ErrorMessage.COMMAND_INVALID_ARGUMENT.say(sender);
			}
		}
		else if(args.length == 3)
		{
			try
			{
				Location loc = null;
				World world = null;
				Location userLoc = null;
				if(sender instanceof Player)
				{
					world = ((Player)sender).getWorld();
					userLoc = ((Player)sender).getLocation();
				}
				else if(sender instanceof BlockCommandSender)
				{
					world = ((BlockCommandSender)sender).getBlock().getWorld();
					userLoc = ((BlockCommandSender)sender).getBlock().getLocation();
					
				}
				double x = Double.parseDouble(args[0].replaceAll("~", "" + userLoc.getX()));
				double y = Double.parseDouble(args[1].replaceAll("~", "" + userLoc.getY()));
				double z = Double.parseDouble(args[2].replaceAll("~", "" + userLoc.getZ()));
				
				loc = new Location(world, x, y, z);
				Random r = new Random();
				FireworkUtil.generate(loc, 
						Type.values()[r.nextInt(Type.values().length)], 
						Color.fromRGB(r.nextInt(256), r.nextInt(256), r.nextInt(256)), 
						Color.fromRGB(r.nextInt(256), r.nextInt(256), r.nextInt(256)),  
						r.nextBoolean(), 
						r.nextBoolean(),  
						r.nextInt(3));
				sender.sendMessage(CommandCenterMessage.FIREWORKSHOOT.toString());
			}
			catch(Exception e)
			{
				ErrorMessage.COMMAND_INVALID_ARGUMENT.say(sender);
			}
		}
		else if(args.length == 6)
		{
			try
			{
				Location loc = null;
				
				if(sender instanceof Player)
				{
					loc = ((Player)sender).getLocation();
				}
				else if(sender instanceof BlockCommandSender)
				{
					loc = ((BlockCommandSender)sender).getBlock().getLocation();
					
				}
				
				if(loc != null)
				{
					FireworkUtil.generate(loc, 
							Type.valueOf(args[0].toUpperCase()), 
							ColorUtil.getColor(args[1]), 
							ColorUtil.getColor(args[2]),
							Boolean.parseBoolean(args[3]), 
							Boolean.parseBoolean(args[4]), 
							Integer.parseInt(args[5]));
					sender.sendMessage(CommandCenterMessage.FIREWORKSHOOT.toString());
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
		}
		else if(args.length == 7)
		{
			try
			{
				Player player = Bukkit.getPlayer(args[0]);
				
				if(player != null)
				{
					FireworkUtil.generate(player.getLocation(), 
							Type.valueOf(args[1].toUpperCase()), 
							ColorUtil.getColor(args[2]),
							ColorUtil.getColor(args[3]),
							Boolean.parseBoolean(args[4]), 
							Boolean.parseBoolean(args[5]), 
							Integer.parseInt(args[6]));
					sender.sendMessage(CommandCenterMessage.FIREWORKSHOOT.toString());
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
		}
		else if(args.length == 9)
		{
			try
			{
				Location loc = null;
				World world = null;
				Location userLoc = null;
				if(sender instanceof Player)
				{
					world = ((Player)sender).getWorld();
					userLoc = ((Player)sender).getLocation();
				}
				else if(sender instanceof BlockCommandSender)
				{
					world = ((BlockCommandSender)sender).getBlock().getWorld();
					userLoc = ((BlockCommandSender)sender).getBlock().getLocation();
					
				}
				double x = Double.parseDouble(args[0].replaceAll("~", "" + userLoc.getX()));
				double y = Double.parseDouble(args[1].replaceAll("~", "" + userLoc.getY()));
				double z = Double.parseDouble(args[2].replaceAll("~", "" + userLoc.getZ()));
				
				loc = new Location(world, x, y, z);
				FireworkUtil.generate(loc, 
						Type.valueOf(args[3].toUpperCase()), 
						ColorUtil.getColor(args[4]),
						ColorUtil.getColor(args[5]),
						Boolean.parseBoolean(args[6]), 
						Boolean.parseBoolean(args[7]), 
						Integer.parseInt(args[8]));
				sender.sendMessage(CommandCenterMessage.FIREWORKSHOOT.toString());
			}
			catch(Exception e)
			{
				ErrorMessage.COMMAND_INVALID_ARGUMENT.say(sender);
			}
		}
		else
		{
			ErrorMessage.COMMAND_INVALID_ARGUMENT.say(sender);
		}
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args)
	{
		return null;
	}

	@Override
	public List<String> getAliases()
	{
		return Arrays.asList("firework", "fw", "fwl", "artifice");
	}

	@Override
	public String getDescription()
	{
		return "Une commande pour lancer un feu d'artifice";
	}

	@Override
	public String getName()
	{
		return "fireworklauncher";
	}

	@Override
	public Permission getPermission()
	{
		return new Permission("CommandCenter.firework-shoot", "Autorise le lancement de feux d'artifice sans protection contre le spam.", PermissionDefault.OP);
	}

	@Override
	public String getUsage()
	{
		return "§cSyntaxe: §f/fireworklauncher";
	}

}
