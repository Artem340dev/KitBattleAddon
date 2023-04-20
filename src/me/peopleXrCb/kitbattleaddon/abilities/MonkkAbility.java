package me.peopleXrCb.kitbattleaddon.abilities;

import me.wazup.kitbattle.Kitbattle;
import me.wazup.kitbattle.PlayerData;
import me.wazup.kitbattle.abilities.Ability;
import me.wazup.kitbattle.utils.Utils;
import me.wazup.kitbattle.utils.XMaterial;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class MonkkAbility extends Ability {
    private PotionEffect confusionEffect, slowEffect;
    private Material activationMaterial;
    private int cooldown;

    @Override
    public String getName() {
        return "Monkk";
    }

    @Override
    public void load(FileConfiguration fileConfiguration) {
        this.cooldown = fileConfiguration.getInt("Abilities.Monkk.Cooldown");
        this.activationMaterial = XMaterial.valueOf(fileConfiguration.getString("Abilities.Monkk.ActivationMaterial")).parseMaterial();
        this.confusionEffect = new PotionEffect(PotionEffectType.CONFUSION, 140, 0);
        this.slowEffect = new PotionEffect(PotionEffectType.SLOW, 140, 0);
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
        if (player.getItemInHand() != null && player.getItemInHand().getType().equals(this.activationMaterial)) {
            if (playerData.hasCooldown(player, "Monkk")) {
                return false;
            } else {
                playerData.setCooldown(player, "Monkk", cooldown, true);

                Player clickedPlayer = (Player)((PlayerInteractEntityEvent)event).getRightClicked();
                Kitbattle.getInstance().sendUseAbility(player, playerData);

                int heldItemSlot = clickedPlayer.getInventory().getHeldItemSlot();

                int i;
                for(i = Utils.random.nextInt(9); heldItemSlot == i; i = Utils.random.nextInt(9)) {
                }

                ItemStack itemInHand = clickedPlayer.getItemInHand();
                ItemStack randomItem = clickedPlayer.getInventory().getItem(i);

                clickedPlayer.getInventory().setItem(heldItemSlot, randomItem);
                clickedPlayer.getInventory().setItem(i, itemInHand);
                clickedPlayer.updateInventory();

                clickedPlayer.addPotionEffect(slowEffect);
                clickedPlayer.addPotionEffect(confusionEffect);
                return true;
            }
        } else {
            return false;
        }
    }
}