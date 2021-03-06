package bot.feature.function;

import bot.DiscordBot;
import bot.feature.command.CommandSound;
import bot.feature.command.sound.Sound;
import sx.blah.discord.handle.obj.IVoiceChannel;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Timer;
import java.util.TimerTask;

public class FunctionHighNoon extends BotFunction{

    private static final String CHANNEL = "General";

    private boolean active;

    public FunctionHighNoon(String name){
        super(name);
    }

    public static long timeUntilNextNoon(){
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime next = now.withHour(12).withMinute(0).withSecond(0).withNano(0);
        if(now.getHour() >= 12){
            next = next.plusDays(1);
        }

        LocalDateTime difference = LocalDateTime.from(now);

        long days = difference.until(next, ChronoUnit.DAYS);
        difference = difference.plusDays(days);

        long hours = difference.until(next, ChronoUnit.HOURS);
        difference = difference.plusHours(hours);

        long minutes = difference.until(next, ChronoUnit.MINUTES);
        difference = difference.plusMinutes(minutes);

        long seconds = difference.until(next, ChronoUnit.SECONDS);

        return (days * 86400000) + (hours * 3600000L) + (minutes * 60000L) + (seconds * 1000L);
    }

    public boolean isActive(){
        return this.active;
    }

    private void checkTime(long delay){
        TimerTask task = new TimerTask(){
            @Override
            public void run(){
                if(!FunctionHighNoon.this.active){
                    this.cancel();
                    return;
                }

                DiscordBot bot = null;//FunctionHighNoon.this.bot;
                for(IVoiceChannel v : bot.getGuild().getVoiceChannels()){
                    if(v.getName().equals(CHANNEL)){
                        CommandSound.playSound(bot, v, Sound.HIGHNOON);
                        checkTime(timeUntilNextNoon());
                        return;
                    }
                }
            }
        };

        new Timer().schedule(task, delay);
    }

    @Override
    public void onRegister(){

    }

    @Override
    public void onEnable(DiscordBot bot){
        checkTime(timeUntilNextNoon());
    }
    
    @Override
    public void onDisable(DiscordBot bot) {}
}
