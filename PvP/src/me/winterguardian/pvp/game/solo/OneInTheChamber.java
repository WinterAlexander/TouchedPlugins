package me.winterguardian.pvp.game.solo;

import me.winterguardian.core.message.Message;
import me.winterguardian.pvp.GameStuff;
import me.winterguardian.pvp.PvP;
import me.winterguardian.pvp.PvPMessage;
import me.winterguardian.pvp.game.PvPPlayerData;
import org.bukkit.entity.Player;

/**
 *
 * Created by Alexander Winter on 2015-12-09.
 */
public class OneInTheChamber extends SoloGame
{
	private GameStuff oitcStuff;

	public OneInTheChamber(PvP game)
	{
		super(game);

		this.oitcStuff = new GameStuff("oitc");
		this.oitcStuff.load();
	}

	@Override
	public void start()
	{
		super.start();
		register(new OITCListener(this));
	}

	@Override
	public GameStuff getNewStuff(Player player)
	{
		return oitcStuff;
	}

	@Override
	public String getName()
	{
		return "Balle chargée";
	}

	@Override
	public Message getStartMessage()
	{
		return PvPMessage.GAME_START_OITC;
	}

	public int getVoteTimer()
	{
		return 25;
	}

	@Override
	public boolean canBuyInLobby()
	{
		return false;
	}

	@Override
	public String getColoredName()
	{
		return "§a§l" + getName();
	}

	@Override
	public int getScore(PvPPlayerData data)
	{
		return data.getKills();
	}

	@Override
	public int getSecondFactor(PvPPlayerData data)
	{
		return -data.getDeaths();
	}

	@Override
	public Message getGuide()
	{
		return PvPMessage.GAME_GUIDE_OITC;
	}
}
