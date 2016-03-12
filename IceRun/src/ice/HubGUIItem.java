package ice;

import me.winterguardian.core.inventorygui.GUIItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by Alexander Winter on 2016-01-04.
 */
public class HubGUIItem extends GUIItem
{
	public HubGUIItem()
	{
		super(32);
	}

	@Override
	public void click(Player player, ClickType click)
	{
		if(click.isLeftClick())
		{
			player.performCommand("ir join");
			return;
		}

		player.performCommand("ir stats");
	}

	@Override
	public ItemStack getItemStack(Player player)
	{
		ItemStack pvpItem = new ItemStack(Material.ICE, 1);
		ItemMeta pvpMeta = pvpItem.getItemMeta();
		pvpMeta.setDisplayName("§b§lIceRun");
		List<String> pvpLore = new ArrayList<>();

		int pvpPlayers = IceRun.players.size();
		pvpLore.add("§e" + pvpPlayers + " joueur" + (pvpPlayers > 1 ? "s" : ""));

		if(IceRun.status instanceof IceRunGame)
		{
			pvpLore.add("§fEn jeu");
		}
		else if(IceRun.status instanceof WaitingSession)
		{
			pvpLore.add("§fCommence bientôt");
		}
		else
		{
			pvpLore.add("§fEn attente");
		}

		pvpLore.add(" ");
		pvpLore.add("§aClic gauche pour jouer");
		pvpLore.add("§eClic droit pour les stats");

		pvpMeta.setLore(pvpLore);

		pvpItem.setItemMeta(pvpMeta);

		return pvpItem;
	}
}
