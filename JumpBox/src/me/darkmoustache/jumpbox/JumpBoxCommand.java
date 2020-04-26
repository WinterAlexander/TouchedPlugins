package me.darkmoustache.jumpbox;


import me.winterguardian.core.Core;
import me.winterguardian.core.game.GameManager;
import me.winterguardian.core.message.ErrorMessage;
import me.winterguardian.core.playerstats.MappedData;
import me.winterguardian.core.util.PlayerUtil;
import me.winterguardian.core.util.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class JumpBoxCommand implements CommandExecutor
{
	  public boolean onCommand(final CommandSender sender, Command cmd, String label, final String[] args)
	  {
			  if (!(sender instanceof Player))
			    {
	    		  ErrorMessage.COMMAND_INVALID_SENDER.say(sender);
			    	return true;
			    }
			  Player p = ((Player)sender).getPlayer();
	
		    if (args.length == 0)
		    {
		    	
		    	p.sendMessage("§f-------------§f[§aJumpBox§f]§f------------");
		    	p.sendMessage("§eVersion: §f" + JumpBox.getPlugin().getDescription().getVersion());
		    	p.sendMessage("§a/jumpbox tp §fSe téléporter à son checkpoint.");
		    	p.sendMessage("§a/jumpbox spawn §fSe téléporter au spawn Jump-Box.");
		    	p.sendMessage("§a/jumpbox reset §fMettre à zéro son checkpoint.");
		    	p.sendMessage("§a/jumpbox stats §fVoir ses statistiques de jeu.");
		    	p.sendMessage("§f---------------------------------");
		    }
		    else if (args.length == 1)
		    {
			      if (args[0].equalsIgnoreCase("tp") || args[0].equalsIgnoreCase("teleport"))
			      {	
			    	  GameManager.leaveAll(p);
			    	  if(JumpBox.getSettings().getLocation(p) == null)
				 			if(JumpBox.getSettings().getSpawn() != null)
				 			{
				 				p.teleport(JumpBox.getSettings().getSpawn());
				 			}
				 			else
				 			{
				 				p.sendMessage(JumpBoxMessage.COMMAND_CANT_WORLD.toString());
				 			}
				 		else
				 			p.teleport(JumpBox.getSettings().getLocation(p));
			    	  PlayerUtil.prepare(p);
		 				PlayerUtil.heal(p);
		 				PlayerUtil.clearInventory(p);
			      }
			      else if (args[0].equalsIgnoreCase("reset") || args[0].equalsIgnoreCase("quitter"))
			      {
		        	  if(JumpBox.getSettings().isInRegion(p.getLocation()))
			    	  {
		        		  GameManager.leaveAll(p);
			        	  JumpBox.getSettings().reset(p);
			        	  p.teleport(JumpBox.getSettings().getSpawn());
			        	  PlayerUtil.prepare(p);
			        	  PlayerUtil.heal(p);
			        	  PlayerUtil.clearInventory(p);
			        	  JumpBoxMessage.COMMAND_CP_RESET.say(sender);
			    	  }
		        	  else
			    	  {
		        		  JumpBoxMessage.COMMAND_CANT_WORLD.say(sender);
			    	  }
			      }
			      else if (args[0].equalsIgnoreCase("spawn"))
			      {
			    	  GameManager.leaveAll(p);
			    	  PlayerUtil.prepare(p);
		        	  PlayerUtil.heal(p);
		        	  PlayerUtil.clearInventory(p);
			    	  p.teleport(JumpBox.getSettings().getSpawn());
			      }
			      
			      else if (args[0].equalsIgnoreCase("setspawn"))
			      {
			    	 if (!(sender instanceof Player))
			    	 {
			    		  	ErrorMessage.COMMAND_INVALID_SENDER.say(sender);
					    	return true;
					 }
		    		 if (p.hasPermission(JumpBox.STAFF))
		    		 {
		    			  JumpBox.getSettings().setSpawn(p.getLocation());
		    			  JumpBoxMessage.COMMAND_SPAWNSET.say(sender);
		    		 }
		    		 else
		    		 {
		    			 ErrorMessage.COMMAND_INVALID_PERMISSION.say(sender);
		    		 }
			      }
			      else if (args[0].equalsIgnoreCase("setregion"))
			      {
			    	  if (!(sender instanceof Player))
					    {
			    		  	ErrorMessage.COMMAND_INVALID_SENDER.say(sender);
					    	return true;
					    }
		    		 if (p.hasPermission(JumpBox.STAFF))
		    		 {
		    			  JumpBox.getSettings().setRegion(Core.getWand().getRegion(p));
		    			  JumpBoxMessage.COMMAND_REGIONSET.say(sender);
		    		 }
		    		 else
		    		 {
		    			 ErrorMessage.COMMAND_INVALID_PERMISSION.say(sender);
		    		 }
			      }
			      else if (args[0].equalsIgnoreCase("save"))
			      {
		    		 if (p.hasPermission(JumpBox.STAFF))
		    		 {
		    			  JumpBox.getSettings().save();
		    		 }
		    		 else
		    		 {
		    			 ErrorMessage.COMMAND_INVALID_PERMISSION.say(sender);
		    		 }
			      }
				else if(args[0].equalsIgnoreCase("stats"))
				{
					if(!(sender instanceof Player))
					{
						ErrorMessage.COMMAND_INVALID_SENDER.say(sender);
						return true;
					}
					
					JumpBoxStats stats = new JumpBoxStats((Player)sender, Core.getUserDatasManager().getUserData((Player)sender));
					
					sender.sendMessage(JumpBoxMessage.COMMAND_STATS + " §a§l" + stats.getName());
					sender.sendMessage("§bScore: §c" + TextUtil.toString(stats.getScore()));
					sender.sendMessage("§aJumps faciles complétés: §f" + TextUtil.toString(stats.getEasyFinished()));
					sender.sendMessage("§eJumps moyens complétés: §f" + TextUtil.toString(stats.getMediumFinished()));
					sender.sendMessage("§6Jumps difficiles complétés: §f" + TextUtil.toString(stats.getHardFinished()));
					sender.sendMessage("§cJumps experts complétés: §f" + TextUtil.toString(stats.getExpertFinished()));
					return true;
				}
		    }
		    else if(args.length == 2)
		    {
		    	if(args[0].equalsIgnoreCase("stats"))
				{
		    		Bukkit.getScheduler().runTaskAsynchronously(JumpBox.getPlugin(), new Runnable()
		    		{
		    			@Override
		    			public void run()
		    			{

		    				MappedData data;
						    if(Bukkit.getPlayer(args[1]) != null)
							    data = Core.getUserDatasManager().getUserData(Bukkit.getPlayer(args[1]));
						    else
						        data = Core.getUserDatasManager().getFirstByValue("core.name", args[1]).getValue();
				    		
							if(data == null)
							{
								ErrorMessage.COMMAND_INVALID_PLAYER.say(sender);
								return;
							}
							
							JumpBoxStats stats = new JumpBoxStats(null, data);
							
							sender.sendMessage(JumpBoxMessage.COMMAND_STATS + " §a§l" + stats.getName());
							sender.sendMessage("§bScore: §c" + TextUtil.toString(stats.getScore()));
							sender.sendMessage("§aJumps faciles complétés: §f" + TextUtil.toString(stats.getEasyFinished()));
							sender.sendMessage("§eJumps moyens complétés: §f" + TextUtil.toString(stats.getMediumFinished()));
							sender.sendMessage("§6Jumps difficiles complétés: §f" + TextUtil.toString(stats.getHardFinished()));
							sender.sendMessage("§cJumps experts complétés: §f" + TextUtil.toString(stats.getExpertFinished()));
		    			}
		    		});
		    		
					return true;
				}
		    	else
			    	ErrorMessage.COMMAND_INVALID_ARGUMENT.say(sender);
		    	return true;
		    }
		    else
		    	ErrorMessage.COMMAND_INVALID_ARGUMENT.say(sender);
			return true;
	  }
}

