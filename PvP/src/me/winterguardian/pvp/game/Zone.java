package me.winterguardian.pvp.game;

import me.winterguardian.core.util.CollectionsUtil;
import me.winterguardian.core.world.Region;
import me.winterguardian.pvp.TeamColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockState;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by Alexander Winter on 2015-12-14.
 */
public class Zone
{
	private TeamColor owner, capturer;
	private Location loc;
	private int captureLevel;
	
	private List<BlockState> backup;
	private List<Location> toColor, toColorWhenComplete;

	public Zone(Location loc)
	{
		this.loc = loc;
		this.owner = TeamColor.NONE;
		this.capturer = null;
		this.captureLevel = 0;
		
		this.backup = new ArrayList<>();
		this.toColor = new ArrayList<>();
		this.toColorWhenComplete = new ArrayList<>();
	}

	public Region getRegion()
	{
		return new Region()
		{
			@Override
			public boolean contains(Location in)
			{
				if(in.getBlockX() > loc.getBlockX() + 2)
					return false;

				if(in.getBlockX() < loc.getBlockX() - 2)
					return false;

				if(in.getBlockZ() > loc.getBlockZ() + 2)
					return false;

				if(in.getBlockZ() < loc.getBlockZ() - 2)
					return false;

				if(in.getBlockY() >= loc.getBlockY() + 5)
					return false;

				if(in.getBlockY() < loc.getBlockY() - 1)
					return false;

				return true;
			}

			@Override
			public World getWorld()
			{
				return loc.getWorld();
			}
		};
	}

	public TeamColor getOwner()
	{
		return owner;
	}


	public void capture(TeamColor color, int players)
	{
		if(color == null)
		{
			if(this.captureLevel == 0)
				return;

			this.captureLevel -= 5;

			if(this.captureLevel <= 0)
			{
				this.captureLevel = 0;
				this.capturer = null;
			}
			color();
			return;
		}

		if(players == 0)
			return;

		if(color == this.getOwner() && captureLevel == 0)
			return;

		if(this.capturer == null)
			this.capturer = color;

		if(color != this.capturer)
			this.captureLevel -= players * 10;

		if(color == this.capturer)
			this.captureLevel += players * 10;

		if(this.captureLevel >= 100)
		{
			this.captureLevel = 0;
			if(this.owner == TeamColor.NONE)
				this.owner = capturer;
			else
				this.owner = TeamColor.NONE;
			this.capturer = null;
		}
		else if(this.captureLevel <= 0)
		{
			this.captureLevel = 0;
			this.capturer = null;
		}

		color();
	}

	@SuppressWarnings("deprecation")
	private void color()
	{
		for(Location loc : toColorWhenComplete)
			loc.getBlock().setData(getOwner().getWoolColor(), false);

		if(this.capturer == null)
		{
			for(Location loc : toColor)
				loc.getBlock().setData(getOwner().getWoolColor(), false);
			return;
		}

		byte capturerDye = getOwner() == TeamColor.NONE ? this.capturer.getWoolColor() : TeamColor.NONE.getWoolColor();
		int capturerBlockCount = (int)(toColor.size() * (captureLevel / 100d));

		for(Location loc : CollectionsUtil.mixList(toColor))
		{
			if(capturerBlockCount > 0)
			{
				loc.getBlock().setData(capturerDye, false);
				capturerBlockCount--;
				continue;
			}
			loc.getBlock().setData(getOwner().getWoolColor(), false);
		}
	}

	public void place()
	{
		//backup
		for(int x = loc.getBlockX() - 2; x <= loc.getBlockX() + 2; x++)
			for(int y = loc.getBlockY() - 1; y <= loc.getBlockY() + 4; y++)
				for(int z = loc.getBlockZ() - 2; z <= loc.getBlockZ() + 2; z++)
					this.backup.add(loc.getWorld().getBlockAt(x, y, z).getState());

		//void
		for(int x = loc.getBlockX() - 2; x <= loc.getBlockX() + 2; x++)
			for(int y = loc.getBlockY(); y <= loc.getBlockY() + 4; y++)
				for(int z = loc.getBlockZ() - 2; z <= loc.getBlockZ() + 2; z++)
					loc.getWorld().getBlockAt(x, y, z).setType(Material.AIR, false);

		//platform
		for(int x = loc.getBlockX() - 2; x <= loc.getBlockX() + 2; x++)
			for(int z = loc.getBlockZ() - 2; z <= loc.getBlockZ() + 2; z++)
			{
				int y = loc.getBlockY() - 1;
				if(Math.abs(x - loc.getBlockX()) == Math.abs(z - loc.getBlockZ()) && Math.abs(x - loc.getBlockX()) == 2)
					continue;

				if(Math.abs(x - loc.getBlockX()) == Math.abs(z - loc.getBlockZ()) && Math.abs(x - loc.getBlockX()) == 0)
				{
					this.toColorWhenComplete.add(new Location(loc.getWorld(), x, y, z));
					loc.getWorld().getBlockAt(x, loc.getBlockY() - 1, z).setType(Material.STAINED_GLASS_PANE, false);
					continue;
				}
				this.toColor.add(new Location(loc.getWorld(), x, y, z));
				loc.getWorld().getBlockAt(x, y, z).setType(Material.WOOL, false);
			}

		//pillars
		for(int x : new int[]{2, -2})
			for(int y = 0; y < 5; y++)
				for(int z : new int[]{2, -2})
				{
					Location newLoc = loc.clone().add(x, y, z);
					if(y < 3)
					{
						newLoc.getBlock().setType(Material.COBBLE_WALL, false);
						continue;
					}

					if(y == 3)
					{
						this.toColorWhenComplete.add(newLoc);
						newLoc.getBlock().setType(Material.WOOL, false);
						continue;
					}

					newLoc.getBlock().setType(Material.STEP, false);
				}

		color();
	}

	public void color(TeamColor color, boolean full)
	{
		for(Location loc : toColorWhenComplete)
			loc.getBlock().setData(color.getWoolColor(), false);

		if(full)
			for(Location loc : toColor)
				loc.getBlock().setData(color.getWoolColor(), false);
	}

	public void dispose()
	{
		for(BlockState state : this.backup)
			state.update(true, false);

		this.backup.clear();
	}
}
