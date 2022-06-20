package fr.rhodless.bot.commands.impl;

import fr.rhodless.bot.Main;
import fr.rhodless.bot.commands.Command;
import fr.rhodless.bot.ticket.Ticket;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.components.selections.SelectionMenu;
import net.dv8tion.jda.api.requests.restaction.MessageAction;

import java.awt.*;
import java.util.Date;

public class ClearDataCommand extends Command {
    @Override
    public void execute(SlashCommandEvent event) {

        if(!event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            event.reply("Vous n'avez pas la permission d'executer cette commande").setEphemeral(true).complete();
            return;
        }

        Main.getInstance().getTicketsGson().clear();
        event.reply("Toutes les données ont été supprimés").setEphemeral(true).queue();
    }
}
