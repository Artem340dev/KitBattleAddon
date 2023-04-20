package me.peopleXrCb.kitbattleaddon.abilities;

import me.wazup.kitbattle.Kitbattle;
import me.wazup.kitbattle.PlayerData;
import me.wazup.kitbattle.abilities.Ability;
import me.wazup.kitbattle.utils.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.stream.Collectors;

public class VampireBAbility extends Ability {
    private Material activationMaterial;
    private int cooldown, damageLevel;

    @Override
    public String getName() {
        return "VampireB";
    }

    @Override
    public void load(FileConfiguration fileConfiguration) {
        this.activationMaterial = XMaterial.valueOf(fileConfiguration.getString("Abilities.VampireB.ActivationMaterial")).parseMaterial();
        this.cooldown = fileConfiguration.getInt("Abilities.VampireB.Cooldown");
        this.damageLevel = fileConfiguration.getInt("Abilities.VampireB.DamageLevel");
    }

    @Override
    public Material getActivationMaterial() {
        return activationMaterial;
    }

    @Override
    public EntityType getActivationProjectile() {
        return null;
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
        if (playerData.hasCooldown(player, "VampireB")) {
            return false;
        } else {
            playerData.setCooldown(player, "VampireB", cooldown, true);
            Kitbattle.getInstance().sendUseAbility(player, playerData);

            Vector direction = player.getLocation().getDirection();
            List<Player> nearbyPlayers = player.getNearbyEntities(20, 20, 20)
                    .stream()
                    .filter(entity -> entity.getType().equals(EntityType.PLAYER))
                    .map(entity -> (Player) entity)
                    .collect(Collectors.toList());

            for (Location location = player.getLocation().clone(); location.distance(player.getLocation()) < 20; location.add(direction)) {
                player.playEffect(location, Effect.LARGE_SMOKE, 10);
                nearbyPlayers.stream()
                        .filter(plr -> plr.getLocation().distance(location) <= 1)
                        .forEach(plr -> plr.damage(damageLevel));
            }

            return true;
        }
    }
}