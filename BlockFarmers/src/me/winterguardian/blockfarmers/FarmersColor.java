package me.winterguardian.blockfarmers;

import me.winterguardian.core.message.Message;
import me.winterguardian.core.sorting.AntiRecursiveRandomSelector;
import me.winterguardian.core.sorting.Selector;
import me.winterguardian.core.util.FireworkUtil;

import org.bukkit.Color;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum FarmersColor
{
	RED(BlockFarmersMessage.COLOR_RED, new FarmBlock(Material.WOOL, 14), Color.RED, Color.WHITE),
	BLUE(BlockFarmersMessage.COLOR_BLUE, new FarmBlock(Material.WOOL, 11), Color.BLUE, Color.WHITE),
	YELLOW(BlockFarmersMessage.COLOR_YELLOW, new FarmBlock(Material.WOOL, 4), Color.YELLOW, Color.WHITE),
	LIGHT_GREEN(BlockFarmersMessage.COLOR_LIGHTGREEN, new FarmBlock(Material.WOOL, 5), Color.LIME, Color.WHITE),
	PURPLE(BlockFarmersMessage.COLOR_PURPLE, new FarmBlock(Material.WOOL, 10), Color.PURPLE, Color.WHITE),
	ORANGE(BlockFarmersMessage.COLOR_ORANGE, new FarmBlock(Material.WOOL, 1), Color.ORANGE, Color.WHITE),
	TEAL(BlockFarmersMessage.COLOR_TEAL, new FarmBlock(Material.WOOL, 9), Color.TEAL, Color.WHITE),
	PINK(BlockFarmersMessage.COLOR_PINK, new FarmBlock(Material.WOOL, 6), Color.FUCHSIA, Color.WHITE),
	
	WHITE(BlockFarmersMessage.COLOR_WHITE, new FarmBlock(Material.WOOL, 0), Color.WHITE, Color.WHITE),
	BLACK(BlockFarmersMessage.COLOR_BLACK, new FarmBlock(Material.WOOL, 15), Color.BLACK, Color.WHITE),
	LIGHT_GRAY(BlockFarmersMessage.COLOR_LIGHTGRAY, new FarmBlock(Material.WOOL, 8), Color.SILVER, Color.WHITE),
	DARK_GRAY(BlockFarmersMessage.COLOR_DARKGRAY, new FarmBlock(Material.WOOL, 7), Color.GRAY, Color.WHITE),
	MAGENTA(BlockFarmersMessage.COLOR_MAGENTA, new FarmBlock(Material.WOOL, 2), Color.FUCHSIA, Color.WHITE),
	LIGHT_BLUE(BlockFarmersMessage.COLOR_LIGHTBLUE, new FarmBlock(Material.WOOL, 3), Color.AQUA, Color.WHITE),
	BROWN(BlockFarmersMessage.COLOR_BROWN, new FarmBlock(Material.WOOL, 12), Color.MAROON, Color.WHITE),
	GREEN(BlockFarmersMessage.COLOR_GREEN, new FarmBlock(Material.WOOL, 13), Color.GREEN, Color.WHITE),
	
	;
	
	private static Selector<Type> fireworkSelector = new AntiRecursiveRandomSelector<>(Type.values());

	private Message name;
	private FarmBlock block;
	private Color color1, color2;
	
	private FarmersColor(Message name, FarmBlock block, Color color1, Color color2)
	{
		this.name = name;
		this.block = block;
		this.color1 = color1;
		this.color2 = color2;
	}
	
	public String getName()
	{
		return name.toString();
	}

	public FarmBlock getBlock()
	{
		return block;
	}
	
	public void shootFirework(Location loc)
	{
		FireworkUtil.generate(loc, fireworkSelector.next(), color1, color2, true, true, 1);
	}
}
