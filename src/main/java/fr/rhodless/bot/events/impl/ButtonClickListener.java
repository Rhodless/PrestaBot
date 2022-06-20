package fr.rhodless.bot.events.impl;

import fr.rhodless.bot.Main;
import fr.rhodless.bot.ticket.Ticket;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyAction;

import java.util.Date;

public class ButtonClickListener {
    public ButtonClickListener(ButtonClickEvent event) {
        if (event.getButton() == null) return;

        Role english = Main.getInstance().getGuild().getRoleById("970148066378285066");
        Role french = Main.getInstance().getGuild().getRoleById("970147735439286353");
        Member member = event.getMember();

        if (english == null || french == null || member == null || event.getButton().getId() == null) return;

        if (event.getButton().getId().equals("FRENCH")) {
            if (member.getRoles().contains(french)) {
                Main.getInstance().getGuild().removeRoleFromMember(member, french).queue();
                event.reply(":x: Le rôle français vous a été retiré.").setEphemeral(true).queue();
            } else {
                Main.getInstance().getGuild().addRoleToMember(member, french).queue();
                event.reply(":white_check_mark: Le rôle français vous a été ajouté.").setEphemeral(true).queue();
            }

            if(!member.getRoles().contains(Main.getInstance().getGuild().getRoleById("968944865838759936"))) {
                Main.getInstance().getGuild().addRoleToMember(member, Main.getInstance().getGuild().getRoleById("968944865838759936")).queue();
            }
        }

        if (event.getButton().getId().equals("ENGLISH")) {
            if (member.getRoles().contains(english)) {
                Main.getInstance().getGuild().removeRoleFromMember(member, english).queue();
                event.reply(":x: You no longer have the english role").setEphemeral(true).queue();
            } else {
                Main.getInstance().getGuild().addRoleToMember(member, english).queue();
                event.reply(":white_check_mark: You now have the english role.").setEphemeral(true).queue();
            }

            if(!member.getRoles().contains(Main.getInstance().getGuild().getRoleById("968944865838759936"))) {
                Main.getInstance().getGuild().addRoleToMember(member, Main.getInstance().getGuild().getRoleById("968944865838759936")).queue();
            }
        }

        Ticket ticket = Main.getInstance().getTicket(event.getTextChannel().getId());

        if (ticket == null) return;

        if (event.getButton().getId().equals("CLAIM")) {
            if (ticket.getClaimedId() != null) {
                if (ticket.isFrench()) {
                    event.reply("Quelqu'un a déjà claim ce ticket...").setEphemeral(true).queue();
                } else {
                    event.reply("Someone has already claimed this ticket").setEphemeral(true).queue();
                }
                return;
            }
            if (ticket.getUserId().equals(event.getUser().getId())) {
                if (ticket.isFrench()) {
                    event.reply("Vous ne pouvez pas claim votre ticket...").setEphemeral(true).queue();
                } else {
                    event.reply("You can't claim your ticket...").setEphemeral(true).queue();
                }
                return;
            }

            ticket.setClaimedId(event.getUser().getId());
            Main.getInstance().updateTicket(ticket);
            ReplyAction replyAction;
            if (ticket.isFrench()) {
                replyAction = event.replyEmbeds(new EmbedBuilder().setDescription("Ce ticket a été **claim** par **" + event.getUser().getName() + "**.").build());
            } else {
                replyAction = event.replyEmbeds(new EmbedBuilder().setDescription("This ticket has been **claim** by **" + event.getUser().getName() + "**.").build());
            }

            replyAction.addActionRow(
                    Button.danger("UNCLAIM", "❌ Unclaim")
            ).complete();

            TextChannel textChannel = event.getTextChannel();
            for (Member m : textChannel.getMembers()) {
                if (m.getId().equals(event.getUser().getId())) continue;
                if (m.getId().equals(ticket.getUserId())) continue;
                if (m.hasPermission(Permission.ADMINISTRATOR)) continue;

                textChannel.getPermissionOverride(m).getManager().setDeny(Permission.VIEW_CHANNEL).queue();
            }
        }

        if (event.getButton().getId().equals("UNCLAIM")) {
            if (ticket.getClaimedId() == null) {
                event.reply("Ce ticket n'a pas été claim").setEphemeral(true).queue();
                return;
            }

            if (!ticket.getClaimedId().equalsIgnoreCase(member.getId()) && !member.hasPermission(Permission.ADMINISTRATOR)) {
                event.reply("Vous ne pouvez pas unclaim un ticket si vous ne l'avez pas claim").setEphemeral(true).queue();
                return;
            }

            ticket.setClaimedId(null);
            Main.getInstance().updateTicket(ticket);

            TextChannel channel = event.getTextChannel();

            for (Member membersWithRole : Main.getInstance().getGuild().getMembersWithRoles(Main.getInstance().getGuild().getRoleById(ticket.getTicketType().getRoleId()))) {
                if (ticket.isFrench() && membersWithRole.getRoles().contains(french)) {
                    if (channel.getPermissionOverride(membersWithRole) == null) {
                        channel.createPermissionOverride(membersWithRole).setAllow(Permission.VIEW_CHANNEL).complete();
                    } else {
                        channel.getPermissionOverride(membersWithRole).getManager().setAllow(Permission.VIEW_CHANNEL).queue();
                    }
                }

                if (!ticket.isFrench() && membersWithRole.getRoles().contains(english)) {
                    if (channel.getPermissionOverride(membersWithRole) == null) {
                        channel.createPermissionOverride(membersWithRole).setAllow(Permission.VIEW_CHANNEL).complete();
                    } else {
                        channel.getPermissionOverride(membersWithRole).getManager().setAllow(Permission.VIEW_CHANNEL).queue();
                    }
                }
            }

            ReplyAction message;

            if (ticket.isFrench()) {
                message = event.replyEmbeds(new EmbedBuilder().setDescription("Ce ticket a été unclaim.").build());
            } else {
                message = event.replyEmbeds(new EmbedBuilder().setDescription("This ticket has been unclaimed.").build());
            }

            message.addActionRow(
                    Button.success("CLAIM", "\uD83D\uDCCC Claim")
            ).queue();
        }

        if (event.getButton().getId().equals("DELETE")) {
            ticket.setClose(new Date().getTime());
            Main.getInstance().updateTicket(ticket);
            if (ticket.isFrench()) {
                event.replyEmbeds(new EmbedBuilder().setDescription(
                        "Fermeture du ticket dans 12h, pour annuler la fermeture utilisez **/cancel**"
                ).build()).queue();
            } else {
                event.replyEmbeds(new EmbedBuilder().setDescription(
                        "Closing the ticket in 12 hours, to cancel the closing use **/cancel**"
                ).build()).queue();
            }

            if (event.getTextChannel().getPermissionOverride(member) == null) {
                event.getTextChannel().createPermissionOverride(member).setDeny(Permission.MESSAGE_SEND).queue();
                return;
            }
            event.getTextChannel().getPermissionOverride(member).getManager().setDeny(Permission.MESSAGE_SEND).queue();
        }
    }
}
