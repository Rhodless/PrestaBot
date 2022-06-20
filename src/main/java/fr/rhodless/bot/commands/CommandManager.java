package fr.rhodless.bot.commands;

import fr.rhodless.bot.commands.impl.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class CommandManager extends ListenerAdapter {

    HashMap<String, Command> commands = new HashMap<>();

    public CommandManager(JDA jda) {
        jda.addEventListener(this);

        commands.put("setup", new SetupCommand());
        commands.put("setuplang", new SetupLangCommand());
        commands.put("cleardata", new ClearDataCommand());
        commands.put("cancel", new CancelCommand());
        commands.put("add", new AddCommand());
    }

    @Override
    public void onSlashCommand(@NotNull SlashCommandEvent event) {

        for (String cmd : commands.keySet()) {
            if (event.getName().equalsIgnoreCase(cmd)) {
                commands.get(cmd).execute(event);
            }
        }

    }

}
