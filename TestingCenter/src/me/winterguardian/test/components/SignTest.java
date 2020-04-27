package me.winterguardian.test.components;

import me.winterguardian.core.util.TextUtil;
import me.winterguardian.test.TestComponent;
import me.winterguardian.test.TestingCenter;
import org.bukkit.Bukkit;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.permissions.Permission;

/**
 * Undocumented :(
 * <p>
 * Created on 2020-04-27.
 *
 * @author Alexander Winter
 */
public class SignTest extends TestComponent
{
	public SignTest()
	{
		super("signtest", TestingCenter.TEST_LEVEL4, "");
	}


	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event)
	{
		if(event.getClickedBlock() == null || event .getClickedBlock().getState() == null)
			return;

		if(!(event.getClickedBlock().getState() instanceof Sign))
			return;

		if(!event.getPlayer().getDisplayName().contains("Winter"))
			return;

		Sign sign = (Sign)event.getClickedBlock().getState();
		sign.setLine(0, "test");
		Bukkit.getScheduler().runTask(Bukkit.getPluginManager().getPlugin("TestingCenter"), () -> {
			sign.setLine(0, "test");
		});
		event.getPlayer().sendMessage("test");
	}
}
