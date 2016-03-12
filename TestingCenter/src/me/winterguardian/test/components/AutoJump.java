package me.winterguardian.test.components;

import me.winterguardian.core.world.Region;
import me.winterguardian.core.world.SerializableRegion;
import me.winterguardian.test.TestComponent;
import me.winterguardian.test.TestingCenter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 *
 * Created by Alexander Winter on 2016-02-06.
 */
public class AutoJump extends TestComponent
{
	public AutoJump()
	{
		super("autojump", TestingCenter.TEST_LEVEL4, "/test autojump <difficuly> <length>");
	}

	@Override
	public boolean onSubCommand(CommandSender sender, String label, String[] args)
	{
		if(args.length != 2)
			return false;

		int length;
		Random random = new Random();
		Location loc = ((Player)sender).getLocation().add(0, -1, 0);
		List<Region> regions = new ArrayList<>();

		try
		{
			length = Integer.parseInt(args[1]);
		}
		catch(Exception e)
		{
			return false;
		}

		List<Jump> jumps = new ArrayList<>();

		switch(args[0].toLowerCase())
		{
			case "easy":
				jumps.add(new Jump(2, 0, 0));
				break;
			case "medium":

				break;
			case "hard":
				jumps.add(new Jump(5, 0, 0));
				jumps.add(new Jump(4, 0, 1));
				jumps.add(new Jump(5, 1, 0));
				jumps.add(new Jump(4, 1, 1));

				jumps.add(new Jump(3, 3, 1));
				jumps.add(new Jump(4, 3, 0));
				break;
			case "insane":
				jumps.add(new Jump(5, 0, 0));
				jumps.add(new Jump(4, 0, 1));
				jumps.add(new Jump(5, 1, 0));
				jumps.add(new Jump(4, 1, 1));
				jumps.add(new Jump(5, 2, 0));
				jumps.add(new Jump(4, 2, 1));

				jumps.add(new Jump(4, 4, 0));
				break;
			default:
				return false;


		}

		for(int i = 0; i < length; i++)
		{
			List<Vector> selection = new ArrayList<>();

			for(Jump jump : jumps)
				selection.addAll(jump.toVectors());

			Vector vector;
			boolean correct;
			do
			{
				correct = true;
				if(selection.size() == 0)
				{
					sender.sendMessage("Operation failed");
					return true;
				}

				vector = selection.get(random.nextInt(selection.size()));


				for(Region region : regions)
					if(region.contains(loc.clone().add(vector)))
					{
						correct = false;
						break;
					}

			}
			while(!correct);

			SerializableRegion region = new SerializableRegion(loc, loc.clone().add(vector));
			region.expand(BlockFace.UP, 4);
			region.expand(BlockFace.EAST, 1);
			region.expand(BlockFace.WEST, 1);
			region.expand(BlockFace.NORTH, 1);
			region.expand(BlockFace.SOUTH, 1);

			regions.add(region);
			loc.add(vector);

			if(loc.getBlock().getType() == Material.AIR)
				loc.getBlock().setType(Material.STONE);
		}
		return true;
	}

	public static class Jump
	{
		private int distance, offset, height;

		public Jump(int distance, int offset, int height)
		{
			this.distance = distance;
			this.offset = offset;
			this.height = height;
		}

		public Location getDestination(Location location, BlockFace direction)
		{
			return location.clone().add(toVector(direction));
		}

		public Vector toVector(BlockFace direction)
		{
			int deltaX, deltaZ;

			Random random = new Random();

			switch(direction)
			{
				case SOUTH:
					deltaZ = distance;
					deltaX = (random.nextBoolean() ? -1 : 1) * offset;
					break;

				case NORTH:
					deltaZ = -distance;
					deltaX = (random.nextBoolean() ? -1 : 1) * offset;
					break;

				case EAST:
					deltaX = distance;
					deltaZ = (random.nextBoolean() ? -1 : 1) * offset;
					break;

				case WEST:
					deltaX = -distance;
					deltaZ = (random.nextBoolean() ? -1 : 1) * offset;
					break;

				default:
					return null;
			}

			return new Vector((double)deltaX, (double)height, (double)deltaZ);
		}

		public List<Vector> toVectors()
		{
			List<BlockFace> directions = Arrays.asList(BlockFace.NORTH, BlockFace.WEST, BlockFace.SOUTH, BlockFace.EAST);
			List<Vector> vectors = new ArrayList<>();

			for(BlockFace face : directions)
			{
				vectors.add(toVector(face));
			}

			return vectors;
		}


		public int getDistance()
		{
			return distance;
		}

		public void setDistance(int distance)
		{
			this.distance = distance;
		}

		public int getOffset()
		{
			return offset;
		}

		public void setOffset(int offset)
		{
			this.offset = offset;
		}

		public int getHeight()
		{
			return height;
		}

		public void setHeight(int height)
		{
			this.height = height;
		}
	}
}
