package me.winterguardian.pvp;

import me.winterguardian.core.Core;
import me.winterguardian.core.command.CommandSplitter;
import me.winterguardian.core.game.GUIItemGame;
import me.winterguardian.core.game.GameConfig;
import me.winterguardian.core.game.state.State;
import me.winterguardian.core.game.state.StateGame;
import me.winterguardian.core.game.state.StateGameSetup;
import me.winterguardian.core.inventorygui.GUIItem;
import me.winterguardian.core.message.ErrorMessage;
import me.winterguardian.core.message.Message;
import me.winterguardian.pvp.command.PvPCommand;
import me.winterguardian.pvp.game.ChatListener;
import me.winterguardian.pvp.game.LobbyListener;
import me.winterguardian.pvp.game.PvPPurchaseHistory;
import me.winterguardian.pvp.game.PvPStandbyState;
import me.winterguardian.pvp.purchase.kit.PvPKitPurchase;
import me.winterguardian.pvp.purchase.mob.MobPurchase;
import me.winterguardian.pvp.stats.PvPStats;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PvP extends StateGame implements GUIItemGame
{
	private final PvPPurchaseHistory purchaseHistory = new PvPPurchaseHistory();

	public PvP(Plugin plugin)
	{
		super(plugin);
	}
	
	@Override
	public void onEnable()
	{
		super.onEnable();
		Bukkit.getPluginManager().registerEvents(new LobbyListener(this), getPlugin());
		Bukkit.getPluginManager().registerEvents(new SignListener(), getPlugin());
		Bukkit.getPluginManager().registerEvents(new ChatListener(this), getPlugin());
		Bukkit.getPluginManager().registerEvents(purchaseHistory, getPlugin());

		Core.getShop().registerPurchaseType(new PvPKitPurchase("[shop]", "§f§lTouched", "§e§lAchat Kit"));
		Core.getShop().registerPurchaseType(new PvPKitPurchase("[shopvip]", "§f§lTouched", "§e§lAchat Kit §6§lVip")
		{
			@Override
			public boolean canGive(String[] sign, Player player)
			{
				if(player.hasPermission("HubCore.buy.vip"))
					return true;

				ErrorMessage.COMMAND_INVALID_VIPRANK.say(player);
				return false;
			}
		});
		Core.getShop().registerPurchaseType(new MobPurchase("[shop]", "§f§lTouched", "§e§lAchat Mob"));
		Core.getShop().registerPurchaseType(new MobPurchase("[shopvip]", "§f§lTouched", "§e§lAchat Mob §6§lVip")
		{
			@Override
			public boolean canGive(String[] sign, Player player)
			{
				if(player.hasPermission("HubCore.buy.vip"))
					return true;

				ErrorMessage.COMMAND_INVALID_VIPRANK.say(player);
				return false;
			}
		});
	}

	@Override
	public String getColoredName()
	{
		return "§f§lTouched§4§lPvP";
	}

	@Override
	public String getStatus()
	{
		if(getState() == null)
			return null;
		
		return getState().getStatus();
	}

	@Override
	public void join(Player player)
	{
		if(contains(player))
		{
			super.join(player);
			return;
		}

		purchaseHistory.join(player);
		super.join(player);

		if(contains(player))
			PvPMessage.JOIN.sayPlayers("<player>", PvPStats.get(player.getUniqueId()).getPvPName());
	}

	@Override
	public void leave(Player player)
	{
		if(!contains(player))
		{
			super.leave(player);
			return;
		}

		List<Player> players = new ArrayList<>(getPlayers());

		super.leave(player);
		purchaseHistory.leave(player);

		if(!contains(player))
			PvPMessage.LEAVE.say(players, "<player>", PvPStats.get(player.getUniqueId()).getPvPName());
	}

	@Override
	public String getChatName(Player player)
	{
		return null;
	}

	@Override
	public String getChatPrefix(Player player)
	{
		return "§4§lPvP";
	}

	@Override
	public Message getAlreadyInGameMessage()
	{
		return PvPMessage.JOIN_ALREADYINGAME;
	}

	@Override
	public Message getAutoJoinKickMessage()
	{
		return null;
	}

	@Override
	public Message getCantLeaveInAutoJoinMessage()
	{
		return null;
	}

	@Override
	public Message getCommandBlockedMessage()
	{
		return PvPMessage.COMMAND_BLOCKED;
	}

	@Override
	public Message getGameClosedMessage()
	{
		return PvPMessage.JOIN_CLOSED;
	}

	@Override
	public Message getGameFullMessage()
	{
		return PvPMessage.JOIN_MAX;
	}

	@Override
	public Message getGameNotReadyMessage()
	{
		return PvPMessage.JOIN_CLOSED;
	}

	@Override
	public Message getJoinMessage()
	{
		return Message.NULL;
	}

	@Override
	public Message getLeaveMessage()
	{
		return Message.NULL;
	}
	
	@Override
	public Message getNotInGameMessage()
	{
		return PvPMessage.LEAVE_NOTINGAME;
	}
	
	@Override
	protected State getDefaultState()
	{
		return new PvPStandbyState(this);
	}

	@Override
	protected CommandSplitter getNewCommand()
	{
		return new PvPCommand(this);
	}

	@Override
	protected GameConfig getNewConfig()
	{
		return new GameConfig(new File(getPlugin().getDataFolder(), "config.yml")){};
	}

	@Override
	protected StateGameSetup getNewSetup()
	{
		return new StateGameSetup(new File(getPlugin().getDataFolder(), "setup.yml"));
	}

	@Override
	public Permission getNoAutoJoinPermission()
	{
		return PvPPlugin.STAFF;
	}

	@Override
	public Permission getNoKickAutoJoinPermission()
	{
		return PvPPlugin.STAFF;
	}

	@Override
	public Permission getNoTeleportJoinPermission()
	{
		return PvPPlugin.STAFF;
	}
	
	@Override
	public Permission getCommandBlockingBypassPermission()
	{
		return PvPPlugin.STAFF;
	}

	@Override
	protected boolean isReady()
	{
		return getSetup().getLobby() != null
				&& getSetup().getExit() != null
				&& getSetup().getRegion() != null
				&& PvPArena.getReadyArenaList().size() > 0;
	}

	@Override
	public GUIItem getGUIItem()
	{
		return new PvPGUIItem(this);
	}

	public PvPPurchaseHistory getPurchaseHistory()
	{
		return purchaseHistory;
	}
}
