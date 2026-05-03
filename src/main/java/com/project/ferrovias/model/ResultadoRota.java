package com.project.ferrovias.model;

import java.util.List;

public record ResultadoRota(
        List<String> caminho,
        double distanciaKm,
        double custoReais,
        String modoTransporte,
        List<SegmentoRota> segmentos
) {}
