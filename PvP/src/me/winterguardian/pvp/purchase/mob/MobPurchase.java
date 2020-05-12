package me.winterguardian.pvp.purchase.mob;

import me.winterguardian.core.message.CoreMessage;
import me.winterguardian.core.message.Message;
import me.winterguardian.core.shop.PurchaseType;
import me.winterguardian.core.util.SoundEffect;
import me.winterguardian.core.util.TextUtil;
import me.winterguardian.pvp.PvPMessage;
import me.winterguardian.pvp.PvPPlugin;
import me.winterguardian.pvp.game.PvPMatch;
import me.winterguardian.pvp.game.PvPPlayerData;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Horse;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permission;

import java.util.Random;

/**
 *
 * Created by Alexander Winter on 2015-12-15.
 */
public class MobPurchase implements PurchaseType
{
	private String creationHeader, header, line2;

	public MobPurchase(String creationHeader, String header, String line2)
	{
		this.creationHeader = creationHeader;
		this.header = header;
		this.line2 = line2;
	}

	@Override
	public void give(String[] sign, Player player)
	{
		PvPPlayerData data = null;

		if(PvPPlugin.getGame().getState() instanceof PvPMatch)
			data = ((PvPMatch)PvPPlugin.getGame().getState()).getPlayerData(player);

		LivingEntity mob = null;
		switch(TextUtil.removeColorCodes(sign[2], '§').toLowerCase())
		{
			case "loup":
				mob = player.getWorld().spawn(player.getLocation(), Wolf.class);
				Wolf wolf = (Wolf)mob;

				wolf.setSitting(true);
				wolf.setAngry(false);
				wolf.setCollarColor(DyeColor.values()[new Random().nextInt(DyeColor.values().length)]);
				wolf.setAdult();
				wolf.setCustomName("Loup de " + player.getName());
				wolf.setCustomNameVisible(true);
				wolf.setOwner(player);
				wolf.setTamed(true);
				wolf.setMaxHealth(40);
				wolf.setHealth(wolf.getMaxHealth());
				break;
			case "cheval":
				mob = player.getWorld().spawn(player.getLocation(), Horse.class);
				Horse horse = (Horse)mob;

				Random r = new Random();

				horse.setCarryingChest(false);
				horse.setStyle(Horse.Style.values()[r.nextInt(Horse.Style.values().length)]);
				horse.setColor(Horse.Color.values()[r.nextInt(Horse.Color.values().length)]);
				horse.setVariant(Horse.Variant.HORSE);
				horse.setJumpStrength(0.75);
				horse.getInventory().setSaddle(new ItemStack(Material.SADDLE, 1));
				horse.getInventory().setArmor(new ItemStack(Material.IRON_BARDING, 1));
				horse.setAdult();
				horse.setCustomName("Cheval de " + player.getName());
				horse.setCustomNameVisible(true);
				horse.setOwner(player);
				horse.setTamed(true);
				horse.setPassenger(player);
				horse.setMaxHealth(60);
				horse.setHealth(horse.getMaxHealth());
				break;
			case "golem":
			case "dragon":
				return;
		}

		if(mob != null && data != null)
			data.setFriend(mob);

		new SoundEffect(Sound.LEVEL_UP, 1f, 1f).play(player);
	}

	@Override
	public Permission getCreationPermission()
	{
		return PvPPlugin.STAFF;
	}

	@Override
	public Message getNotEnoughPointsMessage()
	{
		return CoreMessage.PURCHASE_NOTENOUGHPOINTS;
	}

	@Override
	public Message getPurchaseSuccessMessage()
	{
		return CoreMessage.PURCHASE_SUCCESS;
	}

	@Override
	public boolean canGive(String[] sign, Player player)
	{
		if(!PvPPlugin.getGame().contains(player))
			return true;

		PvPPlayerData data = null;

		if(PvPPlugin.getGame().getState() instanceof PvPMatch)
			data = ((PvPMatch)PvPPlugin.getGame().getState()).getPlayerData(player);

		if(data == null)
			return true;

		if(data.getFriend() != null)
		{
			PvPMessage.GAME_CANTHAVE2FRIENDS.say(player);
			return false;
		}
		return true;
	}


	@Override
	public boolean match(String[] sign)
	{
		if(sign.length != 4)
			return false;

		if(!TextUtil.removeColorCodes(sign[0], '§').equalsIgnoreCase(TextUtil.removeColorCodes(header, '§')))
			return false;

		if(!TextUtil.removeColorCodes(line2, '§').equalsIgnoreCase(TextUtil.removeColorCodes(sign[1], '§')))
			return false;

		if(sign[2].length() <= 2)
			return false;

		try
		{
			Integer.parseInt(TextUtil.removeColorCodes(sign[3], '§'));
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}

	@Override
	public boolean canCreate(String[] sign)
	{
		if(sign.length != 4)
			return false;

		if(!creationHeader.equalsIgnoreCase(sign[0]))
			return false;

		if(!"mob".equalsIgnoreCase(sign[1]))
			return false;

		try
		{
			Integer.parseInt(sign[3]);
		}
		catch(Exception e)
		{
			return false;
		}

		return true;
	}

	@Override
	public String[] create(String[] sign)
	{
		return new String[]{header, line2, "§f" + sign[2], "§c" + sign[3]};
	}

	@Override
	public int getPrice(String[] sign)
	{
		return Integer.parseInt(TextUtil.removeColorCodes(sign[3], '§'));
	}
}
