import { useEffect, useState, useCallback, useMemo } from 'react'
import { ThemeProvider, BaseStyles, Box, Heading, Spinner, Text } from '@primer/react'
import type { Aresta, CamadasVisiveis, Cidade, RedeFerroviaria, ResultadoRota, TipoRota } from './types'
import { buscarCidades, buscarGenetico, buscarMST, buscarRodovias, calcularRota } from './api/ferroviasApi'
import PainelControle from './components/PainelControle'
import MapaFerrovias from './components/MapaFerrovias'
import ModalDetalhesRota from './components/ModalDetalhesRota'

export default function App() {
  const [cidades, setCidades]     = useState<Cidade[]>([])
  const [rodovias, setRodovias]   = useState<Aresta[]>([])
  const [mst, setMst]             = useState<RedeFerroviaria | null>(null)
  const [genetico, setGenetico]   = useState<RedeFerroviaria | null>(null)
  const [iniciando, setIniciando] = useState(true)

  const [origem, setOrigem]     = useState('')
  const [destino, setDestino]   = useState('')
  const [tipoRota, setTipoRota] = useState<TipoRota>('rodoviaria')
  const [rota, setRota]         = useState<ResultadoRota | null>(null)
  const [carregando, setCarregando] = useState(false)
  const [erro, setErro]         = useState<string | null>(null)
  const [modalAberto, setModalAberto] = useState(false)

  const [camadas, setCamadas] = useState<CamadasVisiveis>({
    rodovias: true,
    mst: true,
    genetico: true,
  })

  // Mapa id → nome para o modal
  const nomeCidades = useMemo(
    () => Object.fromEntries(cidades.map((c) => [c.id, c.nome])),
    [cidades]
  )

  useEffect(() => {
    Promise.all([buscarCidades(), buscarRodovias(), buscarMST()])
      .then(([c, r, m]) => {
        setCidades(c)
        setRodovias(r)
        setMst(m)
        setIniciando(false)
        buscarGenetico().then(setGenetico)
      })
      .catch(() => setIniciando(false))
  }, [])

  const handleCalcular = useCallback(async () => {
    if (!origem || !destino) return
    setCarregando(true)
    setErro(null)
    setModalAberto(false)
    try {
      const resultado = await calcularRota(origem, destino, tipoRota)
      setRota(resultado)
    } catch {
      setErro('Erro ao calcular rota. Verifique se o backend está rodando.')
    } finally {
      setCarregando(false)
    }
  }, [origem, destino, tipoRota])

  const handleCidadeClick = useCallback((id: string) => {
    if (!origem) setOrigem(id)
    else if (!destino) setDestino(id)
    else { setOrigem(id); setDestino(''); setRota(null); setModalAberto(false) }
  }, [origem, destino])

  return (
    <ThemeProvider colorMode="dark">
      <BaseStyles>
        <Box sx={{ display: 'flex', flexDirection: 'column', height: '100vh' }}>
          {/* Header */}
          <Box sx={{
            px: 3, py: 2,
            bg: 'canvas.default',
            borderBottom: '1px solid', borderColor: 'border.default',
            display: 'flex', alignItems: 'center', gap: 2,
          }}>
            <Heading sx={{ fontSize: 2, color: '#e6edf3' }}>Ferrovias Brasil</Heading>
            <Text sx={{ fontSize: 1, color: '#8b949e' }}>
              — análise de custos logísticos entre capitais
            </Text>
            {!genetico && !iniciando && (
              <Box sx={{ display: 'flex', alignItems: 'center', gap: 2, ml: 'auto' }}>
                <Spinner size="small" />
                <Text sx={{ fontSize: 0, color: '#8b949e' }}>Calculando rede genética…</Text>
              </Box>
            )}
          </Box>

          {/* Corpo */}
          {iniciando ? (
            <Box sx={{ flex: 1, display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
              <Spinner />
            </Box>
          ) : (
            <Box sx={{ flex: 1, display: 'flex', overflow: 'hidden' }}>
              <PainelControle
                cidades={cidades}
                origem={origem}
                destino={destino}
                tipoRota={tipoRota}
                carregando={carregando}
                rota={rota}
                mst={mst}
                genetico={genetico}
                camadas={camadas}
                erro={erro}
                onOrigem={setOrigem}
                onDestino={setDestino}
                onTipo={setTipoRota}
                onCalcular={handleCalcular}
                onCamadas={setCamadas}
                onAbrirDetalhes={() => setModalAberto(true)}
              />
              <Box sx={{ flex: 1 }}>
                <MapaFerrovias
                  cidades={cidades}
                  rodovias={rodovias}
                  mstFerrovias={mst?.ferrovias ?? []}
                  geneticoFerrovias={genetico?.ferrovias ?? []}
                  rota={rota}
                  camadas={camadas}
                  onCidadeClick={handleCidadeClick}
                />
              </Box>
            </Box>
          )}
        </Box>

        {/* Modal de detalhes */}
        {modalAberto && rota && (
          <ModalDetalhesRota
            rota={rota}
            cidades={nomeCidades}
            onFechar={() => setModalAberto(false)}
          />
        )}
      </BaseStyles>
    </ThemeProvider>
  )
}
