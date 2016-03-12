package me.winterguardian.hub.listener;

import me.winterguardian.core.game.Game;
import me.winterguardian.core.game.GameManager;
import me.winterguardian.core.game.SekaiGame;
import me.winterguardian.core.inventorygui.InventoryGUI;
import me.winterguardian.core.message.HardcodedMessage;
import me.winterguardian.hub.Hub;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 *
 * Created by Alexander Winter on 2015-12-19.
 */
public class GUIListener implements Listener
{
	private InventoryGUI gameSelection;
	private Hub hub;

	public GUIListener(Hub hub)
	{
		this.gameSelection = new InventoryGUI("§f§lSekai§6§lMC §8§lMenu", 36);

		Bukkit.getScheduler().runTask(hub, new Runnable()
		{
			public void run()
			{
				for(Game game : GameManager.getGames())
					if(game instanceof SekaiGame && ((SekaiGame)game).getGUIItem() != null)
						gameSelection.getItems().add(((SekaiGame)game).getGUIItem());

				gameSelection.getItems().add(new CreaItem());
				gameSelection.getItems().add(new FactionItem());
			}
		});

		this.hub = hub;
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerInteract(PlayerInteractEvent event)
	{
		if(!hub.contains(event.getPlayer()))
			return;

		if(event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK)
			return;

		if(event.getItem() == null)
			return;

		switch(event.getItem().getType())
		{
			case COMPASS:
				this.gameSelection.open(event.getPlayer());
				break;

			case GOLD_INGOT:
				event.getPlayer().performCommand("buy");
				break;

			case RED_ROSE:
				new HardcodedMessage("Vote et tu recevras §a1000 §fpoints !\\n§b§nvote.sekaimc.net", true).say(event.getPlayer());
				break;

			case CHEST:
				new HardcodedMessage("Les gadgets arrivent bientôt sur Sekai !", true).say(event.getPlayer());
				break;

			default:
				return;
		}

		event.setCancelled(true);
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event)
	{
		this.gameSelection.onInventoryClick(event);
	}
}
