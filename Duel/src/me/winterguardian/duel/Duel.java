package me.winterguardian.duel;

import java.util.ArrayList;
import java.util.List;

import me.winterguardian.core.Core;
import me.winterguardian.core.game.Game;
import me.winterguardian.core.game.GameManager;
import me.winterguardian.core.game.SekaiGame;
import me.winterguardian.core.inventorygui.GUIItem;
import me.winterguardian.core.json.JsonUtil;
import me.winterguardian.core.util.PlayerUtil;
import me.winterguardian.core.util.TabUtil;
import me.winterguardian.duel.command.DuelCommand;
import me.winterguardian.duel.game.DuelGame;
import me.winterguardian.duel.game.GameListener;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.java.JavaPlugin;

public class Duel extends JavaPlugin implements SekaiGame
{
	public static final String TAB_HEADER = "{\"text\":\"\",\"extra\":[{\"text\":\"[\",\"color\":\"white\"},{\"text\":\"Duel\",\"color\":\"gold\",\"bold\":\"true\"},{\"text\":\"]\",\"color\":\"white\"}]}";
	
	public static Permission ADMINISTRATION = new Permission("Duel.Admin", PermissionDefault.OP);
	public static Permission MODERATION = new Permission("Duel.Mod", PermissionDefault.OP);
	
	public static Duel plugin;
	
	private DuelSettings setting;
	private DuelGame game;
	
	public static Duel getInstance()
	{
		return plugin;
	}
	
	public List<Player> getPlayers()
	{
		List<Player> list = new ArrayList<>();
		if(this.game.getPlayer1() != null)
			list.add(this.game.getPlayer1());
		
		if(this.game.getPlayer2() != null)
			list.add(this.game.getPlayer2());
		
		if(this.game.getWaiting().size() > 0)
			list.addAll(this.game.getWaiting());
		return list;
	}

	@Override
	public void onEnable()
	{
		GameManager.registerGame(this);
		plugin = this;

		Core.getUserDatasManager().enableDB(this, null, null, null, null, DuelStats.getTables());

		this.setting = new DuelSettings();
		this.setting.load();
		
		this.game = new DuelGame();
		
		Bukkit.getPluginManager().registerEvents(new ProtectionListener(), this);
		Bukkit.getPluginManager().registerEvents(new GameListener(), this);
		new DuelCommand(this).register(this);
		
		if(this.setting.isReady())
			this.game.updateBoard();
		
	}

	@Override
	public void onDisable()
	{
		this.game.stop(false);
		HandlerList.unregisterAll(this);
		
		this.game = null;
		this.setting = null;
		
		plugin = null;
		Core.disable(this);
		GameManager.unregisterGame(this);
	}
	
	public String getTabFooter()
	{
		return JsonUtil.toJson(this.getStatus());
	}

	@Override
	public boolean contains(Player p)
	{
		return this.setting.isInRegion(p);
	}

	@Override
	public String getChatPrefix(Player p)
	{
		
		if(p == this.game.getPlayer1())
			return "§6§lDuel §c§lJ1";
		if(p == this.game.getPlayer2())
			return "§6§lDuel §9§lJ2";
		if(this.game.getWaiting().contains(p))
			return "§6§lDuel §7§l" + (this.game.getWaiting().indexOf(p) + 1);
		return "§6§lDuel";
	}

	@Override
	public String getChatName(Player p)
	{
		return p.getDisplayName();
	}

	public boolean gameContains(Player player)
	{
		return this.game.isOnGame(player);
	}

	@Override
	public void join(Player player)
	{
		GameManager.leaveAll(player);
		this.game.join(player);
	}

	@Override
	public void leave(Player player)
	{
		if(this.gameContains(player))
			this.game.leave(player);
		else
		{
			PlayerUtil.heal(player);
			PlayerUtil.prepare(player);
			PlayerUtil.clearInventory(player);
			PlayerUtil.clearBoard(player);
			TabUtil.resetTab(player);
		}
	}
	
	public DuelSettings getSettings()
	{
		return this.setting;
	}
	
	public DuelGame getGame()
	{
		return this.game;
	}

	public List<Player> getPlayersInRegion()
	{
		List<Player> list = new ArrayList<>();
		for(Player p : this.setting.getLobby().getWorld().getPlayers())
			if(this.setting.isInRegion(p))
				list.add(p);
		return list;
	}

	@Override
	public Location getWarp()
	{
		return this.getSettings().getLobby();
	}

	@Override
	public String getColoredName()
	{
		return "§6§lDuel";
	}

	@Override
	public String getStatus()
	{
		if(this.game.getPlayer1() == null)
			return DuelMessage.STATUS_NOBODYINGAME.toString();
		else if(this.game.getPlayer2() == null)
			return DuelMessage.STATUS_ONEPLAYER.toString().replace("<player1>", this.game.getPlayer1().getName());
		else
			return DuelMessage.STATUS_TWOPLAYERS.toString().replace("<player1>", this.game.getPlayer1().getName()).replace("<player2>", this.game.getPlayer2().getName());
	}

	@Override
	public GUIItem getGUIItem()
	{
		return new DuelHubItem(this);
	}
}
