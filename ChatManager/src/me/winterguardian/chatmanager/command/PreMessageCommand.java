package me.winterguardian.chatmanager.command;

import me.winterguardian.core.command.AutoRegistrationCommand;
import me.winterguardian.core.message.ErrorMessage;
import me.winterguardian.core.util.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class PreMessageCommand extends AutoRegistrationCommand
{
	private HashMap<String, String> messages;

	public PreMessageCommand()
	{
		messages = new HashMap<>();

		messages.put("spam", "Cessez d'envoyer autant de messages rapidement, cela nuit à l'image du serveur.");
		messages.put("flood", "N'evoyez pas autant de charactères inutilement.");
		messages.put("sens", "Merci d'envoyer des messages qui ont du sens.");
		messages.put("majs", "Pour conserver une ambiance viable, merci de ne pas utiliser les majuscules.");
		messages.put("provoc", "Afin de conserver une ambiance amicale et sans embrouilles, merci de ne pas provoquer les autres joueurs.");
		messages.put("insulte", "Le respect mutuel est obligatoire sur Touched, ici pas d'insultes.");

		messages.put("spam!", "Cessez immédiatement d'envoyer autant de messages !");
		messages.put("flood!", "Cessez d'envoyer des messages contenant du flood !");
		messages.put("sens!", "Cessez immédiatement d'envoyer des messages sans aucun sens !");
		messages.put("majs!", "Les majuscules sont strictement interdites !");
		messages.put("provoc!", "Cessez vos provocations, l'excès de rage ne vous apportent rien !");
		messages.put("insulte!", "Pas d'insultes, respectez-vous un peu bon sang !");

		messages.put("avertissement", "Ceci était votre dernier avertissement, arrêtez si vous ne voulez pas obtenir une sanction.");

		messages.put("ip", "L'ip de notre serveur est play.touchedcraft.fr");
		messages.put("site", "L'adresse de notre site est touchedcraft.fr");
		messages.put("ts", "L'adresse de notre serveur discord est https://discord.gg/z26wVew");

		messages.put("cheat", "Pour demander à quelqu'un d'être vérifié, envoyer un message privé à un Modo ou Admin.");
		messages.put("accusation", "Accusez publiquement un autre joueur pour triche dans le chat est interdit.");
		messages.put("accusation!", "Cessez vos accusations publiques si vous ne voulez pas devenir muet !");
		messages.put("occupé", "Je suis occupé pour le moment, merci de demander à un guide ou à un autre membre du staff pour questions de tous genres.");
		messages.put("occupé!", "Cessez de me harceler, je suis occupé !");

		messages.put("offres", "Le vip est 12 euros à vie et le Élite est 24 euros à vie.");
		messages.put("paiement", "Le mode de paiement est via buycraft avec paypal ou carte de crédit.");
		messages.put("vip", "Le vip est un grade payant du serveur. Il est obtenable via la boutique sur le site pour 12 euros à vie.");
		messages.put("avantages", "Les joueurs Vip ont du meilleur stuff en PvP et en IceRun, plus de véhicules en MobRacers, des messages de couleurs dans le chat et des plots supplémentaires en créatif.");
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if(args.length == 0)
		{
			ErrorMessage.COMMAND_INVALID_ARGUMENT.say(sender);
			return false;
		}
		String message = messages.get(args[0]);
		if(message == null)
		{
			ErrorMessage.COMMAND_INVALID_ARGUMENT.say(sender);
			return false;
		}
		if((sender instanceof Player))
		{
			((Player)sender).chat(message);
			return true;
		}
		for(Player p : Bukkit.getOnlinePlayers())
		{
			p.sendMessage("§3§lConsole §f§l> §c" + message);
		}
		return true;
	}

	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args)
	{
		return TextUtil.getStringsThatStartWith(args[0], new ArrayList<>(messages.keySet()));
	}

	public List<String> getAliases()
	{
		return Collections.singletonList("prem");
	}

	public String getDescription()
	{
		return "Commande pour écrire des messages préenregistrés.";
	}

	public String getName()
	{
		return "premessage";
	}

	public Permission getPermission()
	{
		return new Permission("ChatManager.premessage", "Permet d'écrire des messages préenregistrés.", PermissionDefault.OP);
	}

	public String getUsage()
	{
		return "§cSyntaxe: §f/premessage [tab]";
	}
}
