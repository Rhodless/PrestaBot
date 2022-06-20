package fr.rhodless.bot.commands.impl;

import fr.rhodless.bot.commands.Command;
import fr.rhodless.bot.ticket.Ticket;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.components.selections.SelectionMenu;
import net.dv8tion.jda.api.requests.restaction.MessageAction;

import java.awt.*;
import java.util.Date;

public class SetupCommand extends Command {
    @Override
    public void execute(SlashCommandEvent event) {

        if(!event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            event.reply("Vous n'avez pas la permission d'executer cette commande").setEphemeral(true).complete();
            return;
        }

        if(event.getTextChannel().getId().equalsIgnoreCase("970151624330838036")) {
            EmbedBuilder builder = new EmbedBuilder();
            builder.setTitle("Create a ticket");
            builder.setDescription("Need help? Click on the reaction below to create a ticket to get help from one of our moderators.");
            builder.setColor(new Color(47, 49, 54));
            builder.setTimestamp(new Date().toInstant());
            builder.setFooter("Developed by Rhodless#0001", "https://imgur.com/UsCsRqb.png");
            builder.setTimestamp(new Date().toInstant());

            MessageAction messageAction = event.getChannel().sendMessageEmbeds(builder.build());
            SelectionMenu.Builder section = SelectionMenu.create("ticket_manager");

            for (Ticket.TicketType value : Ticket.TicketType.values()) {
                section.addOption(value.getName(), value.name(), value.getEmoji());
            }

            messageAction.setActionRow(section.build()).queue();
        }

        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("Créer un ticket");
        builder.setDescription("Vous avez besoin d'aide ? Cliquez sur la réaction ci-dessous pour créer un ticket afin d'obtenir de l'aide d'un de nos modérateurs.");
        builder.setColor(new Color(47, 49, 54));
        builder.setTimestamp(new Date().toInstant());
        builder.setFooter("Développé par Rhodless#0001", "https://imgur.com/UsCsRqb.png");
        builder.setTimestamp(new Date().toInstant());

        MessageAction messageAction = event.getChannel().sendMessageEmbeds(builder.build());
        SelectionMenu.Builder section = SelectionMenu.create("ticket_manager");

        for (Ticket.TicketType value : Ticket.TicketType.values()) {
            section.addOption(value.getName(), value.name(), value.getEmoji());
        }

        messageAction.setActionRow(section.build()).queue();
    }
}
