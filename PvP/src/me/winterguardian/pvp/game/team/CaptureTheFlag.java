package me.winterguardian.pvp.game.team;

import me.winterguardian.core.message.Message;
import me.winterguardian.core.scoreboard.ScoreboardUtil;
import me.winterguardian.core.util.TextUtil;
import me.winterguardian.pvp.GameStuff;
import me.winterguardian.pvp.PvP;
import me.winterguardian.pvp.PvPMessage;
import me.winterguardian.pvp.TeamColor;
import me.winterguardian.pvp.game.PvPPlayerData;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * Created by Alexander Winter on 2015-12-13.
 */
public class CaptureTheFlag extends TeamGame
{

	private HashMap<TeamColor, Player> flagHolder;

	private GameStuff stuff;
	private int teams;

	public CaptureTheFlag(PvP game, int teams)
	{
		super(game);
		this.stuff = new GameStuff("ctf");
		this.stuff.load();
		this.teams = teams;
		this.flagHolder = new HashMap<>();
	}

	public HashMap<TeamColor, Player> getFlagHolder()
	{
		return flagHolder;
	}

	@Override
	public int getTeamCount()
	{
		return teams;
	}

	@Override
	public int getScore(TeamColor color)
	{
		HashMap<TeamColor, Integer> flags = new HashMap<>();

		for(TeamColor team : getTeams())
			flags.put(team, 0);

		for(PvPPlayerData data : getPlayerDatas())
			if(flags.containsKey(data.getTeam()))
				flags.put(data.getTeam(), flags.get(data.getTeam()) + data.getFlagsCaptured());

		return flags.get(color);
	}

	@Override
	public int getSecondFactor(TeamColor color)
	{
		HashMap<TeamColor, Integer> kills = new HashMap<>();

		for(TeamColor team : getTeams())
			kills.put(team, 0);

		for(PvPPlayerData data : getPlayerDatas())
			if(kills.containsKey(data.getTeam()))
				kills.put(data.getTeam(), kills.get(data.getTeam()) + data.getKills());

		return kills.get(color);
	}

	@Override
	public GameStuff getNewStuff(Player player, boolean gameStart)
	{
		return this.stuff;
	}

	@Override
	public String getName()
	{
		return "Capture du drapeau";
	}

	@Override
	public String getColoredName()
	{
		return "§c§l" + getName();
	}

	@Override
	public void updateBoard()
	{
		if(getTeamCount() <= 4)
		{
			String[] data = new String[16];

			data[0] = getColoredName();

			String lastSpace = "";

			for(int i = 0; i < getTeamCount(); i++)
			{
				int pos = (i * 4) + 1;
				data[pos] = getTeam(i + 1).getBukkitColor() + TextUtil.capitalize(getTeam(i + 1).getNamePlural());
				String score = getScore(getTeam(i + 1)) + "";
				String status = getTeam(i + 1).getBukkitColor() + "⚑ §f" + (this.flagHolder.get(getTeam(i + 1)) == null ? "À la base" : getPlayerData(this.flagHolder.get(getTeam(i + 1))).getTeam().getBukkitColor() + this.flagHolder.get(getTeam(i + 1)).getName());
				boolean change;
				do
				{
					change = false;
					for(String current : data)
						if(current != null && current.equals(score))
						{
							score += " ";
							change = true;
						}
				}
				while(change);

				do
				{
					change = false;
					for(String current : data)
						if(current != null && current.equals(status))
						{
							status += " ";
							change = true;
						}
				}
				while(change);


				data[pos + 1] = score;
				data[pos + 2] = status;
				lastSpace = data[pos + 3] = lastSpace + " ";
			}

			ScoreboardUtil.unrankedSidebarDisplay(getGame().getPlayers(), data, this.board);
		}
		else
		{
			HashMap<String, Integer> map = new HashMap<>();

			for(int i = 1; i <= getTeamCount(); i++)
				map.put(getTeam(i).getBukkitColor() + TextUtil.capitalize(getTeam(i).getNamePlural()), getScore(getTeam(i)));

			ScoreboardUtil.rankedSidebarDisplay(getGame().getPlayers(), getColoredName(), map, this.board);
		}
	}

	public void clickFlag(Player player, TeamColor flag)
	{
		PvPPlayerData data = getPlayerData(player);

		if(data == null)
			return;

		boolean own = data.getTeam() == flag;

		if(own && isHolder(player))
		{
			PvPMessage.GAME_CTF_CAPTURE.sayPlayers("<player>", data.getPvPName(), "<color>", data.getTeam().toString());
			TeamColor other = getFlagHolded(player);
			getArena().spawnFlag(other);
			this.flagHolder.remove(other);
			data.captureFlag();
		}
		else if(own)
			PvPMessage.GAME_CTF_YOURFLAG.say(player);
		else if(isHolder(player))
			PvPMessage.GAME_CTF_CANTHOLD2FLAGS.say(player);
		else
		{
			this.flagHolder.put(flag, player);
			PvPMessage.GAME_CTF_PICKUP.sayPlayers("<player>", data.getPvPName(), "<color>", flag.toString());
			getArena().cutFlag(flag);
		}

		updateBoard();
	}

	public boolean isHolder(Player player)
	{
		for(Map.Entry<TeamColor, Player> holder : this.flagHolder.entrySet())
			if(holder.getValue() == player)
				return true;

		return false;
	}

	public TeamColor getFlagHolded(Player player)
	{
		for(Map.Entry<TeamColor, Player> holder : this.flagHolder.entrySet())
			if(holder.getValue() == player)
				return holder.getKey();

		return null;
	}

	@Override
	public void start()
	{
		super.start();
		for(TeamColor team : getTeams())
			getArena().spawnFlag(team);
		register(new CTFListener(this));
	}

	@Override
	public void end()
	{
		getArena().dispose();
		super.end();
	}

	@Override
	public Message getStartMessage()
	{
		return PvPMessage.GAME_START_CTF;
	}

	@Override
	public Message getGuide()
	{
		return PvPMessage.GAME_GUIDE_CTF;
	}
}
