package com.example.BancoDeDados.Controller;

import com.example.BancoDeDados.Model.Professor;
import com.example.BancoDeDados.Repositores.ProfessorRepositores;
import com.example.BancoDeDados.ResponseDTO.AuthResponseDTO;
import com.example.BancoDeDados.ResponseDTO.ProfessorLoginResponseDTO;
import com.example.BancoDeDados.ResponseDTO.ProfessorRegistrarDTO;
import com.example.BancoDeDados.Security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/login/professor")
public class ControllerProfessorLogin {
    @Autowired
    private TokenService tokenService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private ProfessorRepositores professorRepositores;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid ProfessorLoginResponseDTO professorLoginResponseDTO) {
        Professor professor = new Professor(professorLoginResponseDTO);
        var usuarioSenha = new UsernamePasswordAuthenticationToken(professorLoginResponseDTO.email(), professorLoginResponseDTO.senha());
        var auth=this.authenticationManager.authenticate(usuarioSenha);
        var token =tokenService.gerarToken((Professor) auth.getPrincipal());
        try {
            var autorizar = this.authenticationManager.authenticate(usuarioSenha);
            // Aqui você pode gerar um token JWT ou uma resposta personalizada
            return ResponseEntity.ok(new AuthResponseDTO(token));
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Credenciais inválidas");
        }

}
    @PostMapping("/registrar")
    public ResponseEntity registrar(@RequestBody @Valid ProfessorRegistrarDTO professorRegistrarDTO) {
        if (this.professorRepositores.findByEmail(professorRegistrarDTO.email()).isPresent()) {
            return ResponseEntity.badRequest().body("E-mail já está em uso");
        }

        String encriptarSenha = new BCryptPasswordEncoder().encode(professorRegistrarDTO.senha());
        Professor professor = new Professor(professorRegistrarDTO.email(),encriptarSenha,professorRegistrarDTO.role());

        this.professorRepositores.save(professor);

        return ResponseEntity.ok("Registro realizado com sucesso");
    }


}