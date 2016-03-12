package me.winterguardian.test.components;


import java.util.ArrayList;

import me.winterguardian.test.TestComponent;
import me.winterguardian.test.TestingCenter;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

public class ItemBow extends TestComponent
{
	private ArrayList<Item> noPickupItems;
	
	public ItemBow()
	{
		super("itembow", TestingCenter.TEST_LEVEL1, "/test itembow");
		this.noPickupItems = new ArrayList<Item>();
	}
	
	@Override
	public boolean onSubCommand(CommandSender sender, String label, String[] args)
	{
		sender.sendMessage("Renommez un arc ItemBow pour essayer ce composant de tests.");
		return true;
	}
	
	@EventHandler
	public void onEntityShootBow(EntityShootBowEvent e)
	{
		try
		{
			if(e.getEntity() instanceof Player)
				if(((Player)e.getEntity()).hasPermission(this.getPermission()))
					if(e.getBow().hasItemMeta())
						if(e.getBow().getItemMeta().hasDisplayName())
							if(e.getBow().getItemMeta().getDisplayName().contains("ItemBow"))
							{
								ItemStack itemSkin = getItem(((Player) e.getEntity()).getInventory().getContents());
								((Player) e.getEntity()).getInventory().remove(itemSkin);
								Item item = e.getProjectile().getWorld().dropItem(e.getProjectile().getLocation(), itemSkin);
								this.noPickupItems.add(item);
								e.getProjectile().setPassenger(item);
							}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	@EventHandler
	public void onPlayerPickupItem(PlayerPickupItemEvent e)
	{
		if(this.noPickupItems.contains(e.getItem()))
		{
			e.setCancelled(true);
			this.noPickupItems.remove(e.getItem());
		}
	}
	
	public ItemStack getItem(ItemStack[] items)
	{
		for(ItemStack item : items)
			if(item != null)
				if(item.getType() != Material.AIR && item.getType() != Material.BOW)
					return item;
		return new ItemStack(Material.AIR);
	}
}
