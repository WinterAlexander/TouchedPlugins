package me.winterguardian.commandcenter.commands;

import me.winterguardian.core.command.AutoRegistrationCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by Alexander Winter on 2016-01-20.
 */
public class KitCommand extends AutoRegistrationCommand
{
	private List<Kit> kits;

	public KitCommand()
	{
		this.kits = new ArrayList<>();
	}

	@Override
	public void unregister()
	{
		super.unregister();
	}

	@Override
	public void register(Plugin plugin)
	{
		super.register(plugin);

		for(File file : new File(plugin.getDataFolder(), "kits").listFiles())
		{
			YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
			Kit kit = new Kit(file.getName());


		}


	}

	@Override
	public String getName()
	{
		return "kit";
	}

	@Override
	public Permission getPermission()
	{
		return new Permission("CommandCenter.Kit", getDescription(), PermissionDefault.OP);
	}

	@Override
	public String getUsage()
	{
		return "/kit";
	}

	@Override
	public String getDescription()
	{
		return null;
	}

	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings)
	{
		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings)
	{
		return null;
	}

	public static class Kit
	{
		private String name;
		private ItemStack[] content;
		private Permission permission;

		public Kit(String name)
		{
			this.name = name;
		}

		public String getName()
		{
			return name;
		}

		public void setName(String name)
		{
			this.name = name;
		}

		public ItemStack[] getContent()
		{
			return content;
		}

		public void give(Player player)
		{
			for(ItemStack stack : this.content)
				player.getInventory().addItem(stack);
		}

		public void setContent(ItemStack[] content)
		{
			this.content = content;
		}

		public void setContent(Player player)
		{
			this.content = player.getInventory().getContents();
		}

		public Permission getPermission()
		{
			return permission;
		}

		public void setPermission(Permission permission)
		{
			this.permission = permission;
		}

		public void setPermission(String permission)
		{
			this.permission = new Permission(permission, "Permission to use \"" + name + "\" kit with command.");

			if(Bukkit.getPluginManager().getPermission(permission) == null)
				Bukkit.getPluginManager().addPermission(this.permission);
		}
	}
}
