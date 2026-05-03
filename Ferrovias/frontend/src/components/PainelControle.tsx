import { Box, Text, Heading } from '@primer/react'
import type { CamadasVisiveis, Cidade, RedeFerroviaria, ResultadoRota, TipoRota } from '../types'

interface Props {
  cidades: Cidade[]
  origem: string
  destino: string
  tipoRota: TipoRota
  carregando: boolean
  rota: ResultadoRota | null
  mst: RedeFerroviaria | null
  genetico: RedeFerroviaria | null
  camadas: CamadasVisiveis
  erro: string | null
  onOrigem: (v: string) => void
  onDestino: (v: string) => void
  onTipo: (v: TipoRota) => void
  onCalcular: () => void
  onCamadas: (c: CamadasVisiveis) => void
  onAbrirDetalhes: () => void
}

function formatBRL(v: number) {
  return v.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' })
}

const s: Record<string, React.CSSProperties> = {
  label: {
    display: 'block',
    fontSize: 12,
    color: '#8b949e',
    marginBottom: 4,
    fontWeight: 600,
    letterSpacing: '0.03em',
    textTransform: 'uppercase',
  },
  select: {
    width: '100%',
    padding: '6px 10px',
    background: '#161b22',
    color: '#e6edf3',
    border: '1px solid #30363d',
    borderRadius: 6,
    fontSize: 14,
    appearance: 'auto',
    cursor: 'pointer',
  },
  btnPrimary: {
    width: '100%',
    padding: '8px 16px',
    background: '#238636',
    color: '#ffffff',
    border: '1px solid #2ea043',
    borderRadius: 6,
    fontSize: 14,
    fontWeight: 600,
    cursor: 'pointer',
    marginTop: 4,
  },
  btnDisabled: {
    width: '100%',
    padding: '8px 16px',
    background: '#1c2128',
    color: '#484f58',
    border: '1px solid #30363d',
    borderRadius: 6,
    fontSize: 14,
    fontWeight: 600,
    cursor: 'not-allowed',
    marginTop: 4,
  },
  fieldGroup: {
    marginBottom: 12,
  },
  resultCard: {
    background: '#161b22',
    border: '1px solid #30363d',
    borderRadius: 6,
    padding: 12,
  },
  row: {
    display: 'flex' as const,
    justifyContent: 'space-between' as const,
    alignItems: 'center' as const,
    marginBottom: 6,
  },
  badge: {
    background: '#388bfd22',
    color: '#388bfd',
    border: '1px solid #388bfd55',
    borderRadius: 12,
    padding: '2px 8px',
    fontSize: 12,
    fontWeight: 600,
  },
  checkRow: {
    display: 'flex' as const,
    alignItems: 'center' as const,
    gap: 8,
    marginBottom: 8,
    cursor: 'pointer',
  },
  checkbox: {
    width: 16,
    height: 16,
    cursor: 'pointer',
    accentColor: '#388bfd',
  },
  dot: {
    height: 4,
    borderRadius: 2,
    flexShrink: 0,
    width: 20,
    background: '#8b949e',
  } as React.CSSProperties,
  divider: {
    borderTop: '1px solid #21262d',
    margin: '12px 0',
  },
  error: {
    background: '#3d1515',
    border: '1px solid #f85149',
    color: '#f85149',
    borderRadius: 6,
    padding: '8px 12px',
    fontSize: 13,
  },
}

export default function PainelControle({
  cidades, origem, destino, tipoRota, carregando, rota, mst, genetico,
  camadas, erro, onOrigem, onDestino, onTipo, onCalcular, onCamadas, onAbrirDetalhes,
}: Props) {
  const btnStyle = (!origem || !destino || carregando) ? s.btnDisabled : s.btnPrimary

  return (
    <Box
      sx={{
        width: 300,
        flexShrink: 0,
        height: '100%',
        overflowY: 'auto',
        borderRight: '1px solid',
        borderColor: 'border.default',
        bg: 'canvas.default',
        p: 3,
        display: 'flex',
        flexDirection: 'column',
      }}
    >
      <Heading sx={{ fontSize: 1, mb: 3, color: '#e6edf3' }}>Calcular Rota</Heading>

      {/* Origem */}
      <div style={s.fieldGroup}>
        <label style={s.label}>Origem</label>
        <select
          style={s.select}
          value={origem}
          onChange={(e) => onOrigem(e.target.value)}
        >
          <option value="">Selecione...</option>
          {cidades.map((c) => (
            <option key={c.id} value={c.id}>{c.nome} ({c.id})</option>
          ))}
        </select>
      </div>

      {/* Destino */}
      <div style={s.fieldGroup}>
        <label style={s.label}>Destino</label>
        <select
          style={s.select}
          value={destino}
          onChange={(e) => onDestino(e.target.value)}
        >
          <option value="">Selecione...</option>
          {cidades.map((c) => (
            <option key={c.id} value={c.id}>{c.nome} ({c.id})</option>
          ))}
        </select>
      </div>

      {/* Tipo de rota */}
      <div style={s.fieldGroup}>
        <label style={s.label}>Tipo de rota</label>
        <select
          style={s.select}
          value={tipoRota}
          onChange={(e) => onTipo(e.target.value as TipoRota)}
        >
          <option value="rodoviaria">Rodoviária — R$ 5,00/km</option>
          <option value="mista">Mista com MST — R$ 1,20/km ferrovia</option>
          <option value="otimizada">Mista com Rede Genética</option>
        </select>
      </div>

      <button
        style={btnStyle}
        onClick={onCalcular}
        disabled={!origem || !destino || carregando}
      >
        {carregando ? 'Calculando...' : 'Calcular'}
      </button>

      {erro && <div style={{ ...s.error, marginTop: 12 }}>{erro}</div>}

      {/* Resultado */}
      {rota && (
        <>
          <div style={s.divider} />
          <Text sx={{ fontSize: 0, color: '#8b949e', mb: 2, fontWeight: 'bold' }}>RESULTADO</Text>
          <div style={s.resultCard}>
            <div style={s.row}>
              <Text sx={{ fontSize: 1, color: '#8b949e' }}>Modo</Text>
              <span style={s.badge}>{rota.modoTransporte}</span>
            </div>
            <div style={s.row}>
              <Text sx={{ fontSize: 1, color: '#8b949e' }}>Distância</Text>
              <Text sx={{ fontSize: 1, fontWeight: 'bold', color: '#e6edf3' }}>
                {rota.distanciaKm.toLocaleString('pt-BR')} km
              </Text>
            </div>
            <div style={s.row}>
              <Text sx={{ fontSize: 1, color: '#8b949e' }}>Custo</Text>
              <Text sx={{ fontSize: 1, fontWeight: 'bold', color: '#3fb950' }}>
                {formatBRL(rota.custoReais)}
              </Text>
            </div>
            <div style={{ marginTop: 8 }}>
              <Text sx={{ fontSize: 0, color: '#8b949e' }}>Caminho</Text>
              <Box sx={{ display: 'flex', flexWrap: 'wrap', gap: 1, mt: 1 }}>
                {rota.caminho.map((id, i) => (
                  <Text key={i} sx={{ fontSize: 0, color: '#e6edf3' }}>
                    {id}{i < rota.caminho.length - 1 ? ' →' : ''}
                  </Text>
                ))}
              </Box>
            </div>
          </div>

          {/* Botão detalhes */}
          <button
            style={{
              marginTop: 8,
              width: '100%',
              padding: '6px 12px',
              background: '#21262d',
              color: '#e6edf3',
              border: '1px solid #30363d',
              borderRadius: 6,
              fontSize: 13,
              cursor: 'pointer',
            }}
            onClick={onAbrirDetalhes}
          >
            Ver detalhes da rota →
          </button>
        </>
      )}

      {/* Camadas */}
      <div style={s.divider} />
      <Text sx={{ fontSize: 0, color: '#8b949e', mb: 2, fontWeight: 'bold' }}>CAMADAS DO MAPA</Text>

      <label style={s.checkRow}>
        <input type="checkbox" style={s.checkbox} checked={camadas.rodovias}
          onChange={(e) => onCamadas({ ...camadas, rodovias: e.target.checked })} />
        <span style={{ ...s.dot, background: '#8b949e' }} />
        <Text sx={{ fontSize: 1, color: '#e6edf3' }}>Rodovias</Text>
      </label>
      <label style={s.checkRow}>
        <input type="checkbox" style={s.checkbox} checked={camadas.mst}
          onChange={(e) => onCamadas({ ...camadas, mst: e.target.checked })} />
        <span style={{ ...s.dot, background: '#388bfd' }} />
        <Text sx={{ fontSize: 1, color: '#e6edf3' }}>MST Ferroviário</Text>
      </label>
      <label style={s.checkRow}>
        <input type="checkbox" style={s.checkbox} checked={camadas.genetico}
          onChange={(e) => onCamadas({ ...camadas, genetico: e.target.checked })} />
        <span style={{ ...s.dot, background: '#3fb950' }} />
        <Text sx={{ fontSize: 1, color: '#e6edf3' }}>Rede Genética</Text>
      </label>

      {/* Seção Algoritmo Genético */}
      {genetico && (
        <>
          <div style={s.divider} />
          <Text sx={{ fontSize: 0, color: '#8b949e', mb: 2, fontWeight: 'bold' }}>ALGORITMO GENÉTICO</Text>
          <div style={{ background: '#0d1117', border: '1px solid #21262d', borderRadius: 6, padding: 10 }}>
            {mst && (() => {
              const orcamento = mst.custoTotalReais * 0.60
              const utilizado = genetico.custoTotalReais
              const pct = Math.round((utilizado / orcamento) * 100)
              return (
                <>
                  <div style={{ ...s.row, marginBottom: 6 }}>
                    <Text sx={{ fontSize: 1, color: '#8b949e' }}>Orçamento (60% MST)</Text>
                    <Text sx={{ fontSize: 1, color: '#e6edf3' }}>{formatBRL(orcamento)}</Text>
                  </div>
                  <div style={{ ...s.row, marginBottom: 6 }}>
                    <Text sx={{ fontSize: 1, color: '#8b949e' }}>Custo de construção</Text>
                    <Text sx={{ fontSize: 1, color: '#3fb950', fontWeight: 'bold' }}>{formatBRL(utilizado)}</Text>
                  </div>
                  <div style={{ ...s.row, marginBottom: 8 }}>
                    <Text sx={{ fontSize: 1, color: '#8b949e' }}>Trechos selecionados</Text>
                    <Text sx={{ fontSize: 1, color: '#e6edf3' }}>{genetico.ferrovias.length}</Text>
                  </div>
                  {/* Barra de progresso */}
                  <div style={{ fontSize: 11, color: '#8b949e', marginBottom: 4 }}>
                    Utilização do orçamento — {pct}%
                  </div>
                  <div style={{ background: '#21262d', borderRadius: 4, height: 6, overflow: 'hidden' }}>
                    <div style={{
                      height: '100%',
                      width: `${Math.min(pct, 100)}%`,
                      background: pct > 95 ? '#f85149' : '#3fb950',
                      borderRadius: 4,
                      transition: 'width 0.4s ease',
                    }} />
                  </div>
                </>
              )
            })()}
          </div>
        </>
      )}
    </Box>
  )
}
