package ice;


import me.winterguardian.core.game.state.State;
import me.winterguardian.core.util.PlayerUtil;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class WaitingSession implements State
{
	private int taskId;
	
	public void join(Player p)
	{
		p.teleport(IceRun.getSettings().getLobby().getLocation());
		PlayerUtil.clearInventory(p);
		PlayerUtil.clearBoard(p);
		PlayerUtil.heal(p);
		PlayerUtil.prepare(p);
		IceRun.getSettings().giveStuff(p);
		Standby.giveScoreboard(p);
	}

	public void leave(Player p)
	{
		PlayerUtil.clearInventory(p);
		PlayerUtil.clearBoard(p);
		PlayerUtil.heal(p);
		PlayerUtil.prepare(p);
		p.teleport(IceRun.getSettings().getExit().getLocation());
	

		if (IceRun.players.size() < 2)
		{
			end();
			IceRun.status = new Standby();
			IceRun.status.start();
		}
	}

	public void start()
	{
		IceRunMessage.WAITINGSESSION_BEGIN.say(IceRun.players);
		for(Player player : IceRun.players)
		{
			IceRun.getSettings().giveStuff(player);
			Standby.giveScoreboard(player);
		}
		
		this.taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(IceRun.getPlugin(), new WaitingSessionTask(), 1L, 20L);
	}

	private class WaitingSessionTask implements Runnable
	{
		private int timer;

		public WaitingSessionTask()
		{
			this.timer = 30;
		}

		public void run()
		{
			for(Player p : IceRun.players)
			{
				p.setLevel(timer);
			}

			if(this.timer <= 5 && this.timer > 0)
				for(Player p : IceRun.players)
					p.getWorld().playSound(p.getLocation(), Sound.NOTE_BASS, 10.0F, 1.0F);
			
			switch (this.timer)
			{
			case 0:
				for(Player p : IceRun.players)
					p.getWorld().playSound(p.getLocation(), Sound.NOTE_PLING, 10.0F, 1.0F);//autre son

				WaitingSession.this.end();
				IceRun.status = new IceRunGame();
				IceRun.status.start();
				break;

			case 1:
				IceRunMessage.ONESECONDELEFT.say(IceRun.players);
				break;

			default:
				IceRunMessage.SECONDELEFT.say(IceRun.players, "<timer>", "" + timer);
				break;
			}

			if (this.timer < 0)
				Bukkit.getLogger().warning(IceRunMessage.TIMERERROR.toString());
			this.timer -= 1;
		}
	}

	@Override
	public void end() 
	{
		Bukkit.getScheduler().cancelTask(this.taskId);
		for(Player p : IceRun.players)
		{
			PlayerUtil.prepare(p);
		}
	}

	@Override
	public String getStatus()
	{
		return "§eLa partie va bientôt commencer.";
	}
}