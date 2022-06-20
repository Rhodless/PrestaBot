package fr.rhodless.bot.ticket;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
public class Ticket {

    private final String channelId;
    private final UUID ticketId;
    private final String userId;
    private final TicketType ticketType;
    private String explication;
    private String claimedId;
    private long close;
    private String language;

    public Ticket(TextChannel channel, User user, TicketType ticketType, String language) {
        this.channelId = channel.getId();
        this.ticketId = UUID.randomUUID();
        this.userId = user.getId();
        this.claimedId = null;
        this.ticketType = ticketType;
        this.explication = null;
        this.close = -1L;
        this.language = language;
    }

    public boolean isFrench() {
        if(language == null) return true;

        return language.equalsIgnoreCase("FRENCH");
    }

    @Getter
    @RequiredArgsConstructor
    public enum TicketType {
        GD("Game Designer", Emoji.fromMarkdown("\uD83C\uDFA8"), "968939766143926342"),
        ART("Graphic designer", Emoji.fromMarkdown("\uD83D\uDCBC"), "968944795529670686"),
        CONFIG("Configurator", Emoji.fromMarkdown("\uD83D\uDD27"), "968939917847724043"),
        DEV("Developer", Emoji.fromMarkdown("\uD83D\uDCBB"), "968939728986595378"),
        CM("Community Manager", Emoji.fromMarkdown("\uD83C\uDF9F"), "968969429914382416"),
        PACK_MAKER("Pack Maker", Emoji.fromMarkdown("✒️"), "969964998464004106"),
        FAMOUS("Influencer", Emoji.fromMarkdown("\uD83C\uDFAC"), "968971782352695297"),
        BUILDER("Builder", Emoji.fromMarkdown("⛏️"), "968939863644721272"),
        TRAILER("Trailer Maker", Emoji.fromMarkdown("\uD83D\uDCFD"), "968939887384486009"),
        DESSIN("Drawer", Emoji.fromMarkdown("✒️"), "969239249872375868"),
        MODEL("Modélisateur", Emoji.fromMarkdown("⛏️"), "969964959721218088"),
        POSTULER("Postuler...", Emoji.fromMarkdown("✅"), "970148221835935754")

        ;

        private final String name;
        private final Emoji emoji;
        private final String roleId;
    }

}
