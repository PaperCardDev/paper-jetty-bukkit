package cn.paper_card.jetty;

import org.jetbrains.annotations.NotNull;

class ConfigManager {
    private final @NotNull String path_port = "port";
    private final @NotNull String path_token = "token";

    private final @NotNull PaperJetty plugin;

    ConfigManager(@NotNull PaperJetty plugin) {
        this.plugin = plugin;
    }

    int getPort() {
        return this.plugin.getConfig().getInt(path_port, 7878);
    }

    void setPort(int v) {
        this.plugin.getConfig().set(path_port, v);
    }

    @NotNull String getToken() {
        return this.plugin.getConfig().getString(path_token, "paper-card");
    }

    void setToken(@NotNull String token) {
        this.plugin.getConfig().set(path_token, token);
    }

    void setDefaults() {
        this.setToken(this.getToken());
        this.setPort(this.getPort());
    }

    void reload() {
        this.plugin.reloadConfig();
    }

    void save() {
        this.plugin.saveConfig();
    }
}
