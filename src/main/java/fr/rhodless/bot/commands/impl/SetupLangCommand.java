package fr.rhodless.bot.commands.impl;

import fr.rhodless.bot.commands.Command;
import fr.rhodless.bot.ticket.Ticket;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.selections.SelectionMenu;
import net.dv8tion.jda.api.requests.restaction.MessageAction;

import java.awt.*;
import java.util.Date;

public class SetupLangCommand extends Command {
    @Override
    public void execute(SlashCommandEvent event) {

        if(!event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            event.reply("Vous n'avez pas la permission d'executer cette commande").setEphemeral(true).complete();
            return;
        }

        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("Choose my language");
        builder.setDescription("Choose your language from English and French to access the channels reserved for your language.");
        builder.setColor(new Color(47, 49, 54));
        builder.setTimestamp(new Date().toInstant());
        builder.setFooter("Developed by Rhodless#0001", "https://imgur.com/UsCsRqb.png");
        builder.setTimestamp(new Date().toInstant());

        MessageAction messageAction = event.getChannel().sendMessageEmbeds(builder.build());
        messageAction.setActionRow(
                Button.secondary("FRENCH", "\uD83C\uDDEB\uD83C\uDDF7 Français"),
                Button.secondary("ENGLISH", "\uD83C\uDDFA\uD83C\uDDF8 English")
        ).queue();

    }
}
