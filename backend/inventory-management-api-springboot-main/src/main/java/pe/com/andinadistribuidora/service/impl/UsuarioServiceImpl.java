package pe.com.andinadistribuidora.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pe.com.andinadistribuidora.api.request.UsuarioRequestDto;
import pe.com.andinadistribuidora.api.response.UsuarioResponseDto;
import pe.com.andinadistribuidora.entity.Rol;
import pe.com.andinadistribuidora.entity.Usuario;
import pe.com.andinadistribuidora.exception.BusinessException;
import pe.com.andinadistribuidora.exception.NotFoundException;
import pe.com.andinadistribuidora.mapper.UsuarioMapper;
import pe.com.andinadistribuidora.repository.RolRepository;
import pe.com.andinadistribuidora.repository.UsuarioRepository;
import pe.com.andinadistribuidora.service.UsuarioService;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepo;
    private final RolRepository rolRepo;
    private final UsuarioMapper mapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UsuarioResponseDto crear(UsuarioRequestDto req) {
        log.info("Creando usuario '{}'", req.getUsuario());

        if (usuarioRepo.findByUsuario(req.getUsuario()).isPresent()) {
            throw new BusinessException("Ya existe un usuario con el nombre: " + req.getUsuario());
        }

        if (req.getRolId() == null) {
            throw new BusinessException("Debe indicar un rol (rolId).");
        }
        Rol rol = rolRepo.findById(req.getRolId())
                .orElseThrow(() -> new NotFoundException("Rol no encontrado: " + req.getRolId()));

        Usuario entity = mapper.toEntity(req);
        entity.setRol(rol);
        entity.setCreadoEn(LocalDateTime.now());

        // Validar contraseña en texto plano antes de codificar
        validarPassword(req.getPassword());

        entity.setNombre(entity.getNombre().trim());
        entity.setApellido(entity.getApellido().trim());
        entity.setUsuario(entity.getUsuario().trim().toLowerCase());
        entity.setPassword(passwordEncoder.encode(req.getPassword()));

        validarDatosUsuario(entity);

        Usuario saved = usuarioRepo.save(entity);
        log.debug("Usuario creado id='{}'", saved.getId());
        return mapper.toResponseDto(saved);
    }

    @Override
    public UsuarioResponseDto actualizar(Integer idUsuario, UsuarioRequestDto req) {
        log.info("Actualizando usuario id='{}'", idUsuario);

        Usuario actual = usuarioRepo.findById(idUsuario)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado: " + idUsuario));

        usuarioRepo.findByUsuario(req.getUsuario()).ifPresent(u -> {
            if (!u.getId().equals(idUsuario)) {
                throw new BusinessException("El nombre de usuario ya está en uso: " + req.getUsuario());
            }
        });

        String hashActual = actual.getPassword();

        mapper.updateEntityFromDto(req, actual);

        // Codificar solo si se envió contraseña nueva
        if (req.getPassword() != null && !req.getPassword().isBlank()) {
            validarPassword(req.getPassword());
            actual.setPassword(passwordEncoder.encode(req.getPassword()));
        } else {
            actual.setPassword(hashActual);
        }

        if (req.getRolId() != null) {
            Rol nuevoRol = rolRepo.findById(req.getRolId())
                    .orElseThrow(() -> new NotFoundException("Rol no encontrado: " + req.getRolId()));
            actual.setRol(nuevoRol);
        }

        if (actual.getNombre() != null) actual.setNombre(actual.getNombre().trim());
        if (actual.getApellido() != null) actual.setApellido(actual.getApellido().trim());
        if (actual.getUsuario() != null) actual.setUsuario(actual.getUsuario().trim().toLowerCase());

        validarDatosUsuario(actual);

        Usuario saved = usuarioRepo.save(actual);
        return mapper.toResponseDto(saved);
    }

    @Override
    public void eliminar(Integer idUsuario) {
        log.info("Eliminando usuario id='{}'", idUsuario);
        Usuario u = usuarioRepo.findById(idUsuario)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado: " + idUsuario));
        usuarioRepo.delete(u);
        log.debug("Usuario eliminado id='{}'", idUsuario);
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioResponseDto obtener(Integer idUsuario) {
        Usuario u = usuarioRepo.findById(idUsuario)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado: " + idUsuario));
        return mapper.toResponseDto(u);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioResponseDto> listar() {
        return usuarioRepo.findAll().stream()
                .map(mapper::toResponseDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioResponseDto> listarPorRol(Integer idRol) {
        if (!rolRepo.existsById(idRol)) {
            throw new NotFoundException("Rol no encontrado: " + idRol);
        }
        return usuarioRepo.findByRolId(idRol)
                .stream()
                .map(mapper::toResponseDto)
                .toList();
    }

    private void validarPassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            throw new BusinessException("La contraseña es obligatoria.");
        }
        if (password.length() < 4) {
            throw new BusinessException("La contraseña debe tener al menos 4 caracteres.");
        }
    }

    private void validarDatosUsuario(Usuario usuario) {
        if (usuario.getNombre() == null || usuario.getNombre().trim().isEmpty()) {
            throw new BusinessException("El nombre es obligatorio.");
        }
        if (usuario.getApellido() == null || usuario.getApellido().trim().isEmpty()) {
            throw new BusinessException("El apellido es obligatorio.");
        }
        if (usuario.getUsuario() == null || usuario.getUsuario().trim().isEmpty()) {
            throw new BusinessException("El nombre de usuario es obligatorio.");
        }
        if (usuario.getActivo() == null) {
            throw new BusinessException("El estado activo es obligatorio.");
        }
    }
}
