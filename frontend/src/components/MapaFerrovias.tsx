import { MapContainer, TileLayer, Polyline, CircleMarker, Tooltip } from 'react-leaflet'
import type { Aresta, CamadasVisiveis, Cidade, ResultadoRota } from '../types'
import 'leaflet/dist/leaflet.css'

interface Props {
  cidades: Cidade[]
  rodovias: Aresta[]
  mstFerrovias: Aresta[]
  geneticoFerrovias: Aresta[]
  rota: ResultadoRota | null
  camadas: CamadasVisiveis
  onCidadeClick: (id: string) => void
}

const CORES = {
  rodovia:  '#8b949e',
  mst:      '#388bfd',
  genetico: '#3fb950',
  rota:     '#f78166',
  cidade:   '#e6edf3',
  origem:   '#f78166',
  destino:  '#3fb950',
}

function coordsPar(arestas: Aresta[], cidades: Map<string, Cidade>) {
  return arestas.flatMap((a) => {
    const c1 = cidades.get(a.origem)
    const c2 = cidades.get(a.destino)
    if (!c1 || !c2) return []
    return [[[c1.lat, c1.lon], [c2.lat, c2.lon]] as [number, number][]]
  })
}

export default function MapaFerrovias({
  cidades, rodovias, mstFerrovias, geneticoFerrovias, rota, camadas, onCidadeClick,
}: Props) {
  const cidadeMap = new Map(cidades.map((c) => [c.id, c]))

  const rotaCoords: [number, number][] = rota
    ? rota.caminho.flatMap((id) => {
        const c = cidadeMap.get(id)
        return c ? [[c.lat, c.lon] as [number, number]] : []
      })
    : []

  const rotaIds = new Set(rota?.caminho ?? [])
  const origem = rota?.caminho[0]
  const destino = rota?.caminho[rota.caminho.length - 1]

  return (
    <MapContainer
      center={[-14, -52]}
      zoom={4}
      style={{ height: '100%', width: '100%', background: '#0d1117' }}
      zoomControl
    >
      <TileLayer
        attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a>'
        url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
      />

      {/* Rodovias */}
      {camadas.rodovias &&
        coordsPar(rodovias, cidadeMap).map((pos, i) => (
          <Polyline
            key={`rod-${i}`}
            positions={pos}
            color={CORES.rodovia}
            weight={1.5}
            dashArray="6 4"
            opacity={0.6}
          />
        ))}

      {/* MST Ferroviário */}
      {camadas.mst &&
        coordsPar(mstFerrovias, cidadeMap).map((pos, i) => (
          <Polyline
            key={`mst-${i}`}
            positions={pos}
            color={CORES.mst}
            weight={2.5}
            opacity={0.75}
          />
        ))}

      {/* Rede Genética */}
      {camadas.genetico &&
        coordsPar(geneticoFerrovias, cidadeMap).map((pos, i) => (
          <Polyline
            key={`gen-${i}`}
            positions={pos}
            color={CORES.genetico}
            weight={2.5}
            opacity={0.75}
          />
        ))}

      {/* Rota calculada */}
      {rotaCoords.length > 1 && (
        <Polyline
          positions={rotaCoords}
          color={CORES.rota}
          weight={4}
          opacity={0.9}
        />
      )}

      {/* Marcadores das cidades */}
      {cidades.map((c) => {
        const isOrigem = c.id === origem
        const isDestino = c.id === destino
        const naRota = rotaIds.has(c.id)
        const cor = isOrigem ? CORES.origem : isDestino ? CORES.destino : CORES.cidade
        const raio = naRota || isOrigem || isDestino ? 7 : 5

        return (
          <CircleMarker
            key={c.id}
            center={[c.lat, c.lon]}
            radius={raio}
            pathOptions={{
              fillColor: cor,
              color: '#0d1117',
              weight: 1.5,
              fillOpacity: 0.9,
            }}
            eventHandlers={{ click: () => onCidadeClick(c.id) }}
          >
            <Tooltip direction="top" offset={[0, -6]} opacity={0.95}>
              <strong>{c.nome}</strong> — {c.estado}
            </Tooltip>
          </CircleMarker>
        )
      })}
    </MapContainer>
  )
}
