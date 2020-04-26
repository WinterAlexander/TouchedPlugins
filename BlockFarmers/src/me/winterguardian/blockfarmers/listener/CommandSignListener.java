package me.winterguardian.blockfarmers.listener;

import me.winterguardian.blockfarmers.BlockFarmersMessage;
import me.winterguardian.blockfarmers.BlockFarmersPlugin;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class CommandSignListener implements Listener
{
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event)
	{
		if(event.getAction() != Action.RIGHT_CLICK_BLOCK)
			return;
		
		if(!(event.getClickedBlock().getState() instanceof Sign))
			return;
		
		Sign sign = (Sign) event.getClickedBlock().getState();
		
		if(!sign.getLine(0).equals(BlockFarmersMessage.SIGN_TITLE.toString()))
			return;
		
		if(sign.getLine(1).equals(BlockFarmersMessage.SIGN_JOIN.toString()))
		{
			event.getPlayer().performCommand("blockfarmers join");
			return;
		}

		if(sign.getLine(1).equals(BlockFarmersMessage.SIGN_LEAVE.toString()))
		{
			event.getPlayer().performCommand("blockfarmers leave");
			return;
		}
		
		if(sign.getLine(1).equals(BlockFarmersMessage.SIGN_STATS.toString()))
		{
			String arg3 = "";
			
			if(sign.getLine(2) != null)
				arg3 = " " + sign.getLine(2);
			
			event.getPlayer().performCommand("blockfarmers stats" + arg3);
		}
		
	}
	
	@EventHandler
	public void onSignChange(SignChangeEvent event)
	{
		if(!event.getPlayer().hasPermission(BlockFarmersPlugin.STAFF))
			return;
		
		if(!event.getLine(0).equalsIgnoreCase("[blockfarmers]"))
			return;
		
		event.setLine(0, BlockFarmersMessage.SIGN_TITLE.toString());
		event.setLine(3, BlockFarmersMessage.SIGN_CLICK.toString());
		
		if(event.getLine(1).equalsIgnoreCase("join"))
		{
			event.setLine(1, BlockFarmersMessage.SIGN_JOIN.toString());
			event.setLine(2, "");
			return;
		}
		
		if(event.getLine(1).equalsIgnoreCase("leave"))
		{
			event.setLine(1, BlockFarmersMessage.SIGN_LEAVE.toString());
			event.setLine(2, "");
			return;
		}
		
		if(event.getLine(1).equalsIgnoreCase("stats") || event.getLine(1).equalsIgnoreCase("statistics"))
		{
			event.setLine(1, BlockFarmersMessage.SIGN_STATS.toString());
			return;
		}
		
		
		
		event.setLine(1, "§4§l§mINVALID");
	}
}
