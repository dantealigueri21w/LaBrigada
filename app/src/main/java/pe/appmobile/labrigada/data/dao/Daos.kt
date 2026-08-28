package pe.appmobile.labrigada.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import pe.appmobile.labrigada.data.entity.CorreccionRegistradaEntity
import pe.appmobile.labrigada.data.entity.InsigniaEntity
import pe.appmobile.labrigada.data.entity.LugarEntity
import pe.appmobile.labrigada.data.entity.ObjetoRiesgoEntity
import pe.appmobile.labrigada.data.entity.PerfilEntity
import pe.appmobile.labrigada.data.entity.RachaEntity
import pe.appmobile.labrigada.data.entity.RepasoPendienteEntity
import pe.appmobile.labrigada.data.entity.SimulacroResultadoEntity

@Dao
interface PerfilDao {
    @Query("SELECT * FROM perfil WHERE id = 1")
    suspend fun obtener(): PerfilEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun guardar(perfil: PerfilEntity)
}

@Dao
interface LugarDao {
    @Query("SELECT * FROM lugar ORDER BY orden")
    suspend fun obtenerTodos(): List<LugarEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarTodos(lugares: List<LugarEntity>)

    @Query("DELETE FROM lugar WHERE id = :id")
    suspend fun eliminar(id: String)
}

@Dao
interface ObjetoRiesgoDao {
    @Query("SELECT * FROM objeto_riesgo WHERE lugarId = :lugarId ORDER BY orden")
    suspend fun obtenerPorLugar(lugarId: String): List<ObjetoRiesgoEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarTodos(objetos: List<ObjetoRiesgoEntity>)
}

@Dao
interface CorreccionRegistradaDao {
    @Insert
    suspend fun insertar(correccion: CorreccionRegistradaEntity): Long

    @Query("SELECT * FROM correccion_registrada ORDER BY fecha")
    suspend fun obtenerTodas(): List<CorreccionRegistradaEntity>
}

@Dao
interface SimulacroResultadoDao {
    @Insert
    suspend fun insertar(resultado: SimulacroResultadoEntity)

    @Query("SELECT * FROM simulacro_resultado ORDER BY fecha")
    suspend fun obtenerTodos(): List<SimulacroResultadoEntity>
}

@Dao
interface InsigniaDao {
    @Query("SELECT * FROM insignia")
    suspend fun obtenerTodas(): List<InsigniaEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarTodas(insignias: List<InsigniaEntity>)

    @Query("UPDATE insignia SET fechaObtenida = :fecha WHERE id = :insigniaId")
    suspend fun marcarObtenida(insigniaId: String, fecha: Long)

    @Query("SELECT id FROM insignia WHERE fechaObtenida IS NOT NULL")
    suspend fun obtenerIdsGanadas(): List<String>
}

@Dao
interface RachaDao {
    @Query("SELECT * FROM racha WHERE id = 1")
    suspend fun obtener(): RachaEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun guardar(racha: RachaEntity)
}

@Dao
interface RepasoPendienteDao {
    @Query("SELECT * FROM repaso_pendiente WHERE proximaRevision <= :hoy")
    suspend fun obtenerPendientesParaHoy(hoy: Long): List<RepasoPendienteEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun guardar(item: RepasoPendienteEntity)

    @Query("SELECT * FROM repaso_pendiente WHERE itemId = :itemId")
    suspend fun obtenerPorId(itemId: String): RepasoPendienteEntity?
}
