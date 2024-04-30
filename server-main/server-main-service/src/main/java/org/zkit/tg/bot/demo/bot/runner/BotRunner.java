package org.zkit.tg.bot.demo.bot.runner;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import org.zkit.tg.bot.demo.bot.MainBot;

@Component
@Slf4j
public class BotRunner implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        log.info("Hello, I'm a bot!");
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(new MainBot("7183921041:AAEQx8Z8GUblYrJGDfyjHK6zBMQ35fQwsnc"));
    }
}
