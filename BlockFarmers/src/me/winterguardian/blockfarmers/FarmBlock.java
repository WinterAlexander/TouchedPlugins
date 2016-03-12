package me.winterguardian.blockfarmers;

import org.bukkit.Material;
import org.bukkit.block.Block;

public class FarmBlock
{
	private Material material;
	private int data;
	
	public FarmBlock(Material material)
	{
		this(material, -1);
	}
	
	public FarmBlock(Material material, int data)
	{
		this.material = material;
		this.data = data;
	}
	
	public FarmBlock(String string)
	{
		this.material = Material.getMaterial(string.split(":")[0]);
		this.data = Integer.parseInt(string.split(":")[1]);
	}

	public Material getMaterial()
	{
		return material;
	}

	public void setMaterial(Material material)
	{
		this.material = material;
	}

	public int getData()
	{
		return data;
	}

	public void setData(int data)
	{
		this.data = data;
	}
	
	@SuppressWarnings("deprecation")
	public boolean match(Block block)
	{
		return block.getType() == this.material && (this.data < 0 || block.getData() == this.data);
	}
	
	@Override
	public String toString()
	{
		return this.material.name() + ":" + this.data;
	}
	
	public static FarmBlock fromString(String string)
	{
		try
		{
			return new FarmBlock(string);
		}
		catch(Exception e)
		{
			return null;
		}
	}
	
	@SuppressWarnings("deprecation")
	public void apply(Block block)
	{
		block.setType(this.material, false);
		block.setData((byte) (this.data < 0 ? 0 : this.data), false);
	}
}
