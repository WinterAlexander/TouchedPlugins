package me.winterguardian.chatmanager;

import me.winterguardian.core.json.JsonUtil;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class ChatBroadcast
{
  private String name;
  private String[] messages;
  private boolean json;
  private Sound sound;
  
  public ChatBroadcast(String name, String[] messages, boolean json, Sound sound)
  {
    this.name = name;
    this.messages = messages;
    this.json = json;
    this.sound = sound;
  }
  
  public ChatBroadcast(ConfigurationSection section)
  {
    name = section.getName();
    messages = new String[] { "" };
    messages = ((String[])section.getStringList("messages").toArray(messages));
    json = section.getBoolean("isjson");
    sound = Sound.valueOf(section.getString("sound").toUpperCase());
  }
  
  public String getName()
  {
    return name;
  }
  
  public void setName(String name)
  {
    this.name = name;
  }
  
  public void sayAll()
  {
    for (Player p : Bukkit.getOnlinePlayers())
    {
      if (isJson()) {
        for (String message : messages) {
          JsonUtil.sendJsonMessage(p, message);
        }
      } else {
        for (String message : messages) {
          p.sendMessage(message);
        }
      }
      p.playSound(p.getLocation(), sound, 10.0F, 1.0F);
    }
  }
  
  public boolean isJson()
  {
    return json;
  }
  
  public void setJson(boolean json)
  {
    this.json = json;
  }
  
  public void save(ConfigurationSection section)
  {
    section.set("messages", Arrays.asList(messages));
    section.set("isjson", Boolean.valueOf(json));
    if (sound != null) {
      section.set("sound", sound.name());
    }
  }
}
