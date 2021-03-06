package bot.feature;

import bot.DiscordBot;
import bot.locale.Locale;
import org.apache.commons.lang3.Validate;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class BotFeature implements Comparable<BotFeature>{
    
    private static final ArrayList<BotFeature> features = new ArrayList<>();
    
    public String name;

    public BotFeature() {}

    public BotFeature(String name){
        this.name = name;
    }

    /**
     * Gets a list of all registered features of the specified type
     * @param type Class of the BotFeatures to return
     * @return A list containing all registered bot features of the specified type
     */
    @SuppressWarnings("unchecked")
    public static <T extends BotFeature> List<T> getRegisteredFeaturesOfType(Class<T> type){
        return features.stream()
                .filter(f -> type.isAssignableFrom(f.getClass()))
                .map(f -> (T) f)
                .collect(Collectors.toList());
    }
    
    /**
     * Gets a list containing all features registered via {@link BotFeature#registerFeature(BotFeature)}
     * @return A list containing all registered features
     */
    public static List<BotFeature> getAllRegisteredFeatures(){
        return features;
    }

    public static BotFeature getFeatureByName(String name){
        Optional<BotFeature> optional = features.stream().filter(f -> f.getRegisteredName().equals(name)).findFirst();
        return optional.isPresent() ? optional.get() : null;
    }
    
    /**
     * Registers this feature so that it can be enabled on a {@link DiscordBot}
     * @param feature Feature to register
     * @return The registered feature
     */
    public static BotFeature registerFeature(BotFeature feature){
        Validate.notNull(feature.name, "Can not register unnamed feature!");
        features.add(feature);
        System.out.println("Feature '" + feature.name + "' registered. Type: " + feature.getClass().getSuperclass().getSimpleName());
        feature.onRegister();
        return feature;
    }

    /**
     * Unregisters this feature. Disables the feature on any bot it is<br>
     * enabled on and removes it from the global features list.
     * @param feature Feature to unregister
     */
    public static void unregisterFeature(BotFeature feature){
        features.remove(feature);
        
        DiscordBot.getInstances().stream()
                .filter(b -> b.featureEnabled(feature))
                .forEach(b -> b.getFeatures().remove(feature));
    }
    
    /**
     * Gets the name of this feature in the specified locale
     * @return The localized name of this feature
     * @param locale Locale to localize the name to
     */
    public abstract String getName(Locale locale);

    /**
     * Gets the type of this command as a {@link String},<br>
     * to be used for displaying in places such as lists
     * @return The name of the type of this feature
     */
    public abstract String getTypeName();
    
    /**
     * Gets the description of this feature in the specified locale
     * @return The localized description of this feature
     * @param locale Locale to localize the description to
     */
    public abstract String getDescription(Locale locale);

    /**
     * Called when this feature is registered via {@link BotFeature#registerFeature(BotFeature)}
     */
    public abstract void onRegister();

    /**
     * Called when this feature is enabled on a bot via {@link DiscordBot#enableFeature(BotFeature)}
     * @param bot Bot this feature is being enabled on
     */
    public abstract void onEnable(DiscordBot bot);

    /**
     * Called when this feature is disabled on a bot either:<ul>
     *     <li>Programmatically</li>
     *     <li>Through a {@link FeatureSet}</li>
     *     <li>When disabled, if it is a {@link ToggleableBotFeature}</li>
     * </ul>
     * @param bot Bot this feature is being disabled on
     */
    public abstract void onDisable(DiscordBot bot);
    
    /**
     * Gets whether this feature is enabled by default
     * @return Whether this feature is enabled by default
     */
    public boolean defaultEnabled(){
        return true;
    }
    
    /**
     * Gets the name this feature was registered under
     * @return The name this feature was registered under
     */
    public String getRegisteredName(){
        return this.name;
    }
    
    /**
     * Gets whether this feature is currently registered
     * @return Whether this feature is registered
     */
    public boolean isRegistered(){
        return features.contains(this);
    }
    
    @Override
    public int compareTo(@Nonnull BotFeature other){
        if(other instanceof ToggleableBotFeature) return 1;
        else if(this instanceof ToggleableBotFeature) return -1;
        return this.name.compareTo(other.name);
    }
}
