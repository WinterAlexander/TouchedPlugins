package me.winterguardian.pvp.game.solo;

import me.winterguardian.core.message.Message;
import me.winterguardian.pvp.GameStuff;
import me.winterguardian.pvp.PvP;
import me.winterguardian.pvp.PvPMessage;
import me.winterguardian.pvp.game.PvPPlayerData;
import org.bukkit.entity.Player;

/**
 *
 * Created by Alexander Winter on 2015-12-07.
 */
public class FreeForAll extends SoloGame
{
	private GameStuff ffaStuff;

	public FreeForAll(PvP game)
	{
		super(game);

		this.ffaStuff = new GameStuff("ffa");
		this.ffaStuff.load();
	}

	@Override
	public GameStuff getNewStuff(Player player, boolean gameStart)
	{
		return ffaStuff;
	}

	@Override
	public String getName()
	{
		return "Mêlée générale";
	}

	@Override
	public Message getStartMessage()
	{
		return PvPMessage.GAME_START_FFA;
	}

	@Override
	public String getColoredName()
	{
		return "§e§l" + getName();
	}

	@Override
	public int getScore(PvPPlayerData data)
	{
		return data.getKills();
	}

	@Override
	public int getSecondFactor(PvPPlayerData data)
	{
		return data.getAssists();
	}

	@Override
	public Message getGuide()
	{
		return PvPMessage.GAME_GUIDE_FFA;
	}
}
