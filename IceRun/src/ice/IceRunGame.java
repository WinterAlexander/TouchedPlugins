package ice;

import java.util.ArrayList;
import java.util.List;

import me.winterguardian.core.Core;
import me.winterguardian.core.game.state.State;
import me.winterguardian.core.scoreboard.ScoreboardUtil;
import me.winterguardian.core.util.FireworkUtil;
import me.winterguardian.core.util.PlayerUtil;
import me.winterguardian.core.util.TextUtil;
import me.winterguardian.core.world.LocationUtil;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class IceRunGame implements State
{
	private List<Player> alive;
	
	private int taskId;
	private int snowballs;
	private int vipsnowballs;
	
	private int iceBreakTaskId;
	private boolean canBreak;
	private List<Location> toPackedIceRegen;
	private List<Location> toIceRegen;
	private boolean endTimer;
	private int startPlayers;
	
	public IceRunGame()
	{
		this.toPackedIceRegen = new ArrayList<>();
		this.toIceRegen = new ArrayList<>();
		this.alive = new ArrayList<>();
	}

	public void giveScoreboard()
	{
		ArrayList<String> alivePlayers = new ArrayList<String>();

		for(Player alive : this.alive)
			alivePlayers.add(alive.getName());

		String[] content = new String[alivePlayers.size() + 1];
		content[0] = "§bIceRun";

		for(int i = 1; i < content.length; i++)
			content[i] = alivePlayers.get(i - 1);

		ScoreboardUtil.unrankedSidebarDisplay(IceRun.players, content);
	}

	public void join(Player p)
	{
		p.teleport(IceRun.getSettings().getLobby().getLocation());
		PlayerUtil.clearInventory(p);
		PlayerUtil.clearBoard(p);
		PlayerUtil.heal(p);
		PlayerUtil.prepare(p);
		IceRunMessage.JOIN_GAMESTARTED.say(p);
		giveScoreboard();
	}

	public void leave(Player p)
	{
		if(this.isAlive(p))
			lose(p);

		giveScoreboard();
		PlayerUtil.clearInventory(p);
		PlayerUtil.clearBoard(p);
		PlayerUtil.heal(p);
		PlayerUtil.prepare(p);
		p.teleport(IceRun.getSettings().getExit().getLocation());
	}

	public void lose(Player p)
	{
		if(this.isAlive(p))
		{
			this.alive.remove(p);
			giveScoreboard();
			IceRunMessage.LOOSE.say(IceRun.players, "<player>", p.getName());
			IceRunStats looser = new IceRunStats(Core.getUserDatasManager().getUserData(p));
			looser.setGamesPlayed(looser.getGamesPlayed() + 1);
			looser.addPoints(10);
			looser.setScore(looser.getScore() + 10);
			p.sendMessage("§aPoints +10");
	
			PlayerUtil.heal(p);
			p.teleport(IceRun.getSettings().getLobby().getLocation());
			
			if (this.alive.size() == 1)
			{
		    	Player winner = this.alive.get(0);
		    	
		        IceRunMessage.WIN.say(Bukkit.getOnlinePlayers(), "<winner>", winner.getName());
	     
		    	FireworkUtil.generateRandom(IceRun.getSettings().getSpawn().getLocation());
		    	
		    	IceRunStats stats = new IceRunStats(Core.getUserDatasManager().getUserData(winner));
		    	int point = (int) (Math.pow(this.startPlayers - 1, 2) * 10);
	    		stats.setScore(stats.getScore() + point);
	    		stats.setVictories(stats.getVictories() + 1);
	    		stats.setGamesPlayed(stats.getGamesPlayed() + 1);
	    		stats.addPoints(point);
	    		winner.sendMessage("§aPoints +" + TextUtil.toString(point));
	    		
	    		winner.teleport(IceRun.getSettings().getLobby().getLocation());
	    		
	    		for(Player current : IceRun.players)
	    		{
	    			PlayerUtil.heal(current);
	    			PlayerUtil.clearInventory(current);
	    		}
	    		
		    	this.endTimer = false;
		    	this.canBreak = false;
	    	  
		    	end();
	      
		    	IceRun.status = new Standby();
		    	IceRun.status.start();
		    }
		}
  }

	public void start()
	{
		this.startPlayers = IceRun.players.size();
		this.canBreak = false;
		this.alive = new ArrayList<>();
		this.alive.addAll(IceRun.players);
		giveScoreboard();
		IceRunMessage.GAME_BEGIN.say(IceRun.players);
		for (Player p : IceRun.players)
			p.teleport(IceRun.getSettings().getSpawn().getLocation());
		
		this.taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(IceRun.getPlugin(), new Runnable()
		{
			private int timer = 5;

			public void run()
			{
				IceRunMessage.GAMESTARTIN.say(IceRun.players, "<timer>", ""+timer);

				if (this.timer == 0)
				{
					endTimer = true;
					IceRunGame.this.canBreak = true;
					Bukkit.getScheduler().cancelTask(IceRunGame.this.taskId);
					
					IceRunMessage.GAMESTART.say(IceRun.players);

					for(Player p : IceRun.players)
					{
						p.getWorld().playSound(p.getLocation(), Sound.NOTE_PLING, 10.0F, 1.0F);
					}
				}
				this.timer -= 1;
			}
		}, 1L, 20L);
	
		this.iceBreakTaskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(IceRun.getPlugin(), new Runnable()
		{
			@Override
			public void run()
			{
				if(IceRun.status instanceof IceRunGame)
					for(Player p : IceRun.players)
					{
						if(!isAlive(p))
							continue;
						List<Block> blocks = new ArrayList<Block>();
						
						for(int x = -1; x < 2; x++)
							for(int z = -1; z < 2; z++)
								blocks.add(p.getLocation().getBlock().getRelative(x, -1, z));
			
						
						for(Block block : blocks)
							if(LocationUtil.distanceFromCenter(block, p.getLocation().add(0, -1, 0)) < 2)
								if (block.getState().getType() == Material.PACKED_ICE)
								{
									Bukkit.getScheduler().runTaskLater(IceRun.getPlugin(), new DownGradeIce(block), 10L);
									((IceRunGame)IceRun.status).addPackedIceBlock(block.getLocation());
								}
								else if (block.getState().getType() == Material.ICE)
								{
									Bukkit.getScheduler().runTaskLater(IceRun.getPlugin(), new DeleteIce(block), 10L);
									if(!((IceRunGame)IceRun.status).toPackedIceRegen.contains(block.getLocation()))
										((IceRunGame)IceRun.status).addIceBlock(block.getLocation());
								}
						
						if(p.getLocation().getBlock().getRelative(0, -1, 0).getState().getType() == Material.SNOW_BLOCK)
							((IceRunGame)IceRun.status).lose(p);
					}
			}
		}, 100, 2);
	
		this.snowballs = Bukkit.getScheduler().scheduleSyncRepeatingTask(IceRun.getPlugin(), new Runnable()
		{
			public void run() 
			{
				if(endTimer == true)
				{
					if(IceRun.status instanceof IceRunGame)
						for(Player p : alive)
						{
							p.getInventory().addItem(new ItemStack(Material.SNOW_BALL, 1));
							p.updateInventory();
						}
				}
			}
		}, 60L, 60L);
		
		this.vipsnowballs = Bukkit.getScheduler().scheduleSyncRepeatingTask(IceRun.getPlugin(), new Runnable()
		{
			public void run() 
			{
				if(endTimer == true)
				{
					for(Player p : alive)
						if(p.hasPermission(IceRun.VIP))
						{
							p.getInventory().addItem(new ItemStack(Material.SNOW_BALL, 1));
							p.updateInventory();
						}
				}
			}
		 }, 60L, 90L);
	}



	private static class DeleteIce implements Runnable
	{
	  private Block block;
	
	  public DeleteIce(Block param1)
	  {
	    this.block = param1;
	  }
	
		public void run()
		{
			if(this.block.getType() == Material.ICE)
			{
				if (((IceRun.status instanceof IceRunGame)) && 
						(((IceRunGame)IceRun.status).canBreak()))
					this.block.breakNaturally();
			}
		}
	}
	
	private static class DownGradeIce
	  implements Runnable
	{
	  private Block block;
	
	  public DownGradeIce(Block param1)
	  {
	    this.block = param1;
	  }
	
	  public void run()
	  {
	  	if(this.block.getType() == Material.PACKED_ICE)
	  	{
		      if (((IceRun.status instanceof IceRunGame)) && 
		        (((IceRunGame)IceRun.status).canBreak()))
		        this.block.setType(Material.ICE);
	  	}
	  }
	}
	
	  public void addIceBlock(Location block)
	  {
	    this.toIceRegen.add(block);
	  }
	  
	  public void addPackedIceBlock(Location block)
	  {
	    this.toPackedIceRegen.add(block);
	  }
	
	  public boolean isAlive(Player p)
	  {
	    return this.alive.contains(p);
	  }
	
	  public boolean canBreak()
	  {
	    return this.canBreak;
	  }

	public boolean isInPackedIceRegenList(Location location)
	{
		return this.toPackedIceRegen.contains(location);
	}

	@Override
	public void end() 
	{
		Bukkit.getScheduler().cancelTask(this.taskId);
		Bukkit.getScheduler().cancelTask(this.iceBreakTaskId);
		Bukkit.getScheduler().cancelTask(this.snowballs);
		Bukkit.getScheduler().cancelTask(this.vipsnowballs);
		for (Location loc : this.toPackedIceRegen)
			loc.getBlock().setType(Material.PACKED_ICE);
  
		for (Location loc : this.toIceRegen)
			loc.getBlock().setType(Material.ICE);
	}

	@Override
	public String getStatus() {
		return "§aLa partie est en cours.";
	}
}