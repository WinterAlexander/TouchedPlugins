package me.winterguardian.test.components;

import me.winterguardian.test.TestComponent;
import me.winterguardian.test.TestingCenter;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

/**
 *
 * Created by Alexander Winter on 2016-01-05.
 */
public class EventTest extends TestComponent
{
	private CommandSender sender;

	public EventTest()
	{
		super("eventtest", TestingCenter.TEST_LEVEL4, "/eventtest");
	}

	@Override
	public boolean onSubCommand(CommandSender sender, String label, String[] args)
	{
		if(this.sender != sender)
			this.sender = sender;
		else
			this.sender = null;
		return true;
	}

	@EventHandler
	public void on(EntityExplodeEvent event)
	{
		if(sender == null)
			return;

		sender.sendMessage(event.getEventName());
	}

	@EventHandler
	public void on(BlockExplodeEvent event)
	{
		if(sender == null)
			return;

		sender.sendMessage(event.getEventName());
	}

	@EventHandler
	public void on(BlockIgniteEvent event)
	{
		if(sender == null)
			return;

		sender.sendMessage(event.getEventName());
	}
}
