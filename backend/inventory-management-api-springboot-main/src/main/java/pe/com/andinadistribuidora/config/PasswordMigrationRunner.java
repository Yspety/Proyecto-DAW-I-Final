package pe.com.andinadistribuidora.config;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pe.com.andinadistribuidora.entity.Usuario;
import pe.com.andinadistribuidora.repository.UsuarioRepository;

/**
 * Migra contraseñas en texto plano a BCrypt en el primer inicio.
 * Se ejecuta una sola vez: las contraseñas que ya tengan hash ($2a$) se omiten.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PasswordMigrationRunner implements ApplicationRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        int migrados = 0;
        for (Usuario usuario : usuarioRepository.findAll()) {
            // $2a$, $2b$, $2y$ son los prefijos válidos de BCrypt
            boolean yaEsBcrypt = usuario.getPassword().startsWith("$2") && usuario.getPassword().length() == 60;
            if (!yaEsBcrypt) {
                usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
                usuarioRepository.save(usuario);
                migrados++;
                log.info("Contraseña migrada a BCrypt para usuario: {}", usuario.getUsuario());
            }
        }
        if (migrados > 0) {
            log.info("Migración completada: {} contraseña(s) codificada(s) con BCrypt.", migrados);
        }
    }
}
