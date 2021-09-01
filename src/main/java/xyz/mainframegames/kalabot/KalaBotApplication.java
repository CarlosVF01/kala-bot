package xyz.mainframegames.kalabot;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import xyz.mainframegames.kalabot.listeners.RateListener;
import xyz.mainframegames.kalabot.listeners.TestListener;

@SpringBootApplication
public class KalaBotApplication {

	//This is used to safely start the bot using the hidden token
	@Autowired
	private Environment env;

	//Here are all the currently created listeners that the bot use for the different commands
	@Autowired
	private TestListener messageListener;
	@Autowired
	private RateListener rateListener;

	public static void main(String[] args) {
		SpringApplication.run(KalaBotApplication.class, args);
	}


	@Bean
	@ConfigurationProperties
	public DiscordApi discordApi(){
		String str = "Hey this is Ram";
		String [] words = str. split(" ", 4);
		for (String word : words)
			System. out. println(word);

		String token = env.getProperty("TOKEN");
		//Start the bot with the token
		DiscordApi api = new DiscordApiBuilder().setToken(token)
				.setAllNonPrivilegedIntents()
				.login()
				.join();

		//Add the different listeners required to work
		api.addMessageCreateListener(messageListener);
		api.addMessageCreateListener(rateListener);
		return api;
	}

}
