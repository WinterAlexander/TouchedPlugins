package me.winterguardian.test.components;

import me.winterguardian.core.particle.ParticleData;
import me.winterguardian.core.particle.ParticleType;
import me.winterguardian.test.TestComponent;
import me.winterguardian.test.TestingCenter;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ParticleTest extends TestComponent
{

	public ParticleTest()
	{
		super("particle", TestingCenter.TEST_LEVEL3, "nah nah nah");
	}

	@Override
	public boolean onSubCommand(CommandSender sender, String label, String[] args)
	{
		if(!(sender instanceof Player))
			return false;
		
		Player p = (Player) sender;
		
		if(args.length < 6)
			return false;
		
		int[] data = new int[args.length - 6];
		
		for(int i = 6; i < args.length; i++)
			data[i - 6] = Integer.parseInt(args[i]);
		new ParticleData(ParticleType.valueOf(args[0].toUpperCase()), Float.parseFloat(args[1]), Float.parseFloat(args[2]), Float.parseFloat(args[3]), Float.parseFloat(args[4]), Integer.parseInt(args[5]), data).apply(p.getLocation());

		return true;
	}
	
}
