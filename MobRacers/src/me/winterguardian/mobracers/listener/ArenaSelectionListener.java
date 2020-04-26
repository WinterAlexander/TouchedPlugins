package me.winterguardian.mobracers.listener;

import me.winterguardian.mobracers.MobRacersGame;
import me.winterguardian.mobracers.state.lobby.ArenaSelectionState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 *
 * Created by Alexander Winter on 2016-01-24.
 */
public class ArenaSelectionListener implements Listener
{
	private MobRacersGame game;

	public ArenaSelectionListener(MobRacersGame game)
	{
		this.game = game;
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event)
	{
		if(!this.game.getPlayers().contains(event.getPlayer()))
			return;

		if(!(this.game.getState() instanceof ArenaSelectionState))
			return;

		if(event.getAction() == Action.PHYSICAL)
			return;

		if(event.getItem() == null)
			return;

		switch(event.getPlayer().getInventory().getHeldItemSlot())
		{
			case 0:
				((ArenaSelectionState) this.game.getState()).getGui().open(event.getPlayer());
				break;

			case 8:
				event.getPlayer().performCommand("mobracers leave");
		}

	}
}
