package me.winterguardian.core.util;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

/**
 *
 * Created by Alexander Winter on 2015-12-10.
 */
public class InventoryUtil
{
	private InventoryUtil() {}

	public static boolean canAdd(Inventory inventory, ItemStack item)
	{
		if(inventory.firstEmpty() != -1)
			return true;

		int remaining = item.getAmount();

		for(ItemStack stack : inventory.getContents())
			if(stack.isSimilar(item) && stack.getMaxStackSize() > stack.getAmount())
			{
				remaining -= stack.getMaxStackSize() - stack.getAmount();
				if(remaining <= 0)
					return true;
			}

		return false;
	}

	public static boolean canAddAtLeastOne(Inventory inventory, ItemStack item)
	{
		if(inventory.firstEmpty() != -1)
			return true;

		for(ItemStack stack : inventory.getContents())
			if(stack.isSimilar(item) && stack.getMaxStackSize() > stack.getAmount())
				return true;

		return false;
	}

	public static boolean isHelmet(ItemStack item)
	{
		return item.getType() == Material.LEATHER_HELMET
				|| item.getType() == Material.CHAINMAIL_HELMET
				|| item.getType() == Material.GOLD_HELMET
				|| item.getType() == Material.IRON_HELMET
				|| item.getType() == Material.DIAMOND_HELMET;
	}

	public static boolean isChestplate(ItemStack item)
	{
		return item.getType() == Material.LEATHER_CHESTPLATE
				|| item.getType() == Material.CHAINMAIL_CHESTPLATE
				|| item.getType() == Material.GOLD_CHESTPLATE
				|| item.getType() == Material.IRON_CHESTPLATE
				|| item.getType() == Material.DIAMOND_CHESTPLATE;
	}

	public static boolean isLeggings(ItemStack item)
	{
		return item.getType() == Material.LEATHER_LEGGINGS
				|| item.getType() == Material.CHAINMAIL_LEGGINGS
				|| item.getType() == Material.GOLD_LEGGINGS
				|| item.getType() == Material.IRON_LEGGINGS
				|| item.getType() == Material.DIAMOND_LEGGINGS;
	}

	public static boolean isBoots(ItemStack item)
	{
		return item.getType() == Material.LEATHER_BOOTS
				|| item.getType() == Material.CHAINMAIL_BOOTS
				|| item.getType() == Material.GOLD_BOOTS
				|| item.getType() == Material.IRON_BOOTS
				|| item.getType() == Material.DIAMOND_BOOTS;
	}

	public static void addConvenient(PlayerInventory inventory, ItemStack item)
	{
		if(isHelmet(item))
		{
			ItemStack oldHelmet = inventory.getHelmet();

			if(oldHelmet != null)
				inventory.addItem(oldHelmet);
			inventory.setHelmet(item);
		}
		else if(isChestplate(item))
		{
			ItemStack oldChestplate = inventory.getChestplate();

			if(oldChestplate != null)
				inventory.addItem(oldChestplate);

			inventory.setChestplate(item);
		}
		else if(isLeggings(item))
		{
			ItemStack oldLeggings = inventory.getLeggings();

			if(oldLeggings != null)
				inventory.addItem(oldLeggings);

			inventory.setLeggings(item);
		}
		else if(isBoots(item))
		{
			ItemStack oldBoots = inventory.getBoots();

			if(oldBoots != null)
				inventory.addItem(oldBoots);

			inventory.setBoots(item);
		}
		else
			inventory.addItem(item);
	}
}
