package pe.appmobile.labrigada

import android.app.Application
import androidx.room.Room
import pe.appmobile.labrigada.data.AppDatabase
import pe.appmobile.labrigada.data.repository.BrigadaRepository

class LaBrigadaApplication : Application() {
    lateinit var database: AppDatabase
        private set
    lateinit var repository: BrigadaRepository
        private set

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(this, AppDatabase::class.java, "labrigada.db").build()
        repository = BrigadaRepository(database)
    }
}
