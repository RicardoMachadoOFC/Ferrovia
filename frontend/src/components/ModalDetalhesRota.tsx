import type { CSSProperties } from 'react'
import type { ResultadoRota, SegmentoRota } from '../types'

interface Props {
  rota: ResultadoRota
  cidades: Record<string, string>
  onFechar: () => void
}

function formatBRL(v: number) {
  return v.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' })
}

const MODO_COR: Record<string, string> = {
  'rodoviária': '#8b949e',
  'ferroviária': '#388bfd',
  'baldeação':  '#e3b341',
}

const td: CSSProperties = {
  padding: '8px 10px',
  fontSize: 13,
  color: '#e6edf3',
  verticalAlign: 'middle',
  borderBottom: '1px solid #21262d',
}

const th: CSSProperties = {
  padding: '8px 10px',
  fontSize: 11,
  color: '#8b949e',
  fontWeight: 700,
  textTransform: 'uppercase',
  letterSpacing: '0.05em',
  textAlign: 'left',
  borderBottom: '2px solid #30363d',
  background: '#0d1117',
  position: 'sticky',
  top: 0,
}

function LinhaSegmento({ seg, nomes }: { seg: SegmentoRota; nomes: Record<string, string> }) {
  const isBaldeacao = seg.modo === 'baldeação'
  const cor = MODO_COR[seg.modo] ?? '#8b949e'

  return (
    <tr style={{ opacity: isBaldeacao ? 0.75 : 1 }}>
      <td style={td}>{nomes[seg.origem] ?? seg.origem}</td>
      <td style={td}>{nomes[seg.destino] ?? seg.destino}</td>
      <td style={{ ...td, textAlign: 'center' }}>
        <span style={{
          background: cor + '22',
          color: cor,
          border: `1px solid ${cor}55`,
          borderRadius: 10,
          padding: '2px 8px',
          fontSize: 11,
          fontWeight: 600,
        }}>
          {seg.modo}
        </span>
      </td>
      <td style={{ ...td, textAlign: 'right', color: '#8b949e' }}>
        {isBaldeacao ? '—' : `${seg.distanciaKm.toLocaleString('pt-BR')} km`}
      </td>
      <td style={{ ...td, textAlign: 'right', fontWeight: 600 }}>
        {formatBRL(seg.custoReais)}
      </td>
    </tr>
  )
}

export default function ModalDetalhesRota({ rota, cidades, onFechar }: Props) {
  const nomOrigem  = cidades[rota.caminho[0]] ?? rota.caminho[0]
  const nomDestino = cidades[rota.caminho[rota.caminho.length - 1]] ?? rota.caminho[rota.caminho.length - 1]

  return (
    <div
      onClick={onFechar}
      style={{
        position: 'fixed', inset: 0,
        background: 'rgba(0,0,0,0.7)',
        display: 'flex', alignItems: 'center', justifyContent: 'center',
        zIndex: 9999,
      }}
    >
      <div
        onClick={(e) => e.stopPropagation()}
        style={{
          background: '#161b22',
          border: '1px solid #30363d',
          borderRadius: 10,
          width: 700,
          maxWidth: '95vw',
          maxHeight: '85vh',
          display: 'flex',
          flexDirection: 'column',
          overflow: 'hidden',
        }}
      >
        {/* Cabeçalho */}
        <div style={{
          display: 'flex', justifyContent: 'space-between', alignItems: 'center',
          padding: '14px 18px',
          borderBottom: '1px solid #30363d',
          flexShrink: 0,
        }}>
          <div>
            <span style={{ fontSize: 15, fontWeight: 700, color: '#e6edf3' }}>
              Detalhes da Rota
            </span>
            <span style={{ fontSize: 13, color: '#8b949e', marginLeft: 10 }}>
              {nomOrigem} → {nomDestino}
            </span>
          </div>
          <button
            onClick={onFechar}
            style={{
              background: 'none', border: 'none', cursor: 'pointer',
              color: '#8b949e', fontSize: 20, lineHeight: 1, padding: '2px 8px',
            }}
          >
            ✕
          </button>
        </div>

        {/* Tabela com scroll */}
        <div style={{ overflowY: 'auto', flex: 1 }}>
          <table style={{ width: '100%', borderCollapse: 'collapse' }}>
            <thead>
              <tr>
                <th style={th}>De</th>
                <th style={th}>Para</th>
                <th style={{ ...th, textAlign: 'center' }}>Modo</th>
                <th style={{ ...th, textAlign: 'right' }}>Distância</th>
                <th style={{ ...th, textAlign: 'right' }}>Custo</th>
              </tr>
            </thead>
            <tbody>
              {(rota.segmentos ?? []).map((seg, i) => (
                <LinhaSegmento key={i} seg={seg} nomes={cidades} />
              ))}
            </tbody>
          </table>
        </div>

        {/* Rodapé com totais */}
        <div style={{
          borderTop: '2px solid #30363d',
          padding: '12px 18px',
          display: 'flex', justifyContent: 'flex-end', gap: 32,
          flexShrink: 0,
        }}>
          <div style={{ textAlign: 'right' }}>
            <div style={{ fontSize: 11, color: '#8b949e', marginBottom: 2 }}>DISTÂNCIA TOTAL</div>
            <div style={{ fontSize: 16, fontWeight: 700, color: '#e6edf3' }}>
              {rota.distanciaKm.toLocaleString('pt-BR')} km
            </div>
          </div>
          <div style={{ textAlign: 'right' }}>
            <div style={{ fontSize: 11, color: '#8b949e', marginBottom: 2 }}>CUSTO TOTAL</div>
            <div style={{ fontSize: 16, fontWeight: 700, color: '#3fb950' }}>
              {formatBRL(rota.custoReais)}
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}
