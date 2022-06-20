package fr.rhodless.bot;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.rhodless.bot.commands.CommandManager;
import fr.rhodless.bot.events.UserEvents;
import fr.rhodless.bot.task.UpdateTask;
import fr.rhodless.bot.ticket.Ticket;
import lombok.Getter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.redisson.Redisson;
import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;

import java.util.*;

@Getter
public class Main {

    @Getter
    private static Main instance;

    private final Gson gson;
    private final JDA jda;
    private final Guild guild;

    private final RedissonClient redisson;
    private final RSet<String> ticketsGson;

    private boolean database;

    public Main() throws Exception {

        instance = this;

        this.gson = new GsonBuilder().create();
        this.jda = getJDABuilder().build().awaitReady();
        this.redisson = Redisson.create();

        this.ticketsGson = this.redisson.getSet("tickets");
        this.guild = jda.getGuildById("966350628949475448");
        this.setupCommands();

        new Timer().scheduleAtFixedRate(new UpdateTask(), 0, 1000);
    }

    private void setupCommands() {

        CommandData commandData = new CommandData("add", "Permet d'ajouter quelqu'un à un ticket").addOptions(
                new OptionData(OptionType.STRING, "id", "L'id du joueur")
        );
        guild.upsertCommand("setup", "Admin command").queue();
        guild.upsertCommand("cleardata", "Admin command").queue();
        guild.upsertCommand("cancel", "Allows you to cancel the closing of a ticket").queue();
        guild.upsertCommand("setuplang", "Admin command").queue();
        guild.upsertCommand(commandData).queue();

        new CommandManager(jda);

    }

    private JDABuilder getJDABuilder() {
        return JDABuilder.createDefault("OTY5MjI2ODg4ODQzMjU5OTA0.YmqU4Q.ZephIBGi0L5CxnCxqso5-G1oi1M")
                .setActivity(Activity.watching("vos tickets"))
                .setChunkingFilter(ChunkingFilter.ALL)
                .addEventListeners(new UserEvents())
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .enableIntents(GatewayIntent.GUILD_MEMBERS);
    }

    public Set<Ticket> getTickets() {
        Set<Ticket> tickets = new HashSet<>();
        for (String s : ticketsGson) {
            try {
                tickets.add(gson.fromJson(s, Ticket.class));
            } catch (Exception ignored) {
            }
        }

        return tickets;
    }

    public Ticket addTicket(Ticket ticket) {
        this.ticketsGson.add(gson.toJson(ticket));
        return ticket;
    }

    public void deleteTicket(Ticket ticket) {
        this.ticketsGson.removeIf(s -> gson.fromJson(s, Ticket.class).getTicketId().equals(ticket.getTicketId()));
    }

    public void updateTicket(Ticket ticket) {
        deleteTicket(ticket);
        addTicket(ticket);
    }

    public Ticket getTicket(String channelId) {
        return getTickets().stream().filter(ticket -> ticket.getChannelId().equalsIgnoreCase(channelId)).findFirst().orElse(null);
    }

}
