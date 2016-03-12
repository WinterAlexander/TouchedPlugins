package me.winterguardian.test.components;

import me.winterguardian.core.message.ErrorMessage;
import me.winterguardian.core.particle.ParticleType;
import me.winterguardian.core.particle.ParticleUtil;
import me.winterguardian.test.TestComponent;
import me.winterguardian.test.TestingCenter;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class AutoProjectile extends TestComponent
{
	
	public AutoProjectile()
	{
		super("auto-proj", TestingCenter.TEST_LEVEL3, "Nope");
	}

	@Override
	public boolean onSubCommand(CommandSender sender, String label, String[] args)
	{
		if(args.length <= 0)
		{
			ErrorMessage.COMMAND_INVALID_ARGUMENT.say(sender);
			return false;
		}
		
		if(!(sender instanceof Player))
		{
			ErrorMessage.COMMAND_INVALID_SENDER.say(sender);
			return false;
		}
		
		Player p = (Player) sender;
		Player target = Bukkit.getPlayer(args[0]);
		if(target == null)
		{
			ErrorMessage.COMMAND_INVALID_PLAYER.say(sender);
			return false;
		}
		Bukkit.getScheduler().scheduleSyncRepeatingTask(TestingCenter.getInstance(), new AutoProj(p, target), 0, 1);
		return true;
	}
	
	private class AutoProj implements Runnable
	{
		private LivingEntity sender;
		private Location projLocation;
		private LivingEntity receiver;
		private boolean finished;
		
		public AutoProj(LivingEntity sender, LivingEntity receiver)
		{
			this.sender = sender;
			this.projLocation = sender.getEyeLocation();
			this.receiver = receiver;
			this.finished = false;
		}
		
		@Override
		public void run()
		{
			if(finished)
				return;
			
			if(this.projLocation == null || this.receiver.getLocation() == null)
				return;
			
			if(this.projLocation.getWorld() != this.receiver.getWorld())
				return;
				
			if(this.projLocation.distance(this.receiver.getEyeLocation()) < 1)
			{
				ParticleUtil.playSimpleParticles(this.projLocation, ParticleType.SMOKE_LARGE, 1.5f, 1.5f, 1.5f, 0f, 100);
				for(Player p : projLocation.getWorld().getPlayers())
					p.playSound(projLocation, Sound.ANVIL_LAND, 1, 1);
				if(this.receiver instanceof Player)
					if(((Player) this.receiver).getGameMode() == GameMode.CREATIVE)
						((Player) this.receiver).setGameMode(GameMode.SURVIVAL);

				this.receiver.damage(5, this.sender);
				finished = true;
				return;
			}
			this.projLocation = this.projLocation.add(new Vector(
					(this.receiver.getEyeLocation().getX() - this.projLocation.getX()) / (this.projLocation.distance(this.receiver.getEyeLocation()) * 2), 
					((this.receiver.getEyeLocation().getY() - this.projLocation.getY()) / (this.projLocation.distance(this.receiver.getEyeLocation())) + Math.sqrt(Math.pow((this.receiver.getEyeLocation().getX() - this.projLocation.getX()), 2) + Math.pow((this.receiver.getEyeLocation().getZ() - this.projLocation.getZ()), 2)) / 64) / 2, 
					(this.receiver.getEyeLocation().getZ() - this.projLocation.getZ()) / (this.projLocation.distance(this.receiver.getEyeLocation()) * 2)));
			ParticleUtil.playSimpleParticles(projLocation, ParticleType.SPELL_INSTANT, 0, 0, 0, 0, 1);
			this.projLocation = this.projLocation.add(new Vector(
					(this.receiver.getEyeLocation().getX() - this.projLocation.getX()) / (this.projLocation.distance(this.receiver.getEyeLocation()) * 2), 
					((this.receiver.getEyeLocation().getY() - this.projLocation.getY()) / (this.projLocation.distance(this.receiver.getEyeLocation())) + Math.sqrt(Math.pow((this.receiver.getEyeLocation().getX() - this.projLocation.getX()), 2) + Math.pow((this.receiver.getEyeLocation().getZ() - this.projLocation.getZ()), 2)) / 64) / 2, 
					(this.receiver.getEyeLocation().getZ() - this.projLocation.getZ()) / (this.projLocation.distance(this.receiver.getEyeLocation()) * 2)));
			ParticleUtil.playSimpleParticles(projLocation, ParticleType.SPELL_INSTANT, 0, 0, 0, 0, 1);
		}
		
	}
	
}
