package me.winterguardian.pvp.purchase.kit;

import me.winterguardian.pvp.PvPPlugin;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static me.winterguardian.core.util.InventoryUtil.addConvenient;

/**
 *
 * Created by Alexander Winter on 2015-12-10.
 */
public class Kit
{
	private String name; //utilisé pour le fichier yml
	private ItemStack[] content;

	public Kit(String name)
	{
		this.name = name;
		this.content = new ItemStack[36];
		for(int i = 0; i < 36; i++)
			this.content[i] = new ItemStack(Material.AIR);
	}

	public Kit(String name, ItemStack[] content)
	{
		this.name = name;
		this.setContent(content);
	}

	public Kit(String name, PlayerInventory inventory)
	{
		this(name, inventory.getContents());
	}

	public boolean fits(PlayerInventory inventory)
	{
		ItemStack[] backup = inventory.getContents();
		ItemStack[] armorBackup = inventory.getArmorContents();
		try
		{
			for(ItemStack stack : this.content)
				if(stack != null)
				{
					if(inventory.firstEmpty() == -1)
						return false;

					addConvenient(inventory, stack);
				}
			return true;
		}
		finally
		{
			inventory.setContents(backup);
			inventory.setArmorContents(armorBackup);
		}
	}

	public void setContent(PlayerInventory inv)
	{
		this.setContent(inv.getContents());
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

			for(int i = 0; i < 36; i++)
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

			for(int i = 0; i < 36; i++)
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
		for(ItemStack stack : this.content)
			if(stack != null)
				addConvenient(p.getInventory(), stack);
		p.updateInventory();
	}

	public static File getDirectory()
	{
		File dir = new File(PvPPlugin.getPlugin().getDataFolder(), "kits/");
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
		List<String> list = new ArrayList<>();

		for(File file : getDirectory().listFiles())
			list.add(file.getName().substring(0, file.getName().length() - 4));

		return list;
	}
}
