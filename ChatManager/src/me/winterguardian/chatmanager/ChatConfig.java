package me.winterguardian.chatmanager;

import me.winterguardian.core.json.JsonUtil;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChatConfig
{
	private List<String> insults;
	private List<CommandRedirection> redirections;
	private ChatBroadcast[] broadcasts;
	private long broadcastDelay;
	private boolean broadcastActive;
	private String welcomeTitle = "[{text:\"Touched\",color:white, bold:true}, {text:\"Craft\", color:gold, bold:true}]";
	private String welcomeSubTitle = "{text:'Amusez-vous bien !', color:yellow}";
	private String joinTitle = "[{text:\"Touched\",color:white, bold:true}, {text:\"Craft\", color:gold, bold:true}]";
	private String joinSubTitle = "{text:'Amusez-vous bien !', color:yellow}";
	private FileConfiguration helpSection;
	private HashMap<Integer, String> chatFormats;

	public ChatConfig()
	{
		broadcasts = new ChatBroadcast[]{new ChatBroadcast("default", new String[]{JsonUtil.toJson("§eMerci de configurer les messages automatiques dans §cChatManager")}, true, Sound.NOTE_PLING)};
		broadcastDelay = 12000L;
		broadcastActive = false;

		insults = new ArrayList<>();
		redirections = new ArrayList<>();

		chatFormats = new HashMap<>();
	}

	public void save()
	{
		YamlConfiguration config = YamlConfiguration.loadConfiguration(getFile());
		config.set("insulte-liste", insults);
		if(!config.isConfigurationSection("broadcast"))
		{
			for(ChatBroadcast bc : broadcasts)
			{
				if(bc != null)
				{
					bc.save(config.createSection("broadcast." + bc.getName()));
				}
			}
		}
		if(!config.isLong("broadcast-delay"))
		{
			config.set("broadcast-delay", Long.valueOf(broadcastDelay));
		}
		if(!config.isString("join-title.new.title"))
		{
			config.set("join-title.new.title", welcomeTitle);
		}
		if(!config.isString("join-title.new.subtitle"))
		{
			config.set("join-title.new.subtitle", welcomeSubTitle);
		}
		if(!config.isString("join-title.normal.title"))
		{
			config.set("join-title.normal.title", joinTitle);
		}
		if(!config.isString("join-title.normal.subtitle"))
		{
			config.set("join-title.normal.subtitle", joinSubTitle);
		}
		if(!config.isConfigurationSection("chat-format"))
		{
			for(Integer integer : chatFormats.keySet())
			{
				config.set("chat-format.level" + integer, chatFormats.get(integer));
			}
		}
		config.createSection("cmd-redirect");
		for(CommandRedirection redirection : redirections)
		{
			config.set("cmd-redirect." + redirection.getCommand() + ".redirection", redirection.getRedirection());
			config.set("cmd-redirect." + redirection.getCommand() + ".canByPass", Boolean.valueOf(redirection.canByPass()));
		}
		try
		{
			config.save(getFile());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public void load()
	{
		YamlConfiguration config = YamlConfiguration.loadConfiguration(getFile());
		if(config.isList("insulte-liste"))
		{
			insults = config.getStringList("insulte-liste");
		}
		if(config.isConfigurationSection("cmd-redirect"))
		{
			for(String string : config.getConfigurationSection("cmd-redirect").getKeys(false))
			{
				redirections.add(new CommandRedirection(string, config.getString("cmd-redirect." + string + ".redirection"), config.getBoolean("cmd-redirect." + string + ".canByPass")));
			}
		}
		if((config.isLong("broadcast-delay")) || (config.isInt("broadcast-delay")))
		{
			broadcastDelay = config.getLong("broadcast-delay");
		}
		broadcastActive = config.getBoolean("broadcast-active");
		if(config.isConfigurationSection("broadcast"))
		{
			List<ChatBroadcast> temp = new ArrayList<>();
			for(String key : config.getConfigurationSection("broadcast").getKeys(false))
				temp.add(new ChatBroadcast(config.getConfigurationSection("broadcast." + key)));

			broadcasts = temp.toArray(broadcasts);
		}
		if(config.isConfigurationSection("chat-format"))
		{
			for(String key : config.getConfigurationSection("chat-format").getKeys(false))
			{
				try
				{
					chatFormats.put(Integer.valueOf(Integer.parseInt(key.replace("level", ""))), (String)config.get("chat-format." + key));
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		}
		helpSection = YamlConfiguration.loadConfiguration(getHelpFile());
		if(config.isConfigurationSection("join-title"))
		{
			welcomeTitle = config.getString("join-title.new.title");
			welcomeSubTitle = config.getString("join-title.new.subtitle");
			joinTitle = config.getString("join-title.normal.title");
			joinSubTitle = config.getString("join-title.normal.subtitle");
		}
	}

	public File getFile()
	{
		return new File(ChatManager.getPlugin().getDataFolder(), "config.yml");
	}

	public File getHelpFile()
	{
		return new File(ChatManager.getPlugin().getDataFolder(), "aide.yml");
	}

	public boolean removeRedirection(String command)
	{
		CommandRedirection toRemove = null;
		for(CommandRedirection redirection : redirections)
		{
			if(redirection.getCommand().equalsIgnoreCase(command))
			{
				toRemove = redirection;
				break;
			}
		}
		return redirections.remove(toRemove);
	}

	public List<String> getInsults()
	{
		return insults;
	}

	public List<CommandRedirection> getRedirections()
	{
		return redirections;
	}

	public ChatBroadcast[] getBroadcasts()
	{
		return broadcasts;
	}

	public long getBroadcastDelay()
	{
		return broadcastDelay;
	}

	public boolean isBroadcastActive()
	{
		return broadcastActive;
	}

	public String getWelcomeTitle()
	{
		return welcomeTitle;
	}

	public String getWelcomeSubTitle()
	{
		return welcomeSubTitle;
	}

	public String getJoinTitle()
	{
		return joinTitle;
	}

	public String getJoinSubTitle()
	{
		return joinSubTitle;
	}

	public ConfigurationSection getHelpSection()
	{
		return helpSection;
	}

	public HashMap<Integer, String> getChatFormats()
	{
		return chatFormats;
	}
}
