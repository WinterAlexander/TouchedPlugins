package me.winterguardian.test.components;

import me.winterguardian.core.message.ErrorMessage;
import me.winterguardian.test.TestComponent;
import me.winterguardian.test.TestingCenter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.List;

public class ArrowGrenade extends TestComponent
{
	public ArrowGrenade()
	{
		super("arrowgrenade", TestingCenter.TEST_LEVEL3, "/test arrowgrenade x y z arrow_speed arrow_spread incrementation_degree explosion_power arrow_damage arrow_knockback lightning");
	}
	
	@Override
	public boolean onSubCommand(CommandSender sender, String label, String[] args)
	{
		if(!(sender instanceof Player))
		{
			ErrorMessage.COMMAND_INVALID_SENDER.say(sender);
			return true;
		}
		
		if(args.length != 10)
		{
			ErrorMessage.COMMAND_INVALID_ARGUMENT.say(sender);
			return false;
		}
		
		Location loc;
		float speed, spread, degree;
		int power, knockback, lightning;
		double damage;
		try
		{
			loc = new Location(((Player) sender).getWorld(), Double.parseDouble(args[0]), Double.parseDouble(args[1]), Double.parseDouble(args[2]));
			speed = Float.parseFloat(args[3]);
			spread = Float.parseFloat(args[4]);
			degree = Float.parseFloat(args[5]);
			power = Integer.parseInt(args[6]);
			damage = Double.parseDouble(args[7]);
			knockback = Integer.parseInt(args[8]);
			lightning = Integer.parseInt(args[9]);
		}
		catch(Exception e)
		{
			ErrorMessage.COMMAND_INVALID_ARGUMENT.say(sender);
			return false;
		}
		new Thread()
		{
			@Override
			public void run()
			{
				for(float pitch = 90; pitch > -90; pitch -= degree)
				{
					for(float yaw = -180; yaw < 180; yaw += 180 * degree / (180 - (Math.abs(pitch) * 2 + (pitch == 0 ? 1 : 0))))
					{
							Vector vec = new Vector(Math.cos(Math.toRadians(pitch)) * Math.sin(Math.toRadians(yaw)), Math.sin(Math.toRadians(pitch)), Math.cos(Math.toRadians(pitch)) * Math.cos(Math.toRadians(yaw)));
							
							Bukkit.getScheduler().runTaskLater(TestingCenter.getInstance(), new Runnable()
							{
								@Override
								public void run()
								{
									Arrow arrow = loc.getWorld().spawnArrow(loc, vec, speed, spread);
									arrow.setCritical(false);
									arrow.setKnockbackStrength(knockback);
									arrow.setBounce(false);
									arrow.spigot().setDamage(damage);
									arrow.setShooter((Player) sender);
									Bukkit.getScheduler().runTaskLater(TestingCenter.getInstance(), new Runnable()
									{
										@Override
										public void run()
										{
											arrow.remove();
										}	
									}, 500);
								}	
							}, 0);
					}
				}
				Bukkit.getScheduler().runTaskLater(TestingCenter.getInstance(), new Runnable()
				{
					@Override
					public void run()
					{
						for(int i = 0; i < lightning; i++)
							loc.getWorld().strikeLightning(loc);
						if(power >= 0)
							loc.getWorld().createExplosion(loc.getX(), loc.getY(), loc.getZ(), power, false, false);
					}	
				}, 0);
			}
		}.start();
		return true;
	}

	@Override
	public List<String> onSubTabComplete(CommandSender arg0, String arg1, String[] arg2)
	{
		return null;
	}
}
