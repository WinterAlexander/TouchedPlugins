package me.winterguardian.pvp.stats;

import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Golem;
import org.bukkit.entity.Horse;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;

public enum Bonus
{
	FIRSTKILL("Premier sang", 50),
	WOLFKILL("Tueur de loups", 5), HORSEKILL("Tueur de chevaux", 8), GOLEMKILL("Tueur de golems", 10), DRAGONKILL("Dragon slayer", 25),
	HEADSHOT("Tir dans la tête", 2), LONGSHOT("Tir de loin", 3), VERYLONGSHOT("Sniper", 10),
	DOUBLEKILL("Double meurtre", 20), TRIPLEKILL("Triple meurte", 50), MULTIKILL("Multi meurtre", 100),
	CTF_FLAGBACK("Sauveur de drapeaux", 25),
	DOM_DEFENSE("Défense", 10), DOM_OFFENSE("Offense", 10), DOM_NEUTRALIZE("Neutralisation", 5), DOM_CAPTURE("Capture de zone", 10),
	PUMPKIN_HEAD("Tête de citrouille", 10),
	
	KILLSTREAK_3("Série de victimes", 5), 
	KILLSTREAK_5("Série de victimes", 10), 
	KILLSTREAK_8("Tueur en séries", 25), 
	KILLSTREAK_10("Massacre", 50), 
	KILLSTREAK_15("Carnage", 75), 
	KILLSTREAK_20("Catastrophe", 100), 
	KILLSTREAK_30("Génocide", 250), 
	KILLSTREAK_50("Génocide Légendaire", 500), 
	KILLSTREAK_100("Fin du monde", 1000), 
	
	KILLSTREAK_BREAKER("Casseur de séries", 3),
	TEAM_POTION_HEAL("Médecin", 20), TEAM_POTION_OTHER("Travail d'équipe", 20),
	;
	
	private final String message;
	private int value;
	
	private Bonus(String message)
	{
		this(message, 10);
	}
	
	private Bonus(String message, int value)
	{
		this.message = message;
		this.value = value;
	}
	
	public int getValue()
	{
		return this.value;
	}

	public void setValue(int value)
	{
		this.value = value;
	}
	
	public void announce(Player player)
	{
		if(player != null)
			if(player.getPlayer().isOnline())
				player.getPlayer().getPlayer().sendMessage("§c§lBonus §f§l> §f" + this.getMessage() + " (" + this.getValue() + " points)");
	}
	
	public String getMessage()
	{
		return this.message;
	}
	
	public static Bonus getMobKillBonus(LivingEntity entity)
	{
		if(entity instanceof Wolf)
			return Bonus.WOLFKILL;
		if(entity instanceof Horse)
			return Bonus.HORSEKILL;
		if(entity instanceof Golem)
			return Bonus.GOLEMKILL;
		if(entity instanceof EnderDragon)
			return Bonus.DRAGONKILL;
		return null;
	}
}
