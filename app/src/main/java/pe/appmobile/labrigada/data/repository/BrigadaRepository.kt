package pe.appmobile.labrigada.data.repository

import pe.appmobile.labrigada.data.AppDatabase
import pe.appmobile.labrigada.data.entity.CorreccionRegistradaEntity
import pe.appmobile.labrigada.data.entity.LugarEntity
import pe.appmobile.labrigada.data.entity.ObjetoRiesgoEntity
import pe.appmobile.labrigada.data.entity.RachaEntity
import pe.appmobile.labrigada.data.entity.RepasoPendienteEntity
import pe.appmobile.labrigada.data.entity.SimulacroResultadoEntity
import pe.appmobile.labrigada.data.seed.SeedData
import pe.appmobile.labrigada.domain.engine.MotorEscena
import pe.appmobile.labrigada.domain.engine.MotorProgreso
import pe.appmobile.labrigada.domain.engine.MotorRepaso
import pe.appmobile.labrigada.domain.engine.MotorSimulacro
import pe.appmobile.labrigada.domain.model.CorreccionRegistrada
import pe.appmobile.labrigada.domain.model.Escena
import pe.appmobile.labrigada.domain.model.RepasoPendiente
import pe.appmobile.labrigada.domain.model.ResultadoSimulacro

class BrigadaRepository(private val db: AppDatabase) {

    suspend fun sembrarSiEsPrimerLanzamiento() {
        if (db.lugarDao().obtenerTodos().isNotEmpty()) return
        db.lugarDao().insertarTodos(SeedData.lugares)
        db.objetoRiesgoDao().insertarTodos(SeedData.objetos)
        db.insigniaDao().insertarTodas(SeedData.insignias)
    }

    suspend fun obtenerLugares(): List<LugarEntity> = db.lugarDao().obtenerTodos()

    suspend fun obtenerObjetosDeLugar(lugarId: String): List<ObjetoRiesgoEntity> =
        db.objetoRiesgoDao().obtenerPorLugar(lugarId)

    suspend fun registrarCorreccion(lugarId: String, escena: Escena): Boolean {
        val segura = MotorEscena.esEscenaSegura(escena)
        val ahora = System.currentTimeMillis()
        if (segura) {
            db.correccionRegistradaDao().insertar(
                CorreccionRegistradaEntity(lugarId = lugarId, fecha = ahora, escenaQuedoSegura = true),
            )
            actualizarProgreso()
        } else {
            registrarIntentoFallido(lugarId, ahora)
        }
        return segura
    }

    suspend fun evaluarSimulacroFinal(escena: Escena): ResultadoSimulacro {
        val resultado = MotorSimulacro.evaluarSimulacro(escena)
        db.simulacroResultadoDao().insertar(
            SimulacroResultadoEntity(
                lugarId = escena.id,
                fecha = System.currentTimeMillis(),
                paso = resultado.paso,
                objetosQueFallaronCsv = resultado.objetosQueFallaron.joinToString(","),
            ),
        )
        if (resultado.paso) {
            val yaGanada = "simulacro_superado" in db.insigniaDao().obtenerIdsGanadas()
            if (!yaGanada) {
                db.insigniaDao().marcarObtenida("simulacro_superado", System.currentTimeMillis())
            }
        }
        return resultado
    }

    suspend fun obtenerPendientesDeRepasoHoy(hoy: Long): List<RepasoPendiente> =
        db.repasoPendienteDao().obtenerPendientesParaHoy(hoy).map {
            RepasoPendiente(it.itemId, it.fechaUltimoFallo, it.intervaloDias, it.proximaRevision)
        }

    private suspend fun registrarIntentoFallido(lugarId: String, ahora: Long) {
        val existente = db.repasoPendienteDao().obtenerPorId(lugarId)
        val nuevoIntervalo = MotorRepaso.calcularProximoIntervalo(existente?.intervaloDias ?: 1, acerto = false)
        db.repasoPendienteDao().guardar(
            RepasoPendienteEntity(
                itemId = lugarId,
                fechaUltimoFallo = ahora,
                intervaloDias = nuevoIntervalo,
                proximaRevision = MotorRepaso.calcularProximaRevision(ahora, nuevoIntervalo),
            ),
        )
    }

    private suspend fun actualizarProgreso() {
        val historial = db.correccionRegistradaDao().obtenerTodas().map {
            CorreccionRegistrada(lugarId = it.lugarId, fecha = it.fecha, escenaQuedoSegura = it.escenaQuedoSegura)
        }
        val yaGanadas = db.insigniaDao().obtenerIdsGanadas().toSet()
        val nuevas = MotorProgreso.calcularNuevasInsignias(historial, yaGanadas)
        val ahora = System.currentTimeMillis()
        nuevas.forEach { db.insigniaDao().marcarObtenida(it, ahora) }

        val racha = MotorProgreso.calcularRacha(historial, hoy = ahora)
        db.rachaDao().guardar(RachaEntity(diasConsecutivos = racha, ultimaFechaActividad = ahora))
    }
}
