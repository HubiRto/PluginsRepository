package pl.pomoku.pomokupluginsrepository.text;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class Text {
    /**
     * Metoda ta zamienia przestarzały tekst typu string
     * na współcześnie wykorzystywany Component wbudowany
     * w silnik PaperMc
     *
     * @param text - text typu String np. < kolor > przykładowy tekst
     * @return - Komponent z interpretacją kolorów w nawiasach
     */
    public static Component strToComp(String text) {
        return MiniMessage.miniMessage().deserialize(text);
    }
}
