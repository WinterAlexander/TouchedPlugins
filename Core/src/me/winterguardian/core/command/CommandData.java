package me.winterguardian.core.command;

import org.bukkit.permissions.Permission;

import java.util.List;

public interface CommandData
{
	String getName();
	Permission getPermission();
	String getPermissionMessage();
	List<String> getAliases();
	String getUsage();
	String getDescription();
}
