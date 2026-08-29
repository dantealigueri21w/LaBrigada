package pe.appmobile.labrigada.domain.model

/**
 * [esRiesgo] existe porque sin él no hay nada que distinguir (sección 5.12 del maestro): con
 * todos los objetos sembrados como riesgo, "corregir" se reduce a tocar todo, en cualquier
 * orden, sin ninguna posibilidad de fallar. Un distractor (esRiesgo = false) es un objeto que
 * YA está bien -- nunca entra a [corregido], nunca lo exige [MotorEscena.esEscenaSegura] -- y
 * está para que el niño aprenda a distinguir, no solo a barrer la pantalla.
 */
data class ObjetoRiesgo(
    val id: String,
    val nombre: String,
    val corregido: Boolean,
    val esRiesgo: Boolean = true,
)

/**
 * Algunas reglas de seguridad son relativas entre dos objetos (ej. "la vela lejos del
 * abrigo"), no solo "este objeto está en su lugar correcto". [distanciaMinimaCumplida] lo
 * modela sin necesitar coordenadas reales de pantalla en el dominio.
 */
data class ReglaDistancia(
    val objetoAId: String,
    val objetoBId: String,
    val distanciaMinimaCumplida: Boolean,
)

data class Escena(
    val id: String,
    val objetos: List<ObjetoRiesgo>,
    val reglasDistancia: List<ReglaDistancia> = emptyList(),
)

data class ResultadoSimulacro(
    val lugarId: String,
    val paso: Boolean,
    val objetosQueFallaron: List<String>,
)

data class CorreccionRegistrada(
    val lugarId: String,
    val fecha: Long,
    val escenaQuedoSegura: Boolean,
)

/**
 * Los cinco estados de un lugar en el Home (sección 5.1/5.9 del maestro). [INICIADO] y
 * [DOMINADO] se derivan de señales reales ya persistidas, sin tabla nueva: un lugar queda
 * "iniciado" en cuanto registra al menos un intento fallido (columna `repaso_pendiente`, que ya
 * existe para la repetición espaciada), y "dominado" en vez de solo "corregido" cuando se
 * completó sin ningún intento fallido registrado.
 */
enum class EstadoLugar { BLOQUEADO, DISPONIBLE, INICIADO, COMPLETADO, DOMINADO }
