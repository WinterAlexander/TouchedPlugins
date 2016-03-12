package me.winterguardian.blockfarmers;

import java.io.File;

import me.winterguardian.core.game.state.StateGameSetup;
import me.winterguardian.core.world.SerializableLocation;

import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

public class BlockFarmersSetup extends StateGameSetup
{
	private SerializableLocation spawn;
	
	public BlockFarmersSetup(File file)
	{
		super(file);
	}
	
	@Override
	protected void load(YamlConfiguration config)
	{
		super.load(config);
		this.spawn = SerializableLocation.fromString(config.getString("spawn"));
	}

	@Override
	protected void save(YamlConfiguration config)
	{
		super.save(config);
		if(this.spawn != null)
			config.set("spawn", this.spawn.toString());
		else
			config.set("spawn", null);
	}

	public void setSpawn(Location location)
	{
		this.spawn = new SerializableLocation(location);
	}

	public Location getSpawn()
	{
		if(this.spawn != null)
			return this.spawn.getLocation();
		
		return null;
	}
}
