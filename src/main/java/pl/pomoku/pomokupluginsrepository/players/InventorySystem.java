package pl.pomoku.pomokupluginsrepository.players;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@SuppressWarnings("unused")
public class InventorySystem {
    public static boolean oneFreeSlot(Player player) {
        for (ItemStack item : player.getInventory()) {
            if (item == null || item.getType() == Material.AIR) return true;
        }
        return false;
    }

    public static Integer firstFreeSlot(Player player) {
        for (int i = 0; i < player.getInventory().getSize(); i++) {
            ItemStack item = player.getInventory().getItem(i);
            if (item == null || item.getType() == Material.AIR) return i;
        }
        return null;
    }
}
