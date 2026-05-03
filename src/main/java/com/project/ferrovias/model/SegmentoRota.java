package com.project.ferrovias.model;

public record SegmentoRota(
        String origem,
        String destino,
        double distanciaKm,
        double custoReais,
        String modo          // "rodoviária", "ferroviária" ou "baldeação"
) {}
