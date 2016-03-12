package me.winterguardian.soundsapi;


import java.util.Collection;

import net.minecraft.server.v1_8_R3.PacketPlayOutNamedSoundEffect;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.CraftSound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class SoundEffect
{
	private String sound;
	private float volume, pitch;
	
	public SoundEffect(String vanillaName, float volume, float pitch)
	{
		this.sound = vanillaName;
		this.volume = volume;
		this.pitch = pitch;
	}
	
	public SoundEffect(Sound bukkitSound, float volume, float pitch)
	{
		this.sound = CraftSound.getSound(bukkitSound);
		this.volume = volume;
		this.pitch = pitch;
	}
	
	public void play(Player p)
	{
		((CraftPlayer)p).getHandle().playerConnection.sendPacket(new PacketPlayOutNamedSoundEffect(this.sound, p.getEyeLocation().getX(), p.getEyeLocation().getY(), p.getEyeLocation().getZ(), volume, pitch));
	}
	
	public void play(Player p, Location loc)
	{
		((CraftPlayer)p).getHandle().playerConnection.sendPacket(new PacketPlayOutNamedSoundEffect(this.sound, loc.getX(), loc.getY(), loc.getZ(), volume, pitch));
	}
	
	public void play(Collection<Player> players, Location loc)
	{
		for(Player p : players)
			this.play(p, loc);
	}
}
