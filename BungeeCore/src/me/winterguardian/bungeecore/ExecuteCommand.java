package me.winterguardian.bungeecore;

import com.google.common.base.Joiner;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.plugin.Command;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map.Entry;

/**
 * Created by Alexander Winter on 2015-11-17.
 */
public class ExecuteCommand extends Command
{

	public ExecuteCommand()
	{
		super("execute", "BungeeCore.execute", "exe", "e", "exec");
	}

	@Override
	public void execute(CommandSender sender, String[] args)
	{
		if(args.length < 2)
		{
			sender.sendMessage(new TextComponent("NOT ENOUGH ARGUMENTS"));
			return;
		}

		Collection<ServerInfo> servers = null;

		if(args[0].equalsIgnoreCase("*") || args[0].equalsIgnoreCase("all"))
		{
			servers = new ArrayList<ServerInfo>();
			for(Entry<String, ServerInfo> server : ProxyServer.getInstance().getServers().entrySet())
				servers.add(server.getValue());
		}
		else
			servers = Collections.singletonList(ProxyServer.getInstance().getServerInfo(args[0]));

		if(servers.size() == 0)
		{
			sender.sendMessage(new TextComponent("INVALID SERVER"));
			return;
		}



		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("ExecuteConsole");


		String command = args[1];

		for(int i = 2; i < args.length; i++)
			command += " " + args[i];
		out.writeUTF(command);

		for(ServerInfo server : servers)
			server.sendData("BungeeCord", out.toByteArray());
	}
}
