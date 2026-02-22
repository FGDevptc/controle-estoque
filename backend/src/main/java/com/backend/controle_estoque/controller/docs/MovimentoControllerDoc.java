package com.backend.controle_estoque.controller.docs;

import com.backend.controle_estoque.api.BaseResponse;
import com.backend.controle_estoque.dto.MovimentoRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;

public interface MovimentoControllerDoc {

    @Operation(summary = "Realizar movimentação de estoque")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Movimentação realizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de regra de negócio"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    ResponseEntity<BaseResponse<Void>> movimentar(MovimentoRequestDTO dto);
}