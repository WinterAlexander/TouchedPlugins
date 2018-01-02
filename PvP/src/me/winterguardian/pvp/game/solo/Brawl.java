package me.winterguardian.pvp.game.solo;

import me.winterguardian.core.message.Message;
import me.winterguardian.core.shop.PlayerPurchaseEvent;
import me.winterguardian.pvp.GameStuff;
import me.winterguardian.pvp.PvP;
import me.winterguardian.pvp.PvPMessage;
import me.winterguardian.pvp.game.PvPPlayerData;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.HashMap;
import java.util.UUID;

/**
 * Undocumented :(
 * <p>
 * Created by Alexander Winter on 2018-01-02.
 */
public class Brawl extends SoloGame
{
	private GameStuff brawlStuff;
	private HashMap<UUID, Integer> scores = new HashMap<>();
	private boolean updateBoard = false;

	public Brawl(PvP game)
	{
		super(game);
		this.timer = 180;

		this.brawlStuff = new GameStuff("brawl");
		this.brawlStuff.load();
	}

	@Override
	public void start()
	{
		super.start();
		register(new BrawlListener());
	}

	@Override
	public void run()
	{
		super.run();
		if(updateBoard)
		{
			updateBoard();
			updateBoard = false;
		}
	}

	@Override
	public int getScore(PvPPlayerData data)
	{
		if(data == null || data.getPlayer() == null)
			return 0;

		if(!this.scores.containsKey(data.getPlayer().getUniqueId()))
			return 0;
		return this.scores.get(data.getPlayer().getUniqueId());
	}

	@Override
	public int getSecondFactor(PvPPlayerData data)
	{
		return 0;
	}

	@Override
	public GameStuff getNewStuff(Player player)
	{
		return brawlStuff;
	}

	@Override
	public String getName()
	{
		return "Baston";
	}

	@Override
	public String getColoredName()
	{
		return "§c§l" + getName();
	}

	@Override
	public Message getStartMessage()
	{
		return PvPMessage.GAME_START_BRAWL;
	}

	@Override
	public Message getGuide()
	{
		return PvPMessage.GAME_GUIDE_BRAWL;
	}

	public int getVoteTimer()
	{
		return 25;
	}

	private void increment(Player player)
	{
		scores.put(player.getUniqueId(), getScore(getPlayerData(player)) + 1);
		updateBoard = true;
	}

	public class BrawlListener implements Listener
	{
		@EventHandler
		public void onEntityDamageByEntity(EntityDamageByEntityEvent event)
		{
			if(!(event.getEntity() instanceof Player) || !getGame().contains((Player)event.getEntity()))
				return;

			if(event.getDamager() instanceof Player)
			{
				Player damager = (Player)event.getDamager();

				if(!getGame().contains(damager))
					return;

				increment(damager);
				return;
			}

			if(event.getDamager() instanceof Arrow)
			{
				Arrow arrow = (Arrow)event.getDamager();

				if(!(arrow.getShooter() instanceof Player))
					return;

				Player damager = (Player)arrow.getShooter();

				if(!getGame().contains(damager))
					return;

				increment(damager);
				return;
			}
		}

		@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
		public void onEntityDamage(EntityDamageEvent event)
		{
			if(!(event.getEntity() instanceof Player) || !getGame().contains((Player)event.getEntity()))
				return;

			Player player = (Player)event.getEntity();

			event.setDamage(0);
			player.setHealth(player.getMaxHealth());
			getPlayerData(player).noDamage();
		}

		@EventHandler
		public void onPlayerPurchase(PlayerPurchaseEvent event)
		{
			if(getGame().contains(event.getPlayer()))
				event.setCancelled(true);
		}

		@EventHandler(priority = EventPriority.HIGHEST)
		public void onPlayerRespawn(PlayerRespawnEvent event)
		{
			if(getGame().contains(event.getPlayer()))
				getNewStuff(event.getPlayer()).give(event.getPlayer());
		}
	}
}
