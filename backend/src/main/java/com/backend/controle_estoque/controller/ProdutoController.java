package com.backend.controle_estoque.controller;

import com.backend.controle_estoque.api.BaseResponse;
import com.backend.controle_estoque.api.PageBaseResponse;
import com.backend.controle_estoque.api.ResponseFactory;
import com.backend.controle_estoque.controller.docs.ProdutoControllerDoc;
import com.backend.controle_estoque.dto.ProdutoListagemResponseDTO;
import com.backend.controle_estoque.dto.ProdutoRequestDTO;
import com.backend.controle_estoque.dto.ProdutoResponseDTO;
import com.backend.controle_estoque.model.enums.TipoProdutoEnum;
import com.backend.controle_estoque.service.ProdutoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public ResponseEntity<BaseResponse<PageBaseResponse<ProdutoListagemResponseDTO>>> listar(
            @RequestParam(required = false) TipoProdutoEnum tipo,
            @ParameterObject Pageable pageable) {
        return ResponseFactory.success(service.listar(tipo, pageable));
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

    @DeleteMapping("/{id}")
    @Override
    public ResponseEntity<BaseResponse<Void>> deletar(@PathVariable Long id) {

        service.deletar(id);

        return ResponseFactory.success(null);
    }

}