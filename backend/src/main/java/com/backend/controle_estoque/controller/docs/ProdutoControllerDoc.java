package com.backend.controle_estoque.controller.docs;

import com.backend.controle_estoque.api.BaseResponse;
import com.backend.controle_estoque.dto.ProdutoRequestDTO;
import com.backend.controle_estoque.dto.ProdutoResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.List;

import org.springframework.http.ResponseEntity;

public interface ProdutoControllerDoc {

        @Operation(summary = "Criar novo produto")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Produto criado com sucesso"),
                        @ApiResponse(responseCode = "400", description = "Erro de validação"),
                        @ApiResponse(responseCode = "500", description = "Erro interno")
        })
        ResponseEntity<BaseResponse<ProdutoResponseDTO>> criar(
                        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Dados do produto", required = true) ProdutoRequestDTO dto);

        @Operation(summary = "Listar todos os produtos")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
                        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
        })
        ResponseEntity<BaseResponse<List<ProdutoResponseDTO>>> listar();

        @Operation(summary = "Buscar produto por ID")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Produto encontrado"),
                        @ApiResponse(responseCode = "404", description = "Produto não encontrado"),
                        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
        })
        ResponseEntity<BaseResponse<ProdutoResponseDTO>> buscarPorId(
                        @Parameter(description = "ID do produto", example = "1") Long id);
}