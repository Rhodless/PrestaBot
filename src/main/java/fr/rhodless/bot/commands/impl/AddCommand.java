package fr.rhodless.bot.commands.impl;

import fr.rhodless.bot.Main;
import fr.rhodless.bot.commands.Command;
import fr.rhodless.bot.ticket.Ticket;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public class AddCommand extends Command {
    @Override
    public void execute(SlashCommandEvent event) {

        try {
            Ticket ticket = Main.getInstance().getTicket(event.getTextChannel().getId());

            if (ticket == null) return;

            String id = event.getOptions().get(0).getAsString();
            Guild guild = Main.getInstance().getGuild();
            Member member = guild.getMemberById(id);

            if (member == null || member.hasAccess(event.getTextChannel())) {
                event.reply("Impossible d'ajouter ce joueur au ticket, peut-être qu'il a déjà accès ou qu'il n'existe pas ?").setEphemeral(true).queue();
                return;
            }

            event.getTextChannel().createPermissionOverride(member).setAllow(Permission.VIEW_CHANNEL).queue();
            event.replyEmbeds(new EmbedBuilder().setDescription(member.getEffectiveName() + " a été ajouté au ticket.").build()).queue();
        } catch (Exception ignored) {
            event.reply("Une erreur est survenue lors de l'execution de cette commande...").setEphemeral(true).queue();
        }
    }
}
