package simple.social.media.api.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import simple.social.media.api.domain.User.AuthenticationDTO;
import simple.social.media.api.domain.User.User;
import simple.social.media.api.domain.User.UserDTO;
import simple.social.media.api.domain.User.UserRepository;
import simple.social.media.api.infra.secutity.JwtService;
import simple.social.media.api.infra.secutity.TokenJwtDTO;

@RestController
public class AutenticacaoController {

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository repository;

    @PostMapping("/login")
    public ResponseEntity<TokenJwtDTO> Login(@RequestBody @Valid AuthenticationDTO data) {
        System.out.println(data);
        try {
            var token = new UsernamePasswordAuthenticationToken(data.name(), data.password());

            var authentication = manager.authenticate(token);

            var user = (User) authentication.getPrincipal();

            var tokenJwt = jwtService.generateToken(user);

            return ResponseEntity.ok(new TokenJwtDTO(tokenJwt));

        } catch (AuthenticationException e) {
            System.err.println("Authentication failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @PostMapping("/cadastrar")
    public ResponseEntity Register(@RequestBody @Valid AuthenticationDTO data, UriComponentsBuilder uriBuilder) {
        if (!repository.existsByName(data.name())) {

            var hash = bCryptPasswordEncoder.encode(data.password());
            var usuario = new User(data.name(), hash);

            repository.save(usuario);

            var uri = uriBuilder.path("/usuario/{id}").buildAndExpand(usuario.getId()).toUri();

            return ResponseEntity.created(uri).body(new UserDTO(usuario));
        } else {
            return ResponseEntity.badRequest().body("nome: " + data.name() + " ja cadastrado.");
        }
    }
}
