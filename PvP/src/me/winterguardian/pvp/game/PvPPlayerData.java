package me.winterguardian.pvp.game;

import me.winterguardian.core.game.PlayerData;
import me.winterguardian.pvp.GameStuff;
import me.winterguardian.pvp.PvPMessage;
import me.winterguardian.pvp.PvPPlugin;
import me.winterguardian.pvp.TeamColor;
import me.winterguardian.pvp.game.solo.SoloGame;
import me.winterguardian.pvp.game.team.TeamGame;
import me.winterguardian.pvp.stats.Bonus;
import me.winterguardian.pvp.stats.PvPStats;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Horse;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class PvPPlayerData extends PlayerData
{
	private PvPMatch game;

	private String name;
	
	private boolean pvpActive;

	private int kills, deaths, assists;
	
	private long lastKill;
	private int fastKillstreak;
	
	private int killstreak, bestKillstreak;

	private int flagsCaptured, zonesCaptured;
	
	private UUID lastDamager;
	private UUID secondDamager;
	private UUID lastPoisonDamager;
	private UUID lastWhiterDamager;
	private UUID lastFireDamager;
	
	private LivingEntity friend;
	
	private GameStuff stuff;
	
	private Collection<PotionEffect> effects;
	
	private TeamColor team;
	
	private List<Bonus> bonus;

	private long playTime, begin;
	
	public PvPPlayerData(Player p, PvPMatch game)
	{
		super(p);
		this.name = p.getName();
		this.game = game;
		this.setPvpActive(true);
		this.setKills(0);
		this.setDeaths(0);
		this.setAssists(0);
		this.setLastKill(0);
		this.setFastKillstreak(0);
		this.setKillstreak(0);
		this.lastDamager = null;
		this.secondDamager = null;
		this.setLastPoisonDamager(null);
		this.setLastWhiterDamager(null);
		this.setFriend(null);
		this.setTeam(TeamColor.NONE);
		this.setStuff(new GameStuff(p.getName(), p.getInventory()));
		this.setEffects(new ArrayList<PotionEffect>());
		this.bonus = new ArrayList<>();

		this.flagsCaptured = 0;
		this.zonesCaptured = 0;
		this.bestKillstreak = 0;
		this.playTime = 0;
		this.begin = System.currentTimeMillis();
	}

	public String getPvPName()
	{
		return (getTeam() == TeamColor.NONE ? "" : getTeam().getBoardPrefix()) + PvPStats.getPvPName(getLevel(), this.name);
	}

	public boolean isPvpActive()
	{
		return pvpActive;
	}

	public void setPvpActive(boolean pvpActive)
	{
		this.pvpActive = pvpActive;
	}

	public int getKills()
	{
		return kills;
	}

	public void setKills(int kills)
	{
		this.kills = kills;
	}

	public int getDeaths()
	{
		return deaths;
	}

	public void setDeaths(int deaths)
	{
		this.deaths = deaths;
	}

	public int getAssists()
	{
		return assists;
	}

	public void setAssists(int assists)
	{
		this.assists = assists;
	}

	public long getLastKill()
	{
		return lastKill;
	}

	public void setLastKill(long lastKill)
	{
		this.lastKill = lastKill;
	}

	public int getFastKillstreak()
	{
		return fastKillstreak;
	}

	public void setFastKillstreak(int fastKillstreak)
	{
		this.fastKillstreak = fastKillstreak;
	}

	public int getKillstreak()
	{
		return killstreak;
	}

	public void setKillstreak(int killstreak)
	{
		this.killstreak = killstreak;
		if(killstreak > this.bestKillstreak)
			this.bestKillstreak = killstreak;
	}

	public int getBestKillstreak()
	{
		return bestKillstreak;
	}

	public Player getLastDamager()
	{
		if(this.lastDamager == null)
			return null;

		return Bukkit.getPlayer(this.lastDamager);
	}


	public Player getSecondDamager()
	{
		if(this.secondDamager == null)
			return null;

		return Bukkit.getPlayer(this.secondDamager);
	}

	public Player getLastPoisonDamager()
	{
		if(this.lastPoisonDamager == null)
			return null;

		return Bukkit.getPlayer(this.lastPoisonDamager);
	}

	public void setLastPoisonDamager(Player lastPoisonDamager)
	{
		if(lastPoisonDamager == null)
		{
			this.lastPoisonDamager = null;
		}
		else
		{
			this.lastPoisonDamager = lastPoisonDamager.getUniqueId();
		}
	}

	public Player getLastWhiterDamager()
	{
		if(this.lastWhiterDamager == null)
			return null;

		return Bukkit.getPlayer(this.lastWhiterDamager);
	}

	public void setLastWhiterDamager(Player lastWhiterDamager)
	{
		if(lastWhiterDamager == null)
			this.lastWhiterDamager = null;
		else
			this.lastWhiterDamager = lastWhiterDamager.getUniqueId();
	}

	public Player getLastFireDamager()
	{
		if(this.lastFireDamager == null)
			return null;

		return Bukkit.getPlayer(this.lastFireDamager);
	}

	public void setLastFireDamager(Player lastFireDamager)
	{
		if(lastFireDamager == null)
			this.lastFireDamager = null;
		else
			this.lastFireDamager = lastFireDamager.getUniqueId();
	}

	public LivingEntity getFriend()
	{
		return friend;
	}

	public void setFriend(LivingEntity friend)
	{
		this.friend = friend;
	}
	
	public GameStuff getStuff()
	{
		return stuff;
	}

	public void setStuff(GameStuff stuff)
	{
		this.stuff = stuff;
	}
	
	public Collection<PotionEffect> getEffects()
	{
		return effects;
	}

	public void setEffects(Collection<PotionEffect> effects)
	{
		this.effects = effects;
	}
	
	public int getLevel()
	{
		return PvPStats.get(getUUID()).getDisplayLevel();
	}

	public int getFlagsCaptured()
	{
		return flagsCaptured;
	}

	public void setFlagsCaptured(int flagsCaptured)
	{
		this.flagsCaptured = flagsCaptured;
	}

	public int getZonesCaptured()
	{
		return zonesCaptured;
	}

	public void setZonesCaptured(int zonesCaptured)
	{
		this.zonesCaptured = zonesCaptured;
	}

	public long getPlayTime()
	{
		return playTime;
	}

	public void damage(Player p)
	{
		if(p == null)
		{
			return;
		}


		if(this.lastDamager == p.getUniqueId())
			return;
		
		if(this.lastDamager == null)
			this.lastDamager = p.getUniqueId();
		else
		{
			this.secondDamager = this.lastDamager;
			this.lastDamager = p.getUniqueId();
		}
		
	}
	
	public void damagePoison()
	{
		damage(getLastPoisonDamager());
	}
	
	public void damageWhiter()
	{
		damage(getLastWhiterDamager());
	}

	public void damageFire()
	{
		damage(getLastFireDamager());
	}

	public void noDamage()
	{
		this.lastDamager = null;
		this.secondDamager = null;
		this.lastPoisonDamager = null;
		this.lastWhiterDamager = null;
		this.lastFireDamager = null;
	}
	
	public TeamColor getTeam()
	{
		return team;
	}

	public void setTeam(TeamColor team)
	{
		this.team = team;
	}

	public void friendDeath()
	{
		if(this.friend.getKiller() != null && game.getGame().contains(this.friend.getKiller()) && this.friend.getKiller() != getPlayer())
			game.getPlayerData(this.friend.getKiller()).addBonus(Bonus.getMobKillBonus(this.friend));
		
		
		PvPMessage.GAME_FRIENDDEATH.sayIfOnline(getPlayer(), "<friend>", this.friend instanceof Wolf ? "loup" : this.friend instanceof Horse ? "cheval" : "mob");
		this.friend = null;
	}
	
	public void death()
	{
		this.deaths++;

		if(getLastDamager() != null)
		{
			PvPPlayerData killerPlayerData = game.getPlayerData(getLastDamager());

			if(killerPlayerData != null)
			{
				String killerName = killerPlayerData.getPlayer().getName();

				if(getPlayer().isOnline() && PvPPlugin.getGame().getConfig().useDisplaynames())
					killerName = killerPlayerData.getPlayer().getDisplayName();

				boolean firstKill = true;

				for(PlayerData data : game.getPlayerDatas()) //dans tout les datas
					if(((PvPPlayerData)data).getKills() != 0) //si un seul a fait un kill
					{
						firstKill = false; //dans ce cas celui ci n'est pas le premier kill
						break; //et si c'est pas le premier, on a finit de chercher
					}

				if(firstKill)
					killerPlayerData.addBonus(Bonus.FIRSTKILL);

				killerPlayerData.setKills(killerPlayerData.getKills() + 1); //on ajoute précisément ici pour ne pas gêner le calcul du first kill


				if(killerPlayerData.getPlayer().getInventory().getHelmet() != null && killerPlayerData.getPlayer().getInventory().getHelmet().getType() == Material.PUMPKIN)
					killerPlayerData.addBonus(Bonus.PUMPKIN_HEAD);

				killerPlayerData.setKillstreak(killerPlayerData.getKillstreak() + 1);

				if(getKillstreak() > 7)
					killerPlayerData.addBonus(Bonus.KILLSTREAK_BREAKER);

				switch(killerPlayerData.getKillstreak())
				{
					case 3:
						killerPlayerData.addBonus(Bonus.KILLSTREAK_3);
						PvPMessage.GAME_KILLSTREAK.sayPlayers("<player>", PvPStats.getPvPName(killerPlayerData.getLevel(), killerName), "#", "" + killerPlayerData.getKillstreak(), "<color>", "§a");
						break;

					case 5:
						killerPlayerData.addBonus(Bonus.KILLSTREAK_5);
						PvPMessage.GAME_KILLSTREAK.sayPlayers("<player>", PvPStats.getPvPName(killerPlayerData.getLevel(), killerName), "#", "" + killerPlayerData.getKillstreak(), "<color>", "§e");
						break;

					case 8:
						killerPlayerData.addBonus(Bonus.KILLSTREAK_8);
						PvPMessage.GAME_KILLSTREAK.sayAll("<player>", PvPStats.getPvPName(killerPlayerData.getLevel(), killerName), "#", "" + killerPlayerData.getKillstreak(), "<color>", "§e");
						break;

					case 10:
						killerPlayerData.addBonus(Bonus.KILLSTREAK_10);
						PvPMessage.GAME_KILLSTREAK.sayAll("<player>", PvPStats.getPvPName(killerPlayerData.getLevel(), killerName), "#", "" + killerPlayerData.getKillstreak(), "<color>", "§6");
						break;

					case 15:
						killerPlayerData.addBonus(Bonus.KILLSTREAK_15);
						PvPMessage.GAME_KILLSTREAK.sayAll("<player>", PvPStats.getPvPName(killerPlayerData.getLevel(), killerName), "#", "" + killerPlayerData.getKillstreak(), "<color>", "§6");
						break;

					case 20:
						killerPlayerData.addBonus(Bonus.KILLSTREAK_20);
						PvPMessage.GAME_KILLSTREAK.sayAll("<player>", PvPStats.getPvPName(killerPlayerData.getLevel(), killerName), "#", "" + killerPlayerData.getKillstreak(), "<color>", "§c");
						break;

					case 30:
						killerPlayerData.addBonus(Bonus.KILLSTREAK_30);
						PvPMessage.GAME_KILLSTREAK.sayAll("<player>", PvPStats.getPvPName(killerPlayerData.getLevel(), killerName), "#", "" + killerPlayerData.getKillstreak(), "<color>", "§c");
						break;

					case 50:
						killerPlayerData.addBonus(Bonus.KILLSTREAK_50);
						PvPMessage.GAME_KILLSTREAK.sayAll("<player>", PvPStats.getPvPName(killerPlayerData.getLevel(), killerName), "#", "" + killerPlayerData.getKillstreak(), "<color>", "§4");
						break;

					case 100:
						killerPlayerData.addBonus(Bonus.KILLSTREAK_100);
						PvPMessage.GAME_KILLSTREAK.sayAll("<player>", PvPStats.getPvPName(killerPlayerData.getLevel(), killerName), "#", "" + killerPlayerData.getKillstreak(), "<color>", "§4§l");
						break;
				}


				killerPlayerData.setFastKillstreak(killerPlayerData.getFastKillstreak() + 1);
				if(System.currentTimeMillis() - killerPlayerData.getLastKill() <= 2000) //si la différence entre son kill et son précédent est plus petite ou égale à 2s
				{
					if(killerPlayerData.getFastKillstreak() == 2) //si il en a fait 2
						killerPlayerData.addBonus(Bonus.DOUBLEKILL);
					else if(killerPlayerData.getFastKillstreak() == 3) //etc.
						killerPlayerData.addBonus(Bonus.TRIPLEKILL);
					else if(killerPlayerData.getFastKillstreak() >= 4)
						killerPlayerData.addBonus(Bonus.MULTIKILL);
				}else if(killerPlayerData.getFastKillstreak() != 1) //si c'est son premier depuis qu'il a fail sa "fast série", il n'a pas à être comparé
					killerPlayerData.setFastKillstreak(0);

				killerPlayerData.setLastKill(System.currentTimeMillis()); //évidemment

				if(this.getSecondDamager() != null)
				{
					PvPPlayerData assistPlayerData = game.getPlayerData(getSecondDamager());
					assistPlayerData.setAssists(assistPlayerData.getAssists() + 1);
				}

				game.announceKill(killerPlayerData, this);
			}
			else
				game.announceSuicide(this);
		}
		else
			game.announceSuicide(this);
		
		this.lastDamager = null;
		this.secondDamager = null;
		this.lastPoisonDamager = null;
		this.lastWhiterDamager = null;
		this.killstreak = 0;
		this.fastKillstreak = 0;
		this.lastKill = 0;
		this.pvpActive = false;	
		
		this.stuff.setContent(this.getPlayer().getInventory());
		this.effects = this.getPlayer().getActivePotionEffects();

		game.updateBoard();

	}

	public void respawn()
	{
		this.pvpActive = true;
		this.stuff.give(getPlayer());
		this.getPlayer().addPotionEffects(this.effects);
		if(this.friend != null && this.friend.isValid() && !this.friend.isDead())
			this.friend.teleport(this.getPlayer());
		else if(this.friend != null)
		{
			this.friend.remove();
			this.friend = null;
		}
	}

	public void captureFlag()
	{
		this.flagsCaptured++;
	}

	public void captureZone()
	{
		this.zonesCaptured++;
	}

	public void end(PvPStats stats)
	{
		if(!(game instanceof TeamGame) && !(game instanceof SoloGame))
			return;

		this.playTime += System.currentTimeMillis() - this.begin;

		stats.gameSummary(game.getOutcome(getPlayer()),
				getKills(),
				getDeaths(),
				getAssists(),
				getPlayTime(),
				getFlagsCaptured(),
				getZonesCaptured(),
				getBestKillstreak(),
				this.bonus);
	}
	
	public void addBonus(Bonus bonus)
	{
		this.bonus.add(bonus);
		if(getPlayer().isOnline())
			bonus.announce(getPlayer());
	}

	@Override
	public void onJoin()
	{
		start();
		getStuff().give(this.getPlayer());
	}
	
	@Override
	public void onLeave()
	{
		if(!getPlayer().isDead())
		{
			this.stuff.setContent(getPlayer().getInventory());
			this.effects = this.getPlayer().getActivePotionEffects();
			if(this.getLastDamager() != null)
			{
				death();
				this.setPvpActive(true);
			}
		}
		else
			this.pvpActive = true;

		this.playTime += System.currentTimeMillis() - this.begin;

		if(this.friend != null)
		{
			this.friend.remove();
			this.friend = null;
		}
	}

	public void start()
	{
		this.begin = System.currentTimeMillis();
	}

	public boolean isPlaying()
	{
		return this.game.getGame().contains(getPlayer()) && getPlayer() != null;
	}
}
