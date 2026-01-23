package com.store.auth.service;

import com.store.auth.dto.AuthRequestDTO;
import com.store.auth.dto.AuthResponseDTO;
import com.store.exception.InvalidTokenException;
import com.store.security.jwt.JwtService;
import com.store.user.entity.UserEntity;
import com.store.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    /**
     * Registra un nuevo usuario en el sistema.
     * <p>Este método se encuentra deshabilitado en la versión pública del proyecto.
     * En un escenario real, la asignación de roles (ADMIN / USER) se maneja
     * mediante un proceso controlado y no expuesto directamente.
     * <p>Responsabilidades previstas:
     * <ul>
     *   <li>Validar que el email no esté previamente registrado.</li>
     *   <li>Persistir el usuario con contraseña encriptada.</li>
     *   <li>Asignar un rol inicial.</li>
     *   <li>Generar un token JWT de acceso.</li>
     * </ul>
    public AuthResponseDTO register(RegisterRequestDTO request) {

     if (userRepository.findByEmail(request.getEmail()).isPresent())
     throw new UsernameNotFoundException("El email ya está registrado");

        var user = UserEntity.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ADMIN)
                .build();

        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthResponseDTO.builder().token(jwtToken).build();
    }*/


    /**
     * Autentica un usuario y genera tokens JWT de acceso y refresco.
     * <p>El proceso incluye:
     * <ul>
     *   <li>Autenticación mediante {@link AuthenticationManager}.</li>
     *   <li>Generación de un access token con claims personalizados.</li>
     *   <li>Generación de un refresh token.</li>
     * </ul>
     * @param request DTO con las credenciales del usuario
     * @return DTO con access token y refresh token
     * @throws UsernameNotFoundException si el usuario no existe
     * @throws AuthenticationException si las credenciales son inválidas
     */
    public AuthResponseDTO login(AuthRequestDTO request) {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

            var user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado sistema"));

            Map<String, Object> claims = buildAccessTokenClaims(user);

            var jwtToken = jwtService.generateToken(claims,user);
            var refreshToken = jwtService.generateRefreshToken(user);

            log.info("Login exitoso para usuario: {}", request.getEmail());

            return AuthResponseDTO.builder()
                    .accessToken(jwtToken)
                    .refreshToken(refreshToken)
                    .build();
    }

    /**
     * Genera un nuevo access token a partir de un refresh token válido.
     * <p>El refresh token se extrae desde el header {@code Authorization}
     * con el esquema {@code Bearer}.
     * <p>Flujo de validación:
     * <ul>
     *   <li>Extracción del refresh token.</li>
     *   <li>Obtención del usuario desde el token.</li>
     *   <li>Validación criptográfica y de expiración.</li>
     *   <li>Emisión de un nuevo access token.</li>
     * </ul>
     * @param authorizationHeader header Authorization con el refresh token
     * @return DTO con el nuevo access token y el refresh token original
     * @throws InvalidTokenException si el token es inválido o expirado
     * @throws UsernameNotFoundException si el usuario no existe
     */
    public AuthResponseDTO refreshToken(String authorizationHeader) {
        /*versión Pro*/
        return new AuthResponseDTO("","");
    }
                    /*-------------METODOS PRIVADOS-----------------*/
    /**
     * Extrae el token JWT desde un header Authorization con esquema Bearer.
     * @param authorizationHeader valor del header Authorization
     * @return token JWT sin el prefijo Bearer
     * @throws InvalidTokenException si el header es inválido
     */
    private String extractBearerToken(String authorizationHeader) {/*versión Pro*/return "";}

    /**
     * Construye los claims personalizados incluidos en el access token.
     * @param user usuario autenticado
     * @return mapa de claims para el token JWT
     */
    private Map<String, Object> buildAccessTokenClaims(UserEntity user) {
        return Map.of(
                "role", user.getRole().name(),
                "name", user.getFirstname()
        );
    }
}