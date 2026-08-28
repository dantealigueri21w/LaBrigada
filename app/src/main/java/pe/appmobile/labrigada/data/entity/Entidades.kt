package pe.appmobile.labrigada.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "perfil")
data class PerfilEntity(
    @PrimaryKey val id: Int = 1,
    val alias: String,
    val avatarId: Int,
)

@Entity(tableName = "lugar")
data class LugarEntity(
    @PrimaryKey val id: String,
    val nombre: String,
    val orden: Int,
)

@Entity(
    tableName = "objeto_riesgo",
    foreignKeys = [
        ForeignKey(entity = LugarEntity::class, parentColumns = ["id"], childColumns = ["lugarId"], onDelete = ForeignKey.CASCADE),
    ],
    indices = [Index("lugarId")],
)
data class ObjetoRiesgoEntity(
    @PrimaryKey val id: String,
    val lugarId: String,
    val nombre: String,
    val orden: Int,
    val distanciaMinimaDeId: String?,
)

@Entity(
    tableName = "correccion_registrada",
    foreignKeys = [
        ForeignKey(entity = LugarEntity::class, parentColumns = ["id"], childColumns = ["lugarId"], onDelete = ForeignKey.CASCADE),
    ],
    indices = [Index("lugarId")],
)
data class CorreccionRegistradaEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val lugarId: String,
    val fecha: Long,
    val escenaQuedoSegura: Boolean,
)

@Entity(
    tableName = "simulacro_resultado",
    foreignKeys = [
        ForeignKey(entity = LugarEntity::class, parentColumns = ["id"], childColumns = ["lugarId"], onDelete = ForeignKey.CASCADE),
    ],
    indices = [Index("lugarId")],
)
data class SimulacroResultadoEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val lugarId: String,
    val fecha: Long,
    val paso: Boolean,
    val objetosQueFallaronCsv: String,
)

@Entity(tableName = "insignia")
data class InsigniaEntity(
    @PrimaryKey val id: String,
    val nombre: String,
    val descripcion: String,
    val fechaObtenida: Long?,
)

@Entity(tableName = "racha")
data class RachaEntity(
    @PrimaryKey val id: Int = 1,
    val diasConsecutivos: Int,
    val ultimaFechaActividad: Long,
)

@Entity(tableName = "repaso_pendiente")
data class RepasoPendienteEntity(
    @PrimaryKey val itemId: String,
    val fechaUltimoFallo: Long,
    val intervaloDias: Int,
    val proximaRevision: Long,
)
