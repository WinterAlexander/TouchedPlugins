package me.winterguardian.blockfarmers.command;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import me.winterguardian.blockfarmers.BlockFarmersConfig;
import me.winterguardian.blockfarmers.BlockFarmersGame;
import me.winterguardian.blockfarmers.BlockFarmersMessage;
import me.winterguardian.blockfarmers.BlockFarmersPlugin;
import me.winterguardian.blockfarmers.BlockFarmersSetup;
import me.winterguardian.core.command.CloseStateGameSubCommand;
import me.winterguardian.core.command.CommandSplitter;
import me.winterguardian.core.command.JoinGameSubCommand;
import me.winterguardian.core.command.LeaveGameSubCommand;
import me.winterguardian.core.command.OpenStateGameSubCommand;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

public class BlockFarmersCommand extends CommandSplitter
{
	private BlockFarmersGame game;
	
	public BlockFarmersCommand(BlockFarmersGame game)
	{
		super(BlockFarmersMessage.COMMAND_INVALID_SUBCOMMAND);
		this.game = game;
		this.getSubCommands().add(new JoinGameSubCommand(this.game, "join", Arrays.asList("joindre", "rejoindre", "entrer", "enter", "j"), BlockFarmersPlugin.PLAY, BlockFarmersMessage.COMMAND_INVALID_PERMISSION.toString(), null, BlockFarmersMessage.COMMAND_INVALID_SENDER));
		this.getSubCommands().add(new LeaveGameSubCommand(this.game, "leave", Arrays.asList("quitter", "partir", "sortir", "exit", "quit", "q"), null, null, null, BlockFarmersMessage.COMMAND_INVALID_SENDER));
		this.getSubCommands().add(new OpenStateGameSubCommand(this.game, "open", Arrays.asList("ouvrir", "ouverture", "opening", "o"), BlockFarmersPlugin.STAFF, BlockFarmersMessage.COMMAND_INVALID_PERMISSION.toString(), null, BlockFarmersMessage.GAME_OPEN)
		{
			@Override
			protected Collection<? extends Player> getRecipients()
			{
				return ((BlockFarmersConfig)BlockFarmersCommand.this.game.getConfig()).getBroadcastRecipients();
			}
		});
		this.getSubCommands().add(new CloseStateGameSubCommand(this.game, "close", Arrays.asList("fermer", "fermeture", "ending", "c"), BlockFarmersPlugin.STAFF, BlockFarmersMessage.COMMAND_INVALID_PERMISSION.toString(), null, BlockFarmersMessage.GAME_CLOSE));
		
		
		if(this.game.getSetup() != null && this.game.getSetup() instanceof BlockFarmersSetup)
			this.getSubCommands().add(new SetupSubCommand((BlockFarmersSetup) this.game.getSetup()));
		if(this.game.getConfig() != null && this.game.getConfig() instanceof BlockFarmersConfig && ((BlockFarmersConfig) this.game.getConfig()).enableStats())
			this.getSubCommands().add(new StatsSubCommand(game));
	}
	
	@Override
	public List<String> getAliases()
	{
		return Arrays.asList("bf", "blockf", "farmers", "farm", "bfarmers");
	}

	@Override
	public String getDescription()
	{
		return "Main command for BlockFarmers";
	}

	@Override
	public String getName()
	{
		return "blockfarmers";
	}

	@Override
	public Permission getPermission()
	{
		return null;
	}

	@Override
	public boolean index(CommandSender sender, Command cmd, String label)
	{
		if(!(sender instanceof Player) || this.game.contains((Player)sender))
			return this.onCommand(sender, cmd, label, new String[]{"leave"});

		return this.onCommand(sender, cmd, label, new String[]{"join"});
	}
	
	@Override
	public Collection<Permission> getOtherPermissions()
	{
		return Arrays.asList(BlockFarmersPlugin.STAFF);
	}

}
