@file:OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalLayoutApi::class
)

package com.example.davidmesquita_csc371_final_project_birdbuddy.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DrawableRes
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.davidmesquita_csc371_final_project_birdbuddy.R
import com.example.davidmesquita_csc371_final_project_birdbuddy.data.BirdEntity
import com.example.davidmesquita_csc371_final_project_birdbuddy.ui.navigation.Screen
import com.example.davidmesquita_csc371_final_project_birdbuddy.viewmodel.BirdViewModel
import com.example.davidmesquita_csc371_final_project_birdbuddy.viewmodel.UserBirdDisplay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GradientTopAppBar(
    title: String,
    showBack: Boolean = false,
    onBack: (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {}
) {
    val gradient = Brush.horizontalGradient(
        listOf(
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.tertiary
        )
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(gradient)
    ) {
        TopAppBar(
            title = { Text(title) },
            navigationIcon = {
                if (showBack && onBack != null) {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            },
            actions = actions,
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent,
                titleContentColor = Color.White,
                navigationIconContentColor = Color.White,
                actionIconContentColor = Color.White
            )
        )
    }
}

@Composable
private fun InfoPill(text: String) {
    Surface(
        shape = RoundedCornerShape(50),
        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
        )
    }
}

@DrawableRes
private fun defaultBirdImageRes(bird: BirdEntity): Int {
    return when (bird.commonName) {
        "Blue Jay" -> R.drawable.bird_blue_jay
        "Northern Cardinal" -> R.drawable.bird_northern_cardinal
        "American Robin" -> R.drawable.bird_american_robin
        "House Sparrow" -> R.drawable.bird_house_sparrow
        "Mourning Dove" -> R.drawable.bird_mourning_dove
        "American Goldfinch" -> R.drawable.bird_american_goldfinch
        "Black-capped Chickadee" -> R.drawable.bird_black_capped_chickadee
        "Tufted Titmouse" -> R.drawable.bird_tufted_titmouse
        "Downy Woodpecker" -> R.drawable.bird_downy_woodpecker
        "Red-winged Blackbird" -> R.drawable.bird_red_winged_blackbird
        "Canada Goose" -> R.drawable.bird_canada_goose
        "Mallard" -> R.drawable.bird_mallard
        "Eastern Bluebird" -> R.drawable.bird_eastern_bluebird
        "European Starling" -> R.drawable.bird_european_starling
        "Gray Catbird",
        "Grey Catbird",
        "Grey Cat Bird",
        "Gray Cat Bird" -> R.drawable.bird_gray_catbird
        else -> R.drawable.icon
    }
}

//home
@Composable
fun HomeScreen(
    navController: NavController,
    birdViewModel: BirdViewModel,
    onLogout: () -> Unit
) {
    val uiState by birdViewModel.uiState.collectAsState()
    Scaffold(
        topBar = {
            GradientTopAppBar(
                title = "Bird Buddy",
                showBack = false,
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(
                            imageVector = Icons.Default.ExitToApp,
                            contentDescription = "Logout"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(20.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.icon),
                contentDescription = "Bird Buddy icon",
                modifier = Modifier.size(96.dp)
            )
            Text(
                text = "Discover and remember the birds you see outside.",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                HomeActionCard(
                    title = "Identify a Bird",
                    subtitle = "Answer a few simple questions and we’ll suggest birds you might have seen.",
                    onClick = { navController.navigate(Screen.Identify.route) }
                )
                HomeActionCard(
                    title = "My Bird Collection",
                    subtitle = "See all the birds you’ve saved so far.",
                    badgeText = "${uiState.myBirds.size} saved",
                    onClick = { navController.navigate(Screen.MyCollection.route) }
                )
                HomeActionCard(
                    title = "All Birds in Bird Buddy",
                    subtitle = "Browse every bird included in the app.",
                    onClick = { navController.navigate(Screen.AllBirds.route) }
                )
            }
        }
    }
}

@Composable
private fun HomeActionCard(
    title: String,
    subtitle: String,
    badgeText: String? = null,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge
                )
                if (badgeText != null) {
                    AssistChip(onClick = onClick, label = { Text(badgeText) })
                }
            }
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun IdentifyBirdScreen(
    navController: NavController,
    birdViewModel: BirdViewModel
) {
    val uiState by birdViewModel.uiState.collectAsState()
    val size = uiState.identifyAnswers.size
    val color = uiState.identifyAnswers.color
    val habitat = uiState.identifyAnswers.habitat
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        birdViewModel.resetIdentify()
    }
    var showAddPhotoDialog by remember { mutableStateOf(false) }
    var pendingBirdToAdd by remember { mutableStateOf<BirdEntity?>(null) }
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        val bird = pendingBirdToAdd
        if (bird != null) {
            if (uri != null) {
                try {
                    context.contentResolver.takePersistableUriPermission(
                        uri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                } catch (_: SecurityException) {
                }
            }
            birdViewModel.addToMyBirds(bird, uri?.toString())
            pendingBirdToAdd = null
            showAddPhotoDialog = false
        }
    }
    Scaffold(
        topBar = {
            GradientTopAppBar(
                title = "Identify a Bird",
                showBack = true,
                onBack = { navController.popBackStack() }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        text = "Answer these simple questions and we’ll suggest birds you might have seen!",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    QuestionRow(
                        stepLabel = "Step 1 of 3",
                        label = "How big was the bird?",
                        options = listOf("Small", "Medium", "Large"),
                        selected = size,
                        onSelect = { birdViewModel.updateSize(it) }
                    )
                    QuestionRow(
                        stepLabel = "Step 2 of 3",
                        label = "What color was it mostly?",
                        options = listOf("Red/Orange", "Blue", "Yellow", "Brown/Gray", "Black/White"),
                        selected = color,
                        onSelect = { birdViewModel.updateColor(it) }
                    )
                    QuestionRow(
                        stepLabel = "Step 3 of 3",
                        label = "Where did you see it?",
                        options = listOf("Backyard/Tree", "Ground/Lawn", "Feeder", "Near Water"),
                        selected = habitat,
                        onSelect = { birdViewModel.updateHabitat(it) }
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        TextButton(onClick = { birdViewModel.resetIdentify() }) {
                            Text("Start over")
                        }
                    }
                }
            }
            if (uiState.identifyMatches.isEmpty() && (size != null || color != null || habitat != null)) {
                Text(
                    "Hmm, we don't have a perfect match yet. Try changing one of your answers!",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            } else if (uiState.identifyMatches.isNotEmpty()) {
                Text(
                    text = "Here are some birds you might have seen:",
                    style = MaterialTheme.typography.titleMedium
                )
                uiState.identifyMatches.forEach { bird ->
                    BirdCard(
                        bird = bird,
                        onClick = {
                            navController.navigate(Screen.BirdDetail.createRoute(bird.id))
                        },
                        onAddToCollection = {
                            pendingBirdToAdd = bird
                            showAddPhotoDialog = true
                        },
                        userImageUri = null
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
            } else {
                Text(
                    text = "Start by choosing the bird's size, color, and where you saw it.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            uiState.message?.let { msg ->
                Text(
                    text = msg,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = { navController.popBackStack() }) {
                    Text("Back")
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
        if (showAddPhotoDialog && pendingBirdToAdd != null) {
            AlertDialog(
                onDismissRequest = {
                    showAddPhotoDialog = false
                    pendingBirdToAdd = null
                },
                title = { Text("Add a photo?") },
                text = {
                    Text(
                        "Would you like to add a picture of this bird to your collection now? " +
                                "You can always add or change it later."
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            photoPickerLauncher.launch(arrayOf("image/*"))
                        }
                    ) {
                        Text("Yes, add photo")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            pendingBirdToAdd?.let { bird ->
                                birdViewModel.addToMyBirds(bird, null)
                            }
                            pendingBirdToAdd = null
                            showAddPhotoDialog = false
                        }
                    ) {
                        Text("No, just save bird")
                    }
                }
            )
        }
    }
}

@Composable
private fun QuestionRow(
    stepLabel: String,
    label: String,
    options: List<String>,
    selected: String?,
    onSelect: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = stepLabel,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Text(label, style = MaterialTheme.typography.titleMedium)
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            options.forEach { option ->
                val isSelected = selected == option
                val scale by animateFloatAsState(
                    targetValue = if (isSelected) 1.05f else 1f,
                    animationSpec = tween(durationMillis = 150),
                    label = "chipScale"
                )
                FilterChip(
                    selected = isSelected,
                    onClick = { onSelect(option) },
                    label = { Text(option) },
                    modifier = Modifier.graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                    }
                )
            }
        }
    }
}

//collection
@Composable
fun MyCollectionScreen(
    navController: NavController,
    birdViewModel: BirdViewModel
) {
    val uiState by birdViewModel.uiState.collectAsState()
    Scaffold(
        topBar = {
            GradientTopAppBar(
                title = "My Bird Collection",
                showBack = true,
                onBack = { navController.popBackStack() }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            if (uiState.myBirds.isEmpty()) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.icon),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(72.dp)
                        )
                        Text(
                            text = "You haven't saved any birds yet.",
                            style = MaterialTheme.typography.titleMedium,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "Try identifying a bird and adding it to your collection!",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(uiState.myBirds) { userBird: UserBirdDisplay ->
                        BirdCard(
                            bird = userBird.bird,
                            onClick = {
                                navController.navigate(
                                    Screen.BirdDetail.createRoute(userBird.bird.id)
                                )
                            },
                            onAddToCollection = null,
                            userImageUri = userBird.imageUri
                        )
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = { navController.popBackStack() }) {
                    Text("Back")
                }
            }
        }
    }
}

//all
@Composable
fun AllBirdsScreen(
    navController: NavController,
    birdViewModel: BirdViewModel
) {
    val uiState by birdViewModel.uiState.collectAsState()
    Scaffold(
        topBar = {
            GradientTopAppBar(
                title = "All Birds in Bird Buddy",
                showBack = true,
                onBack = { navController.popBackStack() }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(uiState.allBirds) { bird ->
                    BirdCard(
                        bird = bird,
                        onClick = {
                            navController.navigate(Screen.BirdDetail.createRoute(bird.id))
                        },
                        onAddToCollection = null,
                        userImageUri = null
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = { navController.popBackStack() }) {
                    Text("Back")
                }
            }
        }
    }
}

//bird detail
@Composable
fun BirdDetailScreen(
    birdId: Long,
    birdViewModel: BirdViewModel,
    navController: NavController
) {
    val uiState by birdViewModel.uiState.collectAsState()
    val bird = uiState.allBirds.find { it.id == birdId }
    val context = LocalContext.current
    LaunchedEffect(birdId) {
        birdViewModel.loadCurrentBirdImage(birdId)
    }
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        if (uri != null) {
            try {
                context.contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            } catch (_: SecurityException) {
            }
        }
        birdViewModel.updateCurrentBirdImage(birdId, uri?.toString())
    }
    Scaffold(
        topBar = {
            GradientTopAppBar(
                title = bird?.commonName ?: "Bird Details",
                showBack = true,
                onBack = { navController.popBackStack() }
            )
        }
    ) { padding ->
        if (bird == null) {
            Box(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Bird not found.")
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(20.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = bird.commonName,
                            style = MaterialTheme.typography.headlineSmall
                        )
                        Text(
                            text = bird.latinName,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Divider()
                        Text("Size: ${bird.sizeGroup}")
                        Text("Main color: ${bird.mainColorGroup}")
                        Text("Habitat: ${bird.habitatGroup}")
                    }
                }
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.06f)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "Fun fact",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                            Text(
                                text = bird.funFact,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Your photo of this bird",
                            style = MaterialTheme.typography.titleMedium
                        )
                        val imageUri = uiState.currentBirdImageUri
                        if (imageUri != null) {
                            AsyncImage(
                                model = imageUri,
                                contentDescription = "Photo of ${bird.commonName}",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(240.dp)
                                    .clip(RoundedCornerShape(16.dp)),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Text(
                                text = "You haven't added a photo of this bird yet.",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Button(
                                onClick = { photoPickerLauncher.launch(arrayOf("image/*")) }
                            ) {
                                Text(if (imageUri == null) "Add photo" else "Change photo")
                            }

                            if (imageUri != null) {
                                TextButton(
                                    onClick = {
                                        birdViewModel.updateCurrentBirdImage(birdId, null)
                                    }
                                ) {
                                    Text("Remove photo")
                                }
                            }
                        }
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = { navController.popBackStack() }) {
                        Text("Back")
                    }
                }
            }
        }
    }
}

//bird c ard
@Composable
fun BirdCard(
    bird: BirdEntity,
    onClick: () -> Unit,
    onAddToCollection: (() -> Unit)?,
    userImageUri: String? = null
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.tertiary
                            )
                        )
                    )
            )
            Row(
                modifier = Modifier
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    if (userImageUri != null) {
                        AsyncImage(
                            model = userImageUri,
                            contentDescription = "Photo of ${bird.commonName}",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Image(
                            painter = painterResource(defaultBirdImageRes(bird)),
                            contentDescription = bird.commonName,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = bird.commonName,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = bird.latinName,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        InfoPill(bird.sizeGroup)
                        InfoPill(bird.mainColorGroup)
                        InfoPill(bird.habitatGroup)
                    }
                }
            }
            if (onAddToCollection != null) {
                Divider()
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Button(onClick = onAddToCollection) {
                        Text("Add to my collection")
                    }
                }
            }
        }
    }
}
