package me.peopleXrCb.kitbattleaddon.abilities;

import me.peopleXrCb.kitbattleaddon.KitBattleAddonPlugin;
import me.peopleXrCb.kitbattleaddon.utils.LocationUtil;
import me.wazup.kitbattle.Kitbattle;
import me.wazup.kitbattle.PlayerData;
import me.wazup.kitbattle.abilities.Ability;
import me.wazup.kitbattle.utils.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.List;
import java.util.stream.Collectors;

public class GlitcherAbility extends Ability {
    private int cooldown, radius;
    private Material activationMaterial;
    private EntityType activationProjectile;

    @Override
    public String getName() {
        return "Glitcher";
    }

    @Override
    public void load(FileConfiguration fileConfiguration) {
        this.cooldown = fileConfiguration.getInt("Abilities.Glitcher.Cooldown");
        this.radius = fileConfiguration.getInt("Abilities.Glitcher.Radius");

        this.activationMaterial = XMaterial.ENDER_EYE.parseMaterial();
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
            if (playerData.hasCooldown(player, "Glitcher")) return false;

            playerData.setCooldown(player, "Glitcher", cooldown, true);
            Kitbattle.getInstance().sendUseAbility(player, playerData);
            player.launchProjectile(Snowball.class).setMetadata("glitcher", new FixedMetadataValue(KitBattleAddonPlugin.getPlugin(KitBattleAddonPlugin.class), true));
            return true;
        } else {
            EntityDamageByEntityEvent ev = (EntityDamageByEntityEvent) event;
            if (!ev.getDamager().hasMetadata("glitcher")) return false;

            Player target = (Player) ev.getEntity();
            if ((int)target.getLocation().distance(player.getLocation()) > radius) return false;

            Location targetBackPosition = target.getEyeLocation();
            targetBackPosition = targetBackPosition.subtract(targetBackPosition.getDirection().getBlockX()*1.5, 0, targetBackPosition.getDirection().getBlockZ()*1.5);
            targetBackPosition.setY(target.getLocation().getY());

            player.teleport(targetBackPosition);
            return true;
        }
    }
}