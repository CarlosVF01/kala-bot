package xyz.mainframegames.kalabot;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import xyz.mainframegames.kalabot.audioplayer.AudioPlayerListener;
import xyz.mainframegames.kalabot.commands.avatar.AvatarCommand;
import xyz.mainframegames.kalabot.commands.rate.RateCommand;
import xyz.mainframegames.kalabot.services.messages.MessagingService;

@SpringBootApplication
public class Api {

  @Autowired Environment env;

  @Autowired AudioPlayerListener audioPlayerListener;

  @Autowired MessagingService messagingService;

  private DiscordApi discordApi;

  public static void main(String[] args) {
    SpringApplication.run(Api.class, args);
  }

  @Bean
  @ConfigurationProperties
  public DiscordApi discordApi() {
    String token = env.getProperty("TOKEN");
    DiscordApi api =
        new DiscordApiBuilder().setToken(token).setAllNonPrivilegedIntents().login().join();

    api.addMessageCreateListener(audioPlayerListener);
    api.addMessageCreateListener(new RateCommand(messagingService));
    api.addMessageCreateListener(new AvatarCommand(messagingService));
    this.discordApi = api;
    return api;
  }

  public DiscordApi getDiscordApi() {
    return discordApi;
  }
}
