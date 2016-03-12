package me.winterguardian.duel.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.winterguardian.core.Core;
import me.winterguardian.core.scoreboard.ScoreboardUtil;
import me.winterguardian.core.util.AchievementUtil;
import me.winterguardian.core.util.FireworkUtil;
import me.winterguardian.core.util.PlayerUtil;
import me.winterguardian.core.util.TabUtil;
import me.winterguardian.duel.Duel;
import me.winterguardian.duel.DuelMessage;
import me.winterguardian.duel.DuelStats;

import org.bukkit.Achievement;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class DuelGame
{
	private Player player1;
	private Player player2;
	private List<Player> waiting;
	
	private List<Player> toTeleport;
	
	private int taskId, expbarTaskId;
	private boolean pvp/*, finish*/;
	
	public DuelGame()
	{
		this.player1 = null;
		this.player2 = null;
		this.waiting = new ArrayList<Player>();
		this.toTeleport = new ArrayList<Player>();
		
		this.taskId = -1;
		this.expbarTaskId = -1;
		this.pvp = false;
		//this.finish = false;
	}
	
	@SuppressWarnings("deprecation")
	public boolean stop(boolean getError)
	{
		try
		{
			this.pvp = false;
			if(this.taskId != -1)
				Bukkit.getScheduler().cancelTask(this.taskId);
			this.taskId = -1;
			
			if(this.expbarTaskId != -1)
				Bukkit.getScheduler().cancelTask(this.expbarTaskId);
			this.expbarTaskId = -1;
			
			if(Duel.getInstance().getSettings().getLobby() == null)
				return false;
			if(this.player1 != null)
				if(this.player1.isOnline())
				{
					PlayerUtil.heal(this.player1);
					PlayerUtil.clearBoard(this.player1);
					PlayerUtil.prepare(this.player1);
					PlayerUtil.clearInventory(this.player1);
					TabUtil.resetTab(player1);
					this.player1.teleport(Duel.getInstance().getSettings().getLobby());
					this.player1 = null;
				}
			
			if(this.player2 != null)
				if(this.player2.isOnline())
				{
					PlayerUtil.heal(this.player2);
					PlayerUtil.prepare(this.player2);
					PlayerUtil.clearInventory(this.player2);
					TabUtil.resetTab(player2);
					this.player2.teleport(Duel.getInstance().getSettings().getLobby());
					this.player2 = null;
				}
			
			for(Player p : this.waiting)
				if(p != null)
					if(p.isOnline())
					{
						PlayerUtil.heal(p);
						PlayerUtil.prepare(p);
						PlayerUtil.clearInventory(p);
						TabUtil.sendInfos(p, TabUtil.DEFAULT_HEADER, TabUtil.DEFAULT_FOOTER);
						p.teleport(Duel.getInstance().getSettings().getLobby());
					}
			
			this.waiting = new ArrayList<Player>();
			this.updateBoard();
			return true;
		}
		catch(Exception e)
		{
			if(getError)
				throw e;
			return false;
		}
	}
	
	public void updateBoard()
	{
		ScoreboardUtil.unrankedSidebarDisplay(Duel.getInstance().getPlayersInRegion(), getScoreboardElements());
		
		for(Player player : Duel.getInstance().getPlayersInRegion())
			TabUtil.sendInfos(player, Duel.TAB_HEADER, Duel.getInstance().getTabFooter());
	}
	
	public void giveBoardAndTab(Player p)
	{
		ScoreboardUtil.unrankedSidebarDisplay(Arrays.asList(p), getScoreboardElements());
		TabUtil.sendInfos(p, Duel.TAB_HEADER, Duel.getInstance().getTabFooter());
	}
	
	public String[] getScoreboardElements()
	{
		String[] elements = new String[16];
		
		String status = "";
		//if(this.finish)
		//	status = "§9Bravo !";
		/*else */if(this.pvp)
			status = "§cCombattez !";
		else if(this.player2 != null)
			status = "§eÀ vos marques";
		else
			status = "§aEn attente";
		
		elements[0] = "§6§lDuel §f- " + status;
		elements[1] = " ";
		
		if(this.getPlayer2() != null)
		{
			elements[2] = "§c" + this.player1.getName();
			elements[3] = "§evs";
			elements[4] = "§9" + this.getPlayer2().getName();
			elements[5] = "  ";
			
			if(this.waiting.size() <= 0)
			{
				elements[6] = "§fFaites §e§l/duel";
				elements[7] = "§fpour rejoindre";
				elements[8] = "   ";
			}
			else if(this.waiting.size() < 9)
			{
				elements[6] = "§6File d'attente";
				for(int i = 0; i < this.waiting.size(); i++)
				{
					elements[7 + i] = "§e" + (i + 1) + ": §f" + this.waiting.get(i).getName();
				}
			}
			else
			{
				elements[6] = "§6File d'attente";
				int i;
				for(i = 0; i < 7; i++)
					elements[7 + i] = "§e" + (i + 1) + ": §f" + this.waiting.get(i).getName();

				elements[15] = "§fet " + (this.waiting.size() - i) + " autres...";
			}
		}
		else if(this.getPlayer1() != null)
		{
			elements[2] = "§c" + this.player1.getName();
			elements[3] = "§eSeul en jeu";
			elements[4] = "    ";
			elements[5] = "§fFaites §e§l/duel";
			elements[6] = "§fpour rejoindre";
			elements[7] = "     ";
		}
		else
		{
			elements[2] = "§ePersonne en jeu";
			elements[3] = "    ";
			elements[4] = "§fFaites §e§l/duel";
			elements[5] = "§fpour rejoindre";
			elements[6] = "     ";
		}
		
		return elements;
	}
	
	public void join(Player p)
	{
		if(p.getVehicle() != null)
			p.getVehicle().remove();
		
		if(this.player1 == null)
		{
			this.player1 = p;
			p.sendMessage(DuelMessage.DUEL_JOIN.toString());
			this.standBy();
		}
		else if(this.player2 == null)
		{
			this.player2 = p;
			p.sendMessage(DuelMessage.DUEL_JOIN.toString());
			this.start();
		}
		else
		{
			this.waiting.add(p);
			p.teleport(Duel.getInstance().getSettings().getLobby());
			PlayerUtil.heal(p);
			PlayerUtil.clearBoard(p);
			PlayerUtil.prepare(p);
			PlayerUtil.clearInventory(p);
			p.sendMessage(DuelMessage.DUEL_WAIT.toString());
		}
		this.updateBoard();
	}

	public void leave(Player p)
	{
		boolean wasfirst = false;
		if(this.player1 == p)
		{
			this.player1 = this.player2;
			wasfirst = true;
		}
		
		if(wasfirst || this.player2 == p)
		{
			this.pvp = false;
			p.sendMessage(DuelMessage.DUEL_QUIT.toString());
			PlayerUtil.heal(p);
			PlayerUtil.prepare(p);
			PlayerUtil.clearInventory(p);
			p.teleport(Duel.getInstance().getSettings().getLobby());
				
			if(this.player1 != null)
			{
				for(Player p1 : Bukkit.getOnlinePlayers())
					p1.sendMessage(DuelMessage.getPrefix() + " §" + (wasfirst ? 'c' : '9') + p.getName() + " §ea abandonné un duel face à §" + (wasfirst ? '9' : 'c') + this.player1.getName());
				
				DuelStats faggotStats = new DuelStats(Core.getUserDatasManager().getUserData(p));
				faggotStats.addSurrenders(1);
			}
			Bukkit.getScheduler().cancelTask(this.taskId);
			Bukkit.getScheduler().cancelTask(this.expbarTaskId);
			if(this.waiting.size() > 0)
			{
				this.player2 = this.waiting.get(0);
				this.waiting.remove(0);
				this.player2.sendMessage(DuelMessage.DUEL_YOURTURN.toString());
				this.start();
			}
			else
			{
				this.player2 = null;
				if(this.player1 != null)
				{
					this.standBy();
				}
			}
		}
		else if(this.waiting.contains(p))
		{
			this.waiting.remove(p);
			p.sendMessage(DuelMessage.DUEL_QUITWAIT.toString());
			PlayerUtil.heal(p);
			PlayerUtil.prepare(p);
			PlayerUtil.clearInventory(p);
			p.teleport(Duel.getInstance().getSettings().getLobby());
		}
		this.updateBoard();
	}
	
	public boolean isOnGame(Player p)
	{
		return this.player1 == p || this.player2 == p || this.waiting.contains(p);
	}

	public void start()
	{
		DuelMessage.DUEL_WILLSTART.sayPlayers();
		this.player1.teleport(Duel.getInstance().getSettings().getSpawn1());
		PlayerUtil.heal(this.player1);
		PlayerUtil.prepare(this.player1);
		PlayerUtil.clearInventory(this.player1);
		Duel.getInstance().getSettings().giveStuff(this.player1);
		this.player1.setExp(1.0f);
		
		this.player2.teleport(Duel.getInstance().getSettings().getSpawn2());
		PlayerUtil.heal(this.player2);
		PlayerUtil.prepare(this.player2);
		PlayerUtil.clearInventory(this.player2);
		Duel.getInstance().getSettings().giveStuff(this.player2);
		this.player2.setExp(1.0f);
		
		this.pvp = false;
		this.taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(Duel.getInstance(), new DuelGameTask(), 1, 20);
		this.expbarTaskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(Duel.getInstance(), new Runnable()
		{
			@Override
			public void run()
			{
				DuelGame.this.player1.setExp((float) (DuelGame.this.getPlayer2().getHealth() / DuelGame.this.getPlayer2().getMaxHealth()));
				DuelGame.this.player2.setExp((float) (DuelGame.this.getPlayer1().getHealth() / DuelGame.this.getPlayer1().getMaxHealth()));
			}
		}, 0, 1);
		this.updateBoard();
	}	
	
	public void win(Player winner)
	{
		char winnerColor;
		char looserColor;
		Player looser;
		if(winner == this.player1)
		{
			winnerColor = 'c';
			looserColor = '9';
			looser = this.player2;
			FireworkUtil.generateRandom(Duel.getInstance().getSettings().getSpawn1());
		}
		else if(winner == this.player2)
		{
			winnerColor = '9';
			looserColor = 'c';
			looser = this.player1;
			FireworkUtil.generateRandom(Duel.getInstance().getSettings().getSpawn2());
		}
		else return;
		
		this.pvp = false;
		if(winner.getHealth() != winner.getMaxHealth())
			for(Player p : Duel.getInstance().getPlayersInRegion())
				p.sendMessage(DuelMessage.getPrefix() + " §" + winnerColor + winner.getName() + " §ea battu §" + looserColor + looser.getName() + "§e en duel!");
		else
			for(Player p : Bukkit.getOnlinePlayers())
				p.sendMessage(DuelMessage.getPrefix() + " §" + winnerColor + winner.getName() + " §ea §lperfect §" + looserColor + looser.getName() + "§e en duel!");
		this.toTeleport.add(looser);
		
		DuelStats winnerStats = new DuelStats(Core.getUserDatasManager().getUserData(winner)), looserStats = new DuelStats(Core.getUserDatasManager().getUserData(looser));
		winnerStats.addVictories(1);
		winnerStats.addGamesPlayed(1);
		int points = looserStats.getPoints() / 1000;
		
		points = points > 100 ? 100 : points;
		points = points < 10 ? 10 : points;
		winnerStats.addPoints(points);
		winner.sendMessage("§aPoints +" + points);
		
		looserStats.addGamesPlayed(1);
		
		AchievementUtil.flashShow(winner, Achievement.BUILD_SWORD, Duel.getInstance());
		
		TabUtil.resetTab(this.player1);
		TabUtil.resetTab(this.player2);
		
		winner.teleport(Duel.getInstance().getSettings().getLobby());
		PlayerUtil.heal(winner);
		PlayerUtil.clearBoard(winner);
		PlayerUtil.prepare(winner);
		PlayerUtil.clearInventory(winner);
		Bukkit.getScheduler().cancelTask(taskId);
		taskId = -1;
		
		Bukkit.getScheduler().cancelTask(expbarTaskId);
		expbarTaskId = -1;
		
		if(waiting.size() > 0)
		{
			player1 = waiting.get(0);
			waiting.remove(0);
			DuelMessage.DUEL_YOURTURN.say(player1);
			
			if(waiting.size() > 0)
			{
				player2 = waiting.get(0);
				waiting.remove(0);
				DuelMessage.DUEL_YOURTURN.say(player2);
				start();
			}
			else
			{
				player2 = null;
				standBy();
			}
		}
		else
		{
			player1 = null;
			player2 = null;
		}
		updateBoard();
	}
	
	public void standBy()
	{
		this.player1.sendMessage(DuelMessage.DUEL_STANDBY.toString());
		this.player1.teleport(Duel.getInstance().getSettings().getLobby());
		PlayerUtil.heal(this.player1);
		PlayerUtil.clearBoard(this.player1);
		PlayerUtil.prepare(this.player1);
		PlayerUtil.clearInventory(this.player1);
		this.updateBoard();
	}
	
	public Player getPlayer1()
	{
		return player1;
	}
	
	public void setPlayer1(Player player1)
	{
		this.player1 = player1;
	}
	
	public Player getPlayer2()
	{
		return player2;
	}
	
	public void setPlayer2(Player player2)
	{
		this.player2 = player2;
	}
	
	public List<Player> getWaiting()
	{
		return waiting;
	}
	
	public void setWaiting(List<Player> waiting)
	{
		this.waiting = waiting;
	}
	
	public List<Player> getToTeleport()
	{
		return toTeleport;
	}
	
	public void setToTeleport(List<Player> toTeleport)
	{
		this.toTeleport = toTeleport;
	}
	
	public int getTaskId()
	{
		return taskId;
	}
	
	public boolean isPvp()
	{
		return pvp;
	}
	
	public void setPvp(boolean pvp)
	{
		this.pvp = pvp;
	}
	
	public boolean canBattle(Player p)
	{
		return (this.player1 == p || this.player2 == p) && this.pvp;
	}
}