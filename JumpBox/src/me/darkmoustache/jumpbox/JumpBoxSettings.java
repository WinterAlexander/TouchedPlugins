package me.darkmoustache.jumpbox;

import me.winterguardian.core.Core;
import me.winterguardian.core.world.SerializableLocation;
import me.winterguardian.core.world.SerializableRegion;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;

public class JumpBoxSettings
{
	public static final File FILE_CONFIG = new File(JumpBox.getPlugin().getDataFolder(), "config.yml");
	
	private int easyReward;
	private int mediumReward;
	private int hardReward;
	private int expertReward;
	private float noCheckpointBonus; 
	private SerializableLocation spawn;
	private SerializableRegion region;
	
	public JumpBoxSettings()
	{
		this.easyReward = 10;
		this.mediumReward = 50;
		this.hardReward = 250;
		this.expertReward = 1000;
		this.noCheckpointBonus = 1.5f;
		
		this.spawn = null;
		this.region = null;
	}
	
	public void save()
	{
		FileConfiguration config = YamlConfiguration.loadConfiguration(FILE_CONFIG);
		if(this.spawn != null)
			config.set("spawn-location", this.spawn.toString());
		
		if(this.region != null)
			config.set("region", this.region.toString());
		
		if(!config.isInt("easyReward"))
			config.set("easyReward", easyReward);
		
		if(!config.isInt("mediumReward"))
			config.set("mediumReward", mediumReward);
		
		if(!config.isInt("hardReward"))
			config.set("hardReward", hardReward);
		
		if(!config.isInt("expertReward"))
			config.set("expertReward", expertReward);
		
		if(!config.isDouble("noCheckpointBonus"))
			config.set("noCheckpointBonus", (double)noCheckpointBonus);
		try
		{
			config.save(FILE_CONFIG);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void load()
	{

		FileConfiguration config = YamlConfiguration.loadConfiguration(FILE_CONFIG);
		if(config.isString("spawn-location"))
			this.spawn = SerializableLocation.fromString(config.getString("spawn-location"));
		
		if(config.isString("region"))
			this.region = SerializableRegion.fromString(config.getString("region"));
		
		if(config.isInt("easyReward"))
			easyReward = config.getInt("easyReward");
		if(config.isInt("mediumReward"))
			mediumReward = config.getInt("mediumReward");
		if(config.isInt("hardReward"))
			hardReward = config.getInt("hardReward");
		if(config.isInt("expertReward"))
			expertReward = config.getInt("expertReward");
		if(config.isDouble("noCheckpointBonus"))
			noCheckpointBonus = (float) config.getDouble("noCheckpointBonus");
	}

	public World getWorld() 
	{
		if(this.spawn != null)
			if(this.spawn.getLocation() != null)
				return this.spawn.getLocation().getWorld();
		return null;
	}
	
	public Location getSpawn() 
	{
		if(this.spawn != null)
			return this.spawn.getLocation();
		return null;
	}
	
	public void setSpawn(Location loc)
	{
		this.spawn = new SerializableLocation(loc);
	}
	
	public Location getLocation(Player p)
	{
		return new JumpBoxStats(p, Core.getUserDatasManager().getUserData(p)).getLocation();
	}
	
	public boolean isCheckpointFake(Player p)
	{
		return new JumpBoxStats(p, Core.getUserDatasManager().getUserData(p)).isFakeCheckpoint();
	}
	
	public void setLocation(Player p, Location loc, boolean jumpBegin)
	{
		JumpBoxStats stats = new JumpBoxStats(p, Core.getUserDatasManager().getUserData(p));
		
		stats.setLocation(loc);
		stats.setFakeCheckpoint(jumpBegin);
	}
	
	public void reset(Player p)
	{
		JumpBoxStats stats = new JumpBoxStats(p, Core.getUserDatasManager().getUserData(p));
		
		stats.setLocation(null);
		stats.setFakeCheckpoint(false);
	}

	public int getEasyReward()
	{
		return easyReward;
	}

	public void setEasyReward(int easyReward)
	{
		this.easyReward = easyReward;
	}

	public int getMediumReward()
	{
		return mediumReward;
	}

	public void setMediumReward(int mediumReward)
	{
		this.mediumReward = mediumReward;
	}

	public int getHardReward()
	{
		return hardReward;
	}

	public void setHardReward(int hardReward)
	{
		this.hardReward = hardReward;
	}

	public int getExpertReward()
	{
		return expertReward;
	}

	public void setExpertReward(int expertReward)
	{
		this.expertReward = expertReward;
	}

	public float getNoCheckpointBonus()
	{
		return noCheckpointBonus;
	}

	public void setNoCheckpointBonus(float noCheckpointBonus)
	{
		this.noCheckpointBonus = noCheckpointBonus;
	}

	public boolean isInRegion(Location loc)
	{
		if(this.region == null)
			return false;
		return this.region.contains(loc);
	}

	public void setRegion(SerializableRegion region)
	{
		this.region = region;
	}
}
