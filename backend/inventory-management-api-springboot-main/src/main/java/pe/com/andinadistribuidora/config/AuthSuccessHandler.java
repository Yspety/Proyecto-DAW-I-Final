package pe.com.andinadistribuidora.config;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class AuthSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        UsuarioPrincipal principal = (UsuarioPrincipal) authentication.getPrincipal();

        HttpSession session = request.getSession();
        session.setAttribute("nombreCompleto", principal.getNombreCompleto());
        session.setAttribute("rol",            principal.getRolDescripcion());

        response.sendRedirect(request.getContextPath() + "/home");
    }
}
