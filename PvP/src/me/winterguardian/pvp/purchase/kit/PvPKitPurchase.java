package me.winterguardian.pvp.purchase.kit;

import me.winterguardian.core.message.CoreMessage;
import me.winterguardian.core.message.Message;
import me.winterguardian.core.shop.PurchaseType;
import me.winterguardian.core.util.SoundEffect;
import me.winterguardian.core.util.TextUtil;
import me.winterguardian.pvp.PvPPlugin;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

/**
 *
 * Created by Alexander Winter on 2015-12-10.
 */
public class PvPKitPurchase implements PurchaseType
{
	private String creationHeader, header, line2;

	public PvPKitPurchase(String creationHeader, String header, String line2)
	{
		this.creationHeader = creationHeader;
		this.header = header;
		this.line2 = line2;
	}

	@Override
	public void give(String[] sign, Player player)
	{
		Kit kit = new Kit(TextUtil.removeColorCodes(sign[2], '§'));

		kit.load();

		kit.give(player);
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
	public boolean canGive(Player player)
	{
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

		if(!"kit".equalsIgnoreCase(sign[1]))
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
