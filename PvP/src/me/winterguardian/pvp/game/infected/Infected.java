package me.winterguardian.pvp.game.infected;

import me.winterguardian.core.message.Message;
import me.winterguardian.core.util.PlayerUtil;
import me.winterguardian.pvp.GameStuff;
import me.winterguardian.pvp.PvP;
import me.winterguardian.pvp.PvPMessage;
import me.winterguardian.pvp.TeamColor;
import me.winterguardian.pvp.game.GameOutcome;
import me.winterguardian.pvp.game.PvPMatch;
import me.winterguardian.pvp.game.PvPPlayerData;
import me.winterguardian.pvp.game.PvPVoteState;
import me.winterguardian.pvp.stats.PvPStats;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.Random;

import static me.winterguardian.core.scoreboard.ScoreboardUtil.unrankedSidebarDisplay;

/**
 * Undocumented :(
 * <p>
 * Created on 2020-04-26.
 *
 * @author Alexander Winter
 */
public class Infected extends PvPMatch
{
	private final Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
	private Team humanTeam, infectedTeam;
	private final Random random = new Random();

	private ArrayList<Player> firstInfecteds = new ArrayList<>();

	private boolean ended = false;

	private GameStuff humanStuff, infectedStuff, firstInfectedStuff;

	public Infected(PvP game)
	{
		super(game, 8 * 60);

		this.humanStuff = new GameStuff("human");
		this.humanStuff.load();
		this.infectedStuff = new GameStuff("infected");
		this.infectedStuff.load();
		this.firstInfectedStuff = new GameStuff("first-infected");
		this.firstInfectedStuff.load();
	}

	@Override
	public void start()
	{
		super.start();
		register(new InfectedListener(this));

		getGame().getPlayerListener().setCancelHunger(false);

		humanTeam = this.board.registerNewTeam(TeamColor.HUMAN.name());
		humanTeam.setAllowFriendlyFire(false);
		humanTeam.setDisplayName(TeamColor.HUMAN.toString());
		humanTeam.setNameTagVisibility(NameTagVisibility.HIDE_FOR_OTHER_TEAMS);
		humanTeam.setPrefix(TeamColor.HUMAN.getBoardPrefix());

		infectedTeam = this.board.registerNewTeam(TeamColor.INFECTED.name());
		infectedTeam.setAllowFriendlyFire(false);
		infectedTeam.setDisplayName(TeamColor.INFECTED.toString());
		infectedTeam.setNameTagVisibility(NameTagVisibility.HIDE_FOR_OTHER_TEAMS);
		infectedTeam.setPrefix(TeamColor.INFECTED.getBoardPrefix());

		for(Player p : getGame().getPlayers())
		{
			p.setScoreboard(this.board);
			TeamColor color = getPlayerData(p).getTeam();
			if(color == TeamColor.HUMAN)
				humanTeam.addEntry(p.getName());
			else if(color == TeamColor.INFECTED)
				infectedTeam.addEntry(p.getName());
		}

		infectRandom();
		updateBoard();
	}

	@Override
	public void end()
	{
		getGame().getPlayerListener().setCancelHunger(true);

		for(Player p : getGame().getPlayers())
			if(getPlayerData(p).getTeam() == TeamColor.HUMAN)
				humanTeam.removeEntry(p.getName());
			else if(getPlayerData(p).getTeam() == TeamColor.INFECTED)
				infectedTeam.removeEntry(p.getName());

		super.end();
	}


	@Override
	public void finish()
	{
		super.finish();

		if(countHumans() > 0)
			PvPMessage.GAME_INF_HUMAN_WIN.sayAll();
		else
			PvPMessage.GAME_INF_HUMAN_LOSE.sayAll();
	}

	@Override
	public void join(Player p)
	{
		if(getPlayerData(p) != null)
			getPlayerData(p).setStuff(infectedStuff);

		super.join(p);
		p.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, Integer.MAX_VALUE, 0, true, false));
		p.setScoreboard(this.board);
		infectedTeam.addEntry(p.getName());
	}

	@Override
	public void leave(Player player)
	{
		if(getPlayerData(player).getTeam() == TeamColor.HUMAN)
			infect(player, false);
		infectedTeam.removeEntry(player.getName());

		super.leave(player);

		if(getGame().getState() == this)
		{
			if(countInfected() == 0)
				infectRandom();
			else if(countHumans() == 0)
				endSoon();
		}
		else
			ended = true;
	}

	private void infectRandom()
	{
		Player toInfect = getGame().getPlayers().get(random.nextInt(getGame().getPlayers().size()));

		infect(toInfect, true);
		PlayerUtil.heal(toInfect);
		PlayerUtil.clearInventory(toInfect);
		getPlayerData(toInfect).getStuff().give(toInfect);
		toInfect.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, Integer.MAX_VALUE, 0, true, false));

		firstInfecteds.add(toInfect);

		PvPMessage.GAME_INF_FIRSTINFECTED.sayPlayers("<player>", getPlayerData(toInfect).getPvPName());

		int refund = getGame().getPurchaseHistory().getAmount(toInfect);

		PvPMessage.GAME_INF_REFUND.say(toInfect, "#", refund + "");
		PvPStats.get(getPlayerData(toInfect).getUUID()).addPoints(refund);
	}

	public void infect(Player player, boolean first)
	{
		PvPPlayerData playerData = getPlayerData(player);
		playerData.setStuff(first ? firstInfectedStuff : infectedStuff);

		if(playerData.getFriend() != null)
			playerData.getFriend().remove();

		playerData.setTeam(TeamColor.INFECTED);
		Bukkit.getScheduler().runTaskLater(getGame().getPlugin(), () -> {

			humanTeam.removeEntry(player.getName());
			infectedTeam.addEntry(player.getName());
			updateBoard();
		}, 10);

		if(countHumans() == 0)
			endSoon();
	}

	private void endSoon()
	{
		if(ended)
			return;

		Bukkit.getScheduler().runTaskLater(getGame().getPlugin(), () -> {
			if(ended)
				return;

			end();
			finish();
			getGame().setState(new PvPVoteState(this.getGame()));
			getGame().getState().start();
			ended = true;
		}, 3 * 20);
	}

	public void announceKill(PvPPlayerData killer, PvPPlayerData killed)
	{
		if(killed.getTeam() == TeamColor.HUMAN)
			PvPMessage.GAME_INF_INFECT.sayPlayers("<player>", killer.getPvPName(), "<victim>", killed.getPvPName());
	}

	public void announceSuicide(PvPPlayerData player)
	{
		if(player.getTeam() == TeamColor.HUMAN)
			PvPMessage.GAME_INF_SELFINFECT.sayPlayers("<player>", player.getPvPName());
	}

	public Location getSpawnPoint(PvPPlayerData playerData)
	{
		return getArena().getSpawnPoint(TeamColor.NONE);
	}

	@Override
	public GameStuff getNewStuff(Player player, boolean gameStart)
	{
		return gameStart ? humanStuff : infectedStuff;
	}

	@Override
	public TeamColor getNewTeam(Player player, boolean gameStart)
	{
		return gameStart ? TeamColor.HUMAN : TeamColor.INFECTED;
	}

	@Override
	public boolean areEnemies(Player p1, Player p2)
	{
		return getPlayerData(p1).getTeam() != getPlayerData(p2).getTeam();
	}

	@Override
	public String getName()
	{
		return "Infecté";
	}

	@Override
	public String getColoredName()
	{
		return "§c§l" + getName();
	}

	@Override
	public Message getStartMessage()
	{
		return PvPMessage.GAME_START_INFECTED;
	}

	@Override
	public Message getGuide()
	{
		return PvPMessage.GAME_GUIDE_INF;
	}

	public int countHumans()
	{
		int humanCount = 0;

		for(PvPPlayerData playerData : getPlayerDatas())
			if(playerData.isPlaying() && playerData.getTeam() == TeamColor.HUMAN)
				humanCount++;

		return humanCount;
	}

	public int countInfected()
	{
		int infectedCount = 0;

		for(PvPPlayerData playerData : getPlayerDatas())
			if(playerData.isPlaying() && playerData.getTeam() == TeamColor.INFECTED)
				infectedCount++;

		return infectedCount;
	}

	@Override
	public void updateBoard()
	{
		int humanCount = countHumans(), infectedCount = countInfected();

		String[] content = new String[5];
		content[0] = "§c§lInfecté";
		content[1] = " ";
		content[2] = TeamColor.HUMAN.getBukkitColor() + humanCount + " humain" + (humanCount > 1 ? "s" : "");
		content[3] = TeamColor.INFECTED.getBukkitColor() + infectedCount + " infecté" + (infectedCount > 1 ? "s" : "");
		content[4] = "  ";

		unrankedSidebarDisplay(getGame().getPlayers(), content, board);
	}

	@Override
	public GameOutcome getOutcome(Player player)
	{
		if(getPlayerData(player).getTeam() == TeamColor.HUMAN)
			return GameOutcome.WON_AS_HUMAN;

		if(countHumans() > 0)
			return GameOutcome.LOST_AS_INFECTED;

		if(firstInfecteds.contains(player))
			return GameOutcome.WON_AS_INFECTED;

		return GameOutcome.LOST_AS_HUMAN;
	}

	public void resetStuff(Player player)
	{
		if(firstInfecteds.contains(player))
			getPlayerData(player).setStuff(firstInfectedStuff);
		else
			getPlayerData(player).setStuff(infectedStuff);
	}
}
