package me.winterguardian.commandcenter.commands;

import me.winterguardian.core.command.AsyncCommand;
import me.winterguardian.core.message.HardcodedMessage;
import me.winterguardian.core.util.WebCommunicationUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.Plugin;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * Created by Alexander Winter on 2016-01-30.
 */
public class NameCommand extends AsyncCommand
{
	private Pattern mainRegex, lineRegex, nameRegex, timeRegex;

	public NameCommand(Plugin plugin)
	{
		super(plugin);

		mainRegex = Pattern.compile("\\Q<tbody>\\E(.)\\Q</tbody>\\E");
		lineRegex = Pattern.compile("\\Q<td>\\E(.)\\Q</td><td>\\E(.)\\Q</td>\\E");
		nameRegex = Pattern.compile("\\Q<span translate=\"no\" class=\"minecraft-name\"><a href=\"/s?\\E(A-Za-z0-9_)\\Q\">\\1</a></span>\\E");
		timeRegex = Pattern.compile("\\Q<time datetime=\"\\E(.)\\Q\">\\E(.)\\Q</time>\\E");
	}

	@Override
	public String getName()
	{
		return "name";
	}

	@Override
	public Permission getPermission()
	{
		return new Permission("CommandCenter.name", getDescription(), PermissionDefault.TRUE);
	}

	@Override
	public String getUsage()
	{
		return "§cSyntaxe: §f/pseudo <joueur>";
	}

	@Override
	public String getDescription()
	{
		return "Permet de récupérer l'historique des pseudos d'un joueur.";
	}

	@Override
	protected void onAsyncCommand(CommandSender sender, String label, String[] args)
	{
		if(args.length == 0)
		{
			sender.sendMessage(getUsage());
			return;
		}

		names:
		for(String name : args)
		{
			for(char c : name.toCharArray())
				if(c != '_' && !Character.isLetterOrDigit(c))
				{
					new HardcodedMessage("§cPseudo invalide: §f<name>").say(sender, "<name>", name);
					continue names;
				}

			new HardcodedMessage("Recherche du pseudo <name> en cours...").say(sender, "<name>", name);

			try
			{
				String page = WebCommunicationUtil.get("fr.namemc.com/s?" + name).replace("\n", "");

				Matcher matcher = mainRegex.matcher(page);

				if(!matcher.matches())
				{
					new HardcodedMessage("§cPseudo introuvable : §f<name>").say(sender, "<name>", name);
					continue;
				}

				String result = matcher.group(1);

				String[] lines = result.split(Pattern.quote("</tr>"));

				for(String line : lines)
				{
					if(line.equals(lines[lines.length - 1]))
						continue;

					Matcher lineMatch = lineRegex.matcher(line);

					if(!lineMatch.matches())
						continue;

					String nameTD = lineMatch.group(1);
					String dateTD = lineMatch.group(2);

					Matcher nameMatch = nameRegex.matcher(nameTD);

					if(!nameMatch.matches())
						continue;

					String currentName = nameMatch.group(1);
					String date;

					Matcher timeMatcher = timeRegex.matcher(dateTD);

					if(timeMatcher.matches())
						date = timeMatcher.group(1);
					else
						date = "Original";

					sender.sendMessage(currentName + " : " + date);
				}
			}
			catch(IOException e)
			{
				new HardcodedMessage("§cService en maintenance pour le moment.");
				return;
			}
		}
	}

	@Override
	public List<String> getAliases()
	{
		return Arrays.asList("pseudo", "nom", "namemc", "getpseudo", "namehistory", "historiquedespseudos", "getname", "namesearch");
	}

	@Override
	public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings)
	{
		return null;
	}
}
