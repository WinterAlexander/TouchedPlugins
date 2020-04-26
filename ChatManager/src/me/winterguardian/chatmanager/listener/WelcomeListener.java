package me.winterguardian.chatmanager.listener;

import me.winterguardian.chatmanager.ChatManager;
import me.winterguardian.core.Core;
import me.winterguardian.core.playerstats.PlayerStats;
import me.winterguardian.core.util.TextUtil;
import me.winterguardian.core.util.TitleUtil;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Random;

public class WelcomeListener implements Listener
{
	private String lastMessage;

	public WelcomeListener()
	{
		lastMessage = "";
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerJoin(final PlayerJoinEvent event)
	{
		if(event.getPlayer().hasPermission(ChatManager.CONNECTION_MESSAGES))
			event.setJoinMessage("§a[+] §b" + event.getPlayer().getName() + " §as'est connecté.");
		else
			event.setJoinMessage(null);

		if(!Core.getUserDatasManager().isEnabled())
			return;

		Bukkit.getScheduler().runTaskLater(ChatManager.getPlugin(), new Runnable()
		{
			@Override
			public void run()
			{
				if(!Core.getUserDatasManager().isLoaded(event.getPlayer()))
				{
					Bukkit.getScheduler().runTaskLater(ChatManager.getPlugin(), this, 1);
					return;
				}

				if(new PlayerStats(Core.getUserDatasManager().getUserData(event.getPlayer())).isNew())
				{
					for(Player player : Bukkit.getOnlinePlayers())
					{
						if(player.hasPermission(ChatManager.AUTO_WELCOME))
							Bukkit.getScheduler().runTaskLater(ChatManager.getPlugin(), new WelcomeMessage(player, event.getPlayer()), new Random().nextInt(80) + 40);

					}
					for(Player player : Bukkit.getOnlinePlayers())
					{
						player.playSound(player.getLocation(), Sound.LEVEL_UP, 10.0F, 1.0F);
						player.sendMessage("§aBienvenue §r" + event.getPlayer().getDisplayName() + " §asur §f§lTouched§6§lCraft §a!");
					}
					TitleUtil.displayTitle(event.getPlayer(), ChatManager.getChatConfig().getWelcomeTitle(), ChatManager.getChatConfig().getWelcomeSubTitle(), 15, 60, 10);
				}
				else
				{
					TitleUtil.displayTitle(event.getPlayer(), ChatManager.getChatConfig().getJoinTitle(), ChatManager.getChatConfig().getJoinSubTitle(), 15, 60, 10);
				}
			}
		}, 1);
	}

	public String getWelcomeMessage(String player)
	{
		return this.lastMessage = TextUtil.generateWelcomeMessage(player, lastMessage);
	}

	private class WelcomeMessage implements Runnable
	{
		private Player player;
		private Player newPlayer;

		public WelcomeMessage(Player p, Player newPlayer)
		{
			player = p;
			this.newPlayer = newPlayer;
		}

		public void run()
		{
			if((player.isOnline()) && (newPlayer.isOnline()))
			{
				player.chat(getWelcomeMessage(newPlayer.getName()));
			}
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerQuit(PlayerQuitEvent event)
	{
		if(event.getPlayer().hasPermission(ChatManager.CONNECTION_MESSAGES))
			event.setQuitMessage("§c[-] §b" + event.getPlayer().getName() + " §cs'est déconnecté.");
		else
			event.setQuitMessage(null);
	}
}
