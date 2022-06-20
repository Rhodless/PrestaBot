package fr.rhodless.bot.commands;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public abstract class Command {

    public abstract void execute(SlashCommandEvent event);


}
