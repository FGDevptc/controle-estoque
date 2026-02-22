package com.backend.controle_estoque.controller.docs;

import com.backend.controle_estoque.api.BaseResponse;
import com.backend.controle_estoque.dto.LucroProdutoResponseDTO;
import com.backend.controle_estoque.dto.MovimentoRequestDTO;
import com.backend.controle_estoque.dto.MovimentoResponseDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.List;

import org.springframework.http.ResponseEntity;

public interface MovimentoControllerDoc {

        @Operation(summary = "Realizar movimentação de estoque")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Movimentação realizada com sucesso"),
                        @ApiResponse(responseCode = "400", description = "Erro de regra de negócio"),
                        @ApiResponse(responseCode = "404", description = "Produto não encontrado")
        })
        ResponseEntity<BaseResponse<Void>> movimentar(MovimentoRequestDTO dto);

        @Operation(summary = "Listar movimentações por produto")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Movimentações retornadas com sucesso"),
                        @ApiResponse(responseCode = "404", description = "Produto não encontrado")
        })
        ResponseEntity<BaseResponse<List<MovimentoResponseDTO>>> listarPorProduto(
                        @Parameter(description = "ID do produto", example = "1") Long produtoId);

        @Operation(summary = "Calcular lucro total de um produto")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Lucro calculado com sucesso"),
                        @ApiResponse(responseCode = "404", description = "Produto não encontrado")
        })
        ResponseEntity<BaseResponse<LucroProdutoResponseDTO>> calcularLucro(
                        @Parameter(description = "ID do produto", example = "1") Long produtoId);
}