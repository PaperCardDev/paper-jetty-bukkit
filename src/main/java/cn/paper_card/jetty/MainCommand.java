package cn.paper_card.jetty;

import cn.paper_card.mc_command.TheMcCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

class MainCommand extends TheMcCommand.HasSub {

    private final @NotNull PaperJetty plugin;
    private final @NotNull Permission permission;

    public MainCommand(@NotNull PaperJetty plugin) {
        super("jetty");
        this.plugin = plugin;
        this.permission = Objects.requireNonNull(plugin.getServer().getPluginManager().getPermission("paper-jetty.command"));

        this.addSubCommand(new Reload());
        this.addSubCommand(new Restart());
    }

    void register() {
        final PluginCommand c = plugin.getCommand(this.getLabel());
        assert c != null;
        c.setTabCompleter(this);
        c.setExecutor(this);
    }

    @Override
    protected boolean canNotExecute(@NotNull CommandSender commandSender) {
        return !commandSender.hasPermission(this.permission);
    }

    private @NotNull Permission addPermission(@NotNull String name) {
        final Permission p = new Permission(name);
        plugin.getServer().getPluginManager().addPermission(p);
        return p;
    }


    class Reload extends TheMcCommand {

        private final @NotNull Permission permission;

        protected Reload() {
            super("reload");
            this.permission = addPermission(MainCommand.this.permission.getName() + "." + this.getLabel());
        }

        @Override
        protected boolean canNotExecute(@NotNull CommandSender commandSender) {
            return !commandSender.hasPermission(this.permission);
        }

        @Override
        public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

            plugin.getConfigManager().reload();

            // 发送消息
            new MessageSender(commandSender).info("已重载配置");

            return true;
        }

        @Override
        public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
            return null;
        }
    }

    class Restart extends TheMcCommand {

        private final @NotNull Permission permission;

        protected Restart() {
            super("restart");
            this.permission = addPermission(MainCommand.this.permission.getName() + "." + this.getLabel());
        }

        @Override
        protected boolean canNotExecute(@NotNull CommandSender commandSender) {
            return !commandSender.hasPermission(this.permission);
        }

        @Override
        public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
            plugin.getTaskScheduler().runTaskAsynchronously(() -> {
                plugin.stopJetty(commandSender);
                plugin.startJetty(commandSender);
            });
            return true;
        }

        @Override
        public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
            return null;
        }
    }
}
