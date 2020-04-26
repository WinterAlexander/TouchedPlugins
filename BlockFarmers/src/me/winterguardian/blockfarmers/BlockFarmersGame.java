package me.winterguardian.blockfarmers;

import me.winterguardian.blockfarmers.command.BlockFarmersCommand;
import me.winterguardian.blockfarmers.listener.BlockFarmersGameListener;
import me.winterguardian.blockfarmers.listener.CommandSignListener;
import me.winterguardian.blockfarmers.pluginsupport.BFHubItem;
import me.winterguardian.blockfarmers.state.BlockFarmersStandbyState;
import me.winterguardian.core.command.CommandSplitter;
import me.winterguardian.core.game.GUIItemGame;
import me.winterguardian.core.game.GameConfig;
import me.winterguardian.core.game.state.State;
import me.winterguardian.core.game.state.StateGame;
import me.winterguardian.core.game.state.StateGameSetup;
import me.winterguardian.core.inventorygui.GUIItem;
import me.winterguardian.core.json.JsonUtil;
import me.winterguardian.core.listener.ServerMOTDListener;
import me.winterguardian.core.message.Message;
import me.winterguardian.core.util.PlayerState;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BlockFarmersGame extends StateGame implements GUIItemGame
{
	private List<PlayerState> playerStates;

	public BlockFarmersGame(Plugin plugin)
	{
		super(plugin);
		this.playerStates = new ArrayList<>();
	}
	
	@Override
	public void onEnable()
	{
		super.onEnable();
		Bukkit.getPluginManager().registerEvents(new BlockFarmersGameListener(this), this.getPlugin());
		Bukkit.getPluginManager().registerEvents(new CommandSignListener(), this.getPlugin());
		Bukkit.getPluginManager().registerEvents(new ServerMOTDListener()
		{
			@Override
			public boolean isEnabled()
			{
				return ((BlockFarmersConfig)getConfig()).enableMOTD();
			}

			@Override
			public String getMOTD()
			{
				return getStatus();
			}
		}, this.getPlugin());
	}

	public void savePlayerState(Player player)
	{
		if(((BlockFarmersConfig) this.getConfig()).keepPlayerStates())
			this.playerStates.add(new PlayerState(player));
	}

	public void applyPlayerState(Player player)
	{
		if(!((BlockFarmersConfig) this.getConfig()).keepPlayerStates())
			return;

		PlayerState toRemove = null;
		for(PlayerState state : this.playerStates)
			if(state.getUniqueId().equals(player.getUniqueId()))
			{
				state.apply();
				toRemove = state;
				break;
			}

		this.playerStates.remove(toRemove);
	}

	@Override
	public String getChatName(Player player)
	{
		return null;
	}

	@Override
	public String getChatPrefix(Player player)
	{
		return "§c§lB§7§lF";
	}

	@Override
	public String getColoredName()
	{
		return "§c§lBlock§7§lFarmers";
	}

	@Override
	public String getStatus()
	{
		return getState().getStatus();
	}

	@Override
	public Location getWarp()
	{
		return getSetup().getLobby();
	}

	@Override
	public Message getAlreadyInGameMessage()
	{
		return BlockFarmersMessage.JOIN_ALREADYINGAME;
	}

	@Override
	public Message getAutoJoinKickMessage()
	{
		return BlockFarmersMessage.JOIN_AUTOJOINKICK;
	}

	@Override
	public Message getCommandBlockedMessage()
	{
		return BlockFarmersMessage.LISTENER_COMMANDBLOCKED;
	}

	@Override
	public Message getGameClosedMessage()
	{
		return BlockFarmersMessage.GAME_CLOSE;
	}

	@Override
	public Message getGameFullMessage()
	{
		return BlockFarmersMessage.JOIN_PLAYERLIMITREACHED;
	}

	@Override
	public Message getGameNotReadyMessage()
	{
		return BlockFarmersMessage.JOIN_NOTREADY;
	}

	@Override
	public Message getJoinMessage()
	{
		return BlockFarmersMessage.JOIN;
	}

	@Override
	public Message getLeaveMessage()
	{
		return BlockFarmersMessage.LEAVE;
	}
	
	@Override
	public Message getNotInGameMessage()
	{
		return BlockFarmersMessage.LEAVE_NOTINGAME;
	}
	
	@Override
	public Message getCantLeaveInAutoJoinMessage()
	{
		return BlockFarmersMessage.LEAVE_CANTINAUTOJOIN;
	}

	@Override
	protected CommandSplitter getNewCommand()
	{
		return new BlockFarmersCommand(this);
	}

	@Override
	protected GameConfig getNewConfig()
	{
		return new BlockFarmersConfig(new File(getPlugin().getDataFolder(), "config.yml"));
	}

	@Override
	protected StateGameSetup getNewSetup()
	{
		return new BlockFarmersSetup(new File(getPlugin().getDataFolder(), "setup.yml"));
	}

	@Override
	public Permission getNoAutoJoinPermission()
	{
		return BlockFarmersPlugin.STAFF;
	}

	@Override
	public Permission getNoKickAutoJoinPermission()
	{
		return BlockFarmersPlugin.STAFF;
	}

	
	
	@Override
	public Permission getNoTeleportJoinPermission()
	{
		return BlockFarmersPlugin.STAFF;
	}
	
	@Override
	public Permission getCommandBlockingBypassPermission()
	{
		return BlockFarmersPlugin.STAFF;
	}
	
	@Override
	protected boolean isReady()
	{
		if(this.getConfig() == null || this.getSetup() == null || !(getConfig() instanceof BlockFarmersConfig) || !(getSetup() instanceof BlockFarmersSetup))
			return false;
		
		return ((BlockFarmersSetup) getSetup()).getSpawn() != null && (getSetup().getExit() != null || getConfig().isAutoJoin()) && getSetup().getLobby() != null && ((BlockFarmersConfig) getConfig()).getFarmableBlocksSize() != 0;
	}

	@Override
	protected State getDefaultState()
	{
		return new BlockFarmersStandbyState(this);
	}
	
	public String getTabHeader()
	{
		return JsonUtil.toJson(BlockFarmersMessage.HEADER.toString("<players>", "" + getPlayers().size(), "<players-plural>", getPlayers().size() > 1 ? "s" : ""));
	}
	
	public String getTabFooter()
	{
		return JsonUtil.toJson(BlockFarmersMessage.FOOTER.toString("<players>", "" + getPlayers().size(), "<players-plural>", getPlayers().size() > 1 ? "s" : ""));
	}
	
	@Override
	protected Message getInvitationMessage()
	{
		if(getConfig() == null || !((BlockFarmersConfig) getConfig()).invitationMessage())
			return null;
		return BlockFarmersMessage.JOIN_INVITATIONMESSAGE;
	}
	
	@Override
	protected Collection<? extends Player> getInvitationMessageRecipients()
	{
		if(getConfig() == null || getConfig() instanceof BlockFarmersConfig)
			return super.getInvitationMessageRecipients();
		
		return ((BlockFarmersConfig) getConfig()).getBroadcastRecipients();
	}
	
	@Override
	public int getMaxPlayers()
	{
		return super.getMaxPlayers() > 16 ? 16 : super.getMaxPlayers();
	}
	
	@Override
	public void join(Player p)
	{
		super.join(p);
		if(getConfig().isColorInTab())
			p.setPlayerListName("§c" + p.getName());
	}
	
	@Override
	public void leave(Player p)
	{
		if(getConfig().isColorInTab())
			p.setPlayerListName(null);
		super.leave(p);
	}

	@Override
	public GUIItem getGUIItem()
	{
		return new BFHubItem(this);
	}
}
