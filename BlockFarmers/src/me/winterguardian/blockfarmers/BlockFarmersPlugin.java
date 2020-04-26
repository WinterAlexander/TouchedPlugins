package me.winterguardian.blockfarmers;

import me.winterguardian.core.Core;
import me.winterguardian.core.world.Wand;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class BlockFarmersPlugin extends JavaPlugin
{
	public static final Permission PLAY = new Permission("BlockFarmers.play", "BlockFarmers default access to play", PermissionDefault.TRUE);
	public static final Permission STAFF = new Permission("BlockFarmers.staff", "BlockFarmers Mod/Staff access", PermissionDefault.OP);

	public static final Permission STONE_HOE = new Permission("BlockFarmers.stone-hoe", "BlockFarmers access to stone hoe", PermissionDefault.TRUE);
	public static final Permission GOLD_HOE = new Permission("BlockFarmers.gold-hoe", "BlockFarmers access to gold hoe", PermissionDefault.OP);
	public static final Permission IRON_HOE = new Permission("BlockFarmers.iron-hoe", "BlockFarmers access to iron hoe", PermissionDefault.OP);
	public static final Permission DIAMOND_HOE = new Permission("BlockFarmers.diamond-hoe", "BlockFarmers access to diamond hoe", PermissionDefault.OP);

	private static BlockFarmersGame game;
	private static YamlConfiguration lang;
	private static BlockFarmersHook hook;
	
	@Override
	public void onEnable()
	{
		
		try
		{
			File lang = new File(getDataFolder(), "lang.yml");
			File langEN = new File(getDataFolder(), "langEN.yml");
			
			this.saveResource("langEN.yml", true);
			this.saveResource("langFR.yml", true);
			
			if(!lang.exists())
			{
				langEN.renameTo(lang);
				this.saveResource("langEN.yml", true);
			}
			else
			{	
				boolean save = false;
				YamlConfiguration langConfig = YamlConfiguration.loadConfiguration(lang);
				YamlConfiguration langENConfig = YamlConfiguration.loadConfiguration(langEN);
				for(String key : langENConfig.getKeys(false))
					if(!langConfig.isString(key))
					{	save = true;
						langConfig.set(key, langENConfig.get(key));
					}
				
				if(save)
					langConfig.save(lang);
			}
			
			if(!new File(getDataFolder(), "config.yml").exists())
				this.saveResource("config.yml", false);
			this.saveResource("configFR.yml", true);
		}
		catch(Exception e)
		{
			getLogger().warning("§cBlockFarmers.jar is §4CORRUPTED please redownload from http://www.spigotmc.org/.");
		}
		lang = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "lang.yml"));

		Core.getWand().enable(this, STAFF, BlockFarmersMessage.WAND);

		game = new BlockFarmersGame(this);
		game.onEnable();
		game.setOpen(true);

        PLAY.addParent(STAFF, true);

        if(game.getConfig() != null && game.getConfig() instanceof BlockFarmersConfig && ((BlockFarmersConfig)game.getConfig()).enableStats())
        {
            BlockFarmersConfig config = (BlockFarmersConfig) game.getConfig();
            if (config.isSqlEnabled())
                Core.getUserDatasManager().enableDB(this, config.getSqlDriver(), config.getSqlURL(), config.getSqlUser(), config.getSqlPassword(), FarmersStats.getTables());
            else
                Core.getUserDatasManager().enableYaml(this);
        }


            try
		{
			getClass().getClassLoader().loadClass("me.clip.placeholderapi.PlaceholderHook");
			getClass().getClassLoader().loadClass("me.clip.placeholderapi.PlaceholderAPI");
	      
			hook = new BlockFarmersHook();
			if(hook.register(this))
				getLogger().info("§aBlockFarmers Hook have been properly registred to PlaceHoldersAPI");
			else
				hook = null;
	    }
	    catch (Throwable t)
	    {
	    	hook = null;
	    }
	}
	
	@Override
	public void onDisable()
	{
		if (hook != null)
			try
			{
				if (hook.unregister(this))
					getLogger().info("§aBlockFarmers Hook have been properly unregistred to PlaceHoldersAPI");
				hook = null;
			}
			catch (Throwable localThrowable)
			{
				hook = null;
			}
		
		game.onDisable();
		game = null;
		Core.disable(this);
	}

	public static BlockFarmersGame getGame()
	{
		return game;
	}

    @Deprecated
	public static Wand getWand()
	{
		return Core.getWand();
	}

	public static YamlConfiguration getLang()
	{
		return lang;
	}
}
