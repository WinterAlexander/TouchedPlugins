package ice.listener;

import ice.IceRun;

import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class SignListener implements Listener
{
	@EventHandler
	public void onSignChange(SignChangeEvent e)
	{

		if (e.getPlayer().hasPermission(IceRun.STAFF))
		{
			if (e.getLine(0).equalsIgnoreCase("ir"))
			{
				if (e.getLine(1).equalsIgnoreCase("rejoindre"))
				{
					e.setLine(0, "");
					e.setLine(1, "§3[§bIceRun§3]");
					e.setLine(2, "§fRejoindre");
				}
				else if (e.getLine(1).equalsIgnoreCase("quitter"))
				{
					e.setLine(0, "");
					e.setLine(1, "§3[§bIceRun§3]");
					e.setLine(2, "§fQuitter");
				}
				else if (e.getLine(1).equalsIgnoreCase("info"))
				{
					e.setLine(0, "");
					e.setLine(1, "§3[§bIceRun§3]");
					e.setLine(2, "§fInfo");
				}
			}
		}
	}
  
  @EventHandler
  public void onPlayerInteract(PlayerInteractEvent e)
  {
    Player p = e.getPlayer();

    if (e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
    if ((e.getClickedBlock().getState() instanceof Sign))
    {
      Sign s = (Sign)e.getClickedBlock().getState();

      if (s.getLine(1).equalsIgnoreCase("§3[§bIceRun§3]"))
      {
        if (s.getLine(2).equalsIgnoreCase("§fRejoindre"))
        {
          p.performCommand("icerun joindre");
        }
        else if (s.getLine(2).equalsIgnoreCase("§fQuitter"))
        {
          p.performCommand("icerun quitter");
        }
        else if (s.getLine(2).equalsIgnoreCase("§fInfo"))
        {
          p.performCommand("icerun info");
        }
      }
    }
  }
}