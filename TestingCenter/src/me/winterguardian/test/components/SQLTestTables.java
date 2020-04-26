package me.winterguardian.test.components;

import me.winterguardian.core.Core;
import me.winterguardian.core.playerstats.DBUserDataLoader;
import me.winterguardian.core.util.TextUtil;
import me.winterguardian.test.TestComponent;
import me.winterguardian.test.TestingCenter;
import org.bukkit.command.CommandSender;

import java.util.List;

/**
 *
 * Created by Alexander Winter on 2015-12-05.
 */
public class SQLTestTables extends TestComponent
{
	public SQLTestTables()
	{
		super("counttables", TestingCenter.TEST_LEVEL4, "yo you can't get that message");
	}

	@Override
	public boolean onSubCommand(CommandSender sender, String label, String[] args)
	{
		sender.sendMessage(TextUtil.toString(((DBUserDataLoader) Core.getUserDatasManager().getLoader()).getTables().keySet(), ", "));
		return true;
	}

	@Override
	public List<String> onSubTabComplete(CommandSender sender, String label, String[] args)
	{
		return null;
	}
}
