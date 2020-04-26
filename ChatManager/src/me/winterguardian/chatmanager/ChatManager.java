package me.winterguardian.chatmanager;

import me.winterguardian.chatmanager.command.ClearChatCommand;
import me.winterguardian.chatmanager.command.PreMessageCommand;
import me.winterguardian.chatmanager.command.RedirectCommand;
import me.winterguardian.chatmanager.command.SayAllCommand;
import me.winterguardian.chatmanager.listener.ChatListener;
import me.winterguardian.chatmanager.listener.CommandListener;
import me.winterguardian.chatmanager.listener.WelcomeListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.Collection;

public class ChatManager extends JavaPlugin
{
	public static final Permission COLOR = new Permission("ChatManager.chat-color", PermissionDefault.OP);
	public static final Permission SPECIAL = new Permission("ChatManager.chat-special-form", PermissionDefault.OP);
	public static final Permission AUTO_WELCOME = new Permission("ChatManager.auto-welcome", PermissionDefault.OP);
	public static final Permission COMMAND_REDIRECT_BYPASS = new Permission("ChatManager.redirect-bypass", "Permet de gérer les redirections et de passer au travers.", PermissionDefault.OP);
	public static final Permission PREPROCESS_COMMAND_ALIASES = new Permission("ChatManager.pre-aliases", PermissionDefault.OP);
	public static final Permission CONNECTION_MESSAGES = new Permission("ChatManager.connection-messages", PermissionDefault.FALSE);
	public static final String PERMANENT_CHAT_COLOR_BASEFORM = "ChatManager.permanent-chat-color.level";
	private static ChatManager plugin;
	private static ChatConfig config;
	private int broadcastTaskId;
	private int broadcastIndex;

	public Collection<? extends Player> getRecipients()
	{
		return Bukkit.getOnlinePlayers();
	}

	public void broadcast()
	{
		if((config != null) && (config.isBroadcastActive()))
		{
			config.getBroadcasts()[broadcastIndex].sayAll();
			broadcastIndex += 1;
			if(broadcastIndex >= config.getBroadcasts().length)
			{
				broadcastIndex = 0;
			}
		}
	}

	public void onEnable()
	{
		plugin = this;

		config = new ChatConfig();
		config.load();
		for(Field field : ChatManager.class.getFields())
		{
			try
			{
				if(((field.get(null) instanceof Permission)) && (Bukkit.getPluginManager().getPermission(((Permission)field.get(null)).getName()) == null))
				{
					Bukkit.getPluginManager().addPermission((Permission)field.get(null));
				}
			}
			catch(IllegalArgumentException | IllegalAccessException localIllegalArgumentException)
			{
			}
		}
		for(Integer i : config.getChatFormats().keySet())
		{
			Permission permission = new Permission("ChatManager.chat-format.level" + i, "Permission d'obtenir le format de chat niveau " + i + " comme défini dans le fichier config de ChatManager.", PermissionDefault.OP);
			if(Bukkit.getPluginManager().getPermission(permission.getName()) == null)
			{
				Bukkit.getPluginManager().addPermission(permission);
			}
		}
		Bukkit.getPluginManager().registerEvents(new ChatListener(), this);
		Bukkit.getPluginManager().registerEvents(new WelcomeListener(), this);
		Bukkit.getPluginManager().registerEvents(new CommandListener(), this);

		new ClearChatCommand().register(this);
		new RedirectCommand().register(this);
		new SayAllCommand().register(this);
		new PreMessageCommand().register(this);

		broadcastIndex = 0;
		broadcastTaskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable()
		{
			public void run()
			{
				ChatManager.getPlugin().broadcast();
			}
		}, config.getBroadcastDelay() / 2L, config.getBroadcastDelay());
	}

	public void onDisable()
	{
		config.save();
		config = null;

		HandlerList.unregisterAll(this);

		Bukkit.getScheduler().cancelTask(broadcastTaskId);

		plugin = null;
	}

	public static ChatManager getPlugin()
	{
		return plugin;
	}

	public static ChatConfig getChatConfig()
	{
		return config;
	}
}
