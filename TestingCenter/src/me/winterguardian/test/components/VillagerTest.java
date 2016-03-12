package me.winterguardian.test.components;

import me.winterguardian.core.entity.custom.npc.NPCUtil;
import me.winterguardian.test.TestComponent;
import me.winterguardian.test.TestingCenter;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

/**
 *
 * Created by Alexander Winter on 2015-12-19.
 */
public class VillagerTest extends TestComponent
{
	public VillagerTest()
	{
		super("villager", TestingCenter.TEST_LEVEL4, "/you are a freaking noob");
	}

	@Override
	public boolean onSubCommand(CommandSender sender, String label, String[] args)
	{
		if(!(sender instanceof Player))
			return false;

		Player p = (Player) sender;

		NPCUtil.spawnNPC(EntityType.VILLAGER, p.getLocation());

		return true;
	}
}
