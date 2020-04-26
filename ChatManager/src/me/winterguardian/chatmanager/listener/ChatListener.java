package me.winterguardian.chatmanager.listener;

import me.winterguardian.chatmanager.ChatManager;
import me.winterguardian.core.util.TextUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.permissions.Permission;

public class ChatListener implements Listener
{
	@EventHandler(ignoreCancelled = true)
	public void chatCleaner(AsyncPlayerChatEvent e)
	{
		for(String insult : ChatManager.getChatConfig().getInsults())
			e.setMessage(e.getMessage().replaceAll("(?i)" + insult, TextUtil.generateInsultReplacement(insult)));

		if(e.getMessage().contains("using MineChat"))
			e.setCancelled(true);

		e.setMessage(e.getMessage().replaceAll("(.)(\\1{3,})", "$1$1$1"));

		e.setMessage(e.getMessage().replaceAll("([tT])+([oOôÔòÒöÖ])+([uUûÛùÙüÜ])+([cCçÇ(])+([hH])+([eEêÊéÉèÈëË])+([dD])+(([ -+=])*([cCçÇ])+([rR])+([aAâÂàÀäÄ])+([fF])+([tT])+)?", "touchedcrap"));
		e.setMessage(e.getMessage().replaceAll("([eEêÊéÉèÈëË])+([pP])+([iIîÎïÏìÌ|1])+([ -+=])*([cCçÇ(])+([uUûÛùÙüÜ])+([bB])+([eEêÊéÉèÈëË])*", "épicul"));
		e.setMessage(e.getMessage().replaceAll("[sS$]+[aâäàAÂÄÀ4]+[mM]+[aâäàAÂÄÀ4]+(\\s)*[gG][aâäàAÂÄÀ4]+[mM]+[eêèëéEÊÈËÉ3]+[sS$]*", "samacaca"));
		e.setMessage(e.getMessage().replaceAll("[kK]+[oOôÔòÒöÖ]+[hH]+[iIîÎïÏìÌ]+", "prout"));
		e.setMessage(e.getMessage().replaceAll("(^|[ ])(([eEêÊéÉèÈëË])+([zZ])+)+(?!\\S)", "$1<3"));



		if(e.getPlayer().hasPermission(ChatManager.COLOR))
		{
			for(char c = 'a'; c <= 'f'; c++)
				e.setMessage(e.getMessage().replace("&" + c, "§" + c));

			for(char c = '0'; c <= '9'; c++)
				e.setMessage(e.getMessage().replace("&" + c, "§" + c));
		}

		if(e.getPlayer().hasPermission(ChatManager.SPECIAL))
			e.setMessage(e.getMessage().replace("&l", "§l").replace("&r", "§r").replace("&k", "§k").replace("&o", "§o").replace("&n", "§n").replace("&m", "§m"));

	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void onPlayerChat(AsyncPlayerChatEvent e)
	{
		e.setFormat("%s §7§l» §r%s");

		for(Integer i : ChatManager.getChatConfig().getChatFormats().keySet())
		{
			boolean doContinue;
			do
			{
				try
				{
					if(e.getPlayer().hasPermission(new Permission("ChatManager.chat-format.level" + i)))
						e.setFormat(ChatManager.getChatConfig().getChatFormats().get(i));
					doContinue = false;
				}
				catch(Exception ex)
				{
					doContinue = true;
				}
			}
			while(doContinue);
		}
	}
}
