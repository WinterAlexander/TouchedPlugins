package me.winterguardian.test.components;

import me.winterguardian.test.TestComponent;
import me.winterguardian.test.TestingCenter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveTest extends TestComponent
{
	private boolean active = false;
	public PlayerMoveTest()
	{
		super("playermoveTest (not your buisness)", TestingCenter.TEST_LEVEL4, "...");
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event)
	{
		if(event.getPlayer().isOp() && active)
		{
			double x = Math.round((event.getTo().getX() - event.getFrom().getX()) * 100D) / 100D;
			double y = Math.round((event.getTo().getY() - event.getFrom().getY()) * 100D) / 100D;
			double z = Math.round((event.getTo().getZ() - event.getFrom().getZ()) * 100D) / 100D;
			
			if(x == 0 && y == 0 && z == 0)
				return;
			
			event.getPlayer().sendMessage("x:" + (x > 0 ? "+" : "") + x + " y:" + (y > 0 ? "+" : "") + y + " z:" + (z > 0 ? "+" : "") + z);
		}
	}
}
