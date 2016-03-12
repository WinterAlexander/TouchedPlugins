package me.winterguardian.soundsapi;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class SoundsExample
{
	public void playGuardianSound(Player player)
	{
		new SoundEffect("mob.guardian.curse", 1f, 1f).play(player);
	}
	
	public void playHighPitchedSheepSound(Player player)
	{
		new SoundEffect(Sound.SHEEP_IDLE, 1f, 1.3f).play(player);
	}
}
