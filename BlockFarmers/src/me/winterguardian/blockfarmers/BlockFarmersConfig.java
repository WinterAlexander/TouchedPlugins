package me.winterguardian.blockfarmers;

import me.winterguardian.core.game.GameConfig;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BlockFarmersConfig extends GameConfig
{
	private List<FarmBlock> farmables;
	private int waitingTimer, clappingTimer;
	private boolean enableStats, invitationMessage, keepPlayerStates, enableMOTD;
	private List<World> broadcastWorlds;

	private List<String> endGameCommands;

    private boolean sqlEnabled;

    private String sqlDriver, sqlURL, sqlUser, sqlPassword;

	public BlockFarmersConfig(File file)
	{
		super(file);
		this.farmables = new ArrayList<>();
		this.waitingTimer = 15;
		this.clappingTimer = 5;
		this.enableStats = true;
		this.broadcastWorlds = new ArrayList<>();

		this.keepPlayerStates = false;
		this.enableMOTD = false;

		this.endGameCommands = new ArrayList<>();

        this.sqlEnabled = false;

        this.sqlDriver = null;
        this.sqlURL = null;
        this.sqlUser = null;
        this.sqlPassword = null;
	}

	public void load(YamlConfiguration config)
	{
		super.load(config);

		for (String string : config.getStringList("farmable-blocks"))
			this.farmables.add(FarmBlock.fromString(string.toUpperCase()));

		if (this.farmables.size() == 0)
			this.farmables.add(new FarmBlock(Material.GRASS));

		this.waitingTimer = config.getInt("waiting-timer", this.waitingTimer);
		this.clappingTimer = config.getInt("clapping-timer", this.clappingTimer);
		
		this.enableStats = config.getBoolean("enable-stats", this.enableStats);
		this.enableMOTD = config.getBoolean("enable-motd", this.enableMOTD);

		this.invitationMessage = config.getBoolean("invitation-message", this.invitationMessage);

		if(config.isList("endgame-commands"))
			this.endGameCommands = config.getStringList("endgame-commands");

		if(config.isList("broadcast-worlds"))
		{
			for(String string : config.getStringList("broadcast-worlds"))
			{
				World world = Bukkit.getWorld(string);
				if(world == null)
				{
					Bukkit.getLogger().warning("[BlockFarmers] Invalid world found in the broadcast world list: \"" + string + "\"");
					continue;
				}
				broadcastWorlds.add(world);
			}
		}
		else if(config.isString("broadcast-worlds"))
		{
			String string = config.getString("broadcast-worlds");
			if(string.equalsIgnoreCase("*"))
				this.addAllWorlds();
			else if(!string.equalsIgnoreCase("null"))
			{
				World world = Bukkit.getWorld(string);
				if(world == null)
					Bukkit.getLogger().warning("[BlockFarmers] Invalid world found in the broadcast world list: \"" + string + "\"");
				else
					broadcastWorlds.add(world);
			}
				
		}

		this.keepPlayerStates = config.getBoolean("keep-player-states", this.keepPlayerStates);


		this.sqlEnabled = config.getBoolean("sql-enabled", this.sqlEnabled);
        this.sqlDriver = config.getString("sql-driver", this.sqlDriver);
        this.sqlURL = config.getString("sql-url", this.sqlURL);
        this.sqlUser = config.getString("sql-user", this.sqlUser);
        this.sqlPassword = config.getString("sql-password", this.sqlPassword);
	}
	
	private void addAllWorlds()
	{
		this.broadcastWorlds.addAll(Bukkit.getWorlds());
	}

	public boolean canFarm(Block block)
	{
		for (FarmBlock current : this.farmables)
			if (current.match(block))
				return true;

		return false;
	}

	public int getFarmableBlocksSize()
	{
		return this.farmables.size();
	}

	public int getWaitingTimer()
	{
		return this.waitingTimer;
	}

	public int getClappingTimer()
	{
		return this.clappingTimer;
	}

	public boolean enableStats()
	{
		return enableStats;
	}
	
	public boolean invitationMessage()
	{
		return invitationMessage;
	}

	public boolean keepPlayerStates()
	{
		return keepPlayerStates;
	}

	public boolean enableMOTD()
	{
		return enableMOTD;
	}

	public List<String> getEndGameCommands()
	{
		return endGameCommands;
	}

	public Collection<? extends Player> getBroadcastRecipients()
	{
		List<Player> list = new ArrayList<>();
		for(World world : this.broadcastWorlds)
			list.addAll(world.getPlayers());
		
		for(Player p : BlockFarmersPlugin.getGame().getPlayers())
			if(!list.contains(p))
				list.add(p);
		return list;
	}

    public boolean isSqlEnabled()
    {
        return sqlEnabled;
    }

    public String getSqlDriver()
    {
        return sqlDriver;
    }

    public String getSqlURL()
    {
        return sqlURL;
    }

    public String getSqlUser()
    {
        return sqlUser;
    }

    public String getSqlPassword()
    {
        return sqlPassword;
    }
}
