package me.winterguardian.pvp.game.solo;

import me.winterguardian.core.message.Message;
import me.winterguardian.core.shop.PlayerPurchaseEvent;
import me.winterguardian.core.sorting.AntiRecursiveRandomSelector;
import me.winterguardian.core.sorting.Selector;
import me.winterguardian.core.util.PlayerUtil;
import me.winterguardian.pvp.GameStuff;
import me.winterguardian.pvp.PvP;
import me.winterguardian.pvp.PvPMessage;
import me.winterguardian.pvp.game.PvPPlayerData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by Alexander Winter on 2016-01-07.
 */
public class Switch extends SoloGame
{
	private Selector<GameStuff> stuffSelector;

	private GameStuff currentStuff;

	public Switch(PvP game)
	{
		super(game);

		List<GameStuff> stuffList = new ArrayList<>();
		for(String string : GameStuff.listNames())
			if(string != null && string.startsWith("swi"))
			{
				GameStuff stuff = new GameStuff(string);
				stuffList.add(stuff);
				stuff.load();
			}

		this.stuffSelector = new AntiRecursiveRandomSelector<>(stuffList);

		this.currentStuff = this.stuffSelector.next();
	}

	@Override
	public void start()
	{
		super.start();
		register(new Listener()
		{
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
					getNewStuff(event.getPlayer(), false).give(event.getPlayer());
			}

			@EventHandler(priority = EventPriority.HIGHEST)
			public void onInventoryClick(InventoryClickEvent event)
			{
				if(!(event.getWhoClicked() instanceof Player))
					return;

				Player player = (Player)event.getWhoClicked();

				if(!getGame().contains(player))
					return;

				if(InventoryType.SlotType.ARMOR.equals(event.getSlotType()))
					event.setCancelled(true);
			}
		});


	}

	@Override
	public int getScore(PvPPlayerData data)
	{
		return data.getKills();
	}

	@Override
	public int getSecondFactor(PvPPlayerData data)
	{
		return -data.getDeaths();
	}

	@Override
	public GameStuff getNewStuff(Player player, boolean gameStart)
	{
		return this.currentStuff;
	}

	@Override
	public String getName()
	{
		return "Switch";
	}

	@Override
	public String getColoredName()
	{
		return "§d§l" + getName();
	}

	@Override
	public Message getStartMessage()
	{
		return PvPMessage.GAME_START_SWI;
	}

	@Override
	public Message getGuide()
	{
		return PvPMessage.GAME_GUIDE_SWI;
	}

	@Override
	public int getVoteTimer()
	{
		return 25;
	}

	@Override
	public boolean canBuyInLobby()
	{
		return false;
	}

	@Override
	public void run()
	{
		super.run();

		if(getTimer() != 0 && getTimer() % 30 == 0)
		{
			this.currentStuff = this.stuffSelector.next();
			for(PvPPlayerData data : getPlayerDatas())
			{
				data.setStuff(new GameStuff(this.currentStuff));

				if(!data.isPlaying())
					continue;

				PlayerUtil.clearInventory(data.getPlayer());
				for(PotionEffect effect : data.getPlayer().getActivePotionEffects())
					data.getPlayer().removePotionEffect(effect.getType());
				data.getEffects().clear();
				this.currentStuff.give(data.getPlayer());
				data.getPlayer().sendMessage("§lSwitch !!!");
			}
		}
	}
}
