package com.backend.controle_estoque.controller;

import com.backend.controle_estoque.api.BaseResponse;
import com.backend.controle_estoque.api.ResponseFactory;
import com.backend.controle_estoque.controller.docs.ProdutoControllerDoc;
import com.backend.controle_estoque.dto.ProdutoRequestDTO;
import com.backend.controle_estoque.dto.ProdutoResponseDTO;
import com.backend.controle_estoque.service.ProdutoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/produtos")
@RequiredArgsConstructor
public class ProdutoController implements ProdutoControllerDoc {

    private final ProdutoService service;

    @PostMapping
    public ResponseEntity<BaseResponse<ProdutoResponseDTO>> criar(
            @Valid @RequestBody ProdutoRequestDTO dto) {
        ProdutoResponseDTO response = service.criar(dto);
        return ResponseFactory.success(response);
    }

    @GetMapping
    @Override
    public ResponseEntity<BaseResponse<List<ProdutoResponseDTO>>> listar() {
        return ResponseFactory.success(service.listar());
    }

    @GetMapping("/{id}")
    @Override
    public ResponseEntity<BaseResponse<ProdutoResponseDTO>> buscarPorId(
            @PathVariable Long id) {
        return ResponseFactory.success(service.buscarPorId(id));
    }

    @PutMapping("/{id}")
    @Override
    public ResponseEntity<BaseResponse<ProdutoResponseDTO>> editar(
            @PathVariable Long id,
            @Valid @RequestBody ProdutoRequestDTO dto) {
        return ResponseFactory.success(service.editar(id, dto));
    }
}