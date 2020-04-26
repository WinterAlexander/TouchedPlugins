package me.winterguardian.pvp;

import me.winterguardian.core.inventorygui.GUIItem;
import me.winterguardian.pvp.game.PvPMatch;
import me.winterguardian.pvp.game.PvPVoteState;
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
public class PvPGUIItem extends GUIItem
{
	private PvP game;

	public PvPGUIItem(PvP game)
	{
		super(10);
		this.game = game;
	}

	@Override
	public void click(Player player, ClickType click)
	{
		if(click.isLeftClick())
		{
			player.performCommand("pvp join");
			return;
		}

		player.performCommand("pvp stats");
	}

	@Override
	public ItemStack getItemStack(Player player)
	{
		ItemStack pvpItem = new ItemStack(Material.IRON_SWORD, 1);
		ItemMeta pvpMeta = pvpItem.getItemMeta();
		pvpMeta.setDisplayName("§f§lTouched§4§lPvP");
		List<String> pvpLore = new ArrayList<>();

		int pvpPlayers = game.getPlayers().size();
		pvpLore.add("§e" + pvpPlayers + " joueur" + (pvpPlayers > 1 ? "s" : ""));

		if(game.getState() instanceof PvPMatch)
		{
			PvPMatch match = (PvPMatch)game.getState();

			pvpLore.add(match.getColoredName());
			pvpLore.add("§fSur " + match.getArena().getName());
		}
		else if(game.getState() instanceof PvPVoteState)
		{
			PvPVoteState vote = (PvPVoteState)game.getState();

			pvpLore.add(vote.getNextGame().getColoredName());
			pvpLore.add("§fEn votes");
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
