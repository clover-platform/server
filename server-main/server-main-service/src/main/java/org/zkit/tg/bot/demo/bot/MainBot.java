package org.zkit.tg.bot.demo.bot;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
public class MainBot extends TelegramLongPollingBot {

    public MainBot(String botToken) {
        super(botToken);
    }

    @Override
    public String getBotUsername() {
        return "Test";
    }

    @Override
    public void onUpdateReceived(Update update) {
        log.info("{}", update.getMessage());
    }

}
