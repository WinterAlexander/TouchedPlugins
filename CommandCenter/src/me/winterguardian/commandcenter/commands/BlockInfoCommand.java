package me.winterguardian.commandcenter.commands;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import me.winterguardian.core.command.AutoRegistrationCommand;
import me.winterguardian.core.message.ErrorMessage;

import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

public class BlockInfoCommand extends AutoRegistrationCommand
{
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if ((sender instanceof Player))
		{
			BlockState block = ((Player)sender).getTargetBlock((HashSet<Material>)null, 5).getState();
			sender.sendMessage("§f- §cInfo sur le bloc pointé");
			sender.sendMessage("§a" + block.getType().name() + " §e-> §c" + block.getData().getItemTypeId() + ":" + block.getData().getData());
			sender.sendMessage("§6Position: §f{§eworld: §f" + block.getWorld().getName() + " §ex: §f" + block.getX() + " §ey: §f" + block.getY() + " §ez: §f" + block.getZ() + "§f}");
			return true;
		}
		ErrorMessage.COMMAND_INVALID_SENDER.say(sender);
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args)
	{
		return null;
	}

	@Override
	public List<String> getAliases()
	{
		return Arrays.asList("infobloc", "cubeinfo");
	}

	@Override
	public String getDescription()
	{
		return "Permet d'obtenir toutes les informations sur un bloc pointé.";
	}

	@Override
	public String getName()
	{
		return "blockinfo";
	}

	@Override
	public Permission getPermission()
	{
		return new Permission("CommandCenter.block-info", "/blockinfo", PermissionDefault.OP);
	}

	@Override
	public String getUsage()
	{
		return "§cSyntaxe: §f/blockinfo";
	}

}
