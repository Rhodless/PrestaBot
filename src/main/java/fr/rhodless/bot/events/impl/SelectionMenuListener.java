package fr.rhodless.bot.events.impl;

import fr.rhodless.bot.Main;
import fr.rhodless.bot.ticket.Ticket;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import net.dv8tion.jda.api.interactions.components.selections.SelectionMenu;
import net.dv8tion.jda.api.interactions.components.selections.SelectionMenuInteraction;

import java.util.Date;

public class SelectionMenuListener {

    public SelectionMenuListener(SelectionMenuInteraction event) {
        if (event.getMember() == null) return;
        if (event.getSelectedOptions() == null) return;

        SelectOption option = event.getSelectedOptions().get(0);
        Ticket.TicketType type = Ticket.TicketType.valueOf(option.getValue());

        String language;
        if (event.getTextChannel().getId().equalsIgnoreCase("968943397828169798")) {
            language = "FRENCH";
        } else {
            language = "ENGLISH";
        }

        TextChannel channel = Main.getInstance().getGuild().getCategoryById("969281845495726101").createTextChannel(
                type.name() + "-" + event.getUser().getName()
        ).complete();

        Ticket ticket = Main.getInstance().addTicket(new Ticket(channel, event.getUser(), type, language));

        channel.createPermissionOverride(event.getMember()).setAllow(Permission.VIEW_CHANNEL).complete();

        Role english = Main.getInstance().getGuild().getRoleById("970148066378285066");
        Role french = Main.getInstance().getGuild().getRoleById("970147735439286353");

        for (Member membersWithRole : Main.getInstance().getGuild().getMembersWithRoles(Main.getInstance().getGuild().getRoleById(type.getRoleId()))) {
            if (ticket.isFrench() && membersWithRole.getRoles().contains(french)) {
                channel.createPermissionOverride(membersWithRole).setAllow(Permission.VIEW_CHANNEL).complete();
            }

            if (!ticket.isFrench() && membersWithRole.getRoles().contains(english)) {
                channel.createPermissionOverride(membersWithRole).setAllow(Permission.VIEW_CHANNEL).complete();
            }
        }

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setDescription("Ticket: " + channel.getAsMention());
        embedBuilder.setFooter("Developed par Rhodless#0001", "https://imgur.com/UsCsRqb.png");
        embedBuilder.setTimestamp(new Date().toInstant());


        event.replyEmbeds(embedBuilder.build()).setEphemeral(true).queue();

        this.initChannel(channel, event.getMember(), ticket);

        SelectionMenu.Builder section = SelectionMenu.create("ticket_manager");

        for (Ticket.TicketType value : Ticket.TicketType.values()) {
            section.addOption(value.getName(), value.name(), value.getEmoji());
        }

        event.editSelectionMenu(section.build()).queue();
    }

    public void initChannel(TextChannel channel, Member member, Ticket ticket) {
        EmbedBuilder builder = new EmbedBuilder();
        if (ticket.isFrench()) {
            builder.setDescription("Bienvenue sur votre ticket. Merci d'expliquer votre demande en **1 seul message**.");
            builder.setFooter("Développé par Rhodless#0001", "https://imgur.com/UsCsRqb.png");
        } else {
            builder.setDescription("Welcome to your ticket. Please explain your request in **1 message**.");
            builder.setFooter("Developed par Rhodless#0001", "https://imgur.com/UsCsRqb.png");
        }
        builder.setTimestamp(new Date().toInstant());

        channel.sendMessageEmbeds(builder.build()).queue();
        channel.sendMessage(member.getAsMention()).queue();
    }

}
