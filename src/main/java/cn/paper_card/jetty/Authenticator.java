package cn.paper_card.jetty;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.jetty.http.HttpStatus;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.jetbrains.annotations.NotNull;

class Authenticator extends AbstractHandler {

    private final @NotNull PaperJetty plugin;

    Authenticator(@NotNull PaperJetty plugin) {
        this.plugin = plugin;
    }

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) {

        final String token = request.getHeader("paper-token");
        final String correctToken = plugin.getConfigManager().getToken();

        final boolean authOk = correctToken.equals(token);
        if (authOk) {
            baseRequest.setHandled(false);
        } else {
            response.setStatus(HttpStatus.UNAUTHORIZED_401);
            baseRequest.setHandled(true);
        }
    }
}
