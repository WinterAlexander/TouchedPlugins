package me.winterguardian.pvp.game;

import me.winterguardian.core.entity.EntityUtil;
import me.winterguardian.core.gameplay.EntityEffectByProjectileEvent;
import me.winterguardian.core.util.CombatUtil;
import me.winterguardian.core.util.SoundEffect;
import me.winterguardian.pvp.stats.Bonus;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;

/**
 *
 * Created by Alexander Winter on 2015-12-07.
 */
public class GameListener implements Listener
{
	private PvPMatch game;

	public GameListener(PvPMatch game)
	{
		this.game = game;
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPlayerDamageByHand(EntityDamageByEntityEvent event)
	{
		if(!(event.getDamager() instanceof Player))
			return;

		if(!(event.getEntity() instanceof Player))
			return;

		PvPPlayerData playerData = game.getPlayerData((Player)event.getEntity());

		if(playerData == null || !playerData.isPlaying())
			return;

		PvPPlayerData damagerData = game.getPlayerData((Player)event.getDamager());

		if(damagerData == null || !damagerData.isPlaying())
			return;

		if(!playerData.isPvpActive() || !game.areEnemies(playerData.getPlayer(), (Player)event.getDamager()) || !damagerData.isPvpActive())
		{
			event.setCancelled(true);
			return;
		}

		playerData.damage((Player)event.getDamager());

		ItemStack weapon = ((Player)event.getDamager()).getItemInHand();
		if(weapon != null && weapon.containsEnchantment(Enchantment.FIRE_ASPECT))
			playerData.setLastFireDamager((Player)event.getDamager());
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPlayerDamageByMob(EntityDamageByEntityEvent event)
	{
		if(!(event.getDamager() instanceof LivingEntity) || !(event.getEntity() instanceof Player))
			return;

		PvPPlayerData playerData = game.getPlayerData((Player)event.getEntity());

		if(playerData == null || !playerData.isPlaying())
			return;

		if(event.getDamager() instanceof Player && game.getGame().contains((Player)event.getDamager()))
			return;

		PvPPlayerData damagerData = game.getOwner(event.getDamager());

		if(damagerData == null || !damagerData.isPlaying())
		{
			event.setCancelled(true);
			if(event.getDamager() instanceof Player)
				event.getDamager().teleport(game.getGame().getSetup().getExit());
			else
				event.getDamager().remove();
			return;
		}

		if(!playerData.isPvpActive() || !game.areEnemies(playerData.getPlayer(), damagerData.getPlayer()))
		{
			event.setCancelled(true);
			return;
		}

		playerData.damage(damagerData.getPlayer());
		event.setDamage(event.getDamage() * 3);
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPlayerDamageByTNT(EntityDamageByEntityEvent event)
	{
		if(!(event.getDamager() instanceof TNTPrimed))
			return;

		if(!(event.getEntity() instanceof Player))
			return;

		TNTPrimed tnt = (TNTPrimed)event.getDamager();

		if(!(tnt.getSource() instanceof Player))
			return;

		PvPPlayerData playerData = game.getPlayerData((Player)event.getEntity());

		if(playerData == null || !playerData.isPlaying())
			return;

		PvPPlayerData damagerData = game.getPlayerData((Player)tnt.getSource());

		if(damagerData == null)
		{
			event.setCancelled(true);
			return;
		}

		if(damagerData.getPlayer() == playerData.getPlayer())
			return;

		if(!playerData.isPvpActive() || !game.areEnemies(playerData.getPlayer(), damagerData.getPlayer()))
		{
			event.setCancelled(true);
			return;
		}

		playerData.damage(damagerData.getPlayer());
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPlayerDamageByProjectile(EntityDamageByEntityEvent event)
	{
		if(!(event.getDamager() instanceof Projectile))
			return;

		if(event.getDamager() instanceof ThrownPotion && CombatUtil.isMedical((ThrownPotion)event.getDamager()))
			return;

		if(!(((Projectile)event.getDamager()).getShooter() instanceof Player))
			return;

		if(!(event.getEntity() instanceof Player))
			return;

		if(event.getEntity() == ((Projectile)event.getDamager()).getShooter())
			return;

		PvPPlayerData playerData = game.getPlayerData((Player)event.getEntity());

		if(playerData == null || !playerData.isPlaying())
			return;

		Player damager = (Player)((Projectile)event.getDamager()).getShooter();
		PvPPlayerData damagerData = game.getPlayerData(damager);

		if(damagerData == null)
		{
			event.setCancelled(true);
			return;
		}

		if(damagerData.getPlayer() == playerData.getPlayer())
			return;

		if(!playerData.isPvpActive() || !game.areEnemies(playerData.getPlayer(), damager))
		{
			event.setCancelled(true);
			return;
		}

		if(event.getDamager() instanceof Arrow && CombatUtil.isHeadshot(event))
		{
			event.setDamage(event.getDamage() * 1.3f);

			if(damagerData.isPlaying())
				damagerData.addBonus(Bonus.HEADSHOT);
		}

		if(damager.getLocation().distance(event.getEntity().getLocation()) > 60)
		{
			if(damagerData.isPlaying())
				damagerData.addBonus(Bonus.VERYLONGSHOT);
		}
		else if(damager.getLocation().distance(event.getEntity().getLocation()) > 30)
				if(damagerData.isPlaying())
					damagerData.addBonus(Bonus.LONGSHOT);

		playerData.damage(damager);

		if(event.getDamager().getFireTicks() != 0)
			playerData.setLastFireDamager(damager);
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPlayerDamageByEffect(EntityDamageEvent event)
	{
		if(!(event.getEntity() instanceof Player))
			return;

		PvPPlayerData playerData = game.getPlayerData((Player)event.getEntity());

		if(playerData == null || !playerData.isPlaying())
			return;

		if(!playerData.isPvpActive())
		{
			event.setCancelled(true);
			return;
		}

		switch(event.getCause())
		{
			case POISON:
				playerData.damagePoison();
				break;

			case WITHER:
				playerData.damageWhiter();
				break;

			case FIRE_TICK:
				playerData.damageFire();
				break;

		}

	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPotionSplash(PotionSplashEvent event)
	{
		if(!(event.getPotion().getShooter() instanceof Player))
			return;

		Player damager = (Player)event.getPotion().getShooter();

		PvPPlayerData damagerData = game.getPlayerData((Player)damager);

		if(damagerData == null || !damagerData.isPlaying())
			return;

		for(LivingEntity victim : new ArrayList<>(event.getAffectedEntities()))
		{
			if(!(victim instanceof Player) || !(event.getPotion().getShooter() instanceof Player) || victim == damager)
				continue;

			PvPPlayerData playerData = game.getPlayerData((Player)victim);

			if(playerData == null || !playerData.isPlaying())
				continue;


			if(CombatUtil.isFriendly(event.getPotion()))
			{
				if(game.areEnemies((Player)victim, damager))
					continue;

				if(CombatUtil.isMedical(event.getPotion()))
					damagerData.addBonus(Bonus.TEAM_POTION_HEAL);
				else
					damagerData.addBonus(Bonus.TEAM_POTION_OTHER);

				continue;
			}

			if(!playerData.isPvpActive() || !game.areEnemies((Player)victim, damager) || !damagerData.isPvpActive())
			{
				event.setIntensity(victim, 0);
				continue;
			}

			for(PotionEffect effect : event.getPotion().getEffects())
			{
				if(PotionEffectType.POISON.equals(effect.getType()))
					playerData.setLastPoisonDamager(damagerData.getPlayer());

				if(PotionEffectType.WITHER.equals(effect.getType()))
					playerData.setLastWhiterDamager(damagerData.getPlayer());
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onEntityEffectByProjectile(EntityEffectByProjectileEvent event)
	{
		if(!(event.getEntity() instanceof Player))
			return;

		PvPPlayerData playerData = game.getPlayerData((Player)event.getEntity());

		if(playerData == null || !playerData.isPlaying())
			return;

		if(!(event.getProjectile().getShooter() instanceof Player))
			return;

		PvPPlayerData damagerData = game.getPlayerData((Player)event.getProjectile().getShooter());

		if(damagerData == null || !damagerData.isPlaying())
			return;

		if(playerData.getPlayer() == damagerData.getPlayer())
			return;

		if(!game.areEnemies(playerData.getPlayer(), damagerData.getPlayer()) && !CombatUtil.isFriendly(event.getEffect().getType()))
		{
			event.setCancelled(true);
			return;
		}

		if(PotionEffectType.POISON.equals(event.getEffect().getType()))
			playerData.setLastPoisonDamager(damagerData.getPlayer());

		if(PotionEffectType.WITHER.equals(event.getEffect().getType()))
			playerData.setLastWhiterDamager(damagerData.getPlayer());
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerDeath(final PlayerDeathEvent event)
	{

		PvPPlayerData playerData = game.getPlayerData(event.getEntity());

		if(playerData == null || !playerData.isPlaying())
			return;

		playerData.death();

		event.setKeepInventory(false);
		event.setKeepLevel(false);
		event.setNewExp(0);
		event.setNewTotalExp(0);
		event.setDroppedExp(0);
		event.getDrops().clear();
		event.setDeathMessage(null);

		Bukkit.getScheduler().runTaskLater(game.getGame().getPlugin(), () -> {
			event.getEntity().setCanPickupItems(false);
			event.getEntity().spigot().respawn();
		}, 5);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onFriendDeath(EntityDeathEvent event)
	{
		PvPPlayerData playerData = game.getOwner(event.getEntity());

		if(playerData == null || !playerData.isPlaying())
			return;

		playerData.friendDeath();
		event.setDroppedExp(0);
		event.getDrops().clear();
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPlayerDropItem(PlayerDropItemEvent event)
	{
		if(game.getGame().contains(event.getPlayer()))
			event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerRespawn(PlayerRespawnEvent event)
	{
		if(!game.getGame().contains(event.getPlayer()))
			return;

		if(game.getGame().getState() != game)
			return;

		event.getPlayer().setCanPickupItems(true);

		PvPPlayerData playerData = game.getPlayerData(event.getPlayer());

		if(playerData == null || !playerData.isPlaying())
			return;

		event.setRespawnLocation(game.getSpawnPoint(playerData));
		playerData.respawn();

		Player player = event.getPlayer();

		Bukkit.getScheduler().runTask(game.getGame().getPlugin(), () -> {
			player.addPotionEffects(game.getArena().getEffects());
		});
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerTNT(PlayerInteractEvent event)
	{
		if(!game.getGame().contains(event.getPlayer()))
			return;

		PvPPlayerData playerData = game.getPlayerData(event.getPlayer());

		if(playerData == null || !playerData.isPlaying())
			return;

		if(event.getAction() == Action.PHYSICAL)
			return;

		if(event.getItem() == null || event.getItem().getType() != Material.TNT)
			return;

		event.setCancelled(true);
		if(event.getItem().getAmount() > 1)
			event.getItem().setAmount(event.getItem().getAmount() - 1);
		else
			event.getPlayer().getInventory().remove(event.getItem());

		TNTPrimed tnt = (TNTPrimed)event.getPlayer().getWorld().spawnEntity(event.getPlayer().getEyeLocation(), EntityType.PRIMED_TNT);
		EntityUtil.setTNTPrimedSource(tnt, event.getPlayer());

		if(event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK)
		{
			new SoundEffect(Sound.GHAST_FIREBALL, 1, 1).play(event.getPlayer());
			tnt.setVelocity(event.getPlayer().getLocation().getDirection().multiply(2));
		}
		else
		{
			new SoundEffect(Sound.FIRE_IGNITE, 1, 1).play(event.getPlayer());
			tnt.setVelocity(event.getPlayer().getLocation().getDirection().multiply(0.1));
		}
	}
}
