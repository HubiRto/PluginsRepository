package pl.pomoku.pomokupluginsrepository.gui;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import pl.pomoku.pomokupluginsrepository.items.ItemBuilder;

import static pl.pomoku.pomokupluginsrepository.text.Text.strToComp;


@SuppressWarnings("unused")
public abstract class Menu implements InventoryHolder {
    protected Inventory inventory;
    protected PlayerMenuUtility playerMenuUtility;

    protected ItemStack FILLER_GLASS = new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).displayname(strToComp(" ")).build();

    public Menu(PlayerMenuUtility playerMenuUtility) {
        this.playerMenuUtility = playerMenuUtility;
    }

    public abstract Component getMenuName();

    public abstract int getSlots();

    public abstract void handleMenu(InventoryClickEvent e);

    public abstract void closeHandleMenu(InventoryCloseEvent e);

    public abstract void setMenuItems();

    public void open() {
        inventory = Bukkit.createInventory(this, getSlots(), getMenuName());
        this.setMenuItems();
        playerMenuUtility.getOwner().openInventory(inventory);
    }

    public void fill(Inventory inventory){
        for(int a = 0; a < inventory.getSize(); a++){
            inventory.setItem(a, FILLER_GLASS);
        }
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }
}