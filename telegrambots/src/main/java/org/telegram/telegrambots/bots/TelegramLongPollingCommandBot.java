package org.telegram.telegrambots.bots;


import org.telegram.telegrambots.bots.commands.BotCommand;
import org.telegram.telegrambots.bots.commands.CommandRegistry;
import org.telegram.telegrambots.bots.commands.ICommandRegistry;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;

import java.util.Collection;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * This class adds command functionality to the TelegramLongPollingBot
 *
 * @author Timo Schulz (Mit0x2)
 */
public abstract class TelegramLongPollingCommandBot extends TelegramLongPollingBot implements ICommandRegistry {
    private final CommandRegistry commandRegistry;

    /**
     * construct creates CommandRegistry for this bot.
     * Use ICommandRegistry's methods on this bot to register commands
     */
    public TelegramLongPollingCommandBot() {
        super();
        this.commandRegistry = new CommandRegistry();
    }

    @Override
    public final void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            if (message.isCommand()) {
                if (commandRegistry.executeCommand(this, message)) {
                    return;
                }
            }
        }
        processNonCommandUpdate(update);
    }

    @Override
    public final boolean register(BotCommand botCommand) {
        return commandRegistry.register(botCommand);
    }

    @Override
    public final Map<BotCommand, Boolean> registerAll(BotCommand... botCommands) {
        return commandRegistry.registerAll(botCommands);
    }

    @Override
    public final boolean deregister(BotCommand botCommand) {
        return commandRegistry.deregister(botCommand);
    }

    @Override
    public final Map<BotCommand, Boolean> deregisterAll(BotCommand... botCommands) {
        return commandRegistry.deregisterAll(botCommands);
    }

    @Override
    public final Collection<BotCommand> getRegisteredCommands() {
        return commandRegistry.getRegisteredCommands();
    }

    @Override
    public void registerDefaultAction(BiConsumer<AbsSender, Message> defaultConsumer) {
        commandRegistry.registerDefaultAction(defaultConsumer);
    }

    /**
     * Process all updates, that are not commands.
     * @warning Commands that have valid syntax but are not registered on this bot,
     * won't be forwarded to this method <b>if a default action is present</b>.
     *
     * @param update the update
     */
    public abstract void processNonCommandUpdate(Update update);
}
