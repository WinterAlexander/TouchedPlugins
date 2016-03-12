package me.winterguardian.chatmanager.command;

import java.util.Arrays;
import java.util.List;

import me.winterguardian.chatmanager.ChatManager;
import me.winterguardian.chatmanager.ChatManagerMessage;
import me.winterguardian.chatmanager.CommandRedirection;
import me.winterguardian.core.command.AutoRegistrationCommand;
import me.winterguardian.core.message.ErrorMessage;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;

public class RedirectCommand
  extends AutoRegistrationCommand
{
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
  {
    if (args.length == 4)
    {
      if (args[0].equalsIgnoreCase("add"))
      {
        ChatManager.getChatConfig().getRedirections().add(new CommandRedirection(args[1].replaceFirst("/", "").replaceAll("_", " "), args[2].replaceFirst("/", "").replaceAll("_", " "), Boolean.parseBoolean(args[3])));
        ChatManagerMessage.CMDREDIRECT_COMMANDADDED.say(sender);
      }
      else
      {
        ErrorMessage.COMMAND_INVALID_ARGUMENT.say(sender, new String[0]);
      }
    }
    else if (args.length == 2)
    {
      if (args[0].equalsIgnoreCase("remove"))
      {
        if (ChatManager.getChatConfig().removeRedirection(args[1])) {
          ChatManagerMessage.CMDREDIRECT_COMMANDREMOVED.say(sender);
        } else {
          ChatManagerMessage.CMDREDIRECT_NOTONLIST.say(sender);
        }
      }
      else
      {
        if ((args[0].equalsIgnoreCase("example")) || (args[0].equalsIgnoreCase("exemple")))
        {
          if (args[1].equalsIgnoreCase("vip")) {
            ErrorMessage.COMMAND_INVALID_PERMISSION.say(sender, new String[0]);
          }
          if (args[1].equalsIgnoreCase("noperm")) {
            ErrorMessage.COMMAND_INVALID_PERMISSION.say(sender, new String[0]);
          }
          if (args[1].equalsIgnoreCase("invalid")) {
            ErrorMessage.COMMAND_INVALID_ARGUMENT.say(sender, new String[0]);
          }
          return true;
        }
        ErrorMessage.COMMAND_INVALID_ARGUMENT.say(sender, new String[0]);
      }
    }
    else if (args.length == 0)
    {
      sender.sendMessage("§aListe des redirections:");
      for (CommandRedirection redirection : ChatManager.getChatConfig().getRedirections()) {
        sender.sendMessage("§a" + redirection.getCommand() + " §f-> §e" + redirection.getRedirection() + " §fPeu être ignoré: " + (redirection.canByPass() ? "§aOui" : "§cNon"));
      }
    }
    return true;
  }
  
  public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args)
  {
    return null;
  }
  
  public List<String> getAliases()
  {
    return Arrays.asList("cmdrd", "cr", "commandr", "commandredirect");
  }
  
  public String getDescription()
  {
    return "Permet de faire rediriger certaines commandes.";
  }
  
  public String getName()
  {
    return "redirect";
  }
  
  public Permission getPermission()
  {
    return ChatManager.COMMAND_REDIRECT_BYPASS;
  }
  
  public String getPermissionMessage()
  {
    return ErrorMessage.COMMAND_INVALID_PERMISSION.toString();
  }
  
  public String getUsage()
  {
    return "§cSyntaxe: §f/redirect [add|remove|example]";
  }
}
