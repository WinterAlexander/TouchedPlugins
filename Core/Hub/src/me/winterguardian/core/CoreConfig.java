package me.winterguardian.core;

import me.winterguardian.core.game.Config;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

/**
 * Undocumented :(
 * <p>
 * Created on 2020-04-26.
 *
 * @author Alexander Winter
 */
public class CoreConfig extends Config
{
	private String url, username, password;

	private int vipReduction;

	public CoreConfig(File file)
	{
		super(file);
	}

	@Override
	protected void load(YamlConfiguration config)
	{
		this.url = config.getString("url", "jdbc:mysql://localhost/touched");
		this.username = config.getString("username", "root");
		this.password = config.getString("password", "root");
		this.vipReduction = config.getInt("vip-reduction", 25);
	}

	public String getURL()
	{
		return url;
	}

	public String getUsername()
	{
		return username;
	}

	public String getPassword()
	{
		return password;
	}

	public int getVipReduction()
	{
		return vipReduction;
	}
}
