package me.winterguardian.blockfarmers.state;

import me.winterguardian.blockfarmers.BlockFarmersConfig;
import me.winterguardian.blockfarmers.BlockFarmersGame;
import me.winterguardian.blockfarmers.BlockFarmersMessage;
import me.winterguardian.core.game.state.State;
import me.winterguardian.core.game.state.StateGame;
import me.winterguardian.core.game.state.WaitingState;
import me.winterguardian.core.json.JsonUtil;
import me.winterguardian.core.message.Message;
import me.winterguardian.core.scoreboard.Board;
import me.winterguardian.core.util.PlayerUtil;

import org.bukkit.entity.Player;

public class BlockFarmersWaitingState extends WaitingState
{
	public BlockFarmersWaitingState(StateGame game)
	{
		super(game, (game != null && game.getConfig() != null ? ((BlockFarmersConfig) game.getConfig()).getWaitingTimer() : 15));
	}

	@Override
	public String getStatus()
	{
		return BlockFarmersMessage.WAITING_STATUS.toString();
	}

	@Override
	protected State getStandbyState()
	{
		return new BlockFarmersStandbyState(super.getGame());
	}

	@Override
	protected State getNextState()
	{
		return new BlockFarmersFarmingState((BlockFarmersGame) getGame());
	}

	@Override
	protected Message getCountdownMessage(int seconds)
	{
		return BlockFarmersMessage.WAITING_TIMER;
	}

	@Override
	protected Message getStartMessage()
	{
		return null;
	}

	@Override
	protected Message getCountdownFinishMessage()
	{
		return BlockFarmersMessage.WAITING_TIMER_END;
	}

	@Override
	public String getTabHeader(Player p)
	{
		return JsonUtil.toJson(BlockFarmersMessage.HEADER.toString());
	}

	@Override
	public String getTabFooter(Player p)
	{
		return JsonUtil.toJson(BlockFarmersMessage.FOOTER.toString("<players>", getGame().getPlayers().size() + "", "<players-plural>", (getGame().getPlayers().size() > 1 ? "s" : "")));
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
