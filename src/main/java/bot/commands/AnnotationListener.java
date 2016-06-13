package bot.commands;

import bot.NewBot;
import bot.chatter.DefaultResponses;
import com.google.code.chatterbotapi.ChatterBotSession;
import sx.blah.discord.api.EventSubscriber;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.HTTP429Exception;
import sx.blah.discord.util.MessageBuilder;
import sx.blah.discord.util.MissingPermissionsException;

public class AnnotationListener
{

    ChatterBotSession session;

    public AnnotationListener(ChatterBotSession session)
    {
        this.session = session;
    }

    public AnnotationListener()
    {

    }

    @EventSubscriber
    public void onReady(ReadyEvent event) throws HTTP429Exception, DiscordException
    {
        NewBot.getClient().changeUsername("DAT BOT");
    }

    @EventSubscriber
    public void onMessageReceived(MessageReceivedEvent event) throws HTTP429Exception, DiscordException, MissingPermissionsException, Exception
    {
        MessageBuilder builder = new MessageBuilder(NewBot.getClient());

        if (event.getMessage().getChannel().getName().equals("bot"))
        {
            builder.withChannel(event.getMessage().getChannel());

            String message = event.getMessage().getContent();
            String clientName = event.getMessage().getAuthor().getName();

            String response = DefaultResponses.getResponseFor(message.replaceAll("[.!?']+", ""));

            if (response != null)
            {
                response = response.replaceAll("\\{SENDER\\}", clientName);
                builder.appendContent(response).build();
            }
                response = session.think(message);
                builder.appendContent(response).build();

        }
    }
}
