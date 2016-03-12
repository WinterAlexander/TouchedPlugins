package me.winterguardian.commandcenter;

import me.winterguardian.core.message.ErrorMessage;
import me.winterguardian.core.message.HardcodedMessage;


public class CommandCenterMessage
{
	private CommandCenterMessage(){}
	
	public static final HardcodedMessage 
	TASKSRESET = new HardcodedMessage("§eLa liste des tâches à été reset."), 
	TASKREMOVED = new HardcodedMessage("§eLa tâche à été retiré."),
	TASKADDED = new HardcodedMessage("§aLa tâche à été ajouté."), 
	ANYTASK = new HardcodedMessage("§cAucune tâche"),
	TASKLIST = new HardcodedMessage("§bListe des tâches:"),
	
	FIREWORKSHOOT = new HardcodedMessage("§aUn feu d'artifice a été lancé."),
	
	ENCHANTS = new HardcodedMessage("§aEnchantements: §e"),
	ENCHANT_COMPLETE = new HardcodedMessage("§aL'item s'est bien enchanté."),
	
	INVENTORYCLEAR = new HardcodedMessage("§7Le contenu de votre inventaire a été vidé.", false),
	INVENTORYCLEAR_OTHER = new HardcodedMessage("§7Le contenu de l'inventaire du joueur a été vidé.", false),
	INVENTORYCLEAR_OTHER_SILENTLY = new HardcodedMessage("§7L'inventaire du joueur a été vidé sans l'avertir.", false),
	
	JUKEBOXDISC_SET = new HardcodedMessage("§aLe disque a été mis dans le jukebox."),
	JUKEBOXDISC_REMOVE = new HardcodedMessage("§aLe disque a été retiré du jukebox."),
	
	SETGAMEMODE = new HardcodedMessage("§aVotre mode de jeu a été mis à jour."),
	SETGAMEMODE_OTHERS = new HardcodedMessage("§aLe mode du jeu du joueur a été mis à jour."),
	
	MUSICPLAYED = new HardcodedMessage("§aLa musique a bien été démarrée."),
	MUSICSTOPPED = new HardcodedMessage("§aLa musique a bien été arrêtée."),
			
	POINT_GIVE_NOTENOUGH = new HardcodedMessage("§cVous avez pas assez de point(s)."),
	POINT_GIVE_DONE_SENDER = new HardcodedMessage("§aVos §f# point(s) §aont été donnés à <player>."),
	POINT_GIVE_DONE_RECEIVER = new HardcodedMessage("§aVous avez reçu §f# §apoint(s) de la part de <player>."),
	POINT_SHOW = new HardcodedMessage("§a<player> §f# §apoint(s)."),
	
	POINT_ADD = new HardcodedMessage("§a# point(s) ont été ajoutés à <player>."),
	POINT_REMOVE = new HardcodedMessage("§a# point(s) ont été retirés à <player>."),
	POINT_SET = new HardcodedMessage("§a<player> a désormais # point(s)."),
	
	SPEED_CHANGED = new HardcodedMessage("§aVous allez désormais à une vitesse de <speed>."),
	SPEED_CHANGED_OTHER = new HardcodedMessage("§a<player> va désormais à une vitesse de <speed>."),
	
	SKULL_GIVED = new HardcodedMessage("§fVous avez reçus le crâne de <skull>."),
	
	PORTAL_ADD_DONE = new HardcodedMessage("§aLe portail <name> a été ajouté."),
	PORTAL_LIST_HEADER = new HardcodedMessage("§3Liste des portails:"),
	PORTAL_LIST_PORTAL = new HardcodedMessage("  > §e<name> §fde type §c<type>"),
	PORTAL_LIST_EMPTY = new HardcodedMessage("§cListe vide."),
	
	PORTAL_REMOVE_NOTINLIST = new HardcodedMessage("§cCe portail n'est pas dans la liste."),
	PORTAL_REMOVE_DONE = new HardcodedMessage("§cLe portail a bien été supprimmé."),
	
	SERVER_SENDING = new HardcodedMessage("§aVous avez été envoyé vers <server>..."),
	RESENDMESSAGE_NORECIPIENT = new ErrorMessage("§cVous n'avez personne à qui renvoyer un message."),

	ITEMFLAG_FLAGS = new HardcodedMessage("§eVoici les différents ItemFlags:"),
	ITEMFLAG_ADD = new HardcodedMessage("§aItemFlag <flag> ajouté."),
	ITEMFLAG_REMOVE = new HardcodedMessage("§cItemFlag <flag> retiré."),

	HAT_NO_ITEM = new HardcodedMessage("§cVous devez avoir un objet en main pour le mettre sur votre tête.");
	
}
