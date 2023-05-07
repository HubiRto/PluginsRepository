package pl.pomoku.pomokupluginsrepository.text;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;

import java.time.Duration;

import static pl.pomoku.pomokupluginsrepository.text.Text.strToComp;

@SuppressWarnings("unused")
public class Title {
    private long show;
    private long stay;
    private long hide;
    private Component mainTitle;
    private Component subTitle;

    public Title(String title) {
        new Title(title, " ", 3, 5, 3);
    }

    public Title(String title, String subTitle, int show, int stay, int hide) {
        this.mainTitle = strToComp(title);
        this.subTitle = strToComp(subTitle);
        this.show = show * 1000L;
        this.stay = stay * 1000L;
        this.hide = hide * 1000L;
    }

    public void show(Audience audience) {
        final net.kyori.adventure.title.Title.Times times = net.kyori.adventure.title.Title.Times.times(
                Duration.ofMillis(this.show),
                Duration.ofMillis(this.stay),
                Duration.ofMillis(this.hide)
        );
        final net.kyori.adventure.title.Title title = net.kyori.adventure.title.Title.title(mainTitle, subTitle, times);
        audience.showTitle(title);
    }
}