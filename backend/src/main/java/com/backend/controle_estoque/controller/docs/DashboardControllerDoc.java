package com.backend.controle_estoque.controller.docs;

import com.backend.controle_estoque.api.BaseResponse;
import com.backend.controle_estoque.dto.DashboardResumoResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;

public interface DashboardControllerDoc {

    @Operation(summary = "Obter resumo de indicadores do dashboard")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resumo retornado com sucesso")
    })
    ResponseEntity<BaseResponse<DashboardResumoResponseDTO>> resumo();
}
