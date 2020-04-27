package me.winterguardian.pvp.game;

import me.winterguardian.core.shop.PlayerPurchaseEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.Map;

/**
 * Undocumented :(
 * <p>
 * Created on 2020-04-27.
 *
 * @author Alexander Winter
 */
public class PvPPurchaseHistory implements Listener
{
	private final Map<Player, Integer> purchaseAmount = new HashMap<>();

	public void join(Player player)
	{
		purchaseAmount.put(player, 0);
	}

	public void leave(Player player)
	{
		purchaseAmount.remove(player);
	}

	public void reset()
	{
		for(Player player : purchaseAmount.keySet())
			purchaseAmount.put(player, 0);
	}

	public int getAmount(Player player)
	{
		return purchaseAmount.get(player);
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerPurchase(PlayerPurchaseEvent event)
	{
		if(!purchaseAmount.containsKey(event.getPlayer()))
			return;

		purchaseAmount.put(event.getPlayer(), purchaseAmount.get(event.getPlayer()) + event.getPurchase().getPrice());
	}
}
