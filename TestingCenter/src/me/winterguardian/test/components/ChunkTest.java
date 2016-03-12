package me.winterguardian.test.components;

import me.winterguardian.test.TestComponent;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.permissions.Permission;

/**
 *
 * Created by Alexander Winter on 2016-01-31.
 */
public class ChunkTest extends TestComponent
{
	public ChunkTest(String name, Permission permission, String usage)
	{
		super(name, permission, usage);

		World world = Bukkit.getWorld("test");

	}

}
