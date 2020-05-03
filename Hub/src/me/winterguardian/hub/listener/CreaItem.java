package me.winterguardian.hub.listener;

import me.winterguardian.core.Core;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

/**
 *
 * Created by Alexander Winter on 2016-01-15.
 */
public class CreaItem extends ServerItem
{
	public CreaItem()
	{
		super(16, "crea");
	}

	@Override
	public ItemStack getItemStack(Player player)
	{
		int creaPlayers = Core.getBungeeMessager().getPlayerCount("crea", 0);

		ItemStack item = new ItemStack(Material.WOOD_STAIRS, 1, (short)0);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§a§lCréatif");
		meta.setLore(Arrays.asList("§e" + creaPlayers + " connecté" + (creaPlayers > 1 ? "s" : ""), "§aUn clic vous téléporte !"));
		item.setItemMeta(meta);
		return item;
	}
}
