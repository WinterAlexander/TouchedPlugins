package ice.command;

import ice.IceRun;
import ice.IceRunMessage;
import me.winterguardian.core.Core;
import me.winterguardian.core.command.SubCommand;
import me.winterguardian.core.message.ErrorMessage;
import me.winterguardian.core.util.TextUtil;
import me.winterguardian.core.world.SerializableLocation;
import org.bukkit.block.Biome;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ManageSubCommand extends SubCommand
{
	public ManageSubCommand()
	{
		super("manage", Arrays.asList("admin", "config", "setconfig", "feelimportant"), IceRun.STAFF, ErrorMessage.COMMAND_INVALID_PERMISSION.toString(), "§bUsage: §f/icerun manage <setexit|setlobby|setspawn|setregion|setstuff|getstuff|setvipstuff|getvipstuff|saveconfig|reloadconfig>");
	}

	@Override
	public boolean onSubCommand(CommandSender sender, String label, String[] args)
	{
		if(args.length <= 0)
		{
			sender.sendMessage("§f----- [§b§lIceRun §f- §eConfig§f] -----");
			sender.sendMessage("§bLobby: " + (IceRun.getSettings().getLobby() != null ? "§aDéfini" : "§cIndéfini"));
			sender.sendMessage("§bPoint de sortie: " + (IceRun.getSettings().getExit() != null ? "§aDéfini" : "§cIndéfini"));
			sender.sendMessage("§bSpawn: " + (IceRun.getSettings().getSpawn() != null ? "§aDéfini" : "§cIndéfini"));
			sender.sendMessage("§bRégion: " + (IceRun.getSettings().getRegion() != null ? "§aDéfinie" : "§cIndéfinie"));
			sender.sendMessage("§bNombre de commandes allouées en jeu: §f" + IceRun.getSettings().getAllowedCommands().size());
			sender.sendMessage("§bNombre d'items: §f" + IceRun.getSettings().getStuff().size());
			sender.sendMessage("§bNombre d'items vips: §f" + IceRun.getSettings().getVipstuff().size());
			sender.sendMessage("");
			sender.sendMessage("§bPermission Vip: §f" + IceRun.VIP.getName());
			sender.sendMessage("§bPermission Staff: §f" + IceRun.STAFF.getName());
			return false;
		}
		
		if (!(sender instanceof Player))
		{
			ErrorMessage.COMMAND_INVALID_SENDER.say(sender);
			return true;
		}
		
		if(args[0].equalsIgnoreCase("setexit"))
		{
			if(IceRun.getSettings().getRegion() == null || !IceRun.getSettings().getRegion().contains(((Player)sender).getLocation()))
			{
				IceRun.getSettings().setExit(new SerializableLocation(((Player)sender).getLocation()));
				IceRunMessage.EXITSET.say(sender);
			}
			else
				IceRunMessage.EXITNOTINSIDE.say(sender);
			return true;
		}
		
		if(args[0].equalsIgnoreCase("setlobby"))
		{
			IceRun.getSettings().setLobby(new SerializableLocation(((Player)sender).getLocation()));
			IceRunMessage.LOBBYSET.say(sender);
			return true;
		}
		
		if(args[0].equalsIgnoreCase("setspawn"))
		{
			IceRun.getSettings().setSpawn(new SerializableLocation(((Player)sender).getLocation()));
			IceRunMessage.SPAWNSET.say(sender);
			return true;
		}
		
		if(args[0].equalsIgnoreCase("setregion"))
		{
			if(IceRun.getSettings().getExit() == null || !Core.getWand().getRegion((Player) sender).contains(IceRun.getSettings().getExit().getLocation()))
			{
				IceRun.getSettings().setRegion(Core.getWand().getRegion((Player) sender));
				IceRun.getSettings().getRegion().setBiome(Biome.ICE_PLAINS_SPIKES);
				IceRunMessage.REGIONSET.say(sender);
			}
			else
				IceRunMessage.EXITNOTINSIDE.say(sender);
			return true;
		}
		
		if(args[0].equalsIgnoreCase("setstuff"))
		{
			List<ItemStack> items = new ArrayList<ItemStack>();
			for(ItemStack item : ((Player)sender).getInventory().getContents())
				if(item != null)
					items.add(item);
			
			IceRun.getSettings().setStuff(items);
			IceRunMessage.STUFFSET.say(sender);
			return true;
		}
		
		if(args[0].equalsIgnoreCase("getstuff"))
		{
			for(ItemStack item : IceRun.getSettings().getStuff())
				((Player)sender).getInventory().addItem(item);
			IceRunMessage.STUFFGET.say(sender);
			return true;
		}
		
		if(args[0].equalsIgnoreCase("setvipstuff"))
		{
			List<ItemStack> items = new ArrayList<ItemStack>();
			for(ItemStack item : ((Player)sender).getInventory().getContents())
				if(item != null)
					items.add(item);
			
			IceRun.getSettings().setVipstuff(items);
			IceRunMessage.VIPSTUFFSET.say(sender);
			return true;
		}
		
		if(args[0].equalsIgnoreCase("getvipstuff"))
		{
			for(ItemStack item : IceRun.getSettings().getVipstuff())
				((Player)sender).getInventory().addItem(item);
			IceRunMessage.VIPSTUFFGET.say(sender);
			return true;
		}
		
		if(args[0].equalsIgnoreCase("saveconfig"))
		{
			try
			{
				IceRun.getSettings().save();
				IceRunMessage.CONFIG_SAVE.say(sender);
			}
			catch (IOException e)
			{
				e.printStackTrace();
				IceRunMessage.CONFIG_ERROR.say(sender);
			}
			return true;
		}
		
		if(args[0].equalsIgnoreCase("reloadconfig"))
		{
			IceRun.getSettings().load();
			IceRunMessage.CONFIG_RELOAD.say(sender);
			return true;
		}
		
		ErrorMessage.COMMAND_INVALID_ARGUMENT.say(sender);
		return false;
	}

	@Override
	public List<String> onSubTabComplete(CommandSender sender, String label, String[] args)
	{
		return TextUtil.getStringsThatStartWith(args[0], Arrays.asList("setexit", "setlobby", "setspawn", "setregion", "setstuff", "getstuff", "setvipstuff", "getvipstuff", "saveconfig", "reloadconfig"));
	}
}