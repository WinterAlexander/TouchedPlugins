package me.winterguardian.hub;

import me.winterguardian.core.game.Game;
import me.winterguardian.core.game.GameManager;
import me.winterguardian.core.util.PlayerUtil;
import me.winterguardian.core.util.TabUtil;
import me.winterguardian.core.world.SerializableLocation;
import me.winterguardian.core.world.SerializableRegion;
import me.winterguardian.hub.listener.GUIListener;
import me.winterguardian.hub.listener.JoinLeaveListener;
import me.winterguardian.hub.listener.PlayerProtectionListener;
import me.winterguardian.hub.listener.RegionProtectionListener;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Hub extends JavaPlugin implements Game
{
	public static final Permission TPTOHUB = new Permission("Hub.teleport", "Permet de se tp au Hub", PermissionDefault.OP);
	public static final Permission TPOTHERS = new Permission("Hub.teleport.others", "Permet de tp les autres au Hub", PermissionDefault.OP);
	
	public static final Permission RANK_UNDERSTAFF = new Permission("Hub.rank.under-staff", "Permet d'avoir la mention sous-staff sur le scoreboard au Hub", PermissionDefault.OP);
	public static final Permission RANK_VIP3 = new Permission("Hub.rank.vip3", "Permet d'avoir la mention dieu sur le scoreboard au Hub", PermissionDefault.OP);
	public static final Permission RANK_VIP2 = new Permission("Hub.rank.vip2", "Permet d'avoir la mention élite sur le scoreboard au Hub", PermissionDefault.OP);
	public static final Permission RANK_VIP = new Permission("Hub.rank.vip", "Permet d'avoir la mention vip sur le scoreboard au Hub", PermissionDefault.OP);
	public static final Permission RANK_STAFF = new Permission("Hub.rank.staff", "Permet d'avoir la mention staff sur le scoreboard au Hub et de quiter le Hub librement", PermissionDefault.OP);
	public static final Permission RANK_ADMIN = new Permission("Hub.rank.admin", "Permet de modifier le hub", PermissionDefault.OP);
	
	private static Hub plugin;
	
	private SerializableLocation hub;
	private SerializableLocation newPlayersSpawn;
	private SerializableRegion region;
	private int scoreboardAndTabTaskId;
	
	private int tabMessageWidth;
	private String tabMessage;

	@Override
	public void onEnable()
	{
		GameManager.registerGame(this);
		plugin = this;
		
		new HubCommand().register(this);
		new SetHubCommand().register(this);
		new ReceptionCommand().register(this);
		
		if(Bukkit.getPluginManager().getPermission(RANK_UNDERSTAFF.getName()) == null)
			Bukkit.getPluginManager().addPermission(RANK_UNDERSTAFF);
		
		if(Bukkit.getPluginManager().getPermission(RANK_VIP.getName()) == null)
			Bukkit.getPluginManager().addPermission(RANK_VIP);
		
		if(Bukkit.getPluginManager().getPermission(RANK_STAFF.getName()) == null)
			Bukkit.getPluginManager().addPermission(RANK_STAFF);
		
		this.tabMessage = "§7Bon jeu sur §f§lTouched§6§lCraft §7$player !  §e✧  §7Il y a actuellement §c$onlines §7joueurs connectés en ce moment. §e✧  §7Visitez §b§ntouchedcraft.fr§7 pour §7la §eboutique§7, §7les §avotes §7et le §dforum §7!  §e✧  §7Nous avons §cDiscord§7, Ip: §b§ndiscord.touchedcraft.fr§f  §e✧  §7Tu souhaites devenir §6§lVip §7ou §f§lÉlite §7? Rends-toi sur la §eboutique §7pour voir nos offres !  §e✧  §7Envie d'intégrer le staff, direction §dforum §7pour les candidatures !  §e✧  §7Nous acceptons les §fYou§ctubers §7avec certaines conditions. Tu crois être à la hauteur ? Cliques sur l'onglet Partenaires de notre site.  §e✧  §7Nous avons spécialement besoin de §fvous §7! Envie de contribuer à TouchedCraft ? Faites de belles constructions en créatif pour devenir ouvrier ou aidez les gens pour devenir guide et vous pourrez vous rendre plus loin dans le staff de §f§lTouched§6§lCraft §7!  §e✧  ";
		this.tabMessageWidth = 50;
		
		this.load();

		this.scoreboardAndTabTaskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new ScoreboardAndTabTask(), 0, 2);
		
		Bukkit.getPluginManager().registerEvents(new JoinLeaveListener(), plugin);
		Bukkit.getPluginManager().registerEvents(new PlayerProtectionListener(), plugin);
		Bukkit.getPluginManager().registerEvents(new RegionProtectionListener(), plugin);
		Bukkit.getPluginManager().registerEvents(new GUIListener(this), plugin);

		for(Player p : this.getPlayers())
			join(p);
	}

	@Override
	public void onDisable()
	{
		try
		{
			//Bukkit.getScheduler().cancelTask(timeTaskId);
			Bukkit.getScheduler().cancelTask(this.scoreboardAndTabTaskId);
			
			this.save();
			
			HandlerList.unregisterAll(this);
			
			this.newPlayersSpawn = null;
			this.hub = null;
			this.tabMessage = null;
			this.tabMessageWidth = 0;
			this.scoreboardAndTabTaskId = 0;
			this.region = null;
			
			GameManager.unregisterGame(this);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static Hub getPlugin()
	{
		return plugin;
	}
	
	
	private boolean load()
	{
		try
		{
			YamlConfiguration config = YamlConfiguration.loadConfiguration(getDataFile());
			if(config.isString("hub-location"))
				this.hub = SerializableLocation.fromString(config.getString("hub-location"));
			
			if(config.isString("welcome-location"))
				this.newPlayersSpawn = SerializableLocation.fromString(config.getString("welcome-location"));
			
			if(config.isString("region"))
				this.region = SerializableRegion.fromString(config.getString("region"));
			
			if(config.isInt("tab-message-width"))
				this.tabMessageWidth = config.getInt("tab-message-width");
			
			return true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}
	
	private boolean save()
	{
		try
		{
			YamlConfiguration config = YamlConfiguration.loadConfiguration(getDataFile());
			if(this.hub != null)
				config.set("hub-location", this.hub.toString());
			
			if(this.newPlayersSpawn != null)
				config.set("welcome-location", this.newPlayersSpawn.toString());
			
			if(this.region != null)
				config.set("region", this.region.toString());
			
			if(!config.isString("tab-message"))
				config.set("tab-message", this.tabMessage);
			
			if(!config.isInt("tab-message-width"))
				config.set("tab-message-width", this.tabMessageWidth);
			
			config.save(getDataFile());
			return true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}
	
	private static File getDataFile()
	{
		return new File(plugin.getDataFolder(), "config.yml");
	}

	public Location getHub()
	{
		if(this.hub != null)
			return this.hub.getLocation();
		return null;
	}
	
	public void setHub(Location location)
	{
		this.hub = new SerializableLocation(location);
	}

	public Location getNewPlayersSpawn()
	{
		if(this.newPlayersSpawn != null)
			return newPlayersSpawn.getLocation();
		return null;
	}

	public void setNewPlayersSpawn(Location newPlayersSpawn)
	{
		this.newPlayersSpawn = new SerializableLocation(newPlayersSpawn);
	}

	public String getTabMessage()
	{
		return tabMessage;
	}

	public void setTabMessage(String tabMessage)
	{
		this.tabMessage = tabMessage;
	}

	public int getTabMessageWidth()
	{
		return this.tabMessageWidth;
	}

	@Override
	public boolean contains(Player p)
	{
		return this.contains(p.getLocation());
	}
	
	public boolean contains(Location loc)
	{
		if(this.region != null)
			return this.region.contains(loc);
		
		return false;
	}

	@Override
	public String getChatPrefix(Player p)
	{
		return "§e§lHub";
	}

	@Override
	public String getChatName(Player p)
	{
		return p.getDisplayName();
	}

	@Override
	public String getColoredName()
	{
		return "§e§lHub";
	}

	@Override
	public String getStatus()
	{
		return "L'endroit où les gens choisissent leur jeu.";
	}

	@Override
	public Location getWarp()
	{
		return this.getHub();
	}

	@Override
	public void join(Player p)
	{
		p.setPlayerListName("§7" + p.getName());
		PlayerUtil.heal(p);
		if(!p.hasPermission(Hub.RANK_STAFF))
		{
			PlayerUtil.prepare(p);
			PlayerUtil.clearInventory(p);
			if(p.hasPermission(RANK_VIP))
				p.setAllowFlight(true);
		}

		giveStuff(p);
	}

	@Override
	public void leave(Player p)
	{
		p.setPlayerListName(null);
		TabUtil.resetTab(p);
		PlayerUtil.clearBoard(p);
		PlayerUtil.heal(p);
		if(!p.hasPermission(Hub.RANK_STAFF))
		{
			PlayerUtil.clearInventory(p);
			PlayerUtil.prepare(p);
		}
	}

	public void setRegion(SerializableRegion region)
	{
		this.region = region;
	}

	public List<Player> getPlayers()
	{
		List<Player> list = new ArrayList<Player>();
		for(Player p : Bukkit.getOnlinePlayers())
			if(this.contains(p))
				list.add(p);
		
		return list;
	}

	private void giveStuff(Player player)
	{
		ItemStack compass = new ItemStack(Material.COMPASS, 1, (short)0);

		ItemMeta meta = compass.getItemMeta();
		meta.setDisplayName("§7§lMenu");

		compass.setItemMeta(meta);
		player.getInventory().setItem(0, compass);


		ItemStack chest = new ItemStack(Material.CHEST, 1, (short)0);

		ItemMeta chestMeta = compass.getItemMeta();
		chestMeta.setDisplayName("§c§lGadgets");

		chest.setItemMeta(chestMeta);
		player.getInventory().setItem(4, chest);

		ItemStack flower = new ItemStack(Material.RED_ROSE, 1, (short)0);

		ItemMeta flowerMeta = flower.getItemMeta();
		flowerMeta.setDisplayName("§d§lVote");

		flower.setItemMeta(flowerMeta);
		player.getInventory().setItem(7, flower);


		ItemStack gold = new ItemStack(Material.GOLD_INGOT, 1, (short)0);

		ItemMeta goldMeta = gold.getItemMeta();
		goldMeta.setDisplayName("§6§lBoutique");

		gold.setItemMeta(goldMeta);
		player.getInventory().setItem(8, gold);
	}
}
