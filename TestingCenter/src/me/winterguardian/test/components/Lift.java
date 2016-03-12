package me.winterguardian.test.components;

import me.winterguardian.core.util.PlayerUtil;
import me.winterguardian.test.TestComponent;
import me.winterguardian.test.TestingCenter;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

public class Lift extends TestComponent
{
	public Lift()
	{
		super("lift", TestingCenter.TEST_LEVEL3, "/test lift");
	}
	
	@Override
	public boolean onSubCommand(CommandSender sender, String label, String[] args)
	{
		if(!(sender instanceof Player))
			return false;
		
		ItemStack item = new ItemStack(Material.STICK, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§rLift");
		item.setItemMeta(meta);
		
		((Player)sender).getInventory().addItem(item);
		return true;
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event)
	{
		if(!event.hasItem())
			return;
		
		if(!event.getItem().hasItemMeta())
			return;
		
		if(!event.getItem().getItemMeta().hasDisplayName())
			return;
		
		if(!event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("§rLift"))
			return;
		
		Entity entity;
		if((entity = PlayerUtil.getFirstPointed(event.getPlayer(), 1, 50)) == null)
			return;
		
		entity.setVelocity(new Vector(0, 0.2, 0));	
	}
	
}
