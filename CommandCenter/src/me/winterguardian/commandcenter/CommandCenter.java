package me.winterguardian.commandcenter;

import me.winterguardian.commandcenter.commands.*;
import me.winterguardian.core.Core;
import me.winterguardian.core.command.AutoRegistrationCommand;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class CommandCenter extends JavaPlugin
{	
	private static CommandCenter plugin;
	private List<AutoRegistrationCommand> commands;

	public CommandCenter()
	{
		commands = new ArrayList<>();
	}

	@Override
	public void onEnable()
	{
		plugin = this;

		commands.add(new BlockInfoCommand());
		commands.add(new ItemInfoCommand());
		commands.add(new BowEffectCommand());
		commands.add(new FireworkLauncherCommand());
		commands.add(new PermissionListCommand());
		commands.add(new PlayerListCommand());
		commands.add(new RenameItemCommand());
		commands.add(new SignEditCommand());
		commands.add(new MultiCommand());
		commands.add(new EnchantCommand());
		commands.add(new SpoingCommand());
		commands.add(new ClearInventoryCommand());
		commands.add(new SetMusicDiscCommand());
		commands.add(new SetGamemodeCommand());
		commands.add(new PlayRecordCommand());
		commands.add(new SkullCommand());
		commands.add(new SpeedCommand());
		commands.add(new SelfMessageCommand());
		commands.add(new HatCommand());
		commands.add(new SudoCommand());
		commands.add(new ColorCommand());
		commands.add(new TptoCommand());
		commands.add(new TphereCommand());
		commands.add(new WarpCommand());
		commands.add(new WorkbenchCommand());
		commands.add(new TrashBinCommand());
		commands.add(new EnchantingCommand());
		commands.add(new InventoryCommand());
		commands.add(new EnderChestCommand());
		commands.add(new SetHealthCommand());
		commands.add(new PrivateMessageCommand());
		commands.add(new ServerCommand());
		commands.add(new ExecuteCommand());
		commands.add(new ExecuteCommand());
		commands.add(new PortalCommand());
		commands.add(new ChristmasGift());
		commands.add(new ItemFlagCommand());
		commands.add(new NameCommand(this));

		if(Core.getUserDatasManager().isEnabled())
		{
			commands.add(new PointCommand(this));
			commands.add(new RconVoteCommand(this));
		}

		for(AutoRegistrationCommand command : commands)
			command.register(this);
	}
	
	@Override
	public void onDisable()
	{
		for(AutoRegistrationCommand command : commands)
			command.unregister();

		Core.disable(this);
		HandlerList.unregisterAll(this);

		commands.clear();

		plugin = null;
	}

	public static CommandCenter getPlugin()
	{
		return plugin;
	}
}
