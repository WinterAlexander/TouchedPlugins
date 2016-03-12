package me.winterguardian.blockfarmers.state;

import me.winterguardian.blockfarmers.BlockFarmersGame;
import me.winterguardian.blockfarmers.BlockFarmersMessage;
import me.winterguardian.core.game.state.StandbyState;
import me.winterguardian.core.game.state.State;
import me.winterguardian.core.game.state.StateGame;
import me.winterguardian.core.message.Message;
import me.winterguardian.core.scoreboard.Board;
import me.winterguardian.core.util.PlayerUtil;

import org.bukkit.entity.Player;

public class BlockFarmersStandbyState extends StandbyState
{
	public BlockFarmersStandbyState(StateGame game)
	{
		super(game);
	}

	@Override
	protected State getNextState()
	{
		return new BlockFarmersWaitingState(this.getGame());
	}

	@Override
	protected Message getNotEnoughtPlayersToPlayMessage()
	{
		return BlockFarmersMessage.STANDBY_START;
	}

	@Override
	protected Message getNotEnoughtPlayersToStartMessage()
	{
		return BlockFarmersMessage.STANDBY_START;
	}

	@Override
	public String getTabHeader(Player p)
	{
		return ((BlockFarmersGame)getGame()).getTabHeader();
	}

	@Override
	public String getTabFooter(Player p)
	{
		return ((BlockFarmersGame)getGame()).getTabFooter();
	}

	@Override
	public Board getNewScoreboard()
	{
		return null;
	}

	@Override
	public boolean keepScoreboardAndWeather()
	{
		return true;
	}

	@Override
	public String getStatus()
	{
		return BlockFarmersMessage.STANDBY_STATUS.toString();
	}
	
	public void prepare(Player p, boolean joining)
	{
		PlayerUtil.clearInventory(p);
		PlayerUtil.heal(p);
		PlayerUtil.prepare(p);
		PlayerUtil.clearBoard(p);
	}

	@Override
	public void join(Player player)
	{
		((BlockFarmersGame) getGame()).savePlayerState(player);
		super.join(player);
	}

	@Override
	public void leave(Player player)
	{
		super.leave(player);

		((BlockFarmersGame) getGame()).applyPlayerState(player);
	}
}
