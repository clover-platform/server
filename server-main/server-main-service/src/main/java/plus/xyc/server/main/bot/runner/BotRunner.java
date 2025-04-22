package plus.xyc.server.main.bot.runner;

import lombok.extern.slf4j.Slf4j;
import jakarta.annotation.Resource;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import plus.xyc.server.main.bot.configuration.BotConfiguration;

@Component
@Slf4j
public class BotRunner implements CommandLineRunner {

    @Resource
    private BotConfiguration configuration;

    @Override
    public void run(String... args) throws Exception {
        // log.info("start telegram bot!");
        // TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        // botsApi.registerBot(new MainBot(configuration.getToken()));
    }
}
