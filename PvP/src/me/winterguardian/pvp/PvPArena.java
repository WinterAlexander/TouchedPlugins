package me.winterguardian.pvp;

import me.winterguardian.core.game.VotableArena;
import me.winterguardian.core.world.LocationUtil;
import me.winterguardian.core.world.SerializableLocation;
import me.winterguardian.core.world.SerializableRegion;
import me.winterguardian.pvp.game.PvPMatch;
import me.winterguardian.pvp.game.infected.Infected;
import me.winterguardian.pvp.game.solo.Brawl;
import me.winterguardian.pvp.game.solo.FreeForAll;
import me.winterguardian.pvp.game.solo.KingOfTheHill;
import me.winterguardian.pvp.game.solo.OneInTheChamber;
import me.winterguardian.pvp.game.solo.Switch;
import me.winterguardian.pvp.game.team.CaptureTheFlag;
import me.winterguardian.pvp.game.team.Domination;
import me.winterguardian.pvp.game.team.TeamDeathMatch;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Boolean.parseBoolean;
import static java.lang.Integer.parseInt;

public class PvPArena extends VotableArena
{
	public static final File DIR = new File(PvPPlugin.getPlugin().getDataFolder(), "arenas/");
	
	private HashMap<TeamColor, List<SerializableLocation>> spawnPoints;
	private HashMap<TeamColor, List<SerializableLocation>> flags;
	private List<SerializableLocation> zones;
	private HashMap<TeamColor, SerializableRegion> spawnProtects;
	private SerializableRegion gameRegion;

	private List<PotionEffect> effects = new ArrayList<>();

	public PvPArena(String name)
	{
		super(name);
		this.spawnPoints = new HashMap<>();
		this.flags = new HashMap<>();
		this.zones = new ArrayList<>();
		this.spawnProtects = new HashMap<>();
		this.gameRegion = null;
	}

	public void addSpawnPoint(Location point, TeamColor color)
	{
		if(!spawnPoints.containsKey(color))
			spawnPoints.put(color, new ArrayList<>());
		
		spawnPoints.get(color).add(new SerializableLocation(point));
	}

	public void addFlag(Location flag, TeamColor color)
	{
		if(!flags.containsKey(color))
			flags.put(color, new ArrayList<>());
		
		flags.get(color).add(new SerializableLocation(flag));
	}
	
	public void addZone(Location zone)
	{
		this.zones.add(new SerializableLocation(zone));
	}


	public void resetPoints(TeamColor color)
	{
		this.spawnPoints.get(color).clear();
	}
	
	public void resetPoints()
	{
		this.spawnPoints.clear();
	}

	public List<SerializableLocation> getPoints(TeamColor color)
	{
		return this.spawnPoints.get(color);
	}

	public List<SerializableLocation> getPoints()
	{
		return this.spawnPoints.values().stream().flatMap(List::stream).collect(Collectors.toList());
	}
	
	public void resetFlags(TeamColor color)
	{
		this.flags.get(color).clear();
	}
	
	public void resetFlags()
	{
		this.flags.clear();
	}

	public List<SerializableLocation> getFlags(TeamColor color)
	{
		return this.flags.get(color);
	}

	public List<SerializableLocation> getFlags()
	{
		return this.flags.values().stream().flatMap(List::stream).collect(Collectors.toList());
	}
	
	public void resetZones()
	{
		this.zones.clear();
	}

	public SerializableRegion getRegion()
	{
		return gameRegion;
	}

	public void setRegion(SerializableRegion region)
	{
		this.gameRegion = region;
	}

	public SerializableRegion getProtectionRegion(TeamColor color)
	{
		return this.spawnProtects.get(color);
	}

	public void setProtectionRegion(TeamColor color, SerializableRegion region)
	{
		this.spawnProtects.put(color, region);
	}

	@Override
	protected void save(YamlConfiguration config)
	{
		config.set("spawn-points", null);
		for(TeamColor color : this.spawnPoints.keySet())
		{
			int i = 0;
			for(SerializableLocation loc : this.spawnPoints.get(color))
			{
				if(loc == null || loc.getLocation() == null)
					continue;
				
				config.set("spawn-points." + color.name() + "." + i++, loc.toString());
			}
		}
		
		config.set("flags", null);
		for(TeamColor color : this.flags.keySet())
		{
			int i = 0;
			for(SerializableLocation loc : this.flags.get(color))
			{
				if(loc == null || loc.getLocation() == null)
					continue;
				
				config.set("flags." + color.name() + "." + i++, loc.toString());
			}
		}
		
		config.set("zones", null);
		
		int i = 0;
		for(SerializableLocation loc : this.zones)
		{
			if(loc == null || loc.getLocation() == null)
				continue;
			
			config.set("zones." + i++, loc.toString());
		}

		config.set("effects", null);
		i = 0;
		for(PotionEffect effect : effects)
		{
			config.set("effects." + i++ + ".name", effect.getType().getName());
			config.set("effects." + i++ + ".duration", effect.getDuration());
			config.set("effects." + i++ + ".amplifier", effect.getAmplifier());
			config.set("effects." + i++ + ".ambient", effect.isAmbient());
			config.set("effects." + i++ + ".particles", effect.hasParticles());
		}
	}
	
	@Override
	protected void load(YamlConfiguration config)
	{
		this.spawnPoints = new HashMap<>();
		if(config.isConfigurationSection("spawn-points"))
			for(String color : config.getConfigurationSection("spawn-points").getKeys(false))
			{
				if(!spawnPoints.containsKey(TeamColor.valueOf(color)))
					spawnPoints.put(TeamColor.valueOf(color), new ArrayList<>());
			
				for(String i : config.getConfigurationSection("spawn-points." + color).getKeys(false))
					this.spawnPoints.get(TeamColor.valueOf(color)).add(SerializableLocation.fromString(config.getString("spawn-points." + color + "." + i)));
			}

		this.flags = new HashMap<>();
		if(config.isConfigurationSection("flags"))
			for(String color : config.getConfigurationSection("flags").getKeys(false))
			{
				if(!flags.containsKey(TeamColor.valueOf(color)))
					flags.put(TeamColor.valueOf(color), new ArrayList<>());
			
				for(String i : config.getConfigurationSection("flags." + color).getKeys(false))
					this.flags.get(TeamColor.valueOf(color)).add(SerializableLocation.fromString(config.getString("flags." + color + "." + i)));
			}

		this.zones = new ArrayList<>();
		if(config.isConfigurationSection("zones"))
			for(String s : config.getConfigurationSection("zones").getKeys(false))
				this.zones.add(SerializableLocation.fromString(config.getString("zones." + s)));

		this.effects = new ArrayList<>();
		if(config.isConfigurationSection("effects"))
			for(String s : config.getConfigurationSection("effects").getKeys(false))
			{
				try
				{
					PotionEffectType type = PotionEffectType.getByName(config.getString("effects." + s + ".name"));
					int duration = parseInt(config.getString("effects." + s + ".duration"));
					int amplifier = parseInt(config.getString("effects." + s + ".amplifier"));
					boolean ambient = parseBoolean(config.getString("effects." + s + ".ambient"));
					boolean particles = parseBoolean(config.getString("effects." + s + ".particles"));

					effects.add(new PotionEffect(type, duration, amplifier, ambient, particles));
				}
				catch(NumberFormatException ignored) {}
			}
	}


	public boolean isCompatible(PvPMatch type)
	{
		if(type instanceof FreeForAll || type instanceof OneInTheChamber || type instanceof Switch || type instanceof Brawl || type instanceof Infected)
			return this.spawnPoints.get(TeamColor.NONE) != null && this.spawnPoints.get(TeamColor.NONE).size() > 0;

		if(type instanceof KingOfTheHill)
			return this.spawnPoints.get(TeamColor.NONE) != null && this.spawnPoints.get(TeamColor.NONE).size() > 0 && this.zones.size() > 0;

		if(type instanceof TeamDeathMatch)
			return this.spawnPoints.size() - (this.spawnPoints.get(TeamColor.NONE) != null ? 1 : 0) == ((TeamDeathMatch)type).getTeamCount();

		if(type instanceof CaptureTheFlag)
			return this.spawnPoints.size() - (this.spawnPoints.get(TeamColor.NONE) != null ? 1 : 0) == ((CaptureTheFlag)type).getTeamCount() && this.flags.size() - (this.flags.get(TeamColor.NONE) != null ? 1 : 0) == ((CaptureTheFlag)type).getTeamCount();

		if(type instanceof Domination)
			return this.spawnPoints.size() - (this.spawnPoints.get(TeamColor.NONE) != null ? 1 : 0) == ((Domination)type).getTeamCount() && this.zones.size() > 0;

		return false;
	}
	
	@SuppressWarnings("deprecation")
	public void spawnFlag(TeamColor color)
	{
		for(SerializableLocation loc : this.flags.get(color))
		{
			loc.getLocation().getBlock().setType(Material.WOOL, false);
			loc.getLocation().getBlock().setData(color.getWoolColor(), false);
		}
	}

	public void cutFlag(TeamColor color)
	{
		for(SerializableLocation loc : this.flags.get(color))
		{
			loc.getLocation().getBlock().setType(Material.AIR, false);
		}
	}

	public void dispose()
	{
		for(TeamColor color : this.flags.keySet())
		{
			for(SerializableLocation loc : this.flags.get(color))
			{
				loc.getLocation().getBlock().setType(Material.AIR, false);
			}
		}
	}

	public TeamColor getColor(Block flag)
	{
		for(TeamColor color : this.flags.keySet())
			for(SerializableLocation loc : this.flags.get(color))
				if(LocationUtil.equalsAsBlock(loc.getLocation(), flag.getLocation()))
					return color;

		return null;
	}

	@Override
	protected File getArenaDirectory()
	{
		return DIR;
	}
	
	@Override
	public boolean isReady()
	{
		return true;
	}

	public List<PotionEffect> getEffects()
	{
		return effects;
	}

	public List<TeamColor> getTeams()
	{
		ArrayList<TeamColor> teams = new ArrayList<>();

		teams.addAll(this.spawnPoints.keySet());
		teams.remove(TeamColor.NONE);

		return teams;
	}

	public List<SerializableLocation> getZones()
	{
		return this.zones;
	}

	public Location getSpawnPoint(TeamColor color)
	{
		SerializableLocation bestSpawn = this.spawnPoints.get(color).get(0);

		for(SerializableLocation point : this.spawnPoints.get(color))
		{
			if(getPlayersTotalAntiDistance(point) < getPlayersTotalAntiDistance(bestSpawn))
				bestSpawn = point;
		}
		return bestSpawn.getLocation();
	}

	private double getPlayersTotalAntiDistance(SerializableLocation loc)
	{
		double distance = 0;

		for(Player player : PvPPlugin.getGame().getPlayers())
			if(player.getLocation().getWorld() == loc.getWorld())
				distance += (loc.getLocation().distance(player.getLocation()) == 0) ? 1000000 : 1 / loc.getLocation().distance(player.getLocation());

		return distance;
	}
	
	public static List<String> getArenaList()
	{
		List<String> list = new ArrayList<>();
		if(DIR == null)
			return list;
		try
		{
			for (File file : DIR.listFiles())
				list.add(file.getName().replace(".arena", ""));
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return list;
	}
	
	public static List<PvPArena> getLoadedArenaList()
	{
		List<PvPArena> list = new ArrayList<>();
			
		for (String name : getArenaList())
		{
			PvPArena arena = new PvPArena(name);
			arena.load();
			
			list.add(arena);
		}
		
		return list;
	}
	
	public static List<PvPArena> getCompatibleArenaList(PvPMatch type)
	{
		List<PvPArena> list = new ArrayList<>();
			
		for (PvPArena arena : getLoadedArenaList())
			if(arena.isCompatible(type))
				list.add(arena);
		
		return list;
	}
	
	public static List<PvPArena> getReadyArenaList()
	{
		List<PvPArena> list = new ArrayList<>();
		
		for (PvPArena arena : getLoadedArenaList())
			if(arena.isReady())
				list.add(arena);
		
		return list;
	}
}
