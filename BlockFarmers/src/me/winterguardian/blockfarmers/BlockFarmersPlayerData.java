package me.winterguardian.blockfarmers;

import me.winterguardian.blockfarmers.state.BlockFarmersFarmingState;
import me.winterguardian.core.Core;
import me.winterguardian.core.game.PlayerData;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class BlockFarmersPlayerData extends PlayerData
{
	private int id;
	private BlockFarmersFarmingState state;
	
	private int area;
	private long lastFarm = -1L;
	private boolean first;
	
	public BlockFarmersPlayerData(BlockFarmersFarmingState state, int id, OfflinePlayer player)
	{
		super(player);
		this.state = state;
		this.id = id;
		this.first = true;
	}
	
	public void prepare()
	{
		if(getPlayer().isOnline())
			getPlayer().sendMessage(BlockFarmersMessage.FARMING_COLOR.toString().replace("<color>", getColor().getName()));

		if(getPlayer().hasPermission(BlockFarmersPlugin.DIAMOND_HOE))
			getPlayer().getInventory().addItem(new ItemStack(Material.DIAMOND_HOE, 1));

		if(getPlayer().hasPermission(BlockFarmersPlugin.IRON_HOE))
			getPlayer().getInventory().addItem(new ItemStack(Material.IRON_HOE, 1));

		if(getPlayer().hasPermission(BlockFarmersPlugin.GOLD_HOE))
			getPlayer().getInventory().addItem(new ItemStack(Material.GOLD_HOE, 1));

		if(getPlayer().hasPermission(BlockFarmersPlugin.STONE_HOE))
			getPlayer().getInventory().addItem(new ItemStack(Material.STONE_HOE, 1));

		getPlayer().getInventory().addItem(new ItemStack(Material.WOOD_HOE, 1));
	}
	
	public void end(boolean win, int players)
	{
		if(!getPlayer().isOnline())
			return;

		FarmersStats stats = new FarmersStats(getPlayer(), Core.getUserDatasManager().getUserData(getPlayer()));
		stats.addGamePoints(win ? (players - 1) * 25 : 10);
		if(win)
			stats.setVictories(stats.getVictories() + 1);
		stats.setGamesPlayed(stats.getGamesPlayed() + 1);
		stats.setTilesFarmed(stats.getTilesFarmed() + area);
	}

	public FarmersColor getColor()
	{
		return FarmersColor.values()[id];
	}

	public int getArea()
	{
		return area;
	}

	public void farm(Block block)
	{
		this.lastFarm = System.currentTimeMillis();
		farm_inner(block);

		if(state.allStarted())
			tryFill(block.getLocation());
	}

	private void farm_inner(Block block)
	{
		this.area++;
		this.first = false;
		state.addBlockToRegen(block);
		getColor().getBlock().apply(block);
	}

	public long getLastFarm()
	{
		return lastFarm;
	}

	public boolean isFirst()
	{
		return first;
	}

	@Override
	public void onJoin()
	{

	}

	@Override
	public void onLeave()
	{

	}

	private void tryFill(Location source)
	{
		if(!getColor().getBlock().match(source.getBlock()))
			return;

		List<List<Location>> layers = new ArrayList<>();
		layers.add(new ArrayList<>());
		layers.get(0).add(source);

		for(int layer = 0; ; layer++)
		{
			List<Location> currentLayer = layers.get(layer);
			List<Location> nextLayer = new ArrayList<>();

			for(Location currentPoint : currentLayer)
			{
				directions:
				for(int direction = 0; direction < 4; direction++)
				{
					int x = 0, y = 0;

					if(direction < 2)
						x = (int)Math.pow(-1, direction);
					else
						y = (int)Math.pow(-1, direction);

					Location newPoint = currentPoint.clone();
					newPoint.add(x, 0, y);

					for(Location point : nextLayer)
						if(point.equals(newPoint))
							continue directions;

					for(List<Location> previousLayer : layers)
						for(Location point : previousLayer)
							if(point.equals(newPoint))
								continue directions;

					if(((BlockFarmersConfig)state.getGame().getConfig()).canFarm(newPoint.getBlock()))
						nextLayer.add(newPoint);
					else
						for(BlockFarmersPlayerData playerData : state.getPlayerDatas())
							if(playerData.getColor().getBlock().match(newPoint.getBlock()) && playerData != this)
								return;

				}
			}

			if(nextLayer.size() == 0)
				break;
			layers.add(nextLayer);
		}

		for(List<Location> layer : layers)
			for(Location point : layer)
				farm_inner(point.getBlock());
	}
}
