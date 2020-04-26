package me.winterguardian.pvp;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GameStuff
{
	private String name; //utilisé pour le fichier yml
	private ItemStack[] content; //4 premiers = armure et 36 derniers = inventaire
	
	public GameStuff(String name)
	{
		this.name = name;
		this.content = new ItemStack[40];
		for(int i = 0; i < 40; i++)
			this.content[i] = new ItemStack(Material.AIR);
	}
	
	public GameStuff(String name, ItemStack[] content)
	{
		this.name = name;
		this.setContent(content);
	}
	
	public GameStuff(String name, ItemStack[] armor, ItemStack[] inventory)
	{
		this.setContent(armor, inventory);
		this.name = name;
	}

	public GameStuff(GameStuff stuff)
	{
		this.name = stuff.name;
		this.content = stuff.content;
	}
	
	
	public GameStuff(String name, PlayerInventory inventory)
	{
		this(name, inventory.getArmorContents(), inventory.getContents());
	}

	public ItemStack[] getArmor()
	{
		ItemStack[] armor = new ItemStack[4];
		for(int i = 0; i < 4; i++)
			armor[i] = this.content[i];
		return armor;
	}
	
	public ItemStack[] getInventory()
	{
		ItemStack[] inventory = new ItemStack[36];
		for(int i = 0; i < 36; i++)
			inventory[i] = this.content[i + 4];
		return inventory;
	}
	
	public void setArmor(ItemStack[] armor)
	{
		this.setContent(armor, this.getInventory());
	}
	
	public void setInventory(ItemStack[] inventory)
	{
		this.setContent(this.getArmor(), inventory);
	}
	
	public void setContent(ItemStack[] armor, ItemStack[] inventory)
	{
		this.content = new ItemStack[40];
		for(int i = 0; i < 40; i++)
			this.content[i] = i < 4 ? armor[i] : inventory[i - 4];
	}
	
	public void setContent(PlayerInventory inv)
	{
		this.setContent(inv.getArmorContents(), inv.getContents());
	}
	
	public void setContent(ItemStack[] content)
	{
		this.content = content;
	}
	
	public File getFile()
	{
		return new File(getDirectory(), this.name + ".yml");
	}
	
	public void save()
	{
		try
		{
			FileConfiguration config = YamlConfiguration.loadConfiguration(getFile());
			
			for(int i = 0; i < 40; i++)
				config.set("" + i, this.content[i]);
			config.save(getFile());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void load()
	{
		try
		{
			FileConfiguration config = YamlConfiguration.loadConfiguration(getFile());
			
			for(int i = 0; i < 40; i++)
				this.content[i] = config.getItemStack("" + i);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void delete()
	{
		try
		{
			getFile().delete();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void give(Player p)
	{
		p.getInventory().setArmorContents(this.getArmor());
		p.getInventory().setContents(this.getInventory());
		p.updateInventory();
	}
	
	public static File getDirectory()
	{
		File dir = new File(PvPPlugin.getPlugin().getDataFolder(), "stuff/");
		if(!dir.exists())
			dir.mkdirs();
		if(!dir.isDirectory())
		{
			dir.delete();
			dir.mkdirs();
		}
		return dir;
	}
	
	public static List<String> listNames()
	{
		List<String> list = new ArrayList<String>();
		
		for(File file : getDirectory().listFiles())
			list.add(file.getName().substring(0, file.getName().length() - 4));
		
		return list;
	}
}
