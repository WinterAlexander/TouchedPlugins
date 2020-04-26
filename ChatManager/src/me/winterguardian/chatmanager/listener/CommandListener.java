package me.winterguardian.chatmanager.listener;

import me.winterguardian.chatmanager.ChatManager;
import me.winterguardian.chatmanager.CommandRedirection;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandListener
  implements Listener
{
  @EventHandler(priority=EventPriority.HIGHEST)
  public void allAlias(PlayerCommandPreprocessEvent e)
  {
    if (e.getPlayer().hasPermission(ChatManager.PREPROCESS_COMMAND_ALIASES)) {
      if (!e.isCancelled()) {
        if (e.getMessage().contains("@a"))
        {
          for (Player player : Bukkit.getOnlinePlayers()) {
            e.getPlayer().performCommand(e.getMessage().replaceFirst("/", "").replaceAll("@a", player.getName()));
          }
          e.setCancelled(true);
        }
        else if (e.getMessage().contains("@o"))
        {
          for (Player player : Bukkit.getOnlinePlayers()) {
            if (player != e.getPlayer()) {
              e.getPlayer().performCommand(e.getMessage().replaceFirst("/", "").replaceAll("@o", player.getName()));
            }
          }
          e.setCancelled(true);
        }
        else if (e.getMessage().contains("@w"))
        {
          for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getWorld() == e.getPlayer().getWorld()) {
              e.getPlayer().performCommand(e.getMessage().replaceFirst("/", "").replaceAll("@w", player.getName()));
            }
          }
          e.setCancelled(true);
        }
      }
    }
  }
  
  @EventHandler(priority=EventPriority.HIGH, ignoreCancelled=true)
  public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent e)
  {
    String cmd = null;
    String args = "";
    int i;
    if (e.getMessage().contains(" "))
    {
      String[] ss = e.getMessage().split(" ");
      cmd = ss[0].replaceAll("/", "");
      for (i = 1; i < ss.length; i++) {
        args = args + " " + ss[i];
      }
    }
    else
    {
      cmd = e.getMessage().replaceFirst("/", "");
    }
    if (!e.getPlayer().hasPermission(ChatManager.COMMAND_REDIRECT_BYPASS)) {
      for (CommandRedirection redirection : ChatManager.getChatConfig().getRedirections()) {
        if (redirection.getCommand().equalsIgnoreCase(cmd))
        {
          e.setCancelled(true);
          String finalcommand = redirection.getRedirection() + args;
          finalcommand = finalcommand.replaceAll("@p", e.getPlayer().getName());
          e.getPlayer().performCommand(finalcommand);
        }
      }
    } else {
      for (CommandRedirection redirection : ChatManager.getChatConfig().getRedirections()) {
        if (redirection.getCommand().equalsIgnoreCase(cmd)) {
          if (!redirection.canByPass())
          {
            e.setCancelled(true);
            String finalcommand = redirection.getRedirection() + args;
            finalcommand = finalcommand.replaceAll("@p", e.getPlayer().getName());
            e.getPlayer().performCommand(finalcommand);
          }
        }
      }
    }
  }
}
