package me.winterguardian.test.components;

import me.winterguardian.core.message.ErrorMessage;
import me.winterguardian.test.TestComponent;
import me.winterguardian.test.TestingCenter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class AutomaticTurret2 extends TestComponent
{

	public AutomaticTurret2()
	{
		super("turret2", TestingCenter.TEST_LEVEL4, "/test turret2 float-deltaAngle double-height int-fireTicks int-knockback double-intensity int-ticks");
	}
	
	public static List<Arrow> toRemove = new ArrayList<>();
	
	@Override
	public boolean onSubCommand(CommandSender sender, String label, String[] args)
	{
		try
		{
			Player p = (Player) sender;
			Bukkit.getScheduler().scheduleSyncRepeatingTask(TestingCenter.getInstance(), new Turret2(
					p.getLocation().add(0, 1, 0), 
					Float.parseFloat(args[0]), 
					Double.parseDouble(args[1]), 
					Integer.parseInt(args[2]),
					Integer.parseInt(args[3]),
					Double.parseDouble(args[4])), 
					1, Integer.parseInt(args[5]));
			return true;
		}
		catch(Exception e)
		{
			ErrorMessage.COMMAND_INVALID_ARGUMENT.say(sender);
			return false;
		}
	}
	
	@EventHandler
	public void onArrowHit(ProjectileHitEvent e)
	{
		if(toRemove.contains(e.getEntity()))
			Bukkit.getScheduler().runTaskLater(TestingCenter.getInstance(), new Runnable(){

				@Override
				public void run()
				{
					e.getEntity().remove();
				}
			}, 10);
		toRemove.remove(e.getEntity());
	}
	
	private static class Turret2 implements Runnable
	{
		private Location loc;
		private float angle, deltaAngle;
		private double height, intensity;
		private int fireTicks, knockback;
		
		public Turret2(Location loc, float deltaAngle, double height, int fireTicks, int knockback, double intensity)
		{
			this.loc = loc;
			this.angle = 0;
			this.deltaAngle = deltaAngle;
			this.height = height;
			this.intensity = intensity;
			this.fireTicks = fireTicks;
			this.knockback = knockback;
		}
		
		@Override
		public void run()
		{
			Arrow arrow = (Arrow) loc.getWorld().spawnEntity(this.loc, EntityType.ARROW);
			arrow.setFireTicks(this.fireTicks);
			arrow.setKnockbackStrength(this.knockback);
			arrow.setCritical(true);
			arrow.setBounce(false);
			arrow.setVelocity(new Vector(Math.sin(Math.toRadians(angle)) * this.intensity, this.height, Math.cos(Math.toRadians(angle)) * this.intensity));
			toRemove.add(arrow);
			this.move();
		}
		
		private void move()
		{
			this.angle += this.deltaAngle;
		}
	}

}
