package xyz.mainframegames.kalabot;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import xyz.mainframegames.kalabot.listeners.AvatarListener;
import xyz.mainframegames.kalabot.listeners.RateListener;
import xyz.mainframegames.kalabot.listeners.TestListener;

@SpringBootApplication
public class Api {

	//This is used to safely start the bot using the hidden token
	@Autowired
	private Environment env;
	//Here are all the currently created listeners that the bot uses for the different commands
	@Autowired
	private TestListener testListener;
	@Autowired
	private RateListener rateListener;
	@Autowired
	private AvatarListener avatarListener;

	private DiscordApi api;
	public static void main(String[] args) {
		SpringApplication.run(Api.class, args);
	}

	@Bean
	@ConfigurationProperties
	public DiscordApi discordApi(){
		String token = env.getProperty("TOKEN");
		//Start the bot with the token
		DiscordApi api = new DiscordApiBuilder().setToken(token)
				.setAllNonPrivilegedIntents()
				.login()
				.join();

		//Add the different listeners required to work
		api.addMessageCreateListener(testListener);
		api.addMessageCreateListener(rateListener);
		api.addMessageCreateListener(avatarListener);
		this.api = api;
		return api;
	}

	public DiscordApi getApi() {
		return api;
	}
}
