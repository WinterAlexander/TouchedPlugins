package me.winterguardian.blockfarmers.state;

import me.winterguardian.blockfarmers.BlockFarmersConfig;
import me.winterguardian.blockfarmers.BlockFarmersGame;
import me.winterguardian.blockfarmers.BlockFarmersMessage;
import me.winterguardian.blockfarmers.BlockFarmersPlayerData;
import me.winterguardian.blockfarmers.BlockFarmersSetup;
import me.winterguardian.core.game.state.State;
import me.winterguardian.core.scoreboard.ScoreboardUtil;
import me.winterguardian.core.util.PlayerUtil;
import me.winterguardian.core.util.SoundEffect;
import me.winterguardian.core.util.TabUtil;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BlockFarmersFarmingState implements State, Runnable
{
	private int seconds, taskId;
	private List<BlockFarmersPlayerData> playerDatas;
	
	private List<BlockState> toRegen;
	
	private BlockFarmersGame game;
	
	public BlockFarmersFarmingState(BlockFarmersGame game)
	{
		this.playerDatas = new ArrayList<BlockFarmersPlayerData>();
		this.toRegen = new ArrayList<BlockState>();
		this.game = game;
	}
	
	@Override
	public void join(Player p)
	{
		game.savePlayerState(p);
		p.teleport(game.getSetup().getLobby());
		if(this.game.getConfig().isColorInTab())
			TabUtil.sendInfos(p, game.getTabHeader(), game.getTabFooter());
		BlockFarmersMessage.FARMING_SPECTATOR.say(p);
		ScoreboardUtil.unrankedSidebarDisplay(p, new String[]{BlockFarmersMessage.BOARD_TITLE.toString(), BlockFarmersMessage.FARMING_BOARD_SPECTATOR1.toString(), BlockFarmersMessage.FARMING_BOARD_SPECTATOR2.toString(), BlockFarmersMessage.FARMING_BOARD_SPECTATOR3.toString(), BlockFarmersMessage.FARMING_BOARD_SPECTATOR4.toString()});
		PlayerUtil.prepare(p);
		PlayerUtil.clearInventory(p);
		PlayerUtil.heal(p);
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
		getGame().applyPlayerState(p);
	}

	@Override
	public void start()
	{
		this.seconds = 0;
		for(int i = 0; i < game.getPlayers().size(); i++)
			playerDatas.add(new BlockFarmersPlayerData(this, i, game.getPlayers().get(i)));
		
		for(BlockFarmersPlayerData data : playerDatas)
		{
			data.prepare();
			updateBoard(data.getPlayer());
			data.getPlayer().setGameMode(GameMode.SURVIVAL);
			data.getPlayer().teleport(((BlockFarmersSetup) game.getSetup()).getSpawn());
		}
		
		this.taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(game.getPlugin(), this, 0, 20);
	}

	@Override
	public String getStatus()
	{
		return BlockFarmersMessage.FARM_STATUS.toString();
	}
	
	public boolean canFarm()
	{
		return seconds >= 10;
	}

	@Override
	public void run()
	{
		boolean end = true;
		for(BlockFarmersPlayerData data : playerDatas)
			if(data.getLastFarm() + 5_000 > System.currentTimeMillis())
			{
				end = false;
				break;
			}
		
		if(end && seconds >= 30)
		{
			end();
			return;
		}
		
		if(++seconds == 10)
		{
			BlockFarmersMessage.FARMING_START.sayPlayers();
			for(Player p : game.getPlayers())
				new SoundEffect(Sound.SHEEP_IDLE, 1f, 1f).play(p);
		}
	}

	
	public BlockFarmersPlayerData getPlayerData(Player player)
	{
		for(BlockFarmersPlayerData data : this.playerDatas)
			if(data.getPlayer() == player)
				return data;
		
		return null;
	}
	
	public void addBlockToRegen(Block block)
	{
		this.toRegen.add(block.getState());
	}
	
	public void updateBoard(Player p)
	{
		BlockFarmersPlayerData data;
		if((data = getPlayerData(p)) != null)
		{
			HashMap<String, Integer> map = new HashMap<>();
			map.put(BlockFarmersMessage.FARMING_BOARD_BLOCKS.toString(), data.getArea());
			map.put(BlockFarmersMessage.FARMING_BOARD_PLAYERS.toString(), playerDatas.size());
			ScoreboardUtil.rankedSidebarDisplay(p, BlockFarmersMessage.BOARD_TITLE.toString(), map);
		}
	}

	@Override
	public void end()
	{
		BlockFarmersPlayerData winner = null;
		for(BlockFarmersPlayerData data : playerDatas)
			if(winner == null || winner.getArea() < data.getArea())
				winner = data;
		
		//égalité à gérer plus tard avec un sudden death
		
		HashMap<String, Integer> scores = new HashMap<String, Integer>();
		
		for(BlockFarmersPlayerData data : playerDatas)
		{
			if(!game.contains(data.getPlayer()))
				continue;
			
			if(game.getConfig() != null && game.getConfig() instanceof BlockFarmersConfig && ((BlockFarmersConfig)game.getConfig()).enableStats())
				data.end(data == winner, playerDatas.size());
			PlayerUtil.prepare(data.getPlayer());
			PlayerUtil.clearInventory(data.getPlayer());
			scores.put((data == winner ? "§a" : "§e") + data.getPlayer().getName(), data.getArea());
		}
		
		ScoreboardUtil.rankedSidebarDisplay(game.getPlayers(), BlockFarmersMessage.BOARD_TITLE.toString(), scores);
		
		for(BlockState state : toRegen)
			state.update(true, false);
		
		Bukkit.getScheduler().cancelTask(taskId);
		game.setState(new BlockFarmersClappingState(game, winner));
		game.getState().start();
	}
	
	public BlockFarmersGame getGame()
	{
		return this.game;
	}
}
