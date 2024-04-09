package cn.paper_card.jetty;

import com.github.Anon8281.universalScheduler.UniversalScheduler;
import com.github.Anon8281.universalScheduler.scheduling.schedulers.TaskScheduler;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.HandlerList;
import org.jetbrains.annotations.NotNull;

public final class PaperJetty extends JavaPlugin {

    private Server server = null;

    private final @NotNull ConfigManager configManager;
    private final @NotNull TaskScheduler taskScheduler;

    public PaperJetty() {
        this.configManager = new ConfigManager(this);
        this.taskScheduler = UniversalScheduler.getScheduler(this);
    }

    void addAuthenticator(@NotNull HandlerCollection c) {
        c.addHandler(new Authenticator(this));
    }


    void startJetty(@NotNull CommandSender sender) {
        if (this.server != null) return;

        final MessageSender ms = new MessageSender(sender);

        final int port = this.configManager.getPort();

        ms.info("Starting jetty server on port %d...".formatted(port));
        try {
            final Server ser = new Server(port);

            final HandlerList list = new HandlerList();
            this.addAuthenticator(list);
            ser.setHandler(list);

            ser.start();

            this.server = ser;
        } catch (Exception e) {
            final String msg = "Fail to start jetty server!";
            this.getSLF4JLogger().error(msg, e);

            ms.error(msg);
            ms.exception(e);
        }
    }

    void stopJetty(@NotNull CommandSender sender) {
        final Server s = this.server;
        this.server = null;

        if (s == null) return;

        final MessageSender ms = new MessageSender(sender);

        ms.info("Stopping jetty server...");
        try {
            s.stop();
        } catch (Exception e) {
            final String msg = "Fail to stop jetty server!";
            this.getSLF4JLogger().error(msg, e);
            ms.error(msg);
            ms.exception(e);
        }
    }

    public void onEnable() {
        new MainCommand(this).register();
        this.configManager.setDefaults();
        this.configManager.save();
        this.startJetty(this.getServer().getConsoleSender());
    }

    @Override
    public void onDisable() {
        this.stopJetty(this.getServer().getConsoleSender());

        this.taskScheduler.cancelTasks(this);
    }

    @NotNull ConfigManager getConfigManager() {
        return this.configManager;
    }

    @NotNull TaskScheduler getTaskScheduler() {
        return this.taskScheduler;
    }
}
