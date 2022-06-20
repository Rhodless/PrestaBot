package fr.rhodless.bot.events.impl;

import fr.rhodless.bot.Main;
import fr.rhodless.bot.ticket.Ticket;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.requests.restaction.MessageAction;

import java.util.Date;
import java.util.List;

public class MessageReceivedListener {
    public MessageReceivedListener(MessageReceivedEvent event) {

        try {
            event.getTextChannel();
        } catch (IllegalStateException ignored) {
            return;
        }

        if (event.getMember().getUser().isBot()) return;

        Ticket ticket = Main.getInstance().getTicket(event.getTextChannel().getId());

        if (ticket == null) return;

        if (ticket.getExplication() == null) {
            ticket.setExplication(event.getMessage().getContentDisplay());
            Main.getInstance().updateTicket(ticket);

            List<Message> messages = event.getChannel().getHistory().retrievePast(100).complete();
            ((TextChannel) event.getChannel()).deleteMessages(messages).queue();

            EmbedBuilder welcome = new EmbedBuilder();
            EmbedBuilder explication = new EmbedBuilder();
            if(ticket.isFrench()) {
                welcome.setDescription("Bienvenue sur votre ticket **" + Main.getInstance().getGuild().getMemberById(ticket.getUserId()).getEffectiveName() + "**. Un membre de l'équipe va se charger de vous. Si vous n'avez pas de réponses en **24h**, mentionnez un membre de l'équipe.");
                welcome.addField("**Catégorie**", ticket.getTicketType().getName(), true);
                welcome.addField("**Ticket ID**", ticket.getTicketId().toString().substring(0, 13), true);
                welcome.addField("**Channel ID**", ticket.getChannelId(), true);

                explication.setDescription("**Explication:** " + ticket.getExplication());
                explication.setFooter("Développé par Rhodless#0001", "https://imgur.com/UsCsRqb.png");
                explication.setTimestamp(new Date().toInstant());
            } else {
                welcome.setDescription("Welcome to your ticket **" + Main.getInstance().getGuild().getMemberById(ticket.getUserId()).getEffectiveName() + "**. A team member will take care of you. If you don't hear back within **24 hours**, mention a team member.");
                welcome.addField("**Category**", ticket.getTicketType().getName(), true);
                welcome.addField("**Ticket ID**", ticket.getTicketId().toString().substring(0, 13), true);
                welcome.addField("**Channel ID**", ticket.getChannelId(), true);

                explication.setDescription("**Explanation:** " + ticket.getExplication());
                explication.setFooter("Developed par Rhodless#0001", "https://imgur.com/UsCsRqb.png");
                explication.setTimestamp(new Date().toInstant());
            }

            event.getTextChannel().sendMessage(Main.getInstance().getGuild().getRoleById(ticket.getTicketType().getRoleId()).getAsMention()).queue();
            MessageAction message = event.getTextChannel().sendMessageEmbeds(welcome.build(), explication.build());
            message.setActionRow(
                    Button.success("CLAIM", "\uD83D\uDCCC Claim"),
                    Button.danger("DELETE", "\uD83D\uDD12 Close")
            ).queue();
        }
    }
}
