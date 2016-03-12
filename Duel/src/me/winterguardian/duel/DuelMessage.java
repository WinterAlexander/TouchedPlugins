package me.winterguardian.duel;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public enum DuelMessage
{
	DUEL_STANDBY("§cIl n'y a personne d'autre en duel, invitez des gens ou quittez avec §e/duel quitter§c."),
    DUEL_INFO("§aCeci est un jeu de combat 1 versus 1. Faites §e/duel §apour rejoindre."),
    DUEL_JOIN("§aVous avez rejoint le jeu."),
    DUEL_PLAYERJOIN("§aLe joueur spécifié a rejoint Duel"),
    DUEL_WAIT("§aVous avez rejoint la file d'attente de Duel."),
    DUEL_PLAYERQUIT("§cLe joueur a quitté le jeu."),
    DUEL_QUIT("§cVous avez quitté le jeu."),
    DUEL_QUITWAIT("§cVous avez quitté la file d'attente de Duel."),
    DUEL_STOPCOMPLETE("§eRedémarrage terminé."), 
    DUEL_STOPERROR("§cErreur lors de l'arrêt de duel. Merci de reload le plugin."), 
    DUEL_NOTREADY("§cVous ne pouvez pas faire ceci parce que le jeu n'est pas configuré."), 
    DUEL_NOVEHICULE("§cVous devez quitter votre véhicule pour rejoindre."), 
    DUEL_LOBBYSET("§aLa position du lobby à été mise à jour."),
    DUEL_SPAWN1SET("§aLa position du spawn pour le §cjoueur 1 §aà été mise à jour."),
    DUEL_SPAWN2SET("§aLa position du spawn pour le §9joueur 2 §aà été mise à jour."),
    DUEL_STUFFSET("§aL'équipement pour les duels à été mis à jour."), 
    DUEL_NOTREADYADMINBYPASS("§cLe jeu n'est pas encore complêtement configuré, mais vos permissions admins permettent de surpasser la protection, faites attention aux commandes que vous faites."), 
    DUEL_YOURTURN("§aC'est à votre tour de vous battre en duel."), 
    DUEL_ALREADYINGAME("§cVous êtes déjà en jeu."), 
    DUEL_PLAYERALREADYINGAME("§cLe joueur spécifié est déjà en jeu."),
    DUEL_NOTINGAME("§cVous n'êtes pas en jeu."), 
    DUEL_PLAYERNOTINGAME("§cLe joueur spécifié n'est pas en jeu."), 
    DUEL_CMDBLOCK("§cCommande bloquée en jeu."), 
    DUEL_STATS("§f§lStatistiques de jeu"), 
    DUEL_REGIONSET("§aRégion de duel mise à jour."), 
    DUEL_WILLSTART("Le duel va bientôt commencer."), 
    DUEL_SAVEFAILED("§cUne erreur s'est produite lors de la sauvegarde du fichier config de duel."),
    
    STATUS_NOBODYINGAME("Personne en jeu", false),
    STATUS_ONEPLAYER("<player1> seul en jeu", false),
    STATUS_TWOPLAYERS("<player1> §eversus §9<player2>", false),
    
    COMMAND_USAGE("§cSyntaxe:"),
    
    COMMAND_INFO_NOBODYINGAME("§cPersonne en jeu", false),
    COMMAND_INFO_ONEPLAYER("§c<player1> §eest seul en jeu.", false),
    COMMAND_INFO_TWOPLAYERS("§c<player1> §eest en combat contre §9<player2>§e.", false),
    COMMAND_INFO_WAITINGLIST("§6File d'attente:", false),
    ;
	
	private static final String prefix = "§6§lDuel §f§l>§7";

	private String contenu = "";
	private boolean hasPrefix;

	private DuelMessage(String contenu, boolean prefix)
	{
		this.contenu = contenu;
		this.hasPrefix = prefix;
	}

	private DuelMessage(String contenu)
	{
		this.contenu = contenu;
		this.hasPrefix = true;
	}
	
	public String toString()
	{
		return this.hasPrefix ? prefix + " " + this.contenu : this.contenu;
	}
	
	public void say(CommandSender sender)
	{
		sender.sendMessage(this.toString());
	}
	
	public void sayConsole()
	{
		Bukkit.getConsoleSender().sendMessage(this.toString());
	}
	
	public void sayStaff()
	{
		for(Player p : Bukkit.getOnlinePlayers())
			if(p.hasPermission(Duel.MODERATION))
				p.sendMessage(this.toString());
		this.sayConsole();
	}
	
	public void sayAll()
	{
		for(Player p : Bukkit.getOnlinePlayers())
			p.sendMessage(this.toString());
		this.sayConsole();
	}

	public void sayPlayers()
	{
		for(Player player : Duel.getInstance().getPlayers())
			player.sendMessage(this.toString());
	}
	
	public static String getPrefix()
	{
		return prefix;
	}
}