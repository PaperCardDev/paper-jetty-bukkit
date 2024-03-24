package cn.paper_card.jetty;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

class MessageSender {
    private final @NotNull CommandSender sender;

    MessageSender(@NotNull CommandSender sender) {
        this.sender = sender;
    }

    private void appendPrefix(@NotNull TextComponent.Builder text) {
        text.append(Component.text("[").color(NamedTextColor.GRAY));
        text.append(Component.text("Jetty").color(NamedTextColor.DARK_AQUA));
        text.append(Component.text("]").color(NamedTextColor.GRAY));
    }

    void info(@NotNull String info) {
        final TextComponent.Builder text = Component.text();
        this.appendPrefix(text);
        text.appendSpace();
        text.append(Component.text(info));
        sender.sendMessage(text.build().color(NamedTextColor.GREEN));
    }

    void error(@NotNull String error) {
        final TextComponent.Builder text = Component.text();
        this.appendPrefix(text);
        text.appendSpace();
        text.append(Component.text(error));
        sender.sendMessage(text.build().color(NamedTextColor.RED));
    }

    void exception(@NotNull Throwable e) {
        final TextComponent.Builder text = Component.text();
        this.appendPrefix(text);
        text.appendSpace();
        text.append(Component.text("==== 异常信息 ====").color(NamedTextColor.DARK_RED));

        for (Throwable t = e; t != null; t = t.getCause()) {
            text.appendNewline();
            text.append(Component.text(t.toString()).color(NamedTextColor.RED));
        }
    }
}
