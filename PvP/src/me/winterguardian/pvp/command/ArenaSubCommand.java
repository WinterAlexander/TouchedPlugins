package me.winterguardian.pvp.command;

import me.winterguardian.core.command.SubCommand;
import me.winterguardian.core.message.ErrorMessage;
import me.winterguardian.core.world.SerializableLocation;
import me.winterguardian.pvp.PvPArena;
import me.winterguardian.pvp.PvPMessage;
import me.winterguardian.pvp.PvPPlugin;
import me.winterguardian.pvp.TeamColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class ArenaSubCommand extends SubCommand
{

	public ArenaSubCommand()
	{
		super("arena", Arrays.asList("map", "carte"), PvPPlugin.STAFF, ErrorMessage.COMMAND_INVALID_PERMISSION, ErrorMessage.COMMAND_INVALID_ARGUMENT);
	}

	@Override
	public boolean onSubCommand(CommandSender sender, String label, String[] args)
	{
		if(args.length == 0)
			args = new String[]{"help"};
		
		if(args[0].equalsIgnoreCase("help"))
		{
			return true;
		}
		
		if(args[0].equalsIgnoreCase("list"))
		{
			PvPMessage.COMMAND_ARENA_LIST.say(sender);

			for(String arenaName : PvPArena.getArenaList())
				sender.sendMessage("  " + arenaName);

			return true;
		}
		
		if(args.length < 2)
			return false;
		
		PvPArena arena = new PvPArena(args[1]);
		
		if(args[0].equalsIgnoreCase("create"))
		{
			arena.save();
			PvPMessage.COMMAND_ARENA_CREATE.say(sender);
			return true;
		}
		
		if(!arena.exists())
		{
			PvPMessage.COMMAND_ARENA_INVALIDARENA.say(sender);
			return true;
		}
		
		arena.load();
		
		if(args[0].equalsIgnoreCase("info"))
		{
			sender.sendMessage("Nom: " + arena.getName());
			sender.sendMessage("Auteur: " + arena.getAuthor());
			sender.sendMessage("Ready: " + arena.isReady());
			sender.sendMessage("Zones: " + arena.getZones().size());
			return true;
		}

		if(args[0].equalsIgnoreCase("rename"))
		{
			if(args.length < 3)
				return false;

			arena.delete();
			arena.setName(args[2]);
			arena.save();
			PvPMessage.COMMAND_ARENA_RENAME.say(sender);
		}

		if(!(sender instanceof Player))
		{
			ErrorMessage.COMMAND_INVALID_SENDER.say(sender);
			return true;
		}

		if(args[0].equalsIgnoreCase("seteffects"))
		{
			arena.getEffects().clear();
			Player player = (Player)sender;
			arena.getEffects().addAll(player.getActivePotionEffects());
			arena.save();
			PvPMessage.COMMAND_ARENA_SETEFFECTS.say(sender);
			return true;
		}

		if(args[0].equalsIgnoreCase("geteffects"))
		{
			Player player = (Player)sender;
			player.addPotionEffects(arena.getEffects());
			PvPMessage.COMMAND_ARENA_GETEFFECTS.say(sender);
			return true;
		}
		
		if(args.length < 3)
			return false;
		
		TeamColor color = args.length < 4 ? null : TeamColor.valueOf(args[3].toUpperCase());
		
		if(color == null)
			color = TeamColor.NONE;
		
		if(args[0].equalsIgnoreCase("add"))
		{
			if(args[2].equalsIgnoreCase("point"))
			{
				arena.addSpawnPoint(((Player)sender).getLocation(), color);
				arena.save();
				PvPMessage.COMMAND_ARENA_ADD.say(sender, "<type>", "Point de spawn", "<color>", color.getName());
				return true;
			}
			
			if(args[2].equalsIgnoreCase("flag"))
			{
				arena.addFlag(((Player)sender).getLocation(), color);
				arena.save();
				PvPMessage.COMMAND_ARENA_ADD.say(sender, "<type>", "Drapeau", "<color>", color.getName());
				return true;
			}
			
			if(args[2].equalsIgnoreCase("zone"))
			{
				arena.addZone(((Player)sender).getLocation());
				arena.save();
				PvPMessage.COMMAND_ARENA_ADD.say(sender, "<type>", "Zone", "<color>", color.getName());
				return true;
			}
		}
		
		if(args[0].equalsIgnoreCase("reset"))
		{
			if(args[2].equalsIgnoreCase("point"))
			{
				if(args[3].equalsIgnoreCase("*") || args[3].equalsIgnoreCase("all"))
					arena.resetPoints();
				else
					arena.resetPoints(color);
				arena.save();
				PvPMessage.COMMAND_ARENA_RESET.say(sender, "<type>", "point de spawn", "<color>", args[3]);
				return true;
			}
			
			if(args[2].equalsIgnoreCase("flag"))
			{
				if(args[3].equalsIgnoreCase("*") || args[3].equalsIgnoreCase("all"))
					arena.resetFlags();
				else
					arena.resetFlags(color);
				arena.save();
				PvPMessage.COMMAND_ARENA_RESET.say(sender, "<type>", "drapeau", "<color>", args[3]);
				return true;
			}
			
			if(args[2].equalsIgnoreCase("zone"))
			{
				arena.resetZones();
				arena.save();
				PvPMessage.COMMAND_ARENA_RESET.say(sender, "<type>", "zone", "<color>", args[3]);
				return true;
			}
		}

		if(args[0].equalsIgnoreCase("test"))
		{
			if(args[2].equalsIgnoreCase("point"))
			{
				if(args[3].equalsIgnoreCase("*") || args[3].equalsIgnoreCase("all"))
				{
					for(TeamColor col : TeamColor.values())
					{
						for(SerializableLocation loc : arena.getSpawnPoints())
						{
							ArmorStand as = (ArmorStand) loc.getWorld().spawn(loc.getLocation(), ArmorStand.class);

							as.setGravity(false);
							as.setCanPickupItems(false);
							as.setCustomNameVisible(true);
							as.setVisible(true);
							as.setHelmet(new ItemStack(Material.WOOL, 1, (short)0, col.getWoolColor()));
						}
					}
				}
				else
				{
					for(SerializableLocation loc : arena.getSpawnPoints(color))
					{
						ArmorStand as = (ArmorStand) loc.getWorld().spawn(loc.getLocation(), ArmorStand.class);

						as.setGravity(false);
						as.setCanPickupItems(false);
						as.setCustomNameVisible(true);
						as.setVisible(true);
						as.setHelmet(new ItemStack(Material.WOOL, 1, (short)0, color.getWoolColor()));
					}
				}
				PvPMessage.COMMAND_ARENA_TEST.say(sender, "<type>", "point de spawn", "<color>", args[3]);
				return true;
			}

			if(args[2].equalsIgnoreCase("flag"))
			{
				if(args[3].equalsIgnoreCase("*") || args[3].equalsIgnoreCase("all"))
				{
					for(TeamColor col : TeamColor.values())
					{
						for(SerializableLocation loc : arena.getFlags())
						{
							ArmorStand as = (ArmorStand) loc.getWorld().spawn(loc.getLocation(), ArmorStand.class);

							as.setGravity(false);
							as.setCanPickupItems(false);
							as.setCustomNameVisible(true);
							as.setVisible(true);
							as.setHelmet(new ItemStack(Material.WOOL, 1, (short)0, col.getWoolColor()));
						}
					}
				}
				else
				{
					for(SerializableLocation loc : arena.getFlags(color))
					{
						ArmorStand as = (ArmorStand) loc.getWorld().spawn(loc.getLocation(), ArmorStand.class);

						as.setGravity(false);
						as.setCanPickupItems(false);
						as.setCustomNameVisible(true);
						as.setVisible(true);
						as.setHelmet(new ItemStack(Material.WOOL, 1, (short)0, color.getWoolColor()));
					}
				}

				PvPMessage.COMMAND_ARENA_TEST.say(sender, "<type>", "drapeau", "<color>", args[3]);
				return true;
			}

			if(args[2].equalsIgnoreCase("zone"))
			{
				for(SerializableLocation loc : arena.getZones())
				{
					ArmorStand as = (ArmorStand) loc.getWorld().spawn(loc.getLocation(), ArmorStand.class);

					as.setGravity(false);
					as.setCanPickupItems(false);
					as.setCustomNameVisible(true);
					as.setVisible(true);
					as.setHelmet(new ItemStack(Material.WOOL));
				}
				PvPMessage.COMMAND_ARENA_TEST.say(sender, "<type>", "zone", "<color>", args[3]);
				return true;
			}
		}
		
		return false;
	}

	@Override
	public List<String> onSubTabComplete(CommandSender sender, String label, String[] args)
	{
		return null;
	}
}
