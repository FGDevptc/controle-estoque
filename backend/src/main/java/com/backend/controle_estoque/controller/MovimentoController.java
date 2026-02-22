package com.backend.controle_estoque.controller;

import com.backend.controle_estoque.api.BaseResponse;
import com.backend.controle_estoque.api.ResponseFactory;
import com.backend.controle_estoque.controller.docs.MovimentoControllerDoc;
import com.backend.controle_estoque.dto.MovimentoRequestDTO;
import com.backend.controle_estoque.service.MovimentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/movimentos")
@RequiredArgsConstructor
public class MovimentoController implements MovimentoControllerDoc {

    private final MovimentoService service;

    @PostMapping
    @Override
    public ResponseEntity<BaseResponse<Void>> movimentar(
            @Valid @RequestBody MovimentoRequestDTO dto
    ) {
        service.movimentar(dto);
        return ResponseFactory.success(null);
    }
}