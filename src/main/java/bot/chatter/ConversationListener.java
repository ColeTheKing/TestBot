package bot.chatter;

import bot.NewBot;
import bot.behavior.BotAttitude;
import bot.behavior.Mood;
import sx.blah.discord.api.EventSubscriber;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.HTTP429Exception;
import sx.blah.discord.util.MessageBuilder;
import sx.blah.discord.util.MissingPermissionsException;

public class ConversationListener{

    @EventSubscriber
    public void onReady(ReadyEvent event) throws HTTP429Exception, DiscordException{
        NewBot.getClient().changeUsername("Botty McBotface");
    }

    @EventSubscriber
    public void onMessageReceived(MessageReceivedEvent event) throws HTTP429Exception, DiscordException, MissingPermissionsException{
        IMessage imessage = event.getMessage();

        if(!imessage.getContent().startsWith("!")){
            BotAttitude attitude = BotAttitude.getBotFor(imessage.getAuthor().getID());
            if(attitude == null){
                attitude = new BotAttitude(imessage.getAuthor().getID());
                attitude.setDefaultMood(Mood.HAPPY);//Debug
            }

            MessageBuilder builder = new MessageBuilder(NewBot.getClient());

            if(event.getMessage().getChannel().getName().equals("bot")){
                builder.withChannel(event.getMessage().getChannel());

                String message = event.getMessage().getContent();
                String clientName = event.getMessage().getAuthor().getName();

                String response = attitude.talkTo(message);


                if(response != null){
                    response = response.replaceAll("\\{SENDER\\}", clientName);
                    builder.appendContent(response).build();
                }
            }
        }
    }
}