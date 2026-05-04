🚆 Sistema de Otimização de Transporte de Cargas com IA

Projeto desenvolvido com foco na resolução de problemas utilizando algoritmos clássicos de busca e otimização.

📌 Objetivo

Simular e otimizar o transporte de cargas entre capitais brasileiras, considerando:

Malha rodoviária existente
Possível implantação de ferrovias
Redução de custos logísticos
🧠 Técnicas de IA Utilizadas
🔹 A* (A Estrela)
Encontrar a rota mais barata entre duas cidades

Versões implementadas:

Apenas rodoviária
Mista (rodovia + ferrovia)
Mista otimizada (com algoritmo genético)
🔹 Kruskal (Árvore Geradora Mínima - MST)
Gerar uma rede ferroviária conectando todas as capitais
Minimiza o custo total de construção
🔹 Algoritmo Genético
Seleciona quais ferrovias devem ser construídas

Considera:

Orçamento limitado (60% do custo da MST)
Rotas mais utilizadas 

💰 Regras de Custo
Tipo	Custo
Rodovia	R$ 5,00/km
Ferrovia	R$ 1,20/km
Baldeação	R$ 1.000,00
Construção	R$ 2.000.000/km


🧩 Modelagem do Problema
Cada capital = nó do grafo
Arestas = conexões entre estados vizinhos
Peso = distância (km)
Heurística = distância euclidiana aproximada


⚙️ Destaques da Implementação

🔹 A*
Uso de PriorityQueue
Heurística baseada em coordenadas geográficas
Suporte a múltiplos estados (R e F)


🔹 Kruskal
Union-Find com compressão de caminho
Ordenação por menor distância


🔹 Algoritmo Genético
Representação: vetor booleano (ferrovia construída ou não)

Fitness:

Custo logístico total das cargas
Penalização se exceder orçamento


Desenvolvido por: Ricardo Machado, Pietra Andrade, Ana Vitória, Bruno Souza e Stefani Pires
