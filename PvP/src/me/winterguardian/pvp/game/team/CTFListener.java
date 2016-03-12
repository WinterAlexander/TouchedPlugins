package me.winterguardian.pvp.game.team;

import me.winterguardian.pvp.PvP;
import me.winterguardian.pvp.PvPMessage;
import me.winterguardian.pvp.TeamColor;
import me.winterguardian.pvp.game.PvPPlayerData;
import me.winterguardian.pvp.stats.Bonus;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

/**
 *
 * Created by Alexander Winter on 2015-12-13.
 */
public class CTFListener implements Listener
{
	private CaptureTheFlag game;

	public CTFListener(CaptureTheFlag game)
	{
		this.game = game;
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event)
	{
		if(!game.getGame().contains(event.getPlayer()))
			return;

		if(event.getClickedBlock() == null)
			return;

		if(event.getAction() != Action.RIGHT_CLICK_BLOCK)
			return;

		PvPPlayerData data = game.getPlayerData(event.getPlayer());

		if(data == null || !data.isPlaying() || event.getPlayer().isDead())
			return;

		TeamColor color = game.getArena().getColor(event.getClickedBlock());

		if(color == null)
			return;

		game.clickFlag(event.getPlayer(), color);
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerDeath(PlayerDeathEvent event)
	{
		if(!game.isHolder(event.getEntity()))
			return;

		PvPPlayerData victimData = game.getPlayerData(event.getEntity());

		if(victimData == null || !victimData.isPlaying())
			return;

		PvPPlayerData damagerData = game.getPlayerData(victimData.getLastDamager());

		if(damagerData == null || !damagerData.isPlaying())
			return;

		TeamColor team = game.getFlagHolded(event.getEntity());

		PvPMessage.GAME_CTF_FLAGBACK.sayPlayers("<color>", team.toString());
		game.getFlagHolder().remove(team);
		game.getArena().spawnFlag(team);
		if(team == damagerData.getTeam())
			damagerData.addBonus(Bonus.CTF_FLAGBACK);

	}
}
