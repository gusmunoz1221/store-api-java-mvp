package com.store.auth.controller;

import com.store.auth.dto.AuthRequestDTO;
import com.store.auth.dto.AuthResponseDTO;
import com.store.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Gestion de seguridad (Login & Refresh Token)")
public class AuthController {
    private final AuthService authService;

    @Operation(summary = "Login de administrador",
            description = "Autentica a un usuario con rol ADMIN y retorna un access token y refresh token")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Autenticación exitosa",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponseDTO.class))),
            @ApiResponse(
                    responseCode = "401",
                    description = "Credenciales inválidas"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuario no encontrado")})
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody AuthRequestDTO request) {
        return ResponseEntity.ok(authService.login(request));
    }
    @PostMapping("/refresh-token")
    @Hidden
    public ResponseEntity<AuthResponseDTO> refreshToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        return ResponseEntity.ok(authService.refreshToken(authorization));
    }
}
