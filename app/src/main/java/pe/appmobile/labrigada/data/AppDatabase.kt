package pe.appmobile.labrigada.data

import androidx.room.Database
import androidx.room.RoomDatabase
import pe.appmobile.labrigada.data.dao.CorreccionRegistradaDao
import pe.appmobile.labrigada.data.dao.InsigniaDao
import pe.appmobile.labrigada.data.dao.LugarDao
import pe.appmobile.labrigada.data.dao.ObjetoRiesgoDao
import pe.appmobile.labrigada.data.dao.PerfilDao
import pe.appmobile.labrigada.data.dao.RachaDao
import pe.appmobile.labrigada.data.dao.RepasoPendienteDao
import pe.appmobile.labrigada.data.dao.SimulacroResultadoDao
import pe.appmobile.labrigada.data.entity.CorreccionRegistradaEntity
import pe.appmobile.labrigada.data.entity.InsigniaEntity
import pe.appmobile.labrigada.data.entity.LugarEntity
import pe.appmobile.labrigada.data.entity.ObjetoRiesgoEntity
import pe.appmobile.labrigada.data.entity.PerfilEntity
import pe.appmobile.labrigada.data.entity.RachaEntity
import pe.appmobile.labrigada.data.entity.RepasoPendienteEntity
import pe.appmobile.labrigada.data.entity.SimulacroResultadoEntity

@Database(
    entities = [
        PerfilEntity::class,
        LugarEntity::class,
        ObjetoRiesgoEntity::class,
        CorreccionRegistradaEntity::class,
        SimulacroResultadoEntity::class,
        InsigniaEntity::class,
        RachaEntity::class,
        RepasoPendienteEntity::class,
    ],
    version = 1,
    exportSchema = true,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun perfilDao(): PerfilDao
    abstract fun lugarDao(): LugarDao
    abstract fun objetoRiesgoDao(): ObjetoRiesgoDao
    abstract fun correccionRegistradaDao(): CorreccionRegistradaDao
    abstract fun simulacroResultadoDao(): SimulacroResultadoDao
    abstract fun insigniaDao(): InsigniaDao
    abstract fun rachaDao(): RachaDao
    abstract fun repasoPendienteDao(): RepasoPendienteDao
}
