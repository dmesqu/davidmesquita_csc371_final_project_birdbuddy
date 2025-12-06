package com.example.davidmesquita_csc371_final_project_birdbuddy.data

import android.content.Context
import androidx.room.*

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "username") val username: String,
    @ColumnInfo(name = "password") val password: String
)

@Entity(tableName = "birds")
data class BirdEntity(
    @PrimaryKey val id: Long,
    val commonName: String,
    val latinName: String,
    val mainColorGroup: String,
    val sizeGroup: String,
    val habitatGroup: String,
    val funFact: String
)

@Entity(
    tableName = "user_birds",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = BirdEntity::class,
            parentColumns = ["id"],
            childColumns = ["birdId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("userId"), Index("birdId")]
)
data class UserBirdEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: Long,
    val birdId: Long,
    val timestamp: Long = System.currentTimeMillis(),
    val imageUri: String? = null
)

//daaos
@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertUser(user: UserEntity): Long

    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    suspend fun getUserByUsername(username: String): UserEntity?

    @Query("SELECT * FROM users WHERE username = :username AND password = :password LIMIT 1")
    suspend fun login(username: String, password: String): UserEntity?
}

@Dao
interface BirdDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBirds(birds: List<BirdEntity>)

    @Query("SELECT COUNT(*) FROM birds")
    suspend fun countBirds(): Int

    @Query("SELECT * FROM birds ORDER BY commonName ASC")
    suspend fun getAllBirds(): List<BirdEntity>

    @Query("SELECT * FROM birds WHERE id = :id LIMIT 1")
    suspend fun getBirdById(id: Long): BirdEntity?

    @Query(
        """
        SELECT * FROM birds
        WHERE (:size IS NULL OR sizeGroup = :size)
        AND (:color IS NULL OR mainColorGroup = :color)
        AND (:habitat IS NULL OR habitatGroup = :habitat)
        ORDER BY commonName ASC
    """
    )
    suspend fun filterBirds(
        size: String?,
        color: String?,
        habitat: String?
    ): List<BirdEntity>
}

@Dao
interface UserBirdDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUserBird(userBirdEntity: UserBirdEntity): Long

    @Query(
        """
        SELECT b.* FROM birds b
        INNER JOIN user_birds ub ON b.id = ub.birdId
        WHERE ub.userId = :userId
        ORDER BY ub.timestamp DESC
    """
    )
    suspend fun getBirdsForUser(userId: Long): List<BirdEntity>

    @Query(
        """
        SELECT COUNT(*) FROM user_birds
        WHERE userId = :userId AND birdId = :birdId
    """
    )
    suspend fun userHasBird(userId: Long, birdId: Long): Int

    @Query(
        """
        SELECT * FROM user_birds
        WHERE userId = :userId AND birdId = :birdId
        LIMIT 1
    """
    )
    suspend fun getUserBird(userId: Long, birdId: Long): UserBirdEntity?

    @Query(
        """
        UPDATE user_birds
        SET imageUri = :imageUri
        WHERE userId = :userId AND birdId = :birdId
    """
    )
    suspend fun updateImageForUserBird(
        userId: Long,
        birdId: Long,
        imageUri: String?
    )
}

//db
@Database(
    entities = [UserEntity::class, BirdEntity::class, UserBirdEntity::class],
    version = 2,
    exportSchema = false
)
abstract class BirdBuddyDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun birdDao(): BirdDao
    abstract fun userBirdDao(): UserBirdDao
    companion object {
        @Volatile
        private var INSTANCE: BirdBuddyDatabase? = null

        fun getInstance(context: Context): BirdBuddyDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BirdBuddyDatabase::class.java,
                    "bird_buddy_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
