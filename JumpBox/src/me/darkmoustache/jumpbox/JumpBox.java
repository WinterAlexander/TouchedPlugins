package me.darkmoustache.jumpbox;

import java.util.ArrayList;
import java.util.List;

import me.winterguardian.core.Core;
import me.winterguardian.core.game.GameManager;
import me.winterguardian.core.game.SekaiGame;
import me.winterguardian.core.inventorygui.GUIItem;
import me.winterguardian.core.json.JsonUtil;
import me.winterguardian.core.util.PlayerUtil;
import me.winterguardian.core.util.TabUtil;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.java.JavaPlugin;

public class JumpBox extends JavaPlugin implements SekaiGame
{
	public static final String DEFAULT_HEADER = JsonUtil.toJson("§f[§a§lJumpBox§f]");
	public static final String DEFAULT_FOOTER = JsonUtil.toJson("§eBonne chance !");
	
	public static Permission STAFF = new Permission("JumpBox.staff", "Accès aux permissions staff en JumpBox", PermissionDefault.OP);
	
	private static JumpBox plugin;
	
	private JumpBoxSettings settings;
	

	@Override
	public void onEnable()
	{
		GameManager.registerGame(this);
		plugin = this;

		Core.getUserDatasManager().enableDB(this, null, null, null, null, JumpBoxStats.getTables());

		if(Bukkit.getPluginManager().getPermission(STAFF.getName()) == null)
			Bukkit.getPluginManager().addPermission(STAFF);
		
		this.settings = new JumpBoxSettings();
		this.settings.load();
		
		Bukkit.getPluginManager().registerEvents(new JumpBoxListener(), this);
		
		this.getCommand("jumpbox").setExecutor(new JumpBoxCommand());
		
		for(Player p : getPlayers())
			join(p);
		
		
	}

	@Override
	public void onDisable()
	{
		for(Player p : getPlayers())
			leave(p);
		
		HandlerList.unregisterAll(this);
		
		this.settings.save();
		this.settings = null;
		
		plugin = null;
		Core.disable(this);
		GameManager.unregisterGame(this);
	}

	public static JumpBox getPlugin()
	{
		return plugin;
	}

	public static JumpBoxSettings getSettings()
	{
		return getPlugin().settings;
	}

	@Override
	public boolean contains(Player p)
	{
		if(settings.getSpawn() == null)
			return false;
		
		if(p.getWorld() != settings.getSpawn().getWorld())
			return false;
		
		return settings.isInRegion(p.getLocation());
	}

	@Override
	public String getChatPrefix(Player p)
	{
		return "§a§lJump";
	}

	@Override
	public String getChatName(Player p)
	{
		return p.getDisplayName();
	}

	@Override
	public String getColoredName()
	{
		return "§a§lJumpBox";
	}

	@Override
	public String getStatus()
	{
		return "§eLes joueurs jouent le jump de leur choix.";
	}

	@Override
	public Location getWarp()
	{
		return plugin.getWarp();
	}

	@Override
	public void join(Player p)
	{
		p.setPlayerListName("§a" + p.getName());
		TabUtil.sendInfos(p, JumpBox.DEFAULT_HEADER, JumpBox.DEFAULT_FOOTER);
		PlayerUtil.heal(p);
		if(!p.hasPermission(STAFF))
		{
			PlayerUtil.prepare(p);
			PlayerUtil.clearInventory(p);
		}
	}

	@Override
	public void leave(Player p)
	{
		p.setPlayerListName(null);
		TabUtil.resetTab(p);
		PlayerUtil.clearBoard(p);
		PlayerUtil.heal(p);
	}
	
	public List<Player> getPlayers()
	{
		List<Player> list = new ArrayList<>();
		for(Player p : Bukkit.getOnlinePlayers())
			if(this.contains(p))
				list.add(p);
		
		return list;
	}

	@Override
	public GUIItem getGUIItem()
	{
		return new GUIItem(5)
		{
			@Override
			public void click(Player player, ClickType click)
			{
				if(click.isLeftClick())
				{
					player.performCommand("jb tp");
					return;
				}

				player.performCommand("jb stats");
			}

			@Override
			public ItemStack getItemStack(Player player)
			{
				ItemStack item = new ItemStack(Material.SIGN, 1);
				ItemMeta meta = item.getItemMeta();
				meta.setDisplayName("§a§lJumpBox");
				List<String> lore = new ArrayList<>();

				int players = getPlayers().size();
				lore.add("§e" + players + " joueur" + (players > 1 ? "s" : ""));

				lore.add(" ");
				lore.add("§aClic gauche pour jouer");
				lore.add("§eClic droit pour les stats");

				meta.setLore(lore);

				item.setItemMeta(meta);

				return item;
			}
		};
	}
}