package pl.pomoku.pomokupluginsrepository.players;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
@SuppressWarnings("unused")
public class InventorySystem {
    public static boolean oneFreeSlot(Player player){
        for(ItemStack item : player.getInventory()){
            if(item == null || item.getType() == Material.AIR) return true;
        }
        return false;
    }
}
