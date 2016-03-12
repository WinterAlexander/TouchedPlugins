package me.winterguardian.commandcenter.commands;

import me.winterguardian.core.command.AutoRegistrationCommand;
import me.winterguardian.core.message.ErrorMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.util.Arrays;
import java.util.List;

/**
 * Created by 1541869 on 2015-11-19.
 */
public class EnderChestCommand extends AutoRegistrationCommand
{
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if(!(sender instanceof Player))
        {
            ErrorMessage.COMMAND_INVALID_SENDER.say(sender);
            return true;
        }

        Player player = null;

        if(args.length == 0)
            player = (Player)sender;
        else
            player = Bukkit.getPlayer(args[0]);

        if(player == null)
        {
            ErrorMessage.COMMAND_INVALID_PLAYER.say(sender);
            return true;
        }

        ((Player)sender).openInventory(player.getEnderChest());
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args)
    {
        return null;
    }

    @Override
    public String getName()
    {
        return "enderchest";
    }

    @Override
    public String getDescription()
    {
        return "Permet de voir ou éditer le coffre du néan d'un joueur.";
    }

    @Override
    public List<String> getAliases()
    {
        return Arrays.asList("echest", "enderc", "ec",
                "showenderchest", "showechest", "showenderc", "showec",
                "seeenderchest", "seeechest", "seeenderc", "seeec",
                "enderchestsee", "endercsee", "echestsee", "ecsee",
                "enderchestshow", "endercshow", "echestshow", "ecshow",

                "voircoffre", "voircoffrenéan", "coffrenéan");
    }

    @Override
    public Permission getPermission()
    {
        return new Permission("CommandCenter.enderchest", getDescription(), PermissionDefault.OP);
    }

    @Override
    public String getUsage()
    {
        return "/enderchest [player]";
    }
}