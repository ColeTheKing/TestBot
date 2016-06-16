package bot.commands;

import bot.DiscordBot;
import bot.game.GameBot;
import bot.game.TicTacToe;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

public class CommandGame extends Command{

    @Override
    void onCommand(DiscordBot bot, IMessage message, String[] args) throws RateLimitException, DiscordException, MissingPermissionsException{
        if(!(bot instanceof GameBot)){
            System.err.print("Non-game bot tried to play game with user. Shame!");
            return;
        }

        GameBot gameBot = (GameBot) bot;

        if(message.getChannel() != gameBot.getHome()){
            gameBot.respond("Please run this command in the '" + gameBot.getHome().getName() + "' channel if you wish to play a game with me. :slight_smile: ");
            return;
        }

        if(args.length == 0){
            gameBot.say("Which game would you like to play with me? Available games:\n" +
                    "1. Tic-tac-toe");
            gameBot.say("Just enter the number of the game you would like to play.");

            switch(gameBot.nextLine()){
                case "1":
                    gameBot.say("Okay! Here we go!");
                    gameBot.playGame(new TicTacToe(gameBot, 3));
            }

            gameBot.say("Thanks for playing!");
        }
    }
}