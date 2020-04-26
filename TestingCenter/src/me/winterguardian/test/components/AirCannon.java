package me.winterguardian.test.components;

import me.winterguardian.core.particle.ParticleType;
import me.winterguardian.core.particle.ParticleUtil;
import me.winterguardian.test.TestComponent;
import me.winterguardian.test.TestingCenter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

public class AirCannon extends TestComponent
{

	public AirCannon()
	{
		super("AirCannon", TestingCenter.TEST_LEVEL3, "/test aircannon");
	}
	
	@Override
	public boolean onSubCommand(CommandSender sender, String label, String[] args)
	{
		if(!(sender instanceof Player))
			return false;
		
		ItemStack stack = new ItemStack(Material.BUCKET, 1);
		ItemMeta meta = stack.getItemMeta();
		meta.setDisplayName("§f§lAir Cannon !");
		stack.setItemMeta(meta);
		((Player)sender).getInventory().addItem(stack);
		
		return true;
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerInteract(PlayerInteractEvent event)
	{
		if(event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK)
			return;
			
		if(!event.hasItem() || !event.getItem().hasItemMeta() || !event.getItem().getItemMeta().hasDisplayName())
			return;
		
		if(!event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("§f§lAir Cannon !"))
			return;
		
		event.getPlayer().sendMessage("TEST");
		
		for(int i = 0; i < 5; i++)
		{
			
			for(int angle = 0; angle < 360; angle += 15 / (i + 1))
			{
				double pitch = -45 * Math.sin(Math.toRadians(angle));
				double yaw = 45 * Math.cos(Math.toRadians(angle));
				
				Vector vec = new Location(event.getPlayer().getWorld(), 0, 0, 0, (float)yaw + event.getPlayer().getLocation().getYaw(), (float)pitch).getDirection().add(new Location(event.getPlayer().getWorld(), 0, 0, 0, event.getPlayer().getLocation().getYaw(), 0).getDirection().multiply(i)).multiply(i + 1);
				
				Location particle = event.getPlayer().getEyeLocation().clone().add(vec);
				
				ParticleUtil.playSimpleParticles(particle, ParticleType.SPELL_WITCH, 0, 0, 0, 0, 1);
			}
		}
		
		
	}

}
