import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.moview.data.AppDatabase
import com.example.moview.data.User
import com.example.moview.data.UserDao
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@RunWith(AndroidJUnit4::class)
class UserDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var userDao: UserDao

    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()

        userDao = db.userDao()
    }

    @After
    fun teardown() {
        db.close()
    }

    @Test
    fun insertAndRetrieveUser() {
        val user = User(nome = "joao", senha = "1234")
        userDao.insert(user)
        val retrievedUser = userDao.getUserByName("joao")

        assertNotNull(retrievedUser)
        assertEquals("joao", retrievedUser?.nome)
        assertEquals("1234", retrievedUser?.senha)
    }

    @Test
    fun userNotFound() {
        val user = userDao.getUserByName("usuarioinexistente")
        assertEquals(null, user)
    }
}
