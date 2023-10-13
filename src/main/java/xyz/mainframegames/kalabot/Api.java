package xyz.mainframegames.kalabot;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import xyz.mainframegames.kalabot.audioplayer.PlayerManager;
import xyz.mainframegames.kalabot.commands.audio.StopCommand;
import xyz.mainframegames.kalabot.commands.audio.PauseCommand;
import xyz.mainframegames.kalabot.commands.audio.PlayCommand;
import xyz.mainframegames.kalabot.commands.audio.ResumeCommand;
import xyz.mainframegames.kalabot.commands.audio.SkipCommand;
import xyz.mainframegames.kalabot.commands.avatar.AvatarCommand;
import xyz.mainframegames.kalabot.commands.coordinate.CoordinateAdd;
import xyz.mainframegames.kalabot.commands.coordinate.CoordinateRemove;
import xyz.mainframegames.kalabot.commands.coordinate.CoordinateShow;
import xyz.mainframegames.kalabot.commands.help.HelpCommand;
import xyz.mainframegames.kalabot.commands.rate.RateCommand;
import xyz.mainframegames.kalabot.services.messages.MessagingService;

@PropertySource("classpath:local/application.properties")
@SpringBootApplication
public class Api {

  @Autowired MessagingService messagingService;

  @Value("${token}")
  public String token;

  public static void main(String[] args) {
    SpringApplication.run(Api.class, args);
  }

  @Bean
  public DiscordApi discordApi() {

    DiscordApi api =
        new DiscordApiBuilder().setToken(token).setAllIntents().login().join();
    PlayerManager.init();

    addMessageCreateListeners(api);
    return api;
  }

  private void addMessageCreateListeners(DiscordApi api) {
    api.addMessageCreateListener(new CoordinateShow(messagingService));
    api.addMessageCreateListener(new CoordinateRemove(messagingService));
    api.addMessageCreateListener(new CoordinateAdd(messagingService));
    api.addMessageCreateListener(new PlayCommand(messagingService));
    api.addMessageCreateListener(new StopCommand(messagingService));
    api.addMessageCreateListener(new SkipCommand(messagingService));
    api.addMessageCreateListener(new PauseCommand(messagingService));
    api.addMessageCreateListener(new ResumeCommand(messagingService));
    api.addMessageCreateListener(new RateCommand(messagingService));
    api.addMessageCreateListener(new AvatarCommand(messagingService));
    api.addMessageCreateListener(new HelpCommand(messagingService));
  }
}
