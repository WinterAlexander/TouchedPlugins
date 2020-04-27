package me.winterguardian.pvp;

import me.winterguardian.core.message.HardcodedMessage;

public class PvPMessage extends HardcodedMessage
{
	public static final PvPMessage
	
	JOIN = new PvPMessage("§f[§a+§f] <player> §aa rejoint la partie.", false),
	JOIN_ALREADYINGAME = new PvPMessage("§cVous êtes déjà en jeu.", true),
	JOIN_CLOSED = new PvPMessage("§cLe jeu est temporairement fermé.", true),
	JOIN_MAX = new PvPMessage("§cLe jeu est plein, vous ne pouvez pas rejoidnre.", true),
	
	LEAVE = new PvPMessage("§f[§c-§f] <player> §ca quitté la partie.", false),
	LEAVE_NOTINGAME = new PvPMessage("§cVous n'êtes pas en jeu.", true),
	
	STANDBY_NOTENOUGHPLAYERS = new PvPMessage("§cIl n'y a pas assez de joueurs pour commencer une partie.", true),
	
	VOTE_START = new PvPMessage("§aUne partie de <type> va commencer, votez pour votre arène !", true),
	VOTE_END = new PvPMessage("§aL'arène de la partie sera <arena> !", true),
	VOTE_TIMER = new PvPMessage("§eTéléportation à l'arène dans #", false, MessageType.ACTIONBAR),
	VOTE_TELEPORT = new PvPMessage("§eTéléportation à l'arène...", false, MessageType.ACTIONBAR),
	VOTE_ALREADYVOTED = new PvPMessage("§cVous avez déjà voté pour cette arène.", true),
	VOTE_VOTED = new PvPMessage("§aVotre vote a bien été enregistré.", true),
	VOTE_VOTE_NODOUBLEVOTE = new PvPMessage("§eDevenez §f§lÉlite §epour avoir accès au double-votes !", true),
	VOTE_TOOLATE = new PvPMessage("§cL'arène est djà choisi, il est trop tard pour voter.", true),
	VOTE_INCOMPATIBLE = new PvPMessage("§cCette arène est incompatible avec <mode>.", true),
	
	COMMAND_ARENA_INVALIDARENA = new PvPMessage("§cCette arène n'existe pas.", true),
	COMMAND_ARENA_ADD = new PvPMessage("§a<type> de l'équipe <color> ajouté.", true),
	COMMAND_ARENA_RESET = new PvPMessage("§aLes <type>(s) de couleur <color> a été réinitialisés.", true),
	COMMAND_ARENA_CREATE = new PvPMessage("§aL'arène a été crée.", true),
	COMMAND_ARENA_LIST = new PvPMessage("§eListe des arènes:", true),
	
	COMMAND_STATS_HEADER = new PvPMessage("§f§lStatistiques de jeu : §4§l<player>", true),
	COMMAND_STATS_LEVELEXPRATIO = new PvPMessage("&6Niveau: &f<level> | &eExp: &f<exp> | &3Ratio: &f<ratio>", false),
	COMMAND_STATS_KILLSDEATHSASSISTS = new PvPMessage("&aMeutres: &f<kills> | &cMorts: &f<deaths>  | &dAssistances: &f<assists>", false),
	COMMAND_STATS_VICTORIESGAMES = new PvPMessage("&4Victoires: &f<victories> | &2Parties joués: &f<gamesplayed>", false),
	COMMAND_STATS_FLAGSZONES = new PvPMessage("&9Drapeaux capturés: &f<flags> | &3Zones capturés: &f<zones>", false),
	COMMAND_STATS_BESTSTREAKBESTGAME = new PvPMessage("&7Top série de victimes: &f<killstreak> | &fTop meutres en une partie: &f<kills>", false),
	COMMAND_STATS_SCOREPOINTSTIME = new PvPMessage("&eScore: &f<score> | &bTemps de jeu: &f<time>", false),

	COMMAND_VOTE_BADSTATE = new PvPMessage("§cVous ne pouvez pas votez pour une arène maintenant.", true),
	COMMAND_VOTE_LIST = new PvPMessage("§aVoici la liste des arènes compatibles:", true),

	COMMAND_SETUP_SETEXIT = new PvPMessage("La sortie a été placée.", true),
	COMMAND_SETUP_SETLOBBY = new PvPMessage("Le lobby a été placé.", true),
	COMMAND_SETUP_SETREGION = new PvPMessage("La région a été placée.", true),

	COMMAND_BLOCKED = new PvPMessage("§cCette commande est bloquée en jeu.", true),
	
	GAME_KILLSTREAK = new PvPMessage("<player> <color>a une série de # victimes.", false),
	
	GAME_FRIENDDEATH = new PvPMessage("§cVotre <friend> est décédé. :(", true),
	GAME_CANTHAVE2FRIENDS = new PvPMessage("§cVous avez déjà un mob à vos côtés.", true),

	GAME_JOINTEAM = new PvPMessage("<player> §frejoint l'équipe des <color>§f.", false),

	GAME_KILL = new PvPMessage("<player> §ea tué §e<victim>§e.", false),
	GAME_SUICIDE = new PvPMessage("<player> §es'est suicidé.", false),

	GAME_INF_INFECT = new PvPMessage("<player> §c a infecté §e<victim>§e.", false),
	GAME_INF_SELFINFECT = new PvPMessage("<player> §c s'est infecté.", false),

	GAME_START_FFA = new PvPMessage("§eQue la mêlée commence ! Bonne chance à tous.", true),
	GAME_START_OITC = new PvPMessage("§eLa balle chargée prend place. À vos marques, tirez !", true),
	GAME_START_TDM = new PvPMessage("§eLe match à mort est commencé ! Bonne chance aux équipes.", true),
	GAME_START_CTF = new PvPMessage("§eLa chasse aux drapeaux est amorcée !", true),
	GAME_START_DOM = new PvPMessage("§eLaissez place à la domination, bon courage !", true),
	GAME_START_KOTH = new PvPMessage("§eQui sera donc le champion ? Bonne chance !", true),
	GAME_START_SWI = new PvPMessage("§eLa partie de switch commence, bon jeu !", true),
	GAME_START_BRAWL = new PvPMessage("§eLa partie commence, bonne baston !", true),
	GAME_START_INFECTED = new PvPMessage("§eLa partie d'infecté commence!", true),

	GAME_GUIDE_FFA = new PvPMessage("<mode>§f - Le but du jeu est de faire le plus de meurtres pour soi même. Il n'y a pas d'équipes ou d'alliances. §4§lALLIANCES = BAN", false),
	GAME_GUIDE_OITC = new PvPMessage("<mode>§f - Le but du jeu est de faire le plus de meurtres pour soi même. Toutefois, les flèches tuent en un coup et les achats sont désactivés. Vous obtenez une flèche si vous arrivez à faire un meurtre. Il n'y a pas d'équipes ou d'alliances. §4§lALLIANCES = BAN", false),
	GAME_GUIDE_KOTH = new PvPMessage("<mode>§f - Le but du jeu est d'être le plus longtemps possible sur la zone de l'arène. Cette zone est facile à repérer puisqu'elle a un beacon. Le rayon du beacon est blanc s'il est inutilisé, jaune si un joueur se fait des points avec et rouge des plusieurs joueurs se battent dessus.  §4§lALLIANCES = BAN", false),
	GAME_GUIDE_SWI = new PvPMessage("<mode>§f -  Le but du jeu est de faire le plus de meurtres pour soi même. Les armes sont aléatoires (mêmes pour tout le monde) et changent à tout les 30 secondes !s §4§lALLIANCES = BAN", false),
	GAME_GUIDE_TDM = new PvPMessage("<mode>§f - Le but du jeu est de faire le plus de meurtes pour son équipe. L'équipe qui gagne est celle qui a réussi a faire le plus de meurtes. Évitez de mourir pour ne pas céder des points aux autres équipes.", false),
	GAME_GUIDE_CTF = new PvPMessage("<mode>§f - Le but du jeu est de prendre des drapeaux et de les ramener à sa base en équipes. L'équipe gagnante est celle qui a capturée le plus de drapeaux. Attention, vous ne pouvez pas capturer un drapeau si votre drapeau n'est pas à sa base. Protégez-le porteur du drapeau et défendez votre drapeau pour ne pas donner de points aux autres équipes !", false),
	GAME_GUIDE_DOM = new PvPMessage("<mode>§f - Le but du jeu est de capturer des zones pour accumuler des points ! Ce jeu se joue en équipe. Restez sur une zone jusqu'elle obtienne la couleur de votre équipe et défendez vos zones. Plus vous aurez de zones, plus les points de votre équipe augmenteront.", false),
	GAME_GUIDE_BRAWL = new PvPMessage("<mode>§f - Le but du jeu est de se frapper pour obtenir des points. Vous ne pouvez pas mourir, alors battez vous !", false),
	GAME_GUIDE_INF = new PvPMessage("<mode>§f - Le but du jeu est de survivre en tant qu'humain ou d'infecter la planète en tant qu'infecté.", false),

	GAME_TABFOOTER = new PvPMessage("§aVous êtes en <mode> sur <arena>.", false),

	GAME_SOLO_WIN = new PvPMessage("§f<name> §fremporte la partie de <mode> avec <score> !", true),
	GAME_TEAM2_WIN = new PvPMessage("§fLes <team>§f gagnent contre les <second>§f en <mode>§f ! (<score> vs <score_second>)", true),
	GAME_TEAMMORE_WIN = new PvPMessage("§fLes <team>§f gagnent la partie parmi <team_count> équipes en <mode> !", true),
	GAME_INF_HUMAN_WIN = new PvPMessage("§fLes humains ont gagné contre l'invasion d'infectés!", true),
	GAME_INF_HUMAN_LOSE = new PvPMessage("§fLes humains n'ont pas pu survivre à l'invasion d'infectés...", true),

	GAME_TIMER = new PvPMessage("§7Il reste <time> avant la fin de la partie.", false, MessageType.ACTIONBAR),

	GAME_CTF_CANTHOLD2FLAGS = new PvPMessage("§cVous avez déjà un drapeau sur vous, rapportez le à la base !", true),
	GAME_CTF_YOURFLAG = new PvPMessage("§aCeci est votre drapeau, défendez-le et rapportez d'autres drapeaux sur celui-ci !", true),
	GAME_CTF_CAPTURE = new PvPMessage("<player>§f a capturé un drapeau pour les <color>§f.", false),
	GAME_CTF_PICKUP = new PvPMessage("<player>§f a pris le drapeau des <color>§f.", false),
	GAME_CTF_FLAGBACK = new PvPMessage("§fLe drapeau des <color>§r est de retour à sa base.", true),

	STATS_SUMMARY_SEPARATOR = new PvPMessage("§m-§a§m-§f§m-§a§m-§f§m-§a§m-§f§m-§a§m-§f§m-§a§m-§f§m-§a§m-§f§m-§a§m-§f§m-§a§m-§f§m-§a§m-§f§m-§a§m-§f§m-§a§m-§f§m-§a§m-§f§m-§a§m-§f§m-§a§m-§f§m-§a§m-§f§m-§a§m-§f§m-§a§m-§f§m-§a§m-§f§m-§a§m-§f§m-§a§m-§f§m-§a§m-§f§m-§a§m-§f§m-§a§m-§f§m-§a§m-§f§m-§a§m-§f§m-§a§m-§f§m-", false),
	STATS_SUMMARY_TITLE = new PvPMessage("§eRapport de fin de partie", true),
	
	STATS_SUMMARY_TEAMVICTORY = new PvPMessage("§7Victoire de l'équipe §a+# points", false),
	STATS_SUMMARY_TEAMLOSE = new PvPMessage("§7Défaite de l'équipe §c# points", false),
	STATS_SUMMARY_PARTICIPATION = new PvPMessage("§7Participation §a+# points", false),
	
	STATS_SUMMARY_FIRSTPLACE = new PvPMessage("§7Première place §a+# points", false),
	STATS_SUMMARY_SECONDPLACE = new PvPMessage("§7Deuxième place §a+# points", false),
	STATS_SUMMARY_THIRDPLACE = new PvPMessage("§7Troisième place §a+# points", false),

	STATS_SUMMARY_INFWONASHUMAN = new PvPMessage("§7Victoire des humains §a+# points", false),
	STATS_SUMMARY_INFWONASINFECTED = new PvPMessage("§7Victoire des infectés §a+# points", false),
	
	STATS_SUMMARY_KILLS = new PvPMessage("§7<kills> meurtres §a+# points", false),
	STATS_SUMMARY_DEATHS = new PvPMessage("§7<deaths> morts §c# points", false),
	STATS_SUMMARY_ASSISTS = new PvPMessage("§7<assists> assistances §a+# points", false),
	STATS_SUMMARY_CAPTUREDFLAGS = new PvPMessage("§7<flags> drapeaux capturés §a+# points", false),
	STATS_SUMMARY_CAPTUREDZONES = new PvPMessage("§7<zones> zones capturés §a+# points", false),
	STATS_SUMMARY_BONUS = new PvPMessage("§7<bonus> bonus §a+# points", false),
	
	STATS_SUMMARY_TOTAL = new PvPMessage("§eTotal: §f# points", false),
	STATS_SUMMARY_RATIO = new PvPMessage("\\n§3Ratio de partie: §f#", false),
	STATS_SUMMARY_RATIO_PERFECT = new PvPMessage("§d§lParfait !", false),
	STATS_SUMMARY_LEVELUP = new PvPMessage("§a§lLevel Up !", false),

	PURCHASE_CANTBUYNOW = new PvPMessage("§cVous ne pouvez pas acheter pour le moment.", true)
	;
	
	public PvPMessage(String content, boolean prefix, MessageType type)
	{
		super(content, prefix, type);
	}

	public PvPMessage(String content, boolean prefix, String hoverEvent, String hoverEventContent, String clickEvent, String clickEventContent)
	{
		super(content, prefix, hoverEvent, hoverEventContent, clickEvent, clickEventContent);
	}

	public PvPMessage(String content, boolean prefix)
	{
		super(content, prefix);
	}

	public PvPMessage(String title, String subTitle, int delay, int fadeIn, int fadeOut)
	{
		super(title, subTitle, delay, fadeIn, fadeOut);
	}
	
	@Override
	protected String getPrefix()
	{
		return "§f§lTouched§4§lPvP §f§l> §7";
	}
	
	public void sayPlayers(String... replacements)
	{
		this.sayMembers(PvPPlugin.getGame(), replacements);
	}

}
