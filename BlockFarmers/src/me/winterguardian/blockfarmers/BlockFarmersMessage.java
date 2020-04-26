package me.winterguardian.blockfarmers;


import me.winterguardian.core.game.GameManager;
import me.winterguardian.core.message.LangMessage;
import org.bukkit.configuration.file.YamlConfiguration;

public class BlockFarmersMessage extends LangMessage
{
	public static final BlockFarmersMessage
	BOARD_TITLE = new BlockFarmersMessage("board-title", false),
	
	JOIN_PLAYERLIMITREACHED = new BlockFarmersMessage("join-playerlimitreached", true),
	JOIN_ALREADYINGAME = new BlockFarmersMessage("join-alreadyingame", true),
	JOIN_NOTREADY = new BlockFarmersMessage("join-notready", true),
	JOIN = new BlockFarmersMessage("join", true),
	JOIN_AUTOJOINKICK = new BlockFarmersMessage("join-notready", false, MessageType.KICK),
	JOIN_INVITATIONMESSAGE = new BlockFarmersMessage("join-invitation", true, null, null, "run_command", "\"/blockfarmers join\""),
	
	LEAVE_NOTINGAME = new BlockFarmersMessage("leave-notingame", true),
	LEAVE = new BlockFarmersMessage("leave", true),
	LEAVE_CANTINAUTOJOIN = new BlockFarmersMessage("leave-cantinautojoin", true),
	
	GAME_CLOSE = new BlockFarmersMessage("game-close", true),
	GAME_OPEN = new BlockFarmersMessage("game-open", true),
	
	STANDBY_START = new BlockFarmersMessage("standby-notenoughplayers", true),
	FARMING_START = new BlockFarmersMessage("farming-start", true),
	
	STANDBY_STATUS = new BlockFarmersMessage("standby-status", false),
	WAITING_STATUS = new BlockFarmersMessage("waiting-status", false),
	FARM_STATUS = new BlockFarmersMessage("farming-status", false),
	CLAPPING_STATUS = new BlockFarmersMessage("clapping-status", false),
	
	FARMING_SPECTATOR = new BlockFarmersMessage("farming-spectator", true),
	CLAPPING_SPECTATOR = new BlockFarmersMessage("clapping-spectator", true),
	
	FARMING_COLOR = new BlockFarmersMessage("farming-color", true),
	FARMING_BOARD_SPECTATOR1 = new BlockFarmersMessage("farming-board-spectator1", false),
	FARMING_BOARD_SPECTATOR2 = new BlockFarmersMessage("farming-board-spectator2", false),
	FARMING_BOARD_SPECTATOR3 = new BlockFarmersMessage("farming-board-spectator3", false),
	FARMING_BOARD_SPECTATOR4 = new BlockFarmersMessage("farming-board-spectator4", false),
	FARMING_BOARD_BLOCKS = new BlockFarmersMessage("farming-board-blocks", false),
	FARMING_BOARD_PLAYERS = new BlockFarmersMessage("farming-board-players", false),
	
	WAITING_TIMER = new BlockFarmersMessage("waiting-timer", false, MessageType.ACTIONBAR),
	WAITING_TIMER_END = new BlockFarmersMessage("waiting-timer-end", false, MessageType.ACTIONBAR),
	
	CLAPPING_WIN = new BlockFarmersMessage("clapping-win", true),
	
	
	COMMAND_CONFIG_SET = new BlockFarmersMessage("command-config-set", false),
	COMMAND_CONFIG_UNSET = new BlockFarmersMessage("command-config-unset", false),
	COMMAND_CONFIG_USAGE = new BlockFarmersMessage("command-config-usage", false),
	
	COMMAND_CONFIG_SPAWNSET = new BlockFarmersMessage("command-config-spawnset", true),
	COMMAND_CONFIG_LOBBYSET = new BlockFarmersMessage("command-config-lobbyset", true),
	COMMAND_CONFIG_EXITSET = new BlockFarmersMessage("command-config-exitset", true),
	COMMAND_CONFIG_REGIONSET = new BlockFarmersMessage("command-config-regionset", true),
	COMMAND_CONFIG_RELOADED = new BlockFarmersMessage("command-config-reloaded", true),
	COMMAND_CONFIG_SAVED = new BlockFarmersMessage("command-config-saved", true),
	COMMAND_CONFIG_RESET = new BlockFarmersMessage("command-config-reset", true),
	COMMAND_CONFIG_RESET_ERROR = new BlockFarmersMessage("command-config-reset-error", false),
	
	COMMAND_CONFIG_MENU_TITLE = new BlockFarmersMessage("command-config-menu-title", true),
	COMMAND_CONFIG_MENU_LOBBY = new BlockFarmersMessage("command-config-menu-lobby", false),
	COMMAND_CONFIG_MENU_EXIT = new BlockFarmersMessage("command-config-menu-exit", false),
	COMMAND_CONFIG_MENU_SPAWN = new BlockFarmersMessage("command-config-menu-spawn", false),
	COMMAND_CONFIG_MENU_REGION = new BlockFarmersMessage("command-config-menu-region", false),
	COMMAND_CONFIG_MENU_WARNING_EXITINREGION = new BlockFarmersMessage("command-config-menu-warning-exitinregion", false),
	COMMAND_CONFIG_MENU_WARNING_LOBBYNOTINREGION = new BlockFarmersMessage("command-config-menu-warning-lobbynotinregion", false),
	COMMAND_CONFIG_MENU_WARNING_SPAWNNOTINREGION = new BlockFarmersMessage("command-config-menu-warning-spawnnotinregion", false),
	
	COMMAND_STATS_HEADER = new BlockFarmersMessage("command-stats-header", true),
	COMMAND_STATS_VICTORIES = new BlockFarmersMessage("command-stats-victories", false),
	COMMAND_STATS_GAMESPLAYED = new BlockFarmersMessage("command-stats-gamesplayed", false),
	COMMAND_STATS_TILESFARMED = new BlockFarmersMessage("command-stats-tilesfarmed", false),
	COMMAND_STATS_SCORE = new BlockFarmersMessage("command-stats-score", false),
	
	LISTENER_COMMANDBLOCKED = new BlockFarmersMessage("listener-commandblocked", true),
	
	HEADER = new BlockFarmersMessage("tab-header", false),
	FOOTER = new BlockFarmersMessage("tab-footer", false),
	
	COLOR_RED = new BlockFarmersMessage("color-red", false),
	COLOR_BLUE = new BlockFarmersMessage("color-blue", false),
	COLOR_YELLOW = new BlockFarmersMessage("color-yellow", false),
	COLOR_LIGHTGREEN = new BlockFarmersMessage("color-lightgreen", false),
	COLOR_PURPLE = new BlockFarmersMessage("color-purple", false),
	COLOR_ORANGE = new BlockFarmersMessage("color-orange", false),
	COLOR_TEAL = new BlockFarmersMessage("color-teal", false),
	COLOR_PINK = new BlockFarmersMessage("color-pink", false),
	
	COLOR_WHITE = new BlockFarmersMessage("color-white", false),
	COLOR_BLACK = new BlockFarmersMessage("color-black", false),
	COLOR_LIGHTGRAY = new BlockFarmersMessage("color-lightgray", false),
	COLOR_DARKGRAY = new BlockFarmersMessage("color-darkgray", false),
	COLOR_MAGENTA = new BlockFarmersMessage("color-magenta", false),
	COLOR_LIGHTBLUE = new BlockFarmersMessage("color-lightblue", false),
	COLOR_BROWN = new BlockFarmersMessage("color-brown", false),
	COLOR_GREEN = new BlockFarmersMessage("color-green", false),
	
	COMMAND_INVALID_ARGUMENT = new BlockFarmersMessage("command-invalid-argument", true),
	COMMAND_INVALID_SUBCOMMAND = new BlockFarmersMessage("command-invalid-subcommand", true),
	COMMAND_INVALID_PLAYER = new BlockFarmersMessage("command-invalid-player", true),
	COMMAND_INVALID_SENDER = new BlockFarmersMessage("command-invalid-sender", true),
	COMMAND_INVALID_PERMISSION = new BlockFarmersMessage("command-invalid-permission", true),
	
	SIGN_TITLE = new BlockFarmersMessage("sign-title", false),
	SIGN_JOIN = new BlockFarmersMessage("sign-join", false),
	SIGN_LEAVE = new BlockFarmersMessage("sign-leave", false),
	SIGN_STATS = new BlockFarmersMessage("sign-stats", false),
	SIGN_CLICK = new BlockFarmersMessage("sign-click", false),
	
	WAND = new BlockFarmersMessage("wand", true)
	;
	
	public BlockFarmersMessage(String path, boolean prefix, MessageType type)
	{
		super(path, prefix, type);
	}
	
	public BlockFarmersMessage(String path, boolean prefix, String hoverEvent, String hoverEventContent, String clickEvent, String clickEventContent)
	{
		super(path, prefix, hoverEvent, hoverEventContent, clickEvent, clickEventContent);
	}
	
	public BlockFarmersMessage(String path, boolean prefix)
	{
		super(path, prefix);
	}
	
	public BlockFarmersMessage(String path, int delay, int fadeIn, int fadeOut)
	{
		super(path, delay, fadeIn, fadeOut);
	}
	
	public BlockFarmersMessage(String path)
	{
		super(path);
	}

	@Override
	protected YamlConfiguration getLangConfig()
	{
		return BlockFarmersPlugin.getLang();
	}

	public void sayPlayers(String... replacements)
	{
		this.sayMembers(GameManager.getGame(BlockFarmersGame.class), replacements);
	}
}
