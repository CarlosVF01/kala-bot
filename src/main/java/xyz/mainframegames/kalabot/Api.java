package xyz.mainframegames.kalabot;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import xyz.mainframegames.kalabot.audioplayer.PlayerManager;
import xyz.mainframegames.kalabot.commands.audio.LeaveCommand;
import xyz.mainframegames.kalabot.commands.audio.PauseCommand;
import xyz.mainframegames.kalabot.commands.audio.PlayCommand;
import xyz.mainframegames.kalabot.commands.audio.ResumeCommand;
import xyz.mainframegames.kalabot.commands.audio.SkipCommand;
import xyz.mainframegames.kalabot.commands.avatar.AvatarCommand;
import xyz.mainframegames.kalabot.commands.help.HelpCommand;
import xyz.mainframegames.kalabot.commands.rate.RateCommand;
import xyz.mainframegames.kalabot.services.messages.MessagingService;

@SpringBootApplication
public class Api {

  @Autowired
  Environment env;

  @Autowired
  MessagingService messagingService;

  public static void main(String[] args) {
    SpringApplication.run(Api.class, args);
  }

  @Bean
  @ConfigurationProperties
  public DiscordApi discordApi() {
    String token = env.getProperty("TOKEN");
    DiscordApi api =
        new DiscordApiBuilder().setToken(token).setAllNonPrivilegedIntents().login().join();

    PlayerManager.init();
    addMessageCreateListeners(api);
    return api;
  }

  private void addMessageCreateListeners(DiscordApi api) {
    api.addMessageCreateListener(new PlayCommand(messagingService));
    api.addMessageCreateListener(new LeaveCommand(messagingService));
    api.addMessageCreateListener(new SkipCommand(messagingService));
    api.addMessageCreateListener(new PauseCommand(messagingService));
    api.addMessageCreateListener(new ResumeCommand(messagingService));
    api.addMessageCreateListener(new RateCommand(messagingService));
    api.addMessageCreateListener(new AvatarCommand(messagingService));
    api.addMessageCreateListener(new HelpCommand(messagingService));
  }
}
