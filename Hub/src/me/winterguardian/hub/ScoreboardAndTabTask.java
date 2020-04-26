package me.winterguardian.hub;

import me.winterguardian.core.Core;
import me.winterguardian.core.game.Game;
import me.winterguardian.core.game.GameManager;
import me.winterguardian.core.json.JsonUtil;
import me.winterguardian.core.playerstats.PlayerStats;
import me.winterguardian.core.scoreboard.ScoreboardUtil;
import me.winterguardian.core.util.TabUtil;
import me.winterguardian.core.util.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ScoreboardAndTabTask implements Runnable
{
	private int i;
	
	public ScoreboardAndTabTask()
	{
		i = 0;
	}
	
	public String getTouchedLogo()
	{
		switch(i / 5 % 2)
		{
		case 0:
			return "§f§lTouched§6§lCraft";
		case 1:
			return "§f§lTouched§e§lCraft";
			
		default:
			return "§f§lTouched§6§lCraft";
		}
	}
	
	public String getTabHeader()
	{
		return JsonUtil.toJson("                              §6✦ §e✦ " + getTouchedLogo() + " §e✦ §6✦                               ");
	}

	@Override
	public void run()
	{
		for(Player p : Hub.getPlugin().getPlayers())
		{
			String motd = Hub.getPlugin().getTabMessage().replaceAll("\\$player", p.getName()).replaceAll("\\$onlines", "" + TextUtil.toString(Core.getBungeeMessager().getPlayerCount("ALL", Bukkit.getOnlinePlayers().size())));
			TabUtil.sendInfos(p, getTabHeader(), JsonUtil.toJson(TextUtil.minecraftSubstring((motd + motd), i % motd.length(), i % motd.length() + Hub.getPlugin().getTabMessageWidth())));
			
			if(i / 120 % 3 == 0)
				ScoreboardUtil.unrankedSidebarDisplay(p, getScoreboardPage1(p));
			else if(i / 120 % 3 == 1)
				ScoreboardUtil.unrankedSidebarDisplay(p, getScoreboardPage2(p));
			else
				ScoreboardUtil.unrankedSidebarDisplay(p, getScoreboardPage3(p));
			
			if(i % 4 == 0)
			{
				Core.getBungeeMessager().requestPlayerCount("ALL");
				Core.getBungeeMessager().requestPlayerCount("crea");
				Core.getBungeeMessager().requestPlayerCount("faction");
			}
		}
		i++;
	}
	
	public String[] getScoreboardPage1(Player p)
	{
		String[] elements = new String[16];
		elements[0] = getTouchedLogo();
		elements[1] = "§d§lJoueurs:";
		elements[2] = "§f" + TextUtil.toString(Core.getBungeeMessager().getPlayerCount("ALL", Bukkit.getOnlinePlayers().size())) + " / 100";
		elements[3] = "  ";

		if(Core.getUserDatasManager().isEnabled() && Core.getUserDatasManager().isLoaded(p))
		{
			elements[4] = "§a§lPoints:";
			elements[5] = "§f" + TextUtil.toString(new PlayerStats(Core.getUserDatasManager().getUserData(p)).getPoints());
		}
		elements[6] = "   ";
		elements[7] = "§6§lRang:";
		if(p.hasPermission(Hub.RANK_ADMIN))
		{
			elements[8] = "§fAdmin";
			elements[10] = "§e§lC'est l'heure";
			elements[11] = "§e§lde bosser !";

		}
		else if(p.hasPermission(Hub.RANK_STAFF))
		{
			elements[8] = "§fStaff";
			elements[10] = "§e§lC'est l'heure";
			elements[11] = "§e§lde bosser !";
			
		}
		else if(p.hasPermission(Hub.RANK_VIP3))
		{
			elements[8] = "§aDieu";
			elements[10] = "§e§lVous êtes";
			elements[11] = "§e§lun dieu !";
		}
		else if(p.hasPermission(Hub.RANK_VIP2))
		{
			elements[8] = "§fÉlite";
			elements[10] = "§e§lVous êtes";
			elements[11] = "§e§lgénial !";
		}
		else if(p.hasPermission(Hub.RANK_VIP))
		{
			elements[8] = "§fVip";
			elements[10] = "§e§lMerci pour";
			elements[11] = "§e§lvos dons !";
		}
		else if(p.hasPermission(Hub.RANK_UNDERSTAFF))
		{
			elements[8] = "§fSous staff";
			elements[10] = "§e§lAllez,";
			elements[11] = "§e§lau boulot !";
		}
		else
		{
			elements[8] = "§fAucun";
			elements[10] = "§e§lVisitez la";
			elements[11] = "§e§lboutique !";
		}
		elements[9] = " ";
		elements[12] = "    ";
		elements[13] = "§3§lSite:";
		elements[14] = "§ftouchedcraft.fr";
		elements[15] = "     ";
		
		return elements;
	}
	
	public String[] getScoreboardPage2(Player p)
	{
		String[] elements = new String[16];
		elements[0] = getTouchedLogo();
		elements[1] = "§c§lRègles";
		elements[2] = "§f Respect";
		elements[3] = "§f Pas de spam";
		elements[4] = "§f Pas de triche";
		elements[5] = "§f Pas d'abus";
		elements[6] = "§f Pas de pub";
		elements[7] = "§eDétails: §fBientôt";
		elements[8] = "";
		elements[9] = "§3§lConseils";
		elements[10] = "§f Voter tous";
		elements[11] = "les jours pour";
		elements[12] = "avoir des §apoints";
		elements[13] = "§f Être sympa";
		elements[14] = "avec les autres";
		elements[15] = " ";
		
		return elements;
	}
	
	public String[] getScoreboardPage3(Player p)
	{
		int pvpPlayers = 0, arcadePlayers = 0, creaPlayers = Core.getBungeeMessager().getPlayerCount("crea", 0);
		int facPlayers = Core.getBungeeMessager().getPlayerCount("faction", 0);
		for(Player player : Bukkit.getOnlinePlayers())
		{
			Game game = GameManager.getGameContained(player);
			
			if(game == null)
				continue;
			
			if(game.getClass().getSimpleName().equalsIgnoreCase("Duel"))
			{
				pvpPlayers++;
				continue;
			}

			if(game.getClass().getSimpleName().equalsIgnoreCase("PvP"))
			{
				pvpPlayers++;
				continue;
			}
			
			if(game.getClass().getSimpleName().equalsIgnoreCase("IceRun"))
			{
				arcadePlayers++;
				continue;
			}
			
			if(game.getClass().getSimpleName().equalsIgnoreCase("JumpBox"))
			{
				arcadePlayers++;
				continue;
			}
			
			if(game.getClass().getSimpleName().equalsIgnoreCase("BlockFarmersGame"))
			{
				arcadePlayers++;
				continue;
			}
			
			if(game.getClass().getSimpleName().equalsIgnoreCase("MobRacersGame"))
			{
				arcadePlayers++;
			}
		}
		
		String[] elements = new String[16];
		elements[0] = getTouchedLogo();
		elements[1] = "§4§lPvP";
		elements[2] = "§2§f" + TextUtil.toString(pvpPlayers) + " joueur" + (pvpPlayers > 1 ? "s" : "");
		elements[3] = " /pvp /duel";
		elements[4] = "  ";
		elements[5] = "§9§lArcade";
		elements[6] = "§3§f" + TextUtil.toString(arcadePlayers) + " joueur" + (arcadePlayers > 1 ? "s" : "");
		elements[7] = " /bf /ir /jb /mr";
		elements[8] = "   ";
		elements[9] = "§d§lCréatif";
		elements[10] = "§4§f" + TextUtil.toString(creaPlayers) + " joueur" + (creaPlayers > 1 ? "s" : "");
		elements[11] = " /crea";
		elements[12] = " ";
		elements[13] = "§2§lFactions";
		elements[14] = "§1§f" + TextUtil.toString(facPlayers) + " joueur" + (facPlayers > 1 ? "s" : "");
		elements[15] = " /fac";

		
		return elements;
	}
}
