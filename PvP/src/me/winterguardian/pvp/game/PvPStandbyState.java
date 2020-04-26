package me.winterguardian.pvp.game;

import me.winterguardian.core.game.state.State;
import me.winterguardian.core.json.JsonUtil;
import me.winterguardian.core.scoreboard.ScoreboardUtil;
import me.winterguardian.core.util.PlayerUtil;
import me.winterguardian.core.util.TabUtil;
import me.winterguardian.pvp.PvP;
import me.winterguardian.pvp.PvPMessage;
import org.bukkit.entity.Player;

public class PvPStandbyState implements State
{
	private PvP game;
	
	public PvPStandbyState(PvP game)
	{
		this.game = game;
	}

	@Override
	public String getStatus()
	{
		return "Il n'y a pas assez de joueurs pour commencer.";
	}

	@Override
	public void join(Player p)
	{
		p.teleport(game.getSetup().getLobby());
		prepare(p);
		
		if(game.getPlayers().size() >= game.getMinPlayers())
			end();
		else
			PvPMessage.STANDBY_NOTENOUGHPLAYERS.say(p);
	}

	@Override
	public void leave(Player p)
	{
		PlayerUtil.clearInventory(p);
		PlayerUtil.clearBoard(p);
		TabUtil.resetTab(p);
		PlayerUtil.heal(p);
		PlayerUtil.prepare(p);
		p.teleport(game.getSetup().getExit());
	}

	@Override
	public void start()
	{
		PvPMessage.STANDBY_NOTENOUGHPLAYERS.say(game.getPlayers());
		for(Player p : game.getPlayers())
			prepare(p);
	}

	@Override
	public void end()
	{
		game.setState(new PvPVoteState(game));
		game.getState().start();
	}
	
	
	private void prepare(Player player)
	{
		String[] elements = new String[16];
		elements[0] = "§f§lTouched§4§lPvP";
		elements[1] = " ";
		elements[2] = "§cPas assez de";
		elements[3] = "§cjoueurs pour";
		elements[4] = "§ccommencer.";
		elements[5] = "  ";
		
		ScoreboardUtil.unrankedSidebarDisplay(player, elements);
		
		TabUtil.sendInfos(player, JsonUtil.toJson("§f§lTouched§4§lPvP"), JsonUtil.toJson("§cIl n'y a pas assez de joueurs pour commencer."));
		PlayerUtil.clearInventory(player);
		PlayerUtil.heal(player);
		PlayerUtil.prepare(player);
	}
}
