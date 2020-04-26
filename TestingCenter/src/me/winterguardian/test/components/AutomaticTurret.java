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
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Random;

public class AutomaticTurret extends TestComponent
{

	public AutomaticTurret()
	{
		super("turret", TestingCenter.TEST_LEVEL4, "/test turret anglemin anglemax hauteur precision tick_de_feu knockback degrée_d'incrémentation ticks");
	}

	@Override
	public boolean onSubCommand(CommandSender sender, String label, String[] args)
	{
		try
		{
			Player p = (Player) sender;
			Bukkit.getScheduler().scheduleSyncRepeatingTask(TestingCenter.getInstance(), new Turret(
					p.getLocation().add(0, 1, 0), 
					p, 
					(int)(p.getLocation().getYaw() + Float.parseFloat(args[0])), 
					(int)(p.getLocation().getYaw() + Float.parseFloat(args[1])), 
					Double.parseDouble(args[2]), 
					Float.parseFloat(args[3]),
					Integer.parseInt(args[4]),
					Integer.parseInt(args[5]),
					Float.parseFloat(args[6])), 
					1, Integer.parseInt(args[7]));
			return true;
		}
		catch(Exception e)
		{
			ErrorMessage.COMMAND_INVALID_ARGUMENT.say(sender);
			return false;
		}
	}
	
	private static class Turret implements Runnable
	{
		private Location loc;
		private Player p;
		private float angle, minAngle, maxAngle, precision, degreeIncrementation;
		private double height;
		private int fireTicks, knockback;
		private boolean ascending;
		
		public Turret(Location loc, Player p, float minAngle, float maxAngle, double height, float precision, int fireTicks, int knockback, float degreeIncrementation)
		{
			this.loc = loc;
			this.p = p;
			this.minAngle = minAngle;
			this.maxAngle = maxAngle;
			this.angle = this.minAngle;
			this.ascending = true;
			this.height = height;
			this.precision = precision;
			this.fireTicks = fireTicks;
			this.knockback = knockback;
			this.degreeIncrementation = degreeIncrementation;
		}
		
		@Override
		public void run()
		{
			Arrow arrow = (Arrow) loc.getWorld().spawnEntity(this.loc, EntityType.ARROW);
			arrow.setShooter((ProjectileSource)this.p);
			arrow.setFireTicks(this.fireTicks);
			arrow.setKnockbackStrength(this.knockback);
			arrow.setCritical(true);
			arrow.setBounce(false);
			double currentHeight = this.height + (100d - this.precision) / 100d * (new Random().nextDouble() - 0.5);
			arrow.setVelocity(new Vector(-Math.sin(Math.toRadians(angle)), currentHeight, Math.cos(Math.toRadians(angle))));
			this.move();
		}
		
		private void move()
		{
			if(this.ascending)
			{
				this.angle += degreeIncrementation;
				if(angle > this.maxAngle)
				{
					this.angle = this.maxAngle;
					this.ascending = false;
				}
			}
			else
			{
				this.angle -= degreeIncrementation;
				if(angle < this.minAngle)
				{
					this.angle = this.minAngle;
					this.ascending = true;
				}
			}
		}
	}
	
	@Override
	public List<String> onSubTabComplete(CommandSender arg0, String arg1, String[] arg2)
	{
		return null;
	}
}
