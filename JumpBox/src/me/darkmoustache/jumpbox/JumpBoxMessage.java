package me.darkmoustache.jumpbox;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public enum JumpBoxMessage
{
	COMMAND_CP_RESET("§fCheckpoint mis à zéro !"),
	COMMAND_REGISTER_WORLD("§fMonde enregistré !"),
	COMMAND_ERROR("§cUne erreur s'est produite avec votre commande Jump-Box !"),
	CP_SET("§fCheckpoint enregistré !"),
	CP_CREATE("§fCheckpoint créé !"),
	NO_CP("§fVous avez terminé le Jump sans checkpoint !"),
	COMMAND_CANT_WORLD("§cVous ne pouvez pas faire ceci ici."),
	COMMAND_STATS("§f§lStatistique de jeu :"),
	WASLASTJUMP("§cVous n'avez gagné que 20% des points puisque vous avez fait ce jump deux fois de suite."), 
	BEGIN_SET("§fPoint de départ enregistré !"), 
	COMMAND_SPAWNSET("Point de spawn enregistré."),
	COMMAND_REGIONSET("Région enregistré."),
	;
	
  public static final String prefix = "§a§lJumpBox §f§l>§7";
  private String content;

  private JumpBoxMessage(String content)
  {
    this.content = content;
  }

  @Override
  public String toString()
  {
	  return prefix + " " + this.content;
  }

	public void say(CommandSender sender)
	{
		sender.sendMessage(toString());
	}

  public void sayConsole()
  {
    Bukkit.getConsoleSender().sendMessage(toString());
  }

  public void sayStaff()
  {
    for (Player player : Bukkit.getOnlinePlayers())
    {
    	if(player.hasPermission(JumpBox.STAFF))
    		player.sendMessage(toString());
    }
    sayConsole();
  }

  public void sayAll()
  {
    for (Player player : Bukkit.getOnlinePlayers())
    {
      player.sendMessage(toString());
    }
    sayConsole();
  }
}
