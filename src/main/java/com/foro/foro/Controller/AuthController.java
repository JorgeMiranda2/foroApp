package com.foro.foro.Controller;


import com.foro.foro.Dtos.Input.LoginDto;
import com.foro.foro.Dtos.Input.RegisterDto;
import com.foro.foro.Dtos.Output.AccessResponseDto;
import com.foro.foro.Dtos.Output.RegisterResponseDto;
import com.foro.foro.Model.Profile;
import com.foro.foro.Model.User;
import com.foro.foro.Security.JwtAuthenticationProvider;
import com.foro.foro.Security.SecurityConstants;
import com.foro.foro.Service.ProfileService;
import com.foro.foro.Service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final ProfileService profileService;
    private  final PasswordEncoder passwordEncoder;
    private  final JwtAuthenticationProvider jwtAuthenticationProvider;

    private static long cookieTime = SecurityConstants.JWT_EXPIRATION_TOKEN;


    @Autowired
    public AuthController(AuthenticationManager authenticationManager, UserService userService, ProfileService profileService, PasswordEncoder passwordEncoder, JwtAuthenticationProvider jwtAuthenticationProvider) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.profileService = profileService;
        this.passwordEncoder = passwordEncoder;
        this.jwtAuthenticationProvider = jwtAuthenticationProvider;
    }


    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid RegisterDto registerDto, UriComponentsBuilder uriComponentsBuilder) {
            Profile profile = profileService.findProfileById(1L).orElseThrow(() -> new IllegalArgumentException("Profile not found"));

            System.out.println("perfil: " + profile.getName() + "pass: " + profile.getId());
            User user = new User(registerDto.name(), registerDto.email(), passwordEncoder.encode(registerDto.password()), profile);
            user = userService.saveUser(user);

            URI uri = uriComponentsBuilder.path("/api/user/{id}").buildAndExpand(user.getId()).toUri();
            RegisterResponseDto response = new RegisterResponseDto(user.getId(), user.getName(), user.getEmail());
            return ResponseEntity.created(uri).body(response);

    }


    @PostMapping("/login")
    public ResponseEntity<AccessResponseDto> signIn(@RequestBody LoginDto loginDto, HttpServletResponse response) {


        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getIdentifier(), loginDto.getPassword()
        ));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtAuthenticationProvider.createToken(authentication);

        String username = null;

        System.out.println("Autoridades: " + SecurityContextHolder.getContext().getAuthentication().getAuthorities());

        try {
            User user = userService.findByName(loginDto.getIdentifier()).orElseThrow();
            username = user.getUsername();
            // Crear la cookie para el token
            ResponseCookie tokenCookie = ResponseCookie.from("token", token)
                    .httpOnly(true)
                    .secure(true) // Asegúrate de usar HTTPS en producción
                    .path("/")
                    .maxAge(cookieTime) // Duración de la cookie: 7 días
                    .sameSite("Strict")
                    .build();

            // Crear la cookie para el nombre de usuario
            ResponseCookie usernameCookie = ResponseCookie.from("username", username)
                    .path("/")
                    .maxAge(cookieTime)
                    .sameSite("Strict")
                    .build();



            // Añadir las cookies a la respuesta
            response.addHeader(HttpHeaders.SET_COOKIE, tokenCookie.toString());
            response.addHeader(HttpHeaders.SET_COOKIE, usernameCookie.toString());


        } catch (Exception e) {
            System.out.println("identifier not found: " + e);
        }

        return ResponseEntity.status(HttpStatus.OK).body(new AccessResponseDto(token, username));


    }

}
