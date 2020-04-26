package ice;

import me.winterguardian.core.world.SerializableLocation;
import me.winterguardian.core.world.SerializableRegion;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class IceRunSettings
{
	public static final File FILE = new File(IceRun.getPlugin().getDataFolder(), "config.yml");
	
	private SerializableRegion region;
	private SerializableLocation exit;
	private SerializableLocation lobby;
	private SerializableLocation spawn;
	private List<String> allowedCommands;
	private List<ItemStack> stuff;
	private List<ItemStack> vipstuff;
	
	public IceRunSettings()
	{
		this.region = null;
		this.exit = null;
		this.lobby = null;
		this.spawn = null;
		
		this.allowedCommands = new ArrayList<String>();
		this.allowedCommands.add("icerun");
		this.allowedCommands.add("ice-run");
		this.allowedCommands.add("icer");
		this.allowedCommands.add("ir");
		this.allowedCommands.add("reload");
		
		this.stuff = new ArrayList<ItemStack>();
		this.stuff.add(new ItemStack(Material.SNOW_BALL, 5));
		this.stuff.add(new ItemStack(Material.SUGAR, 2));
		
		this.vipstuff = new ArrayList<ItemStack>();
		this.vipstuff.add(new ItemStack(Material.SNOW_BALL, 10));
		this.vipstuff.add(new ItemStack(Material.EGG, 5));
		this.vipstuff.add(new ItemStack(Material.SUGAR, 3));
		this.vipstuff.add(new ItemStack(Material.GLOWSTONE_DUST, 2));
	}
	
	public void save() throws IOException
	{
		YamlConfiguration config = YamlConfiguration.loadConfiguration(FILE);
		
		if(this.region != null)
			config.set("region", this.region.toString());
		
		if(this.exit != null)
			config.set("exit", this.exit.toString());
		
		if(this.lobby != null)
			config.set("lobby", this.lobby.toString());
		
		if(this.spawn != null)
			config.set("spawn", this.spawn.toString());
		
		if(this.allowedCommands.size() != 0)
			config.set("allowedCommands", this.allowedCommands);
		
		config.set("stuff", null);
		int i = 0;
		for(ItemStack item : this.stuff)
		{
			config.set("stuff.item" + i, item);
			i++;
		}
		
		config.set("stuff-vip", null);
		i = 0;
		for(ItemStack item : this.vipstuff)
		{
			config.set("stuff-vip.item" + i, item);
			i++;
		}
		
		config.save(FILE);
	}
	
	public void load()
	{
		YamlConfiguration config = YamlConfiguration.loadConfiguration(FILE);
		
		if(config.isString("region"))
			this.region = SerializableRegion.fromString(config.getString("region"));
		
		if(config.isString("exit"))
			this.exit = SerializableLocation.fromString(config.getString("exit"));
		
		if(config.isString("lobby"))
			this.lobby = SerializableLocation.fromString(config.getString("lobby"));
		
		if(config.isString("spawn"))
			this.spawn = SerializableLocation.fromString(config.getString("spawn"));
		
		if(config.isList("allowedCommands"))
			this.allowedCommands = config.getStringList("allowedCommands");
		
		if(config.isConfigurationSection("stuff"))
		{
			this.stuff = new ArrayList<ItemStack>();
			for(String key : config.getConfigurationSection("stuff").getKeys(false))
				this.stuff.add(config.getItemStack("stuff." + key));
		}
		
		if(config.isConfigurationSection("stuff-vip"))
		{
			this.vipstuff = new ArrayList<ItemStack>();
			for(String key : config.getConfigurationSection("stuff-vip").getKeys(false))
				this.vipstuff.add(config.getItemStack("stuff-vip." + key));
		}
	}
	
	public SerializableRegion getRegion()
	{
		return region;
	}
	
	public void setRegion(SerializableRegion region)
	{
		this.region = region;
	}
	
	public SerializableLocation getExit()
	{
		return exit;
	}
	
	public void setExit(SerializableLocation exit) 
	{
		this.exit = exit;
	}
	
	public SerializableLocation getLobby()
	{
		return lobby;
	}
	
	public void setLobby(SerializableLocation lobby)
	{
		this.lobby = lobby;
	}
	
	public SerializableLocation getSpawn()
	{
		return spawn;
	}
	
	public void setSpawn(SerializableLocation spawn)
	{
		this.spawn = spawn;
	}
	
	public List<String> getAllowedCommands()
	{
		return allowedCommands;
	}
	
	public void setAllowedCommands(List<String> allowedCommands)
	{
		this.allowedCommands = allowedCommands;
	}

	public List<ItemStack> getStuff()
	{
		return stuff;
	}

	public void setStuff(List<ItemStack> stuff)
	{
		this.stuff = stuff;
	}

	public List<ItemStack> getVipstuff()
	{
		return vipstuff;
	}

	public void setVipstuff(List<ItemStack> vipstuff)
	{
		this.vipstuff = vipstuff;
	}
	
	public void giveStuff(Player p)
	{
		if(p.hasPermission(IceRun.VIP))
			for(ItemStack item : IceRun.getSettings().getVipstuff())
				p.getInventory().addItem(item);
		
		else
			for(ItemStack item : IceRun.getSettings().getStuff())
				p.getInventory().addItem(item);
		
		p.updateInventory();
	}
	
	public boolean isGameReady()
	{
		return this.region != null
				&& this.lobby != null 
				&& this.lobby.getWorld() != null 
				&& this.exit != null 
				&& this.exit.getWorld() != null 
				&& this.spawn != null 
				&& this.spawn.getWorld() != null
				&& this.lobby.getWorld() == this.exit.getWorld()
				&& this.lobby.getWorld() == this.spawn.getWorld()
				&& this.region.getMinimum().getWorld() == this.spawn.getWorld()
				&& this.region.getMaximum().getWorld() == this.lobby.getWorld();
	}
}
