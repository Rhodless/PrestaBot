package fr.rhodless.bot.commands.impl;

import fr.rhodless.bot.Main;
import fr.rhodless.bot.commands.Command;
import fr.rhodless.bot.ticket.Ticket;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public class CancelCommand extends Command {

    @Override
    public void execute(SlashCommandEvent event) {
        Ticket ticket = Main.getInstance().getTicket(event.getTextChannel().getId());

        if (ticket == null) return;

        ticket.setClose(-1L);

        Member member = Main.getInstance().getGuild().getMemberById(ticket.getUserId());

        if (member == null) return;

        event.replyEmbeds(new EmbedBuilder().setDescription("La fermeture de ce ticket a été annulée").build()).queue();

        event.getTextChannel().getPermissionOverride(member).getManager().setAllow(Permission.MESSAGE_SEND, Permission.VIEW_CHANNEL).queue();
    }
}
