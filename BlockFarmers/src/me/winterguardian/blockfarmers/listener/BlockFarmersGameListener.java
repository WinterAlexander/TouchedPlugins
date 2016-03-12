package me.winterguardian.blockfarmers.listener;

import java.util.Arrays;
import java.util.Set;

import me.winterguardian.blockfarmers.BlockFarmersConfig;
import me.winterguardian.blockfarmers.BlockFarmersPlayerData;
import me.winterguardian.blockfarmers.BlockFarmersGame;
import me.winterguardian.blockfarmers.state.BlockFarmersFarmingState;
import me.winterguardian.core.util.SoundEffect;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class BlockFarmersGameListener implements Listener
{
	private BlockFarmersGame game;
	
	public BlockFarmersGameListener(BlockFarmersGame game)
	{
		this.game = game;
	}
	
	@EventHandler
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event)
	{
		if(!game.contains(event.getPlayer()))
			return;
		this.onPlayerInteract(new PlayerInteractEvent(event.getPlayer(), Action.LEFT_CLICK_BLOCK, event.getPlayer().getItemInHand(), event.getPlayer().getTargetBlock((Set<Material>)null, 6), BlockFace.UP));
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event)
	{
		if(!game.contains(event.getPlayer()))
			return;
		
		event.setCancelled(true);
		
		if(event.getBlockFace() != BlockFace.UP)
			return;
		
		if(!event.hasItem() || !event.hasBlock())
			return;
		
		if(!(game.getState() instanceof BlockFarmersFarmingState))
			return;
		
		if(event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK)
			return;
		
		BlockFarmersFarmingState state = (BlockFarmersFarmingState) game.getState();
		
		if(!state.canFarm())
			return;
			
		if(game.getConfig() == null || !(game.getConfig() instanceof BlockFarmersConfig))
			return;
		
		if(!((BlockFarmersConfig) game.getConfig()).canFarm(event.getClickedBlock()))
			return;
		
		BlockFarmersPlayerData data = state.getPlayerData(event.getPlayer());
		
		boolean canFarm = false;
		if(data.isFirst())
		{
			canFarm = true;
		}
		else
			for(BlockFace face : Arrays.asList(BlockFace.EAST, BlockFace.WEST, BlockFace.SOUTH, BlockFace.NORTH))
				if(data.getColor().getBlock().match(event.getClickedBlock().getRelative(face)))
				{
					canFarm = true;
					break;
				}
			
		if(!canFarm)
			return;
		
		data.farm(event.getClickedBlock());
		
		state.updateBoard(event.getPlayer());
		new SoundEffect(Sound.STEP_GRAVEL, 1f, 1f).play(event.getPlayer(), event.getClickedBlock().getLocation());
	}
}
