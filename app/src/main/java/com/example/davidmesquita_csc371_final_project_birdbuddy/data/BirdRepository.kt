package com.example.davidmesquita_csc371_final_project_birdbuddy.data

class BirdRepository(
    private val userDao: UserDao,
    private val birdDao: BirdDao,
    private val userBirdDao: UserBirdDao
) {

    //auth
    suspend fun registerUser(username: String, password: String): Result<UserEntity> {
        val existing = userDao.getUserByUsername(username)
        if (existing != null) {
            return Result.failure(IllegalArgumentException("Username already taken"))
        }
        val id = userDao.insertUser(UserEntity(username = username, password = password))
        val created = userDao.getUserByUsername(username)
        return if (id > 0 && created != null) {
            Result.success(created)
        } else {
            Result.failure(IllegalStateException("Could not create user"))
        }
    }

    suspend fun login(username: String, password: String): Result<UserEntity> {
        val user = userDao.login(username, password)
        return if (user != null) Result.success(user)
        else Result.failure(IllegalArgumentException("Invalid username or password"))
    }

    //seed birds
    suspend fun ensureBirdsSeeded() {
        val count = birdDao.countBirds()
        if (count == 0) {
            birdDao.insertBirds(preloadedBirds)
        }
    }

    //birds
    suspend fun getAllBirds(): List<BirdEntity> = birdDao.getAllBirds()

    suspend fun filterBirds(
        size: String?,
        color: String?,
        habitat: String?
    ): List<BirdEntity> = birdDao.filterBirds(size, color, habitat)

    suspend fun getBirdById(id: Long): BirdEntity? = birdDao.getBirdById(id)

    //user collection
    suspend fun addBirdToUser(userId: Long, birdId: Long, imageUri: String?): Boolean {
        val existing = userBirdDao.getUserBird(userId, birdId)
        return if (existing != null) {
            if (imageUri != null && imageUri != existing.imageUri) {
                userBirdDao.updateImageForUserBird(userId, birdId, imageUri)
            }
            false
        } else {
            val rowId = userBirdDao.insertUserBird(
                UserBirdEntity(
                    userId = userId,
                    birdId = birdId,
                    imageUri = imageUri
                )
            )
            rowId > 0
        }
    }

    suspend fun getBirdsForUser(userId: Long): List<BirdEntity> =
        userBirdDao.getBirdsForUser(userId)

    suspend fun getUserBirdImage(userId: Long, birdId: Long): String? =
        userBirdDao.getUserBird(userId, birdId)?.imageUri

    suspend fun updateUserBirdImage(userId: Long, birdId: Long, imageUri: String?) {
        userBirdDao.updateImageForUserBird(userId, birdId, imageUri)
    }

    //perloaded birds
    private val preloadedBirds: List<BirdEntity> = listOf(
        BirdEntity(
            id = 1,
            commonName = "American Robin",
            latinName = "Turdus migratorius",
            mainColorGroup = "Red/Orange",
            sizeGroup = "Medium",
            habitatGroup = "Ground/Lawn",
            funFact = "Robins love hunting worms in grass after it rains."
        ),
        BirdEntity(
            id = 2,
            commonName = "Northern Cardinal",
            latinName = "Cardinalis cardinalis",
            mainColorGroup = "Red/Orange",
            sizeGroup = "Medium",
            habitatGroup = "Backyard/Tree",
            funFact = "Male cardinals are bright red and sing loud, whistly songs."
        ),
        BirdEntity(
            id = 3,
            commonName = "Blue Jay",
            latinName = "Cyanocitta cristata",
            mainColorGroup = "Blue",
            sizeGroup = "Medium",
            habitatGroup = "Backyard/Tree",
            funFact = "Blue Jays are very smart and love to visit backyard feeders."
        ),
        BirdEntity(
            id = 4,
            commonName = "American Goldfinch",
            latinName = "Spinus tristis",
            mainColorGroup = "Yellow",
            sizeGroup = "Small",
            habitatGroup = "Feeder",
            funFact = "Goldfinches are bright yellow in summer and love sunflower seeds."
        ),
        BirdEntity(
            id = 5,
            commonName = "Black-capped Chickadee",
            latinName = "Poecile atricapillus",
            mainColorGroup = "Black/White",
            sizeGroup = "Small",
            habitatGroup = "Backyard/Tree",
            funFact = "Chickadees say their own name in their 'chick-a-dee-dee' call."
        ),
        BirdEntity(
            id = 6,
            commonName = "Mourning Dove",
            latinName = "Zenaida macroura",
            mainColorGroup = "Brown/Gray",
            sizeGroup = "Medium",
            habitatGroup = "Ground/Lawn",
            funFact = "Their soft 'coo-oo' song can sound a little like someone crying."
        ),
        BirdEntity(
            id = 7,
            commonName = "House Sparrow",
            latinName = "Passer domesticus",
            mainColorGroup = "Brown/Gray",
            sizeGroup = "Small",
            habitatGroup = "Ground/Lawn",
            funFact = "House sparrows are very common around buildings and sidewalks."
        ),
        BirdEntity(
            id = 8,
            commonName = "European Starling",
            latinName = "Sturnus vulgaris",
            mainColorGroup = "Black/White",
            sizeGroup = "Medium",
            habitatGroup = "Feeder",
            funFact = "Starlings can gather in huge flocks that swirl like clouds."
        ),
        BirdEntity(
            id = 9,
            commonName = "Downy Woodpecker",
            latinName = "Dryobates pubescens",
            mainColorGroup = "Black/White",
            sizeGroup = "Small",
            habitatGroup = "Backyard/Tree",
            funFact = "The downy woodpecker is the smallest woodpecker in North America."
        ),
        BirdEntity(
            id = 10,
            commonName = "Eastern Bluebird",
            latinName = "Sialia sialis",
            mainColorGroup = "Blue",
            sizeGroup = "Small",
            habitatGroup = "Backyard/Tree",
            funFact = "The Eastern Bluebird is New York's official state bird."
        ),
        BirdEntity(
            id = 11,
            commonName = "Gray Catbird",
            latinName = "Dumetella carolinensis",
            mainColorGroup = "Brown/Gray",
            sizeGroup = "Medium",
            habitatGroup = "Backyard/Tree",
            funFact = "Catbirds can mimic other birds and even make meowing sounds."
        ),
        BirdEntity(
            id = 12,
            commonName = "Canada Goose",
            latinName = "Branta canadensis",
            mainColorGroup = "Brown/Gray",
            sizeGroup = "Large",
            habitatGroup = "Near Water",
            funFact = "Canada Geese fly in big V-shaped flocks and honk loudly."
        )
    )
}
