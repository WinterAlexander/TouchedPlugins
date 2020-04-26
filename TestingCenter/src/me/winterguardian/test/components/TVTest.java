package me.winterguardian.test.components;

import me.winterguardian.test.TestComponent;
import me.winterguardian.test.TestingCenter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class TVTest extends TestComponent
{
	private Location baseLocation;
	
	public TVTest()
	{
		super("tv", TestingCenter.TEST_LEVEL4, "FUCK YOU");
	}
	
	@Override
	public boolean onSubCommand(CommandSender sender, String label, String[] args)
	{
		if(args.length < 1)
			return false;
		
		if(args[0].equalsIgnoreCase("setloc"))
		{
			if(sender instanceof Player)
				baseLocation = ((Player) sender).getLocation();
			return true;
		}
		
		if(args.length < 2)
			return false;
		
		if(args[0].equalsIgnoreCase("setimage"))
		{
			if(baseLocation == null)
				return false;
			
			BufferedImage image = null;
			try
			{
				image = ImageIO.read(new URL(args[1]));
			}
			catch (IOException e)
			{
				return false;
			}
			
			for(Entity entity : baseLocation.getWorld().getEntities())
				if(entity instanceof ArmorStand)
					if(entity.getLocation().distance(baseLocation) < 5)
						entity.remove();
					
			
			for(int x = 0; x < image.getWidth(); x++)
				for(int y = 0; y < image.getHeight(); y++)
				{
					ArmorStand stand = (ArmorStand) baseLocation.getWorld().spawnEntity(baseLocation.add(x / 16, y / -16, 0), EntityType.ARMOR_STAND);
					stand.teleport(baseLocation.add(x / 16, y / -16, 0));
					stand.setGravity(false);
					stand.setHelmet(new ItemStack(Material.WOOL, 1));
					stand.setHeadPose(new EulerAngle(45, 45, 0));
				}
			
			
		}
		return true;
	}
}
