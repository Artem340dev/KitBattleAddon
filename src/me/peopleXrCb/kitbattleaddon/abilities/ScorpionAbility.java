package me.peopleXrCb.kitbattleaddon.abilities;

import me.peopleXrCb.kitbattleaddon.KitBattleAddonPlugin;
import me.peopleXrCb.kitbattleaddon.utils.LocationUtil;
import me.wazup.kitbattle.Kitbattle;
import me.wazup.kitbattle.PlayerData;
import me.wazup.kitbattle.abilities.Ability;
import me.wazup.kitbattle.utils.XMaterial;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.stream.Collectors;

public class ScorpionAbility extends Ability {
    private int cooldown;
    private PotionEffect slowEffect, blindnessEffect;
    private EntityType activationProjectile;
    private Material activationMaterial;

    @Override
    public String getName() {
        return "Scorpion";
    }

    @Override
    public void load(FileConfiguration fileConfiguration) {
        this.cooldown = fileConfiguration.getInt("Abilities.Scorpion.Cooldown");
        this.slowEffect = new PotionEffect(PotionEffectType.SLOW, 200, 1);
        this.blindnessEffect = new PotionEffect(PotionEffectType.BLINDNESS, 100, 0);

        this.activationMaterial = XMaterial.MUSIC_DISC_13.parseMaterial();
        this.activationProjectile = EntityType.SNOWBALL;
    }

    @Override
    public Material getActivationMaterial() {
        return activationMaterial;
    }

    @Override
    public EntityType getActivationProjectile() {
        return activationProjectile;
    }

    @Override
    public boolean isAttackActivated() {
        return false;
    }

    @Override
    public boolean isAttackReceiveActivated() {
        return false;
    }

    @Override
    public boolean isDamageActivated() {
        return false;
    }

    @Override
    public boolean isEntityInteractionActivated() {
        return false;
    }

    @Override
    public boolean execute(Player player, PlayerData playerData, Event event) {
        if (event.getEventName().equals("PlayerInteractEvent")) {
            if (playerData.hasCooldown(player, "Scorpion")) return false;

            playerData.setCooldown(player, "Scorpion", cooldown, true);
            Kitbattle.getInstance().sendUseAbility(player, playerData);
            player.launchProjectile(Snowball.class).setMetadata("scorpion", new FixedMetadataValue(KitBattleAddonPlugin.getPlugin(KitBattleAddonPlugin.class), true));
            return true;
        } else {
            EntityDamageByEntityEvent ev = (EntityDamageByEntityEvent) event;
            if (!ev.getDamager().hasMetadata("scorpion")) return false;

            Player target = (Player) ev.getEntity();
            if ((int)target.getLocation().distance(player.getLocation()) > 30) return false;

            target.teleport(player);
            target.addPotionEffect(this.slowEffect);
            target.addPotionEffect(this.blindnessEffect);
            return true;
        }
    }
}