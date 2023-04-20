package me.peopleXrCb.kitbattleaddon.abilities;

import me.wazup.kitbattle.Kitbattle;
import me.wazup.kitbattle.PlayerData;
import me.wazup.kitbattle.abilities.Ability;
import me.wazup.kitbattle.abilities.list.DragonAbility;
import me.wazup.kitbattle.managers.PlayerDataManager;
import me.wazup.kitbattle.utils.XMaterial;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Iterator;

public class AvatarAbility extends Ability {
    private int cooldown, waterRange, bursts, fireRange, fireDuration;
    private Material activationMaterial;
    private PotionEffect slowEffect;

    @Override
    public String getName() {
        return "Avatar";
    }

    @Override
    public void load(FileConfiguration fileConfiguration) {
        this.cooldown = fileConfiguration.getInt("Abilities.Avatar.Cooldown");
        this.waterRange = fileConfiguration.getInt("Abilities.Avatar.WaterRange");
        this.fireRange = fileConfiguration.getInt("Abilities.Avatar.FireRange");
        this.fireDuration = fileConfiguration.getInt("Abilities.Avatar.FireDuration") * 20;
        this.bursts = fileConfiguration.getInt("Abilities.Avatar.Amount-Of-Bursts");

        this.activationMaterial = XMaterial.valueOf(fileConfiguration.getString("Abilities.Avatar.ActivationMaterial")).parseMaterial();
        int slowDuration = fileConfiguration.getInt("Abilities.Avatar.SlowDuration") * 20;

        this.slowEffect = new PotionEffect(PotionEffectType.SLOW, slowDuration, 1);
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
        if (playerData.hasCooldown(player, "Avatar")) {
            return false;
        } else {
            playerData.setCooldown(player, "Avatar", cooldown, true);
            Kitbattle.getInstance().sendUseAbility(player, playerData);

            if (!player.isSneaking()) {
                Location[] locations = new Location[] {player.getLocation(), player.getLocation().clone().add(0.0, waterRange, 0.0), player.getLocation().clone().add(0.0, -waterRange, 0.0), player.getLocation().clone().add(waterRange, 0.0, 0.0), player.getLocation().clone().add(0.0, 0.0, waterRange), player.getLocation().clone().add(waterRange, 0.0, waterRange), player.getLocation().clone().add(-waterRange, 0.0, -waterRange), player.getLocation().clone().add(waterRange, 0.0, -waterRange), player.getLocation().clone().add(-waterRange, 0.0, waterRange)};

                for(int i = 0; i < locations.length; i++) {
                    Location location = locations[i];
                    player.getWorld().playEffect(location, Effect.WATERDRIP, 1);
                }

                Iterator<Entity> iterator = player.getNearbyEntities(waterRange, waterRange, waterRange).iterator();

                while (iterator.hasNext()) {
                    Entity entity = iterator.next();

                    if (entity.getType().equals(EntityType.PLAYER)) {
                        Player playerEntity = (Player) entity;
                        if (playerEntity.getName().equals(player.getName())) {
                            continue;
                        }

                        PlayerData playerEntityData = PlayerDataManager.get(playerEntity);
                        if (playerEntityData != null && PlayerDataManager.get(playerEntity).getKit() == null || playerData.getMap().isInSpawn(playerEntity)) {
                            player.sendMessage(Kitbattle.getInstance().msgs.messages.get("Use-Ability-Deny"));
                            continue;
                        }

                        playerEntity.addPotionEffect(slowEffect);
                    }
                }
            } else {
                player.setVelocity(player.getLocation().getDirection().multiply(10));

                (new BukkitRunnable() {
                    int burstsCount = 0;

                    public void run() {
                        Location[] locations = new Location[]{player.getLocation(), player.getLocation().clone().add(0.0, fireRange, 0.0), player.getLocation().clone().add(0.0, -fireRange, 0.0), player.getLocation().clone().add(fireRange, 0.0, 0.0), player.getLocation().clone().add(0.0, 0.0, fireRange), player.getLocation().clone().add(fireRange, 0.0, fireRange), player.getLocation().clone().add(-fireRange, 0.0, -fireRange), player.getLocation().clone().add(fireRange, 0.0, -fireRange), player.getLocation().clone().add(-fireRange, 0.0, fireRange)};

                        for(int i = 0; i < locations.length; i++) {
                            Location location = locations[i];
                            player.getWorld().playEffect(location, Effect.FIREWORKS_SPARK, 1);
                        }

                        Iterator<Entity> iterator = player.getNearbyEntities(fireRange, fireRange, fireRange).iterator();

                        while(true) {
                            while(true) {
                                Entity entity;

                                do {
                                    if (!iterator.hasNext()) {
                                        this.burstsCount++;
                                        if (burstsCount >= bursts) {
                                            this.cancel();
                                        }

                                        return;
                                    }

                                    entity = iterator.next();
                                } while(!(entity instanceof Damageable));

                                if (entity.getType().equals(EntityType.PLAYER)) {
                                    Player playerEntity = (Player) entity;
                                    if (playerEntity.getName().equals(player.getName())) {
                                        continue;
                                    }

                                    PlayerData playerEntityData = PlayerDataManager.get(playerEntity);
                                    if (playerEntityData != null && PlayerDataManager.get(playerEntity).getKit() == null || playerData.getMap().isInSpawn(playerEntity)) {
                                        player.sendMessage(Kitbattle.getInstance().msgs.messages.get("Use-Ability-Deny"));
                                        continue;
                                    }
                                }

                                entity.setFireTicks(fireDuration);
                            }
                        }
                    }
                }).runTaskTimer(Kitbattle.getInstance(), 0L, 20L);
            }

            return true;
        }
    }
}
