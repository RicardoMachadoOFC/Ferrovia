import type { Aresta, Cidade, RedeFerroviaria, ResultadoRota, TipoRota } from '../types'

const BASE = '/api'

export async function buscarCidades(): Promise<Cidade[]> {
  const r = await fetch(`${BASE}/cidades`)
  return r.json()
}

export async function buscarRodovias(): Promise<Aresta[]> {
  const r = await fetch(`${BASE}/rodovias`)
  return r.json()
}

export async function buscarMST(): Promise<RedeFerroviaria> {
  const r = await fetch(`${BASE}/ferrovia/mst`)
  return r.json()
}

export async function buscarGenetico(): Promise<RedeFerroviaria> {
  const r = await fetch(`${BASE}/ferrovia/genetico`)
  return r.json()
}

export async function calcularRota(
  origem: string,
  destino: string,
  tipo: TipoRota
): Promise<ResultadoRota> {
  const endpoint =
    tipo === 'rodoviaria' ? '/rota/rodoviaria'
    : tipo === 'mista'    ? '/rota/mista'
                          : '/rota/otimizada'
  const r = await fetch(`${BASE}${endpoint}`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ origem, destino }),
  })
  return r.json()
}
