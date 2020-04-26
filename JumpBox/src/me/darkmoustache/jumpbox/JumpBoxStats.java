package me.darkmoustache.jumpbox;

import me.winterguardian.core.playerstats.MappedData;
import me.winterguardian.core.playerstats.PlayerStats;
import me.winterguardian.core.util.TextUtil;
import me.winterguardian.core.world.SerializableLocation;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;

import java.util.HashMap;

public class JumpBoxStats extends PlayerStats
{
	private OfflinePlayer player;

	public JumpBoxStats(OfflinePlayer player, MappedData data)
	{
		super(data);
		this.player = player;
	}
	
	public Location getLocation()
	{
		if(getSerializableLocation() != null)
			return getSerializableLocation().getLocation();
		return null;
	}
	
	public void setLocation(Location location)
	{
		if(location != null)
			this.getContent().set("jumpbox.location", new SerializableLocation(location).toString());
		else
			this.getContent().set("jumpbox.location", null);
	}
	
	public SerializableLocation getLastJump()
	{
		if(this.getContent().isString("jumpbox.lastjump"))
			return SerializableLocation.fromString(this.getContent().getString("jumpbox.lastjump"));
		return null;
	}
	
	public void setLastJumpLocation(Location location)
	{
		if(location != null)
			this.getContent().set("jumpbox.lastjump", new SerializableLocation(location).toString());
		else
			this.getContent().set("jumpbox.lastjump", null);
	}
	
	public SerializableLocation getSerializableLocation()
	{
		if(this.getContent().isString("jumpbox.location"))
			return SerializableLocation.fromString(this.getContent().getString("jumpbox.location"));
		return null;
	}

	public void addJumpBoxPoints(int points)
	{
		this.getContent().set("jumpbox.score", this.getContent().getInt("jumpbox.score") + points);
		this.addPoints(points);
		if(this.player.isOnline())
			this.player.getPlayer().sendMessage("§aPoints +" + points);
	}

	public int getEasyFinished()
	{
		return this.getContent().getInt("jumpbox.easyfinished");
	}

	public void setEasyFinished(int easyFinished)
	{
		this.getContent().set("jumpbox.easyfinished", easyFinished);
	}

	public int getMediumFinished()
	{
		return this.getContent().getInt("jumpbox.mediumfinished");
	}

	public void setMediumFinished(int mediumFinished)
	{
		this.getContent().set("jumpbox.mediumfinished", mediumFinished);
	}

	public int getHardFinished()
	{
		return this.getContent().getInt("jumpbox.hardfinished");
	}

	public void setHardFinished(int hardFinished)
	{
		this.getContent().set("jumpbox.hardfinished", hardFinished);
	}

	public int getExpertFinished()
	{
		return this.getContent().getInt("jumpbox.expertfinished");
	}

	public void setExpertFinished(int expertFinished)
	{
		this.getContent().set("jumpbox.expertfinished", expertFinished);
	}

	public boolean isFakeCheckpoint()
	{
		return this.getContent().getBoolean("jumpbox.fakecheckpoint");
	}

	public void setFakeCheckpoint(boolean fakeCheckpoint)
	{
		this.getContent().set("jumpbox.fakecheckpoint", fakeCheckpoint);
	}

	public int getScore()
	{
		return this.getContent().getInt("jumpbox.score");
	}

	public static HashMap<String, String> getTables()
	{
		return TextUtil.map("jumpbox->score INTEGER, easyfinished INTEGER, mediumfinished INTEGER, hardfinished INTEGER, expertfinished INTEGER, fakecheckpoint BOOLEAN, location TEXT, lastjump TEXT");
	}
}
