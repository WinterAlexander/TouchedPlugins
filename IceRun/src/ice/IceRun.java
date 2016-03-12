package ice;

import ice.command.IceRunCommand;
import ice.listener.GameListener;
import ice.listener.JoinLeaveListener;
import ice.listener.ProtectionListener;
import ice.listener.SignListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.winterguardian.core.Core;
import me.winterguardian.core.game.GameManager;
import me.winterguardian.core.game.SekaiGame;
import me.winterguardian.core.game.state.State;
import me.winterguardian.core.inventorygui.GUIItem;
import me.winterguardian.core.util.ActionBarUtil;
import me.winterguardian.core.util.RecordUtil;
import me.winterguardian.core.util.TabUtil;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.WeatherType;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.java.JavaPlugin;

public class IceRun extends JavaPlugin implements SekaiGame
{
	public static Permission VIP = new Permission("IceRun.vip", "Accès aux permissions vip en icerun", PermissionDefault.OP);
	public static Permission STAFF = new Permission("IceRun.staff", "Accès aux permissions staff en icerun", PermissionDefault.OP);
	
	public static final String DEFAULT_TAB_HEADER = me.winterguardian.core.json.JsonUtil.toJson("§f[§b§lIceRun§f]");
	
	public static List<Player> players;
	public static State status;
	
	private static IceRun plugin;
	private static IceRunSettings settings;
	
	private HashMap<Player, Integer> musicTasks;
	
	@Override
	public void onEnable()
	{
		GameManager.registerGame(this);
		plugin = this;

		Core.getUserDatasManager().enableDB(this, null, null, null, null, IceRunStats.getTables());
		
		Bukkit.getPluginManager().registerEvents(new JoinLeaveListener(), this);
		Bukkit.getPluginManager().registerEvents(new ProtectionListener(), this);
		Bukkit.getPluginManager().registerEvents(new GameListener(), this);
		Bukkit.getPluginManager().registerEvents(new SignListener(), this);
		new IceRunCommand(this).register(this);
		
		settings = new IceRunSettings();
		settings.load();
		
		players = new ArrayList<>();
		status = new Standby();
		musicTasks = new HashMap<>();
		
		if(settings.isGameReady())
			for(Player p : Bukkit.getOnlinePlayers())
				if(settings.getRegion().contains(p.getLocation()))
					this.join(p);
	}
	
	@Override
	public void onDisable()
	{
		IceRunMessage.GAME_STOP.say(players);
		for (int i = 0; i < IceRun.players.size(); i++)
			leave(IceRun.players.get(i));
		
		HandlerList.unregisterAll(this);
		
		try
		{
			settings.save();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		settings = null;
		players = null;
		status = null;
		plugin = null;

		Core.disable(this);
		GameManager.unregisterGame(this);
	}
	
	@Override
	public void join(final Player p)
	{
		if(!settings.isGameReady())
		{
			IceRunMessage.JOIN_NOTREADY.say(p);
			return;
		}
		
		if(IceRun.getPlugin().contains(p))
		{
			IceRunMessage.JOIN_ALREADYINGAME.say(p);
			return;
		}
		
		GameManager.leaveAll(p);
		players.add(p);
		IceRunMessage.JOIN.say(players, "<player>", p.getName());
		
		status.join(p);
		p.setPlayerListName("§b" + p.getName());
		
		p.setPlayerWeather(WeatherType.DOWNFALL);
		p.setPlayerTime(6000, false);
		
		RecordUtil.playRecord(p, IceRun.getSettings().getSpawn().getLocation(), Material.RECORD_12);
		
		musicTasks.put(p, Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable()
		{
			@Override
			public void run()
			{
				RecordUtil.playRecord(p, IceRun.getSettings().getSpawn().getLocation(), Material.RECORD_12);
				ActionBarUtil.clear(p);
			}
			
		}, 4760, 4760));
		
		for(Player pl : players)
			TabUtil.sendInfos(pl, IceRun.DEFAULT_TAB_HEADER, IceRun.getFooter());
	}
	
	@Override
	public void leave(Player p)
	{
		if(!IceRun.getPlugin().contains(p))
		{
			IceRunMessage.LEAVE_NOTINGAME.say(p);
			return;
		}
		p.resetPlayerTime();
		p.resetPlayerWeather();
		p.setPlayerListName(null);
		
		RecordUtil.stopRecord(p, IceRun.getSettings().getSpawn().getLocation());
		if(musicTasks.containsKey(p))
		{
			Bukkit.getScheduler().cancelTask(musicTasks.get(p));
			musicTasks.remove(p);
		}
		
		TabUtil.resetTab(p);
		
		IceRunMessage.LEAVE.say(players, "<player>", p.getName());
		players.remove(p);
		status.leave(p);
		
		for(Player pl : players)
			TabUtil.sendInfos(pl, IceRun.DEFAULT_TAB_HEADER, IceRun.getFooter());
		
	}

	@Override
	public boolean contains(Player p)
	{
		return players.contains(p);
	}
	
	public static String getFooter()
	{
		return "{\"text\":\"\",\"extra\":[{\"text\":\"" + players.size() + " joueur" + (players.size() > 1 ? "s" : "") + " en jeu\",\"color\":\"green\",\"bold\":\"true\"}]}";
	}

	public static IceRun getPlugin()
	{
		return plugin;
	}

	@Override
	public String getChatPrefix(Player p)
	{
		return "§b§lIce";
	}

	@Override
	public String getChatName(Player p)
	{
		return p.getDisplayName();
	}

	public static IceRunSettings getSettings()
	{
		return settings;
	}

	@Override
	public String getColoredName()
	{
		return "§b§lIceRun";
	}

	@Override
	public String getStatus()
	{
		if(status == null)
		{
			return null;
		}
		
		return status.getStatus();
	}

	@Override
	public Location getWarp()
	{
		if(getSettings().getLobby() != null)
			return getSettings().getLobby().getLocation();
		return null;
	}

	@Override
	public GUIItem getGUIItem()
	{
		return new HubGUIItem();
	}
}