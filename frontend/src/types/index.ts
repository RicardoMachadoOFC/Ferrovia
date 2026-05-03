export interface Cidade {
  id: string
  nome: string
  estado: string
  lat: number
  lon: number
}

export interface Aresta {
  origem: string
  destino: string
  distanciaKm: number
}

export interface SegmentoRota {
  origem: string
  destino: string
  distanciaKm: number
  custoReais: number
  modo: string // "rodoviária" | "ferroviária" | "baldeação"
}

export interface ResultadoRota {
  caminho: string[]
  distanciaKm: number
  custoReais: number
  modoTransporte: string
  segmentos: SegmentoRota[]
}

export interface RedeFerroviaria {
  ferrovias: Aresta[]
  custoTotalReais: number
  orcamentoReais: number
}

export type TipoRota = 'rodoviaria' | 'mista' | 'otimizada'

export interface CamadasVisiveis {
  rodovias: boolean
  mst: boolean
  genetico: boolean
}
