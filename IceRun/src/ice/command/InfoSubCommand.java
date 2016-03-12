package ice.command;

import ice.IceRunGame;
import ice.IceRun;

import java.util.Arrays;
import java.util.List;

import me.winterguardian.core.command.SubCommand;
import me.winterguardian.core.message.ErrorMessage;

import org.bukkit.command.CommandSender;

public class InfoSubCommand extends SubCommand
{

	public InfoSubCommand()
	{
		super("info", Arrays.asList("aide", "infos", "information", "informations", "theshitiamdoingrightnowwontbecompletlyworth"), null, ErrorMessage.COMMAND_INVALID_PERMISSION.toString(), "/icerun info");
	}

	@Override
	public boolean onSubCommand(CommandSender sender, String label, String[] args)
	{
		sender.sendMessage("§f----- [§b§lIceRun§f] -----");
		sender.sendMessage("  §b/icerun joindre §f- Rejoindre la partie.");
		sender.sendMessage("  §b/icerun quitter §f- Quitter la partie.");
		if(sender.hasPermission(IceRun.STAFF))
		{
			sender.sendMessage("  §b/icerun manage §f- Configuration");
			sender.sendMessage("  §b/icerun start §f- Démarrer la partie.");
			sender.sendMessage("  §b/icerun stop §f- Arrêter la partie.");
			sender.sendMessage("  §b/icerun stop debug §f- Arrêter la partie en debug.");
		}
		sender.sendMessage("§f------ [§bInfos§f] ------");
		sender.sendMessage("§7Version: " + IceRun.getPlugin().getDescription().getVersion());
		sender.sendMessage("§7Joueur(s) en jeu: " + IceRun.players.size());
		sender.sendMessage("§7Status: " + (IceRun.status instanceof IceRunGame ? "§bEn partie" : "§eEn attente"));
		sender.sendMessage("§f------------------");
		return true;
	}

	@Override
	public List<String> onSubTabComplete(CommandSender sender, String label, String[] args)
	{
		return null;
	}

}
