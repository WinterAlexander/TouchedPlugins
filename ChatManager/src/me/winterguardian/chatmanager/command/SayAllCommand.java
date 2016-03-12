package me.winterguardian.chatmanager.command;

import java.util.Arrays;
import java.util.List;
import me.winterguardian.core.command.AutoRegistrationCommand;
import me.winterguardian.core.json.JsonUtil;
import me.winterguardian.core.message.ErrorMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

public class SayAllCommand
  extends AutoRegistrationCommand
{
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
  {
    String content = "";
    if (args.length > 0)
    {
      if (args[0].equalsIgnoreCase("-json"))
      {
        if (args.length >= 1) {
          content = args[1];
        }
        for (int i = 2; i < args.length; i++) {
          content = content + " " + args[i];
        }
        for (Player p : Bukkit.getOnlinePlayers()) {
          JsonUtil.sendJsonMessage(p, content);
        }
        return true;
      }
      content = args[0];
    }
    for (int i = 1; i < args.length; i++) {
      content = content + " " + args[i];
    }
    for (Player p : Bukkit.getOnlinePlayers()) {
      p.sendMessage(content.replaceAll("&", "§"));
    }
    return true;
  }
  
  public List<String> onTabComplete(CommandSender arg0, Command arg1, String arg2, String[] arg3)
  {
    return null;
  }
  
  public List<String> getAliases()
  {
    return Arrays.asList("sayraw", "broadcast", "bc");
  }
  
  public String getDescription()
  {
    return "Envoie un message à tout les joueurs du serveur.";
  }
  
  public String getName()
  {
    return "sayall";
  }
  
  public Permission getPermission()
  {
    return new Permission("ChatManager.sayall", "Permet d'envoyer des messages à tout les joueurs dans le chat.", PermissionDefault.OP);
  }
  
  public String getPermissionMessage()
  {
    return ErrorMessage.COMMAND_INVALID_PERMISSION.toString();
  }
  
  public String getUsage()
  {
    return "§cSyntaxe: §f/sayraw [message]";
  }
}
