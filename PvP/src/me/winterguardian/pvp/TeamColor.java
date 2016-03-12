package me.winterguardian.pvp;

public enum TeamColor
{
	NONE("neutre", "neutres", "§7", "§f[§7N§f]§7 ", (byte)0),
	RED("rouge", "rouges", "§c", "§f[§4R§f]§c ",(byte)14),
	BLUE("bleue", "bleus", "§9", "§f[§1B§f]§9 ",(byte)11),
	YELLOW("jaune", "jaunes", "§e", "§f[§6J§f]§e ",(byte)4),
	GREEN("verte", "verts", "§a", "§f[§2V§f]§a ",(byte)5),
	PINK("rose", "roses", "§d", "§f[§5R§f]§d ",(byte)6),
	TEAL("cyan", "cyans", "§b", "§f[§3C§f]§b ",(byte)9),
	BLACK("noire", "noirs", "§8", "§f[§0N§f]§8 ",(byte)15),
	WHITE("blanche", "blancs", "§f", "§f[B§f]§7 ",(byte)8);

	private String name, namePlural, bukkitColor, boardPrefix;
	private byte color;
	
	private TeamColor(String name, String namePlural, String bukkitColor, String boardPrefix, byte color)
	{
		this.name = name;
		this.namePlural = namePlural;
		this.bukkitColor = bukkitColor;
		this.color = color;
		this.boardPrefix = boardPrefix;
	}
	
	public byte getWoolColor()
	{
		return color;
	}

	public String getName()
	{
		return this.name;
	}

	public String getNamePlural()
	{
		return this.namePlural;
	}

	public String getBukkitColor()
	{
		return this.bukkitColor;
	}

	public String getBoardPrefix()
	{
		return this.boardPrefix;
	}

	@Override
	public String toString()
	{
		return getBukkitColor() + getNamePlural();
	}
}
