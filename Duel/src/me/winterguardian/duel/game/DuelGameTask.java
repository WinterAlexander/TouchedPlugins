package me.winterguardian.duel.game;

import me.winterguardian.core.util.ActionBarUtil;
import me.winterguardian.duel.Duel;

import org.bukkit.entity.Player;

public class DuelGameTask implements Runnable
{
	private int timer;
	
	public DuelGameTask()
	{
		this.timer = 130;
	}
	
	@Override
	public void run()
	{
		if(timer <= 130 && timer > 120)
		{
			for(Player p : Duel.getInstance().getPlayers())
					ActionBarUtil.sendActionMessage(p, "§7Le duel commence dans §e" + (timer - 120) + " §7!");
			
			
			if(Duel.getInstance().getGame().getPlayer1().getLocation().distance(Duel.getInstance().getSettings().getSpawn1()) > 1)
				Duel.getInstance().getGame().getPlayer1().teleport(Duel.getInstance().getSettings().getSpawn1());
			
			if(Duel.getInstance().getGame().getPlayer2().getLocation().distance(Duel.getInstance().getSettings().getSpawn2()) > 1)
				Duel.getInstance().getGame().getPlayer2().teleport(Duel.getInstance().getSettings().getSpawn2());
		}
			
		if(this.timer == 120)
		{
			for(Player p : Duel.getInstance().getPlayers())
				ActionBarUtil.sendActionMessage(p, "§e§lLe duel commence !");
			Duel.getInstance().getGame().setPvp(true);
			Duel.getInstance().getGame().updateBoard();
		}
		
		/*if(timer <= 0) À REFAIRE, BUG
		{
			for(Player p : Duel.component.getPlayers())
				p.sendMessage("§6§lDuel §f§l> §eLe duel s'est terminé en égalité.");
			Bukkit.getScheduler().cancelTask(Duel.component.taskId);
		}*/
		timer--;
	}
}