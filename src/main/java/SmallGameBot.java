import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;

public class SmallGameBot {
    /**
     * Runs the main discord bot and its games
     * @param args The filepath to the file with the discord bot's token.
     */
    public static void main(String[] args) {
        // Get the bot token first
        String my_token = BotToken.get_token(args[0]);
        // Bail if we failed to open the file
        if (my_token == "")
            return;
        GatewayDiscordClient client = DiscordClientBuilder.create(my_token)
                .build()
                .login()
                .block();

        client.getEventDispatcher().on(ReadyEvent.class)
                .subscribe(event -> {
                    final User self = event.getSelf();
                    System.out.println(String.format(
                            "Logged in as %s#%s", self.getUsername(), self.getDiscriminator()
                    ));
                });

        client.getEventDispatcher().on(MessageCreateEvent.class)
                .map(MessageCreateEvent::getMessage)
                .filter(message -> message.getAuthor().map(user -> !user.isBot()).orElse(false))
                .filter(message -> message.getContent().equalsIgnoreCase("!ping"))
                .flatMap(Message::getChannel)
                .flatMap(channel -> channel.createMessage("Pong!"))
                .subscribe();

        client.onDisconnect().block();
    }
}
// Handles obtaining the bot's token. Don't want to share my token with rest of the world!
class BotToken {
    /**
     * Gets the token for the discord bot
     * @param filename The file name the bot's token is in
     * @return The bot's token or empty string if an exception was raised
     */
    public static String get_token(String filename){
        try{
            File my_file = new File(filename);
            Scanner file_scan = new Scanner(my_file);
            String token = file_scan.nextLine();
            return token;
        }
        catch (FileNotFoundException e){
            System.out.println("File Was not Found!");
            return "";
        }
    }
}