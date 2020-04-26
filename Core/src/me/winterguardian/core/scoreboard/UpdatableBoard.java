package me.winterguardian.core.scoreboard;

import me.winterguardian.core.util.PlayerUtil;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public abstract class UpdatableBoard extends Board
{
	private List<Player> players;
	
	public UpdatableBoard()
	{
		this.players = new ArrayList<Player>();
	}
	
	@Override
	public void startDisplay(Player p)
	{
		players.add(p);
		update(p);
	}

	@Override
	public void stopDisplay(Player p)
	{
		players.remove(p);
		PlayerUtil.clearBoard(p);
	}

	public void update()
	{
		for(Player p : players)
			update(p);
	}
	
	protected abstract void update(Player p);
}
