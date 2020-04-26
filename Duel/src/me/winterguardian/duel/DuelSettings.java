package me.winterguardian.duel;

import me.winterguardian.core.util.PlayerUtil;
import me.winterguardian.core.world.SerializableLocation;
import me.winterguardian.core.world.SerializableRegion;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DuelSettings
{
	private boolean clearStuff;
	
	private SerializableLocation lobby;
	private SerializableLocation player1Spawn;
	private SerializableLocation player2Spawn;
	
	private SerializableRegion region;
	
	private ItemStack[] armor;
	private ItemStack[] inventory;
	
	private List<String> allowedCommands;
	
	public DuelSettings()
	{
		this.clearStuff = true;
		
		this.armor = new ItemStack[4];
		this.inventory = new ItemStack[36];
		this.allowedCommands = new ArrayList<String>();
	}
	
	public void load()
	{
		YamlConfiguration config = YamlConfiguration.loadConfiguration(this.getFile());
		
		if(config.isBoolean("clear-stuff"))
			this.clearStuff = config.getBoolean("clear-stuff");
		
		if(config.isString("lobby"))
			this.lobby = SerializableLocation.fromString(config.getString("lobby"));
		
		if(config.isString("spawn1"))
			this.player1Spawn = SerializableLocation.fromString(config.getString("spawn1"));
		
		if(config.isString("spawn2"))
			this.player2Spawn = SerializableLocation.fromString(config.getString("spawn2"));
		
		if(config.isConfigurationSection("armor"))
			for(int i = 0; i < 4; i++)
				armor[i] = config.getItemStack("armor." + i);
		
		if(config.isConfigurationSection("inventory"))
			for(int i = 0; i < 36; i++)
				inventory[i] = config.getItemStack("inventory." + i);
		
		this.allowedCommands = config.getStringList("allowcmds");
		
		if(!this.allowedCommands.contains("duel"))
			this.allowedCommands.add("duel");
		
		if(!this.allowedCommands.contains("reload"))
			this.allowedCommands.add("reload");
		
		
		if(config.isString("region"))
			this.region = SerializableRegion.fromString(config.getString("region"));
	}
	
	public boolean save()
	{
		try
		{
			YamlConfiguration config = YamlConfiguration.loadConfiguration(this.getFile());
		
			config.set("clear-stuff", this.clearStuff);
			
			if(this.lobby != null)
				config.set("lobby", this.lobby.toString());
			
			
			if(this.player1Spawn != null)
				config.set("spawn1", this.player1Spawn.toString());
			
			if(this.player2Spawn != null)
				config.set("spawn2", this.player2Spawn.toString());
			
			int i = 0;
			for(ItemStack item : this.armor)
			{
				config.set("armor." + i, item);
				i++;
			}
			
			i = 0;
			for(ItemStack item : this.inventory)
			{
				config.set("inventory." + i, item);
				i++;
			}
			
			if(this.region != null)
				config.set("region", this.region.toString());
			
			config.save(this.getFile());
			return true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}
	
	public File getFile()
	{
		return new File(Duel.getInstance().getDataFolder(), "config.yml");
	}
	
	public boolean isReady()
	{
		return this.lobby != null 
				&& this.player1Spawn != null 
				&& this.player2Spawn != null 
				&& this.lobby.getLocation() != null 
				&& this.player1Spawn.getLocation() != null 
				&& this.player2Spawn.getLocation() != null 
				&& this.region != null 
				&& this.region.contains(this.lobby.getLocation())
				&& this.region.contains(this.player1Spawn.getLocation())
				&& this.region.contains(this.player2Spawn.getLocation());
	}
	
	public void giveStuff(Player p)
	{
		if(this.clearStuff)
		{
			PlayerUtil.clearInventory(p);
			p.getInventory().setArmorContents(this.armor);
			p.getInventory().setContents(this.inventory);
			p.updateInventory();
		}
	}
	
	public void setStuff(ItemStack[] armor, ItemStack[] inventory)
	{
		this.armor = armor;
		this.inventory = inventory;
	}

	public List<String> getAllowedCommands()
	{
		return allowedCommands;
	}
	
	public Location getLobby()
	{
		if(this.lobby != null)
			return this.lobby.getLocation();
		return null;
	}
	
	public void setLobby(Location loc)
	{
		this.lobby = new SerializableLocation(loc);
	}
	
	public Location getSpawn1()
	{
		if(this.player1Spawn != null)
			return this.player1Spawn.getLocation();
		return null;
	}
	
	public void setSpawn1(Location loc)
	{
		this.player1Spawn = new SerializableLocation(loc);
	}
	
	public Location getSpawn2()
	{
		if(this.player2Spawn != null)
			return this.player2Spawn.getLocation();
		return null;
	}
	
	public void setSpawn2(Location loc)
	{
		this.player2Spawn = new SerializableLocation(loc);
	}
	
	public boolean isInRegion(Player p)
	{
		if(this.region == null)
			return false;
		return this.region.contains(p.getLocation());
	}
	
	public boolean isInRegion(Block b)
	{
		if(this.region == null)
			return false;
		return this.region.contains(b.getLocation());
	}
	
	public void setRegion(Location loc1, Location loc2)
	{
		this.region = new SerializableRegion(loc1, loc2);
	}

	public void setRegion(SerializableRegion region)
	{
		this.region = region;
	}

	public boolean isRegionSet()
	{
		return this.region != null;
	}
	
	public int getNumberOfItems()
	{
		int number = 0;
		for(ItemStack item : armor)
			if(item != null && item.getType() != Material.AIR)
				number++;
		for(ItemStack item : inventory)
			if(item != null && item.getType() != Material.AIR)
				number++;
		return number;
	}

	public boolean doClearStuff() {
		return clearStuff;
	}

	public void setClearStuff(boolean clearStuff) {
		this.clearStuff = clearStuff;
	}
}
