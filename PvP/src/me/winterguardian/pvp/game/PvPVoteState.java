package me.winterguardian.pvp.game;

import me.winterguardian.core.game.state.State;
import me.winterguardian.core.json.JsonUtil;
import me.winterguardian.core.scoreboard.ScoreboardUtil;
import me.winterguardian.core.util.PlayerUtil;
import me.winterguardian.core.util.SoundEffect;
import me.winterguardian.core.util.TabUtil;
import me.winterguardian.pvp.PvP;
import me.winterguardian.pvp.PvPArena;
import me.winterguardian.pvp.PvPMessage;
import me.winterguardian.pvp.PvPPlugin;
import me.winterguardian.pvp.game.solo.FreeForAll;
import me.winterguardian.pvp.game.solo.KingOfTheHill;
import me.winterguardian.pvp.game.solo.OneInTheChamber;
import me.winterguardian.pvp.game.solo.Switch;
import me.winterguardian.pvp.game.team.CaptureTheFlag;
import me.winterguardian.pvp.game.team.Domination;
import me.winterguardian.pvp.game.team.TeamDeathMatch;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

public class PvPVoteState implements State, Runnable
{
	private static Class<? extends PvPMatch> previous = null;
	public static PvPMatch definedNext = null;

	private List<PvPArena> arenas;
	private Map<Player, PvPArena> votes;
	
	private PvP game;
	private PvPMatch nextState;
	private int taskId, timer;
	
	public PvPVoteState(PvP game)
	{
		this.game = game;
		this.nextState = null;
		this.taskId = -1;
		this.votes = new HashMap<>();

		this.arenas = PvPArena.getReadyArenaList();
	}
	
	@Override
	public void join(Player p)
	{
		p.teleport(game.getSetup().getLobby());
		prepare(p);
		this.nextState.getNewStuff(p, true).give(p);
		PvPMessage.VOTE_START.say(p, "<type>", getNextGameName());
	}

	@Override
	public void leave(Player p)
	{
		PlayerUtil.clearInventory(p);
		PlayerUtil.clearBoard(p);
		TabUtil.resetTab(p);
		PlayerUtil.heal(p);

		if(this.votes.containsKey(p))
			this.votes.remove(p);
		
		p.teleport(game.getSetup().getExit());
		
		if(game.getPlayers().size() < game.getMinPlayers())
			end();
	}

	@Override
	public void start()
	{
		this.nextState = getNewGameState(game);
		this.timer = this.nextState.getVoteTimer();
		PvPMessage.VOTE_START.say(game.getPlayers(), "<type>", getNextGameName());

		for(Player player : game.getPlayers())
		{
			prepare(player);
			this.nextState.getNewStuff(player, true).give(player);
		}

		this.taskId = Bukkit.getScheduler().runTaskTimer(game.getPlugin(), this, 0, 20).getTaskId();
		updateBoard();
	}
	
	@Override
	public void run()
	{
		--timer;

		if(timer <= 5 && timer > 0)
			new SoundEffect(Sound.NOTE_BASS, 1f, 1f).play(game.getPlayers());

		if(timer == 15 && nextState.getGuide() != null)
		{
			PvPMessage.STATS_SUMMARY_SEPARATOR.sayPlayers();
			nextState.getGuide().say(game.getPlayers(), "<mode>", nextState.getColoredName());
			PvPMessage.STATS_SUMMARY_SEPARATOR.sayPlayers();
		}

		if(timer == 5)
		{
			nextState.setArena(chooseArena());
			PvPMessage.VOTE_END.say(game.getPlayers(), "<arena>", nextState.getArena().getName());
		}
		else if(timer == 0)
		{
			Bukkit.getScheduler().cancelTask(this.taskId);
			previous = nextState.getClass();
			game.setState(nextState);
			game.getState().start();
			PvPMessage.VOTE_TELEPORT.say(game.getPlayers());
			new SoundEffect(Sound.NOTE_PLING, 1f, 1f).play(game.getPlayers());
			return;
		}
		
		PvPMessage.VOTE_TIMER.say(game.getPlayers(), "#", timer + "");
	}

	public PvPMatch getNextGame()
	{
		return nextState;
	}

	private PvPArena chooseArena()
	{
		int max = 0;

		for(Entry<PvPArena, Integer> entry : this.getVotes().entrySet())
			max += entry.getValue();

		if(max == 0)
		{
			List<PvPArena> compatibles = PvPArena.getCompatibleArenaList(this.nextState);
			return compatibles.get(new Random().nextInt(compatibles.size()));
		}

		int id = new Random().nextInt(max);

		for(Entry<PvPArena, Integer> entry : this.getVotes().entrySet())
			if((id -= entry.getValue()) <= 0)
			{
				entry.getKey().load();
				return entry.getKey();
			}

		return null;
	}
	
	public void vote(Player p, String arena)
	{
		if(timer <= 5)
		{
			PvPMessage.VOTE_TOOLATE.say(p);
			return;
		}

		PvPArena pvpArena = getArena(arena);

		if(pvpArena == null)
		{
			PvPMessage.COMMAND_ARENA_INVALIDARENA.say(p);
			return;
		}

		if(!pvpArena.isCompatible(this.nextState))
		{
			PvPMessage.VOTE_INCOMPATIBLE.say(p, "<mode>", this.getNextGameName());
			return;
		}

		if(this.votes.containsKey(p) && this.votes.get(p).getName().equals(arena))
		{
			PvPMessage.VOTE_ALREADYVOTED.say(p);
			return;
		}

		this.votes.put(p, pvpArena);
		PvPMessage.VOTE_VOTED.say(p);
		if(!p.hasPermission(PvPPlugin.DOUBLE_VOTE))
			PvPMessage.VOTE_VOTE_NODOUBLEVOTE.say(p);

		updateBoard();
	}

	public PvPArena getArena(String name)
	{
		for(PvPArena arena : this.arenas)
			if(arena.getName().equalsIgnoreCase(name))
				return arena;
		return null;
	}

	@Override
	public void end()
	{
		Bukkit.getScheduler().cancelTask(this.taskId);
		game.setState(new PvPStandbyState(game));
		game.getState().start();
	}

	@Override
	public String getStatus()
	{
		return "Les joueurs sont en train de voter pour leur arène du jeu " + getNextGameName();
	}
	
	public String getNextGameName()
	{
		return nextState.getName();
	}

	private static PvPMatch getNewGameState(PvP game)
	{


		if(definedNext != null)
		{
			PvPMatch next = definedNext;
			definedNext = null;
			return next;
		}

		HashMap<PvPMatch, Integer> games = new HashMap<>();

		if(game.getPlayers().size() < 4) //moins que 4
		{
			games.put(new FreeForAll(game), 1);
			games.put(new Switch(game), 1);
			games.put(new OneInTheChamber(game), 1);
			games.put(new KingOfTheHill(game), 1);
		}
		else if(game.getPlayers().size() < 6) //à partir de 4
		{
			games.put(new FreeForAll(game), 1);
			games.put(new Switch(game), 1);
			games.put(new OneInTheChamber(game), 1);
			games.put(new KingOfTheHill(game), 1);
			games.put(new TeamDeathMatch(game, 2), 1);
		}
		else if(game.getPlayers().size() < 10) //à partir de 6
		{
			games.put(new FreeForAll(game), 1);
			games.put(new Switch(game), 1);
			games.put(new OneInTheChamber(game), 1);
			games.put(new KingOfTheHill(game), 2);
			games.put(new TeamDeathMatch(game, 2), 2);
			games.put(new CaptureTheFlag(game, 2), 2);
			games.put(new Domination(game, 2), 2);
		}
		else if(game.getPlayers().size() < 15) //à partir de 10
		{
			games.put(new FreeForAll(game), 1);
			games.put(new Switch(game), 1);
			games.put(new KingOfTheHill(game), 1);
			games.put(new TeamDeathMatch(game, 2), 2);
			games.put(new CaptureTheFlag(game, 2), 2);
			games.put(new Domination(game, 2), 2);
		}
		else if(game.getPlayers().size() < 20) //à partir de 15
		{
			games.put(new TeamDeathMatch(game, 2), 1);
			games.put(new TeamDeathMatch(game, 3), 1);

			games.put(new CaptureTheFlag(game, 2), 1);
			games.put(new CaptureTheFlag(game, 3), 1);

			games.put(new Domination(game, 2), 1);
			games.put(new Domination(game, 3), 1);
		}
		else if(game.getPlayers().size() < 30) //à partir de 20
		{
			games.put(new TeamDeathMatch(game, 2), 1);
			games.put(new TeamDeathMatch(game, 3), 1);
			games.put(new TeamDeathMatch(game, 4), 1);

			games.put(new CaptureTheFlag(game, 2), 1);
			games.put(new CaptureTheFlag(game, 3), 1);
			games.put(new CaptureTheFlag(game, 4), 1);

			games.put(new Domination(game, 2), 1);
			games.put(new Domination(game, 3), 1);
			games.put(new Domination(game, 4), 1);
		}
		else if(game.getPlayers().size() < 40) //à partir de 30
		{
			games.put(new TeamDeathMatch(game, 3), 1);
			games.put(new TeamDeathMatch(game, 4), 1);
			games.put(new TeamDeathMatch(game, 6), 1);

			games.put(new CaptureTheFlag(game, 3), 1);
			games.put(new CaptureTheFlag(game, 4), 1);
			games.put(new CaptureTheFlag(game, 5), 1);

			games.put(new Domination(game, 3), 1);
			games.put(new Domination(game, 4), 1);
			games.put(new Domination(game, 5), 1);
		}
		else if(game.getPlayers().size() < 60) //à partir de 40
		{
			games.put(new TeamDeathMatch(game, 4), 1);
			games.put(new TeamDeathMatch(game, 6), 2);
			games.put(new TeamDeathMatch(game, 8), 2);

			games.put(new CaptureTheFlag(game, 4), 1);
			games.put(new CaptureTheFlag(game, 6), 2);
			games.put(new CaptureTheFlag(game, 8), 2);

			games.put(new Domination(game, 4), 1);
			games.put(new Domination(game, 6), 2);
			games.put(new Domination(game, 8), 2);

		}
		else if(game.getPlayers().size() < 80) //à partir de 60
		{
			games.put(new TeamDeathMatch(game, 6), 1);
			games.put(new TeamDeathMatch(game, 8), 2);

			games.put(new CaptureTheFlag(game, 6), 1);
			games.put(new CaptureTheFlag(game, 8), 2);

			games.put(new Domination(game, 6), 1);
			games.put(new Domination(game, 8), 2);
		}
		else //à partir de 80
		{
			games.put(new TeamDeathMatch(game, 8), 1);
			games.put(new CaptureTheFlag(game, 8), 1);
			games.put(new Domination(game, 8), 1);
		}

		int total = 0;

		for(Entry<PvPMatch, Integer> type : games.entrySet())
			total += type.getValue();

		PvPMatch match = null;

		do
		{
			float current = 0, r = new Random().nextFloat();

			for(Entry<PvPMatch, Integer> type : games.entrySet())
			{
				float previous = current;
				current += type.getValue();
				if(r >= previous / total && r < current / total)
					match = type.getKey();
			}
		}
		while(match == null || PvPArena.getCompatibleArenaList(match).size() == 0 || previous == match.getClass());

		return match;
	}

	public void updateBoard()
	{
		for(Player player : game.getPlayers())
			displayBoard(player);
	}

	public void skip()
	{
		if(this.timer > 6)
			this.timer = 6;
	}

	public Map<PvPArena, Integer> getVotes()
	{
		HashMap<PvPArena, Integer> map = new HashMap<>();

		for(Entry<Player, PvPArena> entry : votes.entrySet())
			map.put(entry.getValue(), (map.containsKey(entry.getValue()) ? map.get(entry.getValue()) : 0) + (entry.getKey().hasPermission(PvPPlugin.DOUBLE_VOTE) ? 2 : 1));

		return map;
	}

	private void displayBoard(Player player)
	{
		HashMap<String, Integer> map = new HashMap<>();

		for(Entry<PvPArena, Integer> entry : getVotes().entrySet())
			map.put("§e" + entry.getKey().getName(), entry.getValue());

		if(map.size() == 0)
			map.put("Aucun vote.", -1);

		ScoreboardUtil.rankedSidebarDisplay(player, this.nextState.getColoredName(), map);
	}

	private void prepare(Player player)
	{
		displayBoard(player);
		TabUtil.sendInfos(player, JsonUtil.toJson("§f§lTouched§4§lPvP"), JsonUtil.toJson("§aUne partie de " + getNextGameName() + " va bientôt commencer."));
		PlayerUtil.prepare(player);
		PlayerUtil.clearInventory(player);
		PlayerUtil.heal(player);
	}

}
