package me.winterguardian.test.components;

import me.winterguardian.core.entity.custom.rideable.RideableEntityUtil;
import me.winterguardian.test.TestComponent;
import me.winterguardian.test.TestingCenter;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Cow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.MushroomCow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CowRush extends TestComponent
{
	private List<Player> rushers;

	public CowRush()
	{
		super("cowrush", TestingCenter.TEST_LEVEL4, "Syntaxe: /test cowrush args");
		this.rushers = new ArrayList<Player>();
	}
	
	@Override
	public boolean onSubCommand(CommandSender sender, String label, String[] args)
	{
		if(!(sender instanceof Player))
			return false;
			
		Player p = (Player)sender;
		
		if(p.getVehicle() == null || !(p.getVehicle() instanceof Cow || p.getVehicle() instanceof MushroomCow) || !RideableEntityUtil.isRideable(p.getVehicle()))
		{
			p.sendMessage("bad vehicle");
			return true;
		}
		
		RideableEntityUtil.setSpeed(p.getVehicle(), RideableEntityUtil.getSpeed(p.getVehicle()) + 0.5f);
		this.rushers.add(p);
		
		
		int max = 100;
		for(int i = 0; i <= max; i += 10)
		{
			final int i2 = i;
			Bukkit.getScheduler().runTaskLater(TestingCenter.getInstance(), new Runnable()
			{
				@Override
				public void run()
				{
					if(i2 == max)
					{
						if(rushers.contains(p))
						{
							RideableEntityUtil.setSpeed(p.getVehicle(), RideableEntityUtil.getSpeed(p.getVehicle()) - 0.5f);
							rushers.remove(p);
						}
					}
					//else (dunno, we'll see)
					for(Player current : p.getWorld().getPlayers())
						current.playSound(p.getLocation(), Sound.COW_IDLE, 5, new Random().nextFloat() + 0.6f); //pitch change may be not needed
				}
			}, i);
		}
		
		return true;
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event)
	{
		if(rushers.contains(event.getPlayer()))
		{
			if(event.getPlayer().getVehicle() == null || !(event.getPlayer().getVehicle() instanceof Cow || event.getPlayer().getVehicle() instanceof MushroomCow) || !RideableEntityUtil.isRideable(event.getPlayer().getVehicle()))
			{
				rushers.remove(event.getPlayer());
				return;
			}
			
			for(LivingEntity entity : event.getPlayer().getWorld().getLivingEntities())
			{
				if(entity == event.getPlayer().getVehicle())
					continue;
				
				if(event.getPlayer().getVehicle().getLocation().distance(entity.getLocation()) < 1.5)
				{
					double x = event.getPlayer().getVehicle().getLocation().getX() - entity.getLocation().getX();
					if(x == 0)
						x = new Random().nextBoolean() ? 2 : -2;
					else
						x = 1 / x;
					
					if(x > 2)
						x = 2;
						
					if(x < -2)
						x = -2;
						
					double y = 1;
					double z = event.getPlayer().getVehicle().getLocation().getZ() - entity.getLocation().getZ();
					
					if(z == 0)
						z = new Random().nextBoolean() ? 2 : -2;
					else
						z = 1 / z;
					
					if(z > 2)
						z = 2;
						
					if(z < -2)
						z = -2;
						
					double reverseX = -Math.sin(Math.toRadians(-entity.getLocation().getYaw()));
					double reverseZ = -Math.cos(Math.toRadians(-entity.getLocation().getYaw()));
					
					if(x * reverseX < 0)
						x = x + reverseX;
					
					if(z * reverseZ < 0)
						z = z + reverseZ;
					
					entity.setVelocity(new Vector(x, y, z));
				}
			}
		}
	}
}