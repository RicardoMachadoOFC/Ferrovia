package com.project.ferrovias.grafo;

import com.project.ferrovias.model.Cidade;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class InicializadorGrafo {

    private final GrafoRodoviario grafo = new GrafoRodoviario();
    private List<RotaCarga> rotasCarga;

    public record RotaCarga(String origem, String destino, int cargasDia) {}

    @PostConstruct
    public void inicializar() {
        inicializarCidades();
        inicializarArestas();
        inicializarRotasCarga();
    }

    private void inicializarCidades() {
        grafo.adicionarCidade(new Cidade("AC", "Rio Branco",      "Acre",                  -9.97400,  -67.80760));
        grafo.adicionarCidade(new Cidade("AL", "Maceió",          "Alagoas",               -9.66625,  -35.73510));
        grafo.adicionarCidade(new Cidade("AM", "Manaus",          "Amazonas",              -3.10719,  -60.02610));
        grafo.adicionarCidade(new Cidade("AP", "Macapá",          "Amapá",                  0.03446,  -51.06660));
        grafo.adicionarCidade(new Cidade("BA", "Salvador",        "Bahia",                -12.97040,  -38.51240));
        grafo.adicionarCidade(new Cidade("CE", "Fortaleza",       "Ceará",                 -3.71839,  -38.54340));
        grafo.adicionarCidade(new Cidade("DF", "Brasília",        "Distrito Federal",     -15.78010,  -47.92920));
        grafo.adicionarCidade(new Cidade("ES", "Vitória",         "Espírito Santo",       -20.32220,  -40.33840));
        grafo.adicionarCidade(new Cidade("GO", "Goiânia",         "Goiás",                -16.67990,  -49.25500));
        grafo.adicionarCidade(new Cidade("MA", "São Luís",        "Maranhão",              -2.53073,  -44.30680));
        grafo.adicionarCidade(new Cidade("MG", "Belo Horizonte",  "Minas Gerais",         -19.81570,  -43.95420));
        grafo.adicionarCidade(new Cidade("MS", "Campo Grande",    "Mato Grosso do Sul",   -20.44350,  -54.64780));
        grafo.adicionarCidade(new Cidade("MT", "Cuiabá",          "Mato Grosso",          -15.59890,  -56.09490));
        grafo.adicionarCidade(new Cidade("PA", "Belém",           "Pará",                  -1.45502,  -48.50240));
        grafo.adicionarCidade(new Cidade("PB", "João Pessoa",     "Paraíba",               -7.11532,  -34.86100));
        grafo.adicionarCidade(new Cidade("PE", "Recife",          "Pernambuco",            -8.05428,  -34.88130));
        grafo.adicionarCidade(new Cidade("PI", "Teresina",        "Piauí",                 -5.08921,  -42.80160));
        grafo.adicionarCidade(new Cidade("PR", "Curitiba",        "Paraná",               -25.42840,  -49.27330));
        grafo.adicionarCidade(new Cidade("RJ", "Rio de Janeiro",  "Rio de Janeiro",       -22.90350,  -43.20960));
        grafo.adicionarCidade(new Cidade("RN", "Natal",           "Rio Grande do Norte",   -5.79448,  -35.21100));
        grafo.adicionarCidade(new Cidade("RO", "Porto Velho",     "Rondônia",              -8.76183,  -63.90200));
        grafo.adicionarCidade(new Cidade("RR", "Boa Vista",       "Roraima",                2.81954,  -60.67140));
        grafo.adicionarCidade(new Cidade("RS", "Porto Alegre",    "Rio Grande do Sul",    -30.02770,  -51.22870));
        grafo.adicionarCidade(new Cidade("SC", "Florianópolis",   "Santa Catarina",       -27.59690,  -48.54950));
        grafo.adicionarCidade(new Cidade("SE", "Aracaju",         "Sergipe",              -10.90950,  -37.07480));
        grafo.adicionarCidade(new Cidade("SP", "São Paulo",       "São Paulo",            -23.54890,  -46.63880));
        grafo.adicionarCidade(new Cidade("TO", "Palmas",          "Tocantins",            -10.16890,  -48.33170));
    }

    private void inicializarArestas() {
        // Sul
        grafo.adicionarAresta("RS", "SC",   463); // BR-101
        grafo.adicionarAresta("SC", "PR",   307); // BR-101
        grafo.adicionarAresta("PR", "SP",   416); // BR-116 Régis Bittencourt
        grafo.adicionarAresta("MS", "PR",   362); // BR-163 + BR-277
        // Sudeste
        grafo.adicionarAresta("RJ", "SP",   435); // BR-116 Via Dutra
        grafo.adicionarAresta("SP", "MG",   583); // BR-381 Fernão Dias
        grafo.adicionarAresta("SP", "MS",   780); // BR-262
        grafo.adicionarAresta("RJ", "MG",   441); // BR-040
        grafo.adicionarAresta("RJ", "ES",   518); // BR-101
        grafo.adicionarAresta("MG", "ES",   515); // BR-262
        grafo.adicionarAresta("MG", "BA",  1434); // BR-116
        grafo.adicionarAresta("MG", "GO",   891); // BR-040
        grafo.adicionarAresta("MG", "MS",  1356); // BR-262 + BR-040
        grafo.adicionarAresta("MG", "DF",   739); // BR-040 (exceção: Brasília-BH)
        grafo.adicionarAresta("ES", "BA",  1175); // BR-101
        // Centro-Oeste
        grafo.adicionarAresta("MS", "GO",   840); // BR-060
        grafo.adicionarAresta("MS", "MT",   707); // BR-163
        grafo.adicionarAresta("GO", "BA",  1646); // BR-242 + BR-060
        grafo.adicionarAresta("GO", "TO",   824); // BR-153
        grafo.adicionarAresta("GO", "MT",   887); // BR-364
        grafo.adicionarAresta("GO", "DF",   207); // BR-060
        grafo.adicionarAresta("MT", "TO",  1487); // BR-158
        grafo.adicionarAresta("MT", "PA",  2629); // BR-163
        grafo.adicionarAresta("MT", "AM",  2349); // BR-319 + BR-364
        grafo.adicionarAresta("MT", "RO",  1461); // BR-364
        // Norte
        grafo.adicionarAresta("TO", "MA",  1249); // BR-010
        grafo.adicionarAresta("TO", "PA",  1207); // BR-010
        grafo.adicionarAresta("TO", "BA",  1474); // BR-242 + BR-010
        grafo.adicionarAresta("PA", "AM",  3048); // AM-010 + BR-010
        grafo.adicionarAresta("PA", "MA",   576); // BR-316
        grafo.adicionarAresta("PA", "AP",   527); // BR-156 + travessia fluvial
        grafo.adicionarAresta("PA", "RR",  2808); // BR-010 + BR-174
        grafo.adicionarAresta("AM", "RR",   747); // BR-174
        grafo.adicionarAresta("AM", "RO",   889); // BR-319 + BR-364
        grafo.adicionarAresta("AM", "AC",  1395); // BR-364 + AM-010
        grafo.adicionarAresta("RO", "AC",   509); // BR-364
        // Nordeste
        grafo.adicionarAresta("BA", "SE",   323); // BR-101
        grafo.adicionarAresta("BA", "AL",   579); // BR-101
        grafo.adicionarAresta("BA", "PE",   806); // BR-101
        grafo.adicionarAresta("BA", "PI",  1152); // BR-324 + BR-135
        grafo.adicionarAresta("SE", "AL",   272); // BR-101
        grafo.adicionarAresta("AL", "PE",   256); // BR-101
        grafo.adicionarAresta("PE", "PB",   116); // BR-101
        grafo.adicionarAresta("PE", "CE",   779); // BR-116
        grafo.adicionarAresta("PE", "PI",  1127); // BR-232 + BR-316
        grafo.adicionarAresta("CE", "PB",   673); // BR-116 + BR-230
        grafo.adicionarAresta("PB", "RN",   181); // BR-101
        grafo.adicionarAresta("RN", "CE",   524); // BR-304
        grafo.adicionarAresta("CE", "PI",   603); // BR-222 + BR-343
        grafo.adicionarAresta("PI", "MA",   436); // BR-316 + BR-343
        grafo.adicionarAresta("PI", "TO",  1107); // BR-010 + BR-226
    }

    private void inicializarRotasCarga() {
        // Rotas conforme Anexo I do trabalho (50 rotas mais comuns)
        rotasCarga = List.of(
            new RotaCarga("SP", "RJ", 150), //  1
            new RotaCarga("DF", "GO", 140), //  2
            new RotaCarga("RJ", "MG", 130), //  3
            new RotaCarga("SP", "PE", 120), //  4
            new RotaCarga("AM", "SP", 110), //  5
            new RotaCarga("CE", "SP", 100), //  6
            new RotaCarga("RS", "DF",  90), //  7
            new RotaCarga("SP", "BA",  90), //  8
            new RotaCarga("RJ", "BA",  85), //  9
            new RotaCarga("PA", "AM",  80), // 10
            new RotaCarga("MG", "DF",  80), // 11
            new RotaCarga("PA", "RJ",  75), // 12
            new RotaCarga("SC", "RS",  75), // 13
            new RotaCarga("PE", "BA",  75), // 14
            new RotaCarga("PR", "RN",  70), // 15
            new RotaCarga("PR", "SC",  70), // 16
            new RotaCarga("RS", "RJ",  70), // 17
            new RotaCarga("MA", "PA",  65), // 18
            new RotaCarga("SP", "SC",  65), // 19
            new RotaCarga("BA", "DF",  65), // 20
            new RotaCarga("MG", "MA",  60), // 21
            new RotaCarga("RN", "CE",  60), // 22
            new RotaCarga("MT", "GO",  60), // 23
            new RotaCarga("MT", "ES",  55), // 24
            new RotaCarga("CE", "PI",  55), // 25
            new RotaCarga("ES", "RJ",  55), // 26
            new RotaCarga("MS", "PR",  55), // 27
            new RotaCarga("DF", "MT",  55), // 28
            new RotaCarga("MS", "PE",  50), // 29
            new RotaCarga("PE", "PB",  50), // 30
            new RotaCarga("PI", "MA",  50), // 31
            new RotaCarga("AM", "RO",  50), // 32
            new RotaCarga("RJ", "ES",  50), // 33
            new RotaCarga("AL", "PE",  45), // 34
            new RotaCarga("PB", "RN",  45), // 35
            new RotaCarga("RO", "MT",  45), // 36
            new RotaCarga("PI", "DF",  45), // 37
            new RotaCarga("BA", "SE",  40), // 38
            new RotaCarga("AM", "RR",  40), // 39
            new RotaCarga("TO", "DF",  40), // 40
            new RotaCarga("PB", "BA",  40), // 41
            new RotaCarga("SE", "AL",  35), // 42
            new RotaCarga("RO", "AC",  35), // 43
            new RotaCarga("TO", "PA",  35), // 44
            new RotaCarga("AL", "MG",  35), // 45
            new RotaCarga("PA", "AP",  30), // 46
            new RotaCarga("AP", "CE",  30), // 47
            new RotaCarga("SE", "RJ",  30), // 48
            new RotaCarga("AC", "SP",  25), // 49
            new RotaCarga("RR", "DF",  20)  // 50
        );
    }

    public GrafoRodoviario getGrafo() {
        return grafo;
    }

    public List<RotaCarga> getRotasCarga() {
        return rotasCarga;
    }
}
