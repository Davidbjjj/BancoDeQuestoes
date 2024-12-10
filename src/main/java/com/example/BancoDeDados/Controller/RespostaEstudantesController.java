package com.example.BancoDeDados.Controller;

import com.example.BancoDeDados.ResponseDTO.EnviarRespostaDTO;
import com.example.BancoDeDados.ResponseDTO.RespostaEstudanteDTO;
import com.example.BancoDeDados.Services.RespostaEstudantesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class RespostaEstudantesController {

    private final RespostaEstudantesService respostaEstudantesService;

    public RespostaEstudantesController(RespostaEstudantesService respostaEstudantesService) {
        this.respostaEstudantesService = respostaEstudantesService;
    }

    @GetMapping("/respostas/buscar")
    public RespostaEstudanteDTO buscarRespostaPorQuestaoEEstudante(
            @RequestParam Long questaoId,
            @RequestParam Long estudanteId) {
        return respostaEstudantesService.buscarRespostaPorQuestaoEEstudante(questaoId, estudanteId);
    }
    @PostMapping("/enviaresposta")
    public ResponseEntity<String> enviarResposta(@RequestBody EnviarRespostaDTO enviarRespostaDTO) {
        respostaEstudantesService.salvarResposta(enviarRespostaDTO);
        return ResponseEntity.ok("Resposta enviada com sucesso.");
    }
}