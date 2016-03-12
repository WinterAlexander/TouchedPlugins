package me.winterguardian.test.components;

import me.winterguardian.test.TestComponent;
import me.winterguardian.test.TestingCenter;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * Created by Alexander Winter on 2015-12-08.
 */
public class PlayerTotalExp extends TestComponent
{
	public PlayerTotalExp()
	{
		super("playertotalexp", TestingCenter.TEST_LEVEL1, "/yousuck");
	}

	@Override
	public boolean onSubCommand(CommandSender sender, String label, String[] args)
	{
		((Player)sender).setTotalExperience(Integer.parseInt(args[0]));
		return true;
	}
}
