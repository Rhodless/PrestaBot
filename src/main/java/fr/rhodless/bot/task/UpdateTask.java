package fr.rhodless.bot.task;

import fr.rhodless.bot.Main;
import fr.rhodless.bot.ticket.Ticket;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;

import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;

public class UpdateTask extends TimerTask {

    int i = 0;

    @Override
    public void run() {
        i++;

        Guild guild = Main.getInstance().getGuild();

        for (Ticket ticket : Main.getInstance().getTickets()) {
            if (ticket.getClose() == -1L) continue;

            TextChannel channel = guild.getTextChannelById(ticket.getChannelId());

            if (channel == null) {
                Main.getInstance().deleteTicket(ticket);
                continue;
            }

            if (new Date(ticket.getClose() + 43200000).getTime() - System.currentTimeMillis() <= 0) {
                channel.delete().queue();
                Main.getInstance().deleteTicket(ticket);
            }
        }

        if (i % 120 == 0) {
            VoiceChannel channel = guild.getVoiceChannelById("969254106038403162");
            if (channel != null) {
                channel.getManager().setName("\uD83C\uDF88︱Membres: " + guild.getMembers().size()).queue();
            }
        }

        if(i % 60 == 0) {
            TextChannel textChannel = guild.getTextChannelById("969588167399321660");
            Message message = textChannel.retrieveMessageById("969590717972377644").complete();

            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setAuthor("Staff List | PrestaDev");
            for (Ticket.TicketType value : Ticket.TicketType.values()) {
                List<String> names = new ArrayList<>();
                Role role = guild.getRoleById(value.getRoleId());
                guild.getMembersWithRoles(role).forEach(member -> names.add(member.getEffectiveName()));

                StringBuilder display = new StringBuilder();

                names.forEach(s -> display.append(" » ").append(s).append(" \n"));
                embedBuilder.addField("**" + role.getName() + "** (" + names.size() + ")", display.toString(), true);
            }
            embedBuilder.setThumbnail("https://media.discordapp.net/attachments/911610505607659601/968968071932956753/telecharge.png");
            embedBuilder.setFooter("Developed par Rhodless#0001", "https://imgur.com/UsCsRqb.png");
            embedBuilder.setTimestamp(new Date().toInstant());

            message.editMessageEmbeds(embedBuilder.build()).queue();
        }
    }
}
