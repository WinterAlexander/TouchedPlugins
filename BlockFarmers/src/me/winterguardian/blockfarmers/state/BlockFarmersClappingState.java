package me.winterguardian.blockfarmers.state;

import me.winterguardian.blockfarmers.BlockFarmersConfig;
import me.winterguardian.blockfarmers.BlockFarmersGame;
import me.winterguardian.blockfarmers.BlockFarmersMessage;
import me.winterguardian.blockfarmers.BlockFarmersPlayerData;
import me.winterguardian.blockfarmers.FarmersColor;
import me.winterguardian.core.game.state.State;
import me.winterguardian.core.util.PlayerUtil;
import me.winterguardian.core.util.TabUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class BlockFarmersClappingState implements State, Runnable
{
	private BlockFarmersGame game;
	private int i;
	
	private Player winner;
	private FarmersColor color;
	
	private int taskId;
	
	public BlockFarmersClappingState(BlockFarmersGame game, BlockFarmersPlayerData winner)
	{
		if(winner != null)
		{
			this.winner = winner.getPlayer();
			this.color = winner.getColor();
		}
		this.game = game;
	}

	@Override
	public void join(Player p)
	{
		game.savePlayerState(p);
		p.teleport(game.getSetup().getLobby());
		if(this.game.getConfig().isColorInTab())
			TabUtil.sendInfos(p, game.getTabHeader(), game.getTabFooter());
		PlayerUtil.prepare(p);
		PlayerUtil.clearInventory(p);
		PlayerUtil.heal(p);
		PlayerUtil.clearBoard(p);
		BlockFarmersMessage.CLAPPING_SPECTATOR.say(p);
	}

	@Override
	public void leave(Player p)
	{
		PlayerUtil.prepare(p);
		PlayerUtil.clearInventory(p);
		PlayerUtil.heal(p);
		PlayerUtil.clearBoard(p);
		if(this.game.getConfig().isColorInTab())
			TabUtil.resetTab(p);
		
		p.teleport(game.getSetup().getExit());
		game.applyPlayerState(p);
	}

	@Override
	public void start()
	{
		String name = winner.getName();
		
		if(game.getConfig() != null && game.getConfig().useDisplaynames())
			name = winner.getDisplayName();
		
		if(game.getConfig() != null && game.getConfig() instanceof BlockFarmersConfig)
			BlockFarmersMessage.CLAPPING_WIN.say(((BlockFarmersConfig) game.getConfig()).getBroadcastRecipients(), "<player>", name);
		this.i = ((BlockFarmersConfig) game.getConfig()).getClappingTimer();
		if(this.game != null && this.game.getConfig() != null && this.game.getConfig() instanceof BlockFarmersConfig)
			this.i = ((BlockFarmersConfig) this.game.getConfig()).getClappingTimer();
		this.taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(game.getPlugin(), this, 0, 20);

		for(String command : ((BlockFarmersConfig) game.getConfig()).getEndGameCommands())
		{
			try
			{

				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("<winner>", name));
			}
			catch(Exception e)
			{
				Bukkit.getLogger().warning("§cAn internal error occured when BlockFarmers tried to execute a preconfigured command: " + command);
				e.printStackTrace();
			}
		}
	}

	@Override
	public String getStatus()
	{
		return BlockFarmersMessage.CLAPPING_STATUS.toString("<player>", winner.getName());
	}

	@Override
	public void run()
	{
		if(i <= 0)
		{
			this.end();
			return;
		}
		if(winner != null && game.contains(winner))
			color.shootFirework(winner.getLocation());
		this.i--;
	}

	@Override
	public void end()
	{
		for(Player p : game.getPlayers())
		{
			PlayerUtil.clearBoard(p);
			p.teleport(game.getSetup().getLobby());
		}
		
		Bukkit.getScheduler().cancelTask(taskId);
		if(game.getPlayers().size() >= game.getMinPlayers())
		{
			game.setState(new BlockFarmersWaitingState(game));
			game.getState().start();
		}
		else
		{
			game.setState(new BlockFarmersStandbyState(game));
			game.getState().start();
		}
	}

	public Player getWinner()
	{
		return winner;
	}
}
