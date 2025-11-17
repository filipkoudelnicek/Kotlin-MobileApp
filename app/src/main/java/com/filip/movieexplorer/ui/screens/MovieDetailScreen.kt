package com.filip.movieexplorer.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.filip.movieexplorer.MovieExplorerApp
import com.filip.movieexplorer.R
import com.filip.movieexplorer.ui.detail.MovieDetailUiState
import com.filip.movieexplorer.ui.detail.MovieDetailViewModel

@Composable
fun MovieDetailRoute(
    imdbId: String,
    onNavigateBack: () -> Unit
) {
    val app = LocalContext.current.applicationContext as MovieExplorerApp
    val viewModel: MovieDetailViewModel = viewModel(
        factory = MovieDetailViewModel.provideFactory(app.appContainer.movieRepository)
    )
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(imdbId) {
        viewModel.loadMovie(imdbId)
    }

    MovieDetailScreen(
        state = uiState,
        onNavigateBack = onNavigateBack,
        onRetry = { viewModel.loadMovie(imdbId) },
        onToggleFavorite = { viewModel.toggleFavorite() }
    )
}

@Composable
fun MovieDetailScreen(
    state: MovieDetailUiState,
    onNavigateBack: () -> Unit,
    onRetry: () -> Unit,
    onToggleFavorite: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = state.movie?.title ?: "Detail filmu")
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = onToggleFavorite, enabled = state.movie != null) {
                        val icon = if (state.isFavorite) Icons.Default.Star else Icons.Outlined.StarOutline
                        Icon(
                            imageVector = icon,
                            contentDescription = if (state.isFavorite) "Remove from favorites" else "Add to favorites"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { paddingValues ->
        when {
            state.isLoading -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Načítám detail filmu…")
                }
            }

            state.errorMessage != null -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = state.errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    TextButton(onClick = onRetry) {
                        Text("Zkusit znovu")
                    }
                }
            }

            state.movie != null -> {
                MovieDetailContent(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    uiState = state
                )
            }
        }
    }
}

@Composable
private fun MovieDetailContent(
    modifier: Modifier = Modifier,
    uiState: MovieDetailUiState
) {
    val movie = uiState.movie ?: return
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.Start
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(movie.posterUrl.takeIf { it.isNotBlank() })
                .crossfade(true)
                .build(),
            contentDescription = movie.title,
            modifier = Modifier
                .fillMaxWidth()
                .height(320.dp),
            contentScale = ContentScale.Crop,
            placeholder = painterResource(id = R.drawable.ic_launcher_foreground),
            error = painterResource(id = R.drawable.ic_launcher_foreground)
        )

        Text(
            text = movie.title,
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
        )
        Text(text = "${movie.year} • ${movie.genre}")
        Text(text = "Režisér: ${movie.director}")
        if (movie.actors.isNotBlank()) {
            Text(text = "Herci: ${movie.actors}")
        }
        if (movie.imdbRating.isNotBlank()) {
            Text(text = "IMDb rating: ${movie.imdbRating}")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = movie.plot,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}


