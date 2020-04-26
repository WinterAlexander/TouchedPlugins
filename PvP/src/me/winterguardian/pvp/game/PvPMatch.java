package me.winterguardian.pvp.game;

import me.winterguardian.core.Core;
import me.winterguardian.core.game.state.State;
import me.winterguardian.core.json.JsonUtil;
import me.winterguardian.core.message.Message;
import me.winterguardian.core.util.PlayerUtil;
import me.winterguardian.core.util.SoundEffect;
import me.winterguardian.core.util.TabUtil;
import me.winterguardian.pvp.GameStuff;
import me.winterguardian.pvp.PvP;
import me.winterguardian.pvp.PvPArena;
import me.winterguardian.pvp.PvPMessage;
import me.winterguardian.pvp.TeamColor;
import me.winterguardian.pvp.stats.PvPStats;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class PvPMatch implements State, Runnable
{
	private final PvP game;

	private PvPArena arena = null;

	private final Set<PvPPlayerData> playerDatas = new HashSet<>();
	private final List<Listener> listeners = new ArrayList<>();
	private int taskId;
	private int timer;

	public abstract GameStuff getNewStuff(Player player);

	public abstract TeamColor getNewTeam(Player player);

	public abstract boolean areEnemies(Player p1, Player p2);

	public abstract String getName();
	public abstract String getColoredName();

	public abstract Message getStartMessage();
	public abstract Message getGuide();

	public abstract void updateBoard();

	protected PvPMatch(PvP game)
	{
		this(game, 600);
	}

	protected PvPMatch(PvP game, int length)
	{
		this.game = game;
		this.timer = length;
	}

	@Override
	public void join(Player p)
	{
		if(getPlayerData(p) == null)
		{
			this.playerDatas.add(new PvPPlayerData(p, this));
			getPlayerData(p).setTeam(getNewTeam(p));
			p.teleport(this.arena.getSpawnPoint(getPlayerData(p).getTeam()));
			displayTab(p);
			PlayerUtil.prepare(p);
			PlayerUtil.heal(p);
			PlayerUtil.clearBoard(p);
			PlayerUtil.clearInventory(p);
			getNewStuff(p).give(p);
			getPlayerData(p).start();
		}
		else
		{
			p.teleport(this.arena.getSpawnPoint(getPlayerData(p).getTeam()));
			displayTab(p);
			PlayerUtil.prepare(p);
			PlayerUtil.heal(p);
			PlayerUtil.clearBoard(p);
			PlayerUtil.clearInventory(p);
			getPlayerData(p).onJoin();
		}
	}

	private void displayTab(Player p)
	{
		TabUtil.sendInfos(p, JsonUtil.toJson("§f§lTouched§4§lPvP"), JsonUtil.toJson(PvPMessage.GAME_TABFOOTER.toString("<mode>", getName(), "<arena>", getArena().getName())));
	}

	public void skip()
	{
		if(this.timer > 5)
			this.timer = 5;
	}

	@Override
	public void leave(Player p)
	{
		getPlayerData(p).onLeave();
		PlayerUtil.prepare(p);
		PlayerUtil.heal(p);
		PlayerUtil.clearBoard(p);
		PlayerUtil.clearInventory(p);

		p.teleport(this.game.getSetup().getExit());

		if(game.getPlayers().size() < getMinimum())
		{
			end();
			if(game.getPlayers().size() < game.getMinPlayers())
				game.setState(new PvPStandbyState(this.getGame()));
			else
				game.setState(new PvPVoteState(this.getGame()));
			game.getState().start();
		}
	}

	@Override
	public void start()
	{
		register(new GameListener(this));

		game.getPlayerListener().setCancelDamage(false);
		game.getPlayerListener().setCancelDamageOthers(false);
		game.getPlayerListener().setCancelCombust(false);

		for(Player player : game.getPlayers())
		{
			PlayerUtil.clearBoard(player);
			this.playerDatas.add(new PvPPlayerData(player, this));
			getPlayerData(player).setTeam(getNewTeam(player));
			getPlayerData(player).start();
			player.teleport(this.arena.getSpawnPoint(getPlayerData(player).getTeam()));
			displayTab(player);
		}

		getStartMessage().say(game.getPlayers(), "<game>", getName());

		this.taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(game.getPlugin(), this, 0, 20);
	}

	@Override
	public void end()
	{
		game.getPlayerListener().setCancelDamage(true);
		game.getPlayerListener().setCancelDamageOthers(true);
		game.getPlayerListener().setCancelCombust(true);


		for(Player player : game.getPlayers())
			player.teleport(game.getSetup().getLobby());


		for(Listener listener : this.listeners)
			HandlerList.unregisterAll(listener);

		Bukkit.getScheduler().cancelTask(this.taskId);
	}

	public void finish()
	{
		for(final PvPPlayerData playerData : playerDatas)
		{
			if(playerData.getPlayer() != null)
				playerData.end(PvPStats.get(playerData.getUUID()));
			else
				Bukkit.getScheduler().runTaskAsynchronously(game.getPlugin(), new Runnable()
				{
					@Override
					public void run()
					{
						PvPStats stats = PvPStats.get(playerData.getUUID());
						playerData.end(stats);
						Core.getUserDatasManager().saveUserData(playerData.getUUID(), stats.getContent());
					}
				});
		}
		new SoundEffect(Sound.NOTE_PLING, 1f, 1f).play(game.getPlayers());
	}

	@Override
	public void run()
	{
		int minutes = timer / 60, seconds = timer % 60;
		String time = minutes > 0 ? minutes + " minute" + (minutes > 1 ? "s" : "") : "";
		time += seconds > 0 ? (time.length() > 0 ? " " : "") + seconds + " seconde" + (seconds > 1 ? "s" : "") : "";

		PvPMessage.GAME_TIMER.sayPlayers("<time>", time);

		if(--timer == 0)
		{
			end();
			finish();
			game.setState(new PvPVoteState(this.getGame()));
			game.getState().start();
		}
		else if(timer <= 5)
			new SoundEffect(Sound.NOTE_BASS, 1f, 1f).play(game.getPlayers());
	}

	protected void register(Listener listener)
	{
		Bukkit.getPluginManager().registerEvents(listener, this.game.getPlugin());
		listeners.add(listener);
	}

	@Override
	public String getStatus()
	{
		return "Les joueurs sont en partie de " + getName() + " sur " + getArena().getName() + ".";
	}

	public PvPArena getArena()
	{
		return arena;
	}

	public void setArena(PvPArena arena)
	{
		this.arena = arena;
	}

	public int getVoteTimer()
	{
		return 90;
	}

	public boolean canBuyInLobby()
	{
		return true;
	}

	public PvPPlayerData getPlayerData(Player player)
	{
		if(player == null)
			return null;

		for(PvPPlayerData playerData : playerDatas)
			if(playerData.getUUID().equals(player.getUniqueId()))
				return playerData;
		return null;
	}

	public PvPPlayerData getOwner(Entity friend)
	{
		for(PvPPlayerData playerData : playerDatas)
			if(playerData.getFriend() == friend)
				return playerData;
		return null;
	}

	public Set<PvPPlayerData> getPlayerDatas()
	{
		return playerDatas;
	}

	public PvP getGame()
	{
		return game;
	}

	public int getMinimum()
	{
		return game.getMinPlayers();
	}

	public int getTimer()
	{
		return timer;
	}
}
