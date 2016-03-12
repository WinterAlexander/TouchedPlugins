package ice;

import me.winterguardian.core.message.HardcodedMessage;

public class IceRunMessage extends HardcodedMessage {
	
	public static final IceRunMessage JOIN_ALREADYINGAME = new IceRunMessage("§cVous êtes déjà en jeu.");
	public static final IceRunMessage JOIN_PLAYERALREADYINGAME = new IceRunMessage("§cLe joueur spécifié est déjà en jeu.");
	public static final IceRunMessage JOIN_NOTREADY = new IceRunMessage("Le jeu n'est pas entièrement configuré.");
	public static final IceRunMessage JOIN = new IceRunMessage("§f[§3+§f] §b<player> §aa rejoint IceRun.");
	public static final IceRunMessage JOIN_GAMESTARTED = new IceRunMessage("§fUne partie est déja en cours.");
	
	public static final IceRunMessage LEAVE_NOTINGAME = new IceRunMessage("§cVous n'êtes pas en jeu."); 
	public static final IceRunMessage LEAVE_PLAYERNOTINGAME = new IceRunMessage("§cLe joueur spécifié n'est pas en jeu.");
	public static final IceRunMessage LEAVE = new IceRunMessage("§f[§3-§f] §b<player> §ca quitté IceRun.");
	
	public static final IceRunMessage LOOSE = new IceRunMessage("§c<player> §fest éliminé de la partie.");
	public static final IceRunMessage WIN = new IceRunMessage("§a<winner> §fa gagné la partie.");
	
	public static final IceRunMessage EXITSET = new IceRunMessage("§aLe point de sortie de l'arène a été défini.");
	public static final IceRunMessage EXITNOTINSIDE = new IceRunMessage("§cLe point de sortie de l'arène ne peut pas être placé dans l'arène.");
	
	public static final IceRunMessage SPAWNSET = new IceRunMessage("§aLe spawn de l'arène a été défini.");
	public static final IceRunMessage LOBBYSET = new IceRunMessage("§aLe lobby de l'arène a été défini.");
	public static final IceRunMessage REGIONSET = new IceRunMessage("§aLa région de l'arène a été définie.");
	public static final IceRunMessage STUFFSET = new IceRunMessage("§aL'équipement pour joueurs a été défini.");
	public static final IceRunMessage VIPSTUFFSET = new IceRunMessage("§aL'équipement pour joueurs vips a été défini.");
	
	public static final IceRunMessage STUFFGET = new IceRunMessage("Voici l'équipement pour joueurs.");
	public static final IceRunMessage VIPSTUFFGET = new IceRunMessage("Voici l'équipement pour joueurs vips.");
	
	public static final IceRunMessage CONFIG_SAVE = new IceRunMessage("§aLa config a été sauvegardée.");
	public static final IceRunMessage CONFIG_RELOAD = new IceRunMessage("§aLa config a été rechargée.");
	public static final IceRunMessage CONFIG_ERROR = new IceRunMessage("§cUne erreur de configuration s'est produite."); 
	
	public static final IceRunMessage GAME_JOIN = new IceRunMessage("§aVous avez rejoint la partie.");
	public static final IceRunMessage GAME_LEAVE = new IceRunMessage("§cVous avez quitté la partie.");
	public static final IceRunMessage GAME_NOCOMMAND = new IceRunMessage("§cCette commande n'est pas autorisée en partie."); 
	public static final IceRunMessage GAME_NOTREADY = new IceRunMessage("§cLe jeu n'est pas complètement configuré."); 

	public static final IceRunMessage GAME_NOTENOUGHTPLAYERS = new IceRunMessage("§cIl n'y a pas assez de joueurs pour commencer."); 
	
	public static final IceRunMessage WAITINGSESSION_BEGIN = new IceRunMessage("§aIl y a assez de joueurs pour commencer."); 
	public static final IceRunMessage GAME_BEGIN = new IceRunMessage("La partie d'Ice-Run va bientôt commencer."); 
	
	public static final IceRunMessage GAME_STOP = new IceRunMessage("§fLa partie s'est arrêtée."); 
	public static final IceRunMessage GAME_CANT_START = new IceRunMessage("§fLa partie ne peut pas être lancée.");
	
	public static final IceRunMessage GAME_COOLDOWNBONUS = new IceRunMessage("Vous devez attendre <timer> secondes pour utiliser le saut.");
	public static final IceRunMessage GAME_COOLDOWNBONUS_ONESECOND = new IceRunMessage("Vous devez attendre une seconde pour utiliser le saut.");
	
	public static final IceRunMessage GAME_NOBONUS = new IceRunMessage("§cVous ne pouvez pas utiliser ceci maintenant."); 
	public static final IceRunMessage ICERUN_STATS = new IceRunMessage("§f§lStatistiques de jeu");
	
	public static final IceRunMessage ONESECONDELEFT = new IceRunMessage("Téléportation dans l'arène dans une seconde.", false, MessageType.ACTIONBAR);
	public static final IceRunMessage SECONDELEFT = new IceRunMessage("Téléportation dans l'arène dans <timer> secondes.", false, MessageType.ACTIONBAR);
	
	public static final IceRunMessage TIMERERROR = new IceRunMessage("§cLe timer WaitingSession de Ice-Run ne s'est pas arrêté.");
	
	public static final IceRunMessage GAMESTARTIN = new IceRunMessage("§7§lLe jeu commence dans <timer> !",false, MessageType.ACTIONBAR);
	public static final IceRunMessage GAMESTART = new IceRunMessage("§e§lLa partie commance !",false, MessageType.ACTIONBAR);
	
	public IceRunMessage(String content, boolean prefix, MessageType type) {
		super(content, prefix, type);
	}

	public IceRunMessage(String content, boolean prefix, String hoverEvent, String hoverEventContent, String clickEvent, String clickEventContent) {
		super(content, prefix, hoverEvent, hoverEventContent, clickEvent, clickEventContent);
	}

	public IceRunMessage(String content, boolean prefix) {
		super(content, prefix);
	}

	public IceRunMessage(String title, String subTitle, int delay, int fadeIn, int fadeOut) {
		super(title, subTitle, delay, fadeIn, fadeOut);
	}

	public IceRunMessage(String content) {
		super(content);
	}
	
	protected String getPrefix()
	{
		return("§b§lIceRun §f§l>§7 ");
	}
}
