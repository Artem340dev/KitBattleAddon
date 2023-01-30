package me.peopleXrCb.kitbattleaddon.utils;

import me.peopleXrCb.kitbattleaddon.KitBattleAddonPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class LocationUtil {
    public static Player getDirectionPlayer(Player player, int max_distance) {
        List<Player> players = player
                .getNearbyEntities(max_distance, max_distance, max_distance)
                .stream()
                .filter(entity -> entity instanceof Player)
                .filter(entity -> hasEntityAtDirection(player, entity, max_distance))
                .map(entity -> (Player) entity)
                .collect(Collectors.toList());

        if (players.size() > 0) {
            double minDistance = max_distance + 1;
            Player nearestPlayer = null;

            for (Player object : players) {
                if (nearestPlayer == null || object.getLocation().distance(player.getLocation()) < minDistance) nearestPlayer = object;
            }

            return nearestPlayer;
        }

        return null;
    }

    private static boolean hasEntityAtDirection(Player player, Entity entity, int max_distance) {
        BlockIterator iterator = new BlockIterator(player, max_distance);

        while (iterator.hasNext()) {
            Block block = iterator.next();
            if (block.getLocation().distance(entity.getLocation()) <= 1.5) {
                return true;
            }
        }

        return false;
    }

    /*public static Player getDirectionPlayer(Player sighter, int distance) {
        List<Player> playersInVector = Bukkit
                .getOnlinePlayers()
                .stream()
                .filter(obj -> obj.getWorld().equals(sighter.getWorld()))
                .filter(obj -> sighter.hasLineOfSight(obj))
                .filter(obj -> !obj.getName().equals(sighter.getName()))
                .collect(Collectors.toList());

        if (playersInVector.size() > 0) {
            double minDistance = distance + 1;
            Player nearestPlayer = null;

            for (Player object : playersInVector) {
                if (nearestPlayer == null || object.getLocation().distance(sighter.getLocation()) < minDistance) nearestPlayer = object;
            }

            return nearestPlayer;
        }

        return null;
    }*/
}