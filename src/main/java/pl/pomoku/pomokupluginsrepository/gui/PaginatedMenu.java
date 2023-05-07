package pl.pomoku.pomokupluginsrepository.gui;

import pl.pomoku.pomokupluginsrepository.items.ItemBuilder;

import java.util.List;

import static org.bukkit.Material.ARROW;
import static pl.pomoku.pomokupluginsrepository.text.Text.strToComp;

@SuppressWarnings("unused")
public abstract class PaginatedMenu extends Menu {
    protected int page = 0;
    protected int maxItemsPerPage;
    protected int index = 0;

    public abstract int setMaxItemsPerPage();

    public PaginatedMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
        this.maxItemsPerPage = setMaxItemsPerPage();
    }

    public void previewsPage(int slotNumber) {
        if (page != 0)
            inventory.setItem(slotNumber, new ItemBuilder(ARROW)
                    .displayname(strToComp("<yellow><bold><< Poprzednia strona"))
                    .lore(List.of(
                            strToComp("<gray>Kliknij, aby przejść"),
                            strToComp("<gray>do poprzedniej strony <yellow>" + (page - 1) + "</yellow>.")
                    )).build());
    }

    public void nextPage(int slotNumber, int amountOfItems) {
        if (page != amountOfItems / maxItemsPerPage)
            inventory.setItem(slotNumber, new ItemBuilder(ARROW)
                    .displayname(strToComp("<yellow><bold>Następna strona >>"))
                    .lore(List.of(
                            strToComp("<gray>Kliknij, aby przejść"),
                            strToComp("<gray>do następnej strony <yellow>" + (page + 1) + "</yellow>.")
                    )).build());
    }

    public void addMenuBorder() {
        for (int i = 0; i < 10; i++) if (inventory.getItem(i) == null) inventory.setItem(i, FILLER_GLASS);
        inventory.setItem(17, FILLER_GLASS);
        inventory.setItem(18, FILLER_GLASS);
        inventory.setItem(26, FILLER_GLASS);
        inventory.setItem(27, FILLER_GLASS);
        inventory.setItem(35, FILLER_GLASS);
        inventory.setItem(36, FILLER_GLASS);
    }
}
