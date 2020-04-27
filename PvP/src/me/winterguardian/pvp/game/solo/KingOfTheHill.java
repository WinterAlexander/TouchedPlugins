package me.winterguardian.pvp.game.solo;

import me.winterguardian.core.message.Message;
import me.winterguardian.pvp.GameStuff;
import me.winterguardian.pvp.PvP;
import me.winterguardian.pvp.PvPMessage;
import me.winterguardian.pvp.TeamColor;
import me.winterguardian.pvp.game.PvPPlayerData;
import me.winterguardian.pvp.game.Zone;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

/**
 *
 * Created by Alexander Winter on 2015-12-17.
 */
public class KingOfTheHill extends SoloGame
{
	private Zone zone;
	private HashMap<UUID, Integer> scores = new HashMap<>();
	private GameStuff kothStuff;

	public KingOfTheHill(PvP game)
	{
		super(game);
		this.kothStuff = new GameStuff("koth");
		this.kothStuff.load();
	}

	@Override
	public GameStuff getNewStuff(Player player, boolean gameStart)
	{
		return kothStuff;
	}

	@Override
	public String getName()
	{
		return "Roi de la colline";
	}

	@Override
	public Message getStartMessage()
	{
		return PvPMessage.GAME_START_KOTH;
	}

	@Override
	public String getColoredName()
	{
		return "§6§l" + getName();
	}

	@Override
	public int getScore(PvPPlayerData data)
	{
		if(data == null || data.getPlayer() == null)
			return 0;

		if(!this.scores.containsKey(data.getPlayer().getUniqueId()))
			return 0;
		return this.scores.get(data.getPlayer().getUniqueId());
	}

	@Override
	public int getSecondFactor(PvPPlayerData data)
	{
		return data.getKills();
	}

	@Override
	public void start()
	{
		super.start();
		this.zone = new Zone(getArena().getZones().get(0).getLocation());
		this.zone.place();
	}

	@Override
	public void end()
	{
		this.zone.dispose();
		super.end();
	}

	@Override
	public void run()
	{
		super.run();
		Player player = null;
		for(Player current : getGame().getPlayers())
		{
			if(!zone.getRegion().contains(current.getLocation()))
				continue;

			if(player == null)
				player = current;
			else
			{
				zone.color(TeamColor.RED, false);
				return;
			}
		}

		if(player != null)
		{
			zone.color(TeamColor.YELLOW, false);
			this.scores.put(player.getUniqueId(), getScore(getPlayerData(player)) + 1);
			updateBoard();
			return;
		}

		zone.color(TeamColor.NONE, false);
	}

	@Override
	public Message getGuide()
	{
		return PvPMessage.GAME_GUIDE_KOTH;
	}
}
