package me.winterguardian.test.components;

import me.winterguardian.test.TestComponent;
import me.winterguardian.test.TestingCenter;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityShootBowEvent;

public class SpoingBow extends TestComponent
{
	
	public SpoingBow()
	{
		super("spoingbow", TestingCenter.TEST_LEVEL3, "/test spoingbow");
	}
	
	@Override
	public boolean onSubCommand(CommandSender sender, String label, String[] args)
	{
		sender.sendMessage("Renommez un arc SpoingBow pour essayer ce composant de tests.");
		return true;
	}
	
	@EventHandler
	public void onEntityShootBow(EntityShootBowEvent e)
	{
		try
		{
			if(e.getEntity() instanceof Player)
				if(e.getBow() != null)
					if(e.getBow().getItemMeta() != null)
						if(e.getBow().getItemMeta().getDisplayName() != null)
							if(e.getBow().getItemMeta().getDisplayName().contains("SpoingBow"))
								if(((Player) e.getEntity()).hasPermission(TestingCenter.TEST_LEVEL3))
									e.getProjectile().setPassenger(e.getEntity());
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
}
