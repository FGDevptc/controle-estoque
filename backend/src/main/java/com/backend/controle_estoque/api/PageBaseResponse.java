package com.backend.controle_estoque.api;

import java.util.List;

public record PageBaseResponse<T>(
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean hasNext,
        boolean hasPrevious
) {}