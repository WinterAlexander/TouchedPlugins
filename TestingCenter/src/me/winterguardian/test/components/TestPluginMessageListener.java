package me.winterguardian.test.components;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import me.winterguardian.test.TestComponent;
import me.winterguardian.test.TestingCenter;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class TestPluginMessageListener extends TestComponent implements PluginMessageListener
{
	public TestPluginMessageListener()
	{
		super("tpml", TestingCenter.TEST_LEVEL4, "/test tpml <things>");
	}

	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] message)
	{

	}

	@Override
	public boolean onSubCommand(CommandSender sender, String label, String[] args)
	{
		if(!(sender instanceof Player))
			return false;
		
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		for(String string : args)
			out.writeUTF(string);

		((Player)sender).sendPluginMessage(TestingCenter.getInstance(), "BungeeCord", out.toByteArray());
		return true;
	}
}
