package me.winterguardian.test;

import me.winterguardian.core.command.SubCommand;
import me.winterguardian.test.components.*;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class TestingCenter extends JavaPlugin
{
	public static final Permission TEST_LEVEL1 = new Permission("TestingCenter.Level1", "§aSécurité faible", PermissionDefault.OP);
	public static final Permission TEST_LEVEL2 = new Permission("TestingCenter.Level2", "§eSécurité moyenne", PermissionDefault.OP);
	public static final Permission TEST_LEVEL3 = new Permission("TestingCenter.Level3", "§cSécurité élévé", PermissionDefault.OP);
	public static final Permission TEST_LEVEL4 = new Permission("TestingCenter.Level4", "§4Sécurité maximale", PermissionDefault.OP);
	private TestCommand testCommand;
	
	private static TestingCenter plugin;
	
	@Override
	public void onEnable()
	{
		try
		{
			plugin = this;
			
			this.testCommand = new TestCommand();
			this.testCommand.getSubCommands().add(new ActionBar());
			this.testCommand.getSubCommands().add(new ArrowGrenade());
			this.testCommand.getSubCommands().add(new AutomaticTurret());
			this.testCommand.getSubCommands().add(new AutomaticTurret2());
			this.testCommand.getSubCommands().add(new ItemBow());
			this.testCommand.getSubCommands().add(new SpoingBow());
			this.testCommand.getSubCommands().add(new SpinningBlock());
			this.testCommand.getSubCommands().add(new MountTest());
			this.testCommand.getSubCommands().add(new AutoProjectile());
			this.testCommand.getSubCommands().add(new TVTest());
			this.testCommand.getSubCommands().add(new ParticleTest());
			this.testCommand.getSubCommands().add(new GroundPound());
			this.testCommand.getSubCommands().add(new CowRush());
			this.testCommand.getSubCommands().add(new WaterTest());
			this.testCommand.getSubCommands().add(new PlayerMoveTest());
			this.testCommand.getSubCommands().add(new MaterialTest());
			this.testCommand.getSubCommands().add(new AirCannon());
			this.testCommand.getSubCommands().add(new EntityMethods());
			this.testCommand.getSubCommands().add(new Lift());
			this.testCommand.getSubCommands().add(new SQLTestTables());
			this.testCommand.getSubCommands().add(new PlayerTotalExp());
			this.testCommand.getSubCommands().add(new VillagerTest());
			this.testCommand.getSubCommands().add(new PlayerTest());
			this.testCommand.getSubCommands().add(new EventTest());
			this.testCommand.getSubCommands().add(new AutoJump());
			//this.testCommand.getSubCommands().add(new SignTest());

			this.testCommand.register(this);
			
			for(SubCommand sub : this.testCommand.getSubCommands())
			{
				Bukkit.getPluginManager().registerEvents((TestComponent)sub, this);
				if(sub instanceof PluginMessageListener)
				{
					this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
					this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", (PluginMessageListener) sub);
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void onDisable()
	{
		try
		{
			HandlerList.unregisterAll(this);
			this.testCommand = null;
			plugin = null;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static TestingCenter getInstance()
	{
		return plugin;
	}
}
