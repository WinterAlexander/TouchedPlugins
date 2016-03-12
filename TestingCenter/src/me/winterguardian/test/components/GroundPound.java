package me.winterguardian.test.components;

import java.util.ArrayList;
import java.util.List;

import me.winterguardian.test.TestComponent;
import me.winterguardian.test.TestingCenter;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

public class GroundPound extends TestComponent
{
	private List<Player> pounders;

	public GroundPound()
	{
		super("groundpound", TestingCenter.TEST_LEVEL3, "Syntaxe: /test groundpound");
		pounders = new ArrayList<Player>();
	}
	
	@Override
	public boolean onSubCommand(CommandSender sender, String label, String[] args)
	{
		if(!(sender instanceof Player))
			return false;
			
		Player p = (Player)sender;
		
		p.setVelocity(new Vector(Math.sin(Math.toRadians(-p.getLocation().getYaw())), 1, Math.cos(Math.toRadians(-p.getLocation().getYaw())))); 
		Bukkit.getScheduler().runTaskLater(TestingCenter.getInstance(), new Runnable()
		{
			@Override
			public void run()
			{
				p.setVelocity(new Vector(0, -10, 0));
				pounders.add(p);
			}
		}, 20);
		
		return true;
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) //not sure
	{
		if(pounders.contains(event.getPlayer()))
		{
			Bukkit.getScheduler().runTaskLater(TestingCenter.getInstance(), new Runnable()
			{
				@Override
				public void run()
				{
					if(((Entity)event.getPlayer()).isOnGround())
					{
						pound(event.getPlayer(), 2, 10);
						pounders.remove(event.getPlayer());
					}
				}
			}, 0);
		}
	}
	
	private static void pound(Player player, double power, double radius)
	{
		for(LivingEntity e : player.getWorld().getLivingEntities())
		{
			if(e == player)
				continue;
			
			double distance = player.getLocation().distanceSquared(e.getLocation());
			
			if(distance > radius * radius)
				continue;
			
			if(distance < 1)
				distance = 1;
			
			double reverseX = -Math.sin(Math.toRadians(-e.getLocation().getYaw()));
			double reverseZ = -Math.cos(Math.toRadians(-e.getLocation().getYaw()));
			
			double x = (e.getLocation().getX() - player.getLocation().getX()) / distance * power;
			double y = 1 / Math.pow(distance, 0.1);
			double z = (e.getLocation().getZ() - player.getLocation().getZ()) / distance * power;
			
			if(x * reverseX < 0)
				x = x + reverseX;
			
			if(z * reverseZ < 0)
				z = z + reverseZ;

			e.setVelocity(new Vector(x, y, z));
		}	
	}
}
