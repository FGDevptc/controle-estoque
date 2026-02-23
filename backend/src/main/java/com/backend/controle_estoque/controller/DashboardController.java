package com.backend.controle_estoque.controller;

import com.backend.controle_estoque.api.BaseResponse;
import com.backend.controle_estoque.api.ResponseFactory;
import com.backend.controle_estoque.controller.docs.DashboardControllerDoc;
import com.backend.controle_estoque.dto.DashboardResumoResponseDTO;
import com.backend.controle_estoque.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController implements DashboardControllerDoc {

    private final DashboardService service;

    @GetMapping("/resumo")
    @Override
    public ResponseEntity<BaseResponse<DashboardResumoResponseDTO>> resumo() {
        return ResponseFactory.success(service.resumo());
    }
}
