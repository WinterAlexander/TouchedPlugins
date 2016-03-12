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
public class FactionItem extends ServerItem
{
	public FactionItem()
	{
		super(12, "faction");
	}

	@Override
	public ItemStack getItemStack(Player player)
	{
		int facPlayers = Core.getBungeeMessager().getPlayerCount("faction", 0);

		ItemStack item = new ItemStack(Material.DIAMOND_ORE, 1, (short)0);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§2§lFactions");
		meta.setLore(Arrays.asList("§e" + facPlayers + " connecté" + (facPlayers > 1 ? "s" : ""), "§4§lBÉTA", "§aUn clic vous téléporte !"));
		item.setItemMeta(meta);
		return item;
	}
}