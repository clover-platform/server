package plus.xyc.server.main.bot.runner;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import plus.xyc.server.main.bot.MainBot;
import plus.xyc.server.main.bot.configuration.BotConfiguration;

@Component
@Slf4j
public class BotRunner implements CommandLineRunner {

    private BotConfiguration configuration;

    @Override
    public void run(String... args) throws Exception {
        // log.info("start telegram bot!");
        // TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        // botsApi.registerBot(new MainBot(configuration.getToken()));
    }


    @Autowired
    public void setConfiguration(BotConfiguration configuration) {
        this.configuration = configuration;
    }
}
