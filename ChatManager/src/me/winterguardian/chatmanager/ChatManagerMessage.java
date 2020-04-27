package me.winterguardian.chatmanager;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public enum ChatManagerMessage
{
	INSULT_ADDED("§aL'insulte a été ajoutée à liste."), INSULT_REMOVED("§aL'insulte a été retirée de la liste."), INSULT_RELOADED("§aLa liste a été rechargée depuis la config."), INSULT_LIST("§aVoici la liste des insultes bannis:"), INSULT_LISTEMPTY("§cLa liste est vide."), INSULT_HELP_INTRO("§aAide sur la commande §e/insulte §a:"), INSULT_HELP_LIST("§e/insulte list §a : afficher la liste des insultes bannis et leurs id."), INSULT_HELP_ADD("§e/insulte add <insulte>§a : ajouter une insulte à la liste. Les _ deviennent des espaces."), INSULT_HELP_REMOVE("§e/insulte remove <id> §a : retiré une insulte à partir d'un id."), INSULT_HELP_RELOAD("§e/insulte reload §a : recharge la liste à partir du fichier config."), CMDREDIRECT_COMMANDADDED("§aLa commande a été rajouté."), CMDREDIRECT_COMMANDREMOVED("§cLa commande a été retiré."), CMDREDIRECT_NOTONLIST("§cLa commande a été retiré."), WEBCHAT_URLADDED("§aL'url a été ajouté avec son cooldown."), WEBCHAT_LISTEMPTY("§cLa liste des chats synchronisé est vide."), WEBCHAT_LIST("§a§lListe des chats synchronisés:");

	private String contenu = "";
	private boolean hasPrefix;

	private ChatManagerMessage(String contenu, boolean prefix)
	{
		this.contenu = contenu;
		hasPrefix = prefix;
	}

	private ChatManagerMessage(String contenu)
	{
		this.contenu = contenu;
		hasPrefix = true;
	}

	public String toString()
	{
		return hasPrefix ? "§f§lTouched§6§lCraft §f§l> " + contenu : contenu;
	}

	public void say(CommandSender sender)
	{
		sender.sendMessage(toString());
	}

	public void sayConsole()
	{
		Bukkit.getConsoleSender().sendMessage(toString());
	}

	public void sayAll()
	{
		for(Player p : Bukkit.getOnlinePlayers())
		{
			p.sendMessage(toString());
		}
		sayConsole();
	}
}
