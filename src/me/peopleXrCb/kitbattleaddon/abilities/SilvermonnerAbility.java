package me.peopleXrCb.kitbattleaddon.abilities;

import me.peopleXrCb.kitbattleaddon.KitBattleAddonPlugin;
import me.wazup.kitbattle.PlayerData;
import me.wazup.kitbattle.abilities.Ability;
import me.wazup.kitbattle.utils.XMaterial;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Silverfish;
import org.bukkit.event.Event;
import me.wazup.kitbattle.Kitbattle;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import pt.foxspigot.jar.FoxSpigot;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SilvermonnerAbility extends Ability {
    private Material activationMaterial;
    private int cooldown, radius;

    @Override
    public String getName() {
        return "Silvermonner";
    }

    @Override
    public void load(FileConfiguration fileConfiguration) {
        this.cooldown = fileConfiguration.getInt("Abilities.Silvermonner.Cooldown");
        this.activationMaterial = XMaterial.valueOf(fileConfiguration.getString("Abilities.Silvermonner.ActivationMaterial")).parseMaterial();
        this.radius = fileConfiguration.getInt("Abilities.Silvermonner.Radius");
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
        if (playerData.hasCooldown(player, "Silvermonner")) {
            return false;
        } else {
            playerData.setCooldown(player, "Silvermonner", cooldown, true);
            Kitbattle.getInstance().sendUseAbility(player, playerData);

            Stream<Player> nearestPlayers = player
                    .getNearbyEntities(radius, 1, radius)
                    .stream()
                    .filter(entity -> entity.getType().equals(EntityType.PLAYER))
                    .map(entity -> (Player) entity)
                    .filter(plr -> !plr.getUniqueId().equals(player.getUniqueId()));

            Optional<Player> nearestPlayerOptional;

            if (nearestPlayers.count() < 1) {
                player.sendMessage(ChatColor.RED + "Поблизости никого нет!");
                return false;
            }

            nearestPlayerOptional = nearestPlayers
                    .min((plr1, plr2) -> (int) plr1.getLocation().distance(player.getLocation()));

            for (int i = 0; i < 6; i++) {
                Silverfish silverfish = (Silverfish) player.getWorld().spawnEntity(player.getLocation(), EntityType.SILVERFISH);

                silverfish.setNoDamageTicks(300);
                silverfish.setTarget(nearestPlayerOptional.get());
            }

            return true;
        }
    }
}