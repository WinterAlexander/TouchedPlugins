package me.winterguardian.duel;

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
public class DuelHubItem extends GUIItem
{
	private Duel game;

	public DuelHubItem(Duel game)
	{
		super(19);
		this.game = game;
	}

	@Override
	public void click(Player player, ClickType click)
	{
		if(click.isLeftClick())
		{
			player.performCommand("duel");
			return;
		}

		player.performCommand("duel stats");
	}

	@Override
	public ItemStack getItemStack(Player player)
	{
		ItemStack pvpItem = new ItemStack(Material.DIAMOND_CHESTPLATE, 1);
		ItemMeta pvpMeta = pvpItem.getItemMeta();
		pvpMeta.setDisplayName("§6§lDuel");
		List<String> pvpLore = new ArrayList<>();

		int pvpPlayers = game.getPlayersInRegion().size();
		pvpLore.add("§e" + pvpPlayers + " joueur" + (pvpPlayers > 1 ? "s" : ""));

		if(game.getGame().getPlayer1() != null && game.getGame().getPlayer2() != null)
		{
			pvpLore.add("§c" + game.getGame().getPlayer1().getName() + "§f vs §9" + game.getGame().getPlayer2().getName());
		}
		else if(game.getGame().getPlayer1() != null)
		{
			pvpLore.add("§c" + game.getGame().getPlayer1().getName() + "§f en attente");
		}

		pvpLore.add(" ");
		pvpLore.add("§aClic gauche pour jouer");
		pvpLore.add("§eClic droit pour les stats");

		pvpMeta.setLore(pvpLore);

		pvpItem.setItemMeta(pvpMeta);

		return pvpItem;
	}
}
