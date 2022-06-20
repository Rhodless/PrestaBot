package fr.rhodless.bot.events.impl;

import fr.rhodless.bot.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;

import java.util.Date;

public class GuildMemberJoinListener {
    public GuildMemberJoinListener(GuildMemberJoinEvent event) {
        EmbedBuilder embedBuilder = new EmbedBuilder();

            embedBuilder.setThumbnail(event.getMember().getEffectiveAvatarUrl());
            embedBuilder.setTitle("Un nouveau membre !");
        embedBuilder.setDescription("Bienvenue à **" + event.getMember().getEffectiveName() + "** sur PrestaDev. Merci beaucoup d'avoir rejoint le discord. La création de ticket se fait dans " + Main.getInstance().getGuild().getTextChannelById("968943397828169798").getAsMention() + ".");
        embedBuilder.setFooter("Développé par Rhodless#0001", "https://imgur.com/UsCsRqb.png");
        embedBuilder.setTimestamp(new Date().toInstant());



        Main.getInstance().getGuild().getTextChannelById("968940640253648926").sendMessageEmbeds(embedBuilder.build()).queue();
    }
}
