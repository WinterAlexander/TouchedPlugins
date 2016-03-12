package me.winterguardian.test.components;

import me.winterguardian.core.entity.custom.npc.NPCUtil;
import me.winterguardian.test.TestComponent;
import me.winterguardian.test.TestingCenter;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

/**
 *
 * Created by Alexander Winter on 2016-01-05.
 */
public class PlayerTest extends TestComponent
{
	public PlayerTest()
	{
		super("player", TestingCenter.TEST_LEVEL4, "/you are a freaking noob");
	}

	@Override
	public boolean onSubCommand(CommandSender sender, String label, String[] args)
	{
		if(!(sender instanceof Player))
			return false;

		Player p = (Player) sender;


		NPCUtil.spawnNPC(EntityType.PLAYER, p.getLocation());

		return true;
	}
}
