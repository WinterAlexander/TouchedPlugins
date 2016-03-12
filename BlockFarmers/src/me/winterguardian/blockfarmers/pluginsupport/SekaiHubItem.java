package me.winterguardian.blockfarmers.pluginsupport;

import me.winterguardian.blockfarmers.BlockFarmersGame;
import me.winterguardian.blockfarmers.state.BlockFarmersClappingState;
import me.winterguardian.blockfarmers.state.BlockFarmersFarmingState;
import me.winterguardian.blockfarmers.state.BlockFarmersStandbyState;
import me.winterguardian.blockfarmers.state.BlockFarmersWaitingState;
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
public class SekaiHubItem extends GUIItem
{
	private BlockFarmersGame game;

	public SekaiHubItem(BlockFarmersGame game)
	{
		super(23);
		this.game = game;
	}

	@Override
	public void click(Player player, ClickType click)
	{
		if(click.isLeftClick())
		{
			player.performCommand("blockfarmers join");
			return;
		}

		player.performCommand("blockfarmers stats");
	}

	@Override
	public ItemStack getItemStack(Player player)
	{
		ItemStack hoe = new ItemStack(Material.STONE_HOE, 1);
		ItemMeta meta = hoe.getItemMeta();
		meta.setDisplayName("§c§lBlock§7§lFarmers");
		List<String> lore = new ArrayList<>();

		int players = game.getPlayers().size();
		lore.add("§e" + players + " joueur" + (players > 1 ? "s" : ""));

		if(game.getState() instanceof BlockFarmersFarmingState)
		{
			lore.add("§fEn jeu");
		}
		else if(game.getState() instanceof BlockFarmersClappingState)
		{
			lore.add("§fVictoire de");
			lore.add("§f" + ((BlockFarmersClappingState)game.getState()).getWinner().getName());
		}
		else if(game.getState() instanceof BlockFarmersWaitingState)
		{
			lore.add("§fCommence bientôt");
		}
		else if(game.getState() instanceof BlockFarmersStandbyState)
		{
			lore.add("§fEn attente");
		}

		lore.add(" ");
		lore.add("§aClic gauche pour jouer");
		lore.add("§eClic droit pour les stats");

		meta.setLore(lore);

		hoe.setItemMeta(meta);

		return hoe;
	}
}
