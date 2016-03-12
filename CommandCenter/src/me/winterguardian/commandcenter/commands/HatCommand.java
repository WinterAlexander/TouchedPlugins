package me.winterguardian.commandcenter.commands;
import java.util.Collections;
import java.util.List;

import me.winterguardian.commandcenter.CommandCenterMessage;
import me.winterguardian.core.command.AutoRegistrationCommand;
import me.winterguardian.core.message.ErrorMessage;
import me.winterguardian.core.particle.ParticleType;
import me.winterguardian.core.particle.ParticleUtil;
import me.winterguardian.core.util.SoundEffect;

import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class HatCommand extends AutoRegistrationCommand
{
 
        @Override
        public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
        {
                if(!(sender instanceof Player))
                {
                        ErrorMessage.COMMAND_INVALID_SENDER.say(sender);
                        return true;
                }
 
                if(args.length != 0)
                {
                        ErrorMessage.COMMAND_INVALID_ARGUMENT.say(sender);
                        return false;
                }
               
                Player p = ((Player) sender);
                PlayerInventory pInv = p.getInventory();
               
                if(pInv.getItemInHand() == null)
                {
                	CommandCenterMessage.HAT_NO_ITEM.say(sender);
                    return true;
                }
               
                if(pInv.getHelmet() != null)
                        p.getInventory().addItem(pInv.getHelmet());
               
                pInv.setHelmet(p.getItemInHand());
                pInv.setItemInHand(null);
                p.updateInventory();
               
                new SoundEffect(Sound.ITEM_PICKUP, 1, 1).play(p);
                p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20, 255));
                ParticleUtil.playSimpleParticles(p.getEyeLocation(), ParticleType.SPELL_INSTANT, 0, 0, 0, 0, 10);
               
                return true;
        }
 
        @Override
        public List<String> onTabComplete(CommandSender arg0, Command arg1,
                        String arg2, String[] arg3) {
                return null;
        }
 
        @Override
        public List<String> getAliases() {
                return Collections.singletonList("hat");
        }
 
        @Override
        public String getDescription() {
                return "Permet de placer un bloc sur sa tête.";
        }
 
        @Override
        public String getName() {
                return "chapeau";
        }
 
        @Override
        public Permission getPermission() {
                return new Permission("CommandCenter.hat", "Commande /hat", PermissionDefault.OP);
        }
 
        @Override
        public String getUsage() {
                return "§cSyntaxe: §f/hat";
        }
 
}