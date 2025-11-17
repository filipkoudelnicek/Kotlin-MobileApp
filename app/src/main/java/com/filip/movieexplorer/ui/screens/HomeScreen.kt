package com.filip.movieexplorer.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.filip.movieexplorer.MovieExplorerApp
import com.filip.movieexplorer.R
import com.filip.movieexplorer.domain.model.MovieSummary
import com.filip.movieexplorer.ui.home.HomeUiState
import com.filip.movieexplorer.ui.home.HomeViewModel

@Composable
fun HomeRoute(
    onNavigateToDetails: (String) -> Unit,
    onNavigateToFavorites: () -> Unit
) {
    val app = LocalContext.current.applicationContext as MovieExplorerApp
    val viewModel: HomeViewModel = viewModel(
        factory = HomeViewModel.provideFactory(app.appContainer.movieRepository)
    )
    val uiState by viewModel.uiState.collectAsState()
    var query by rememberSaveable { mutableStateOf(uiState.lastQuery) }

    LaunchedEffect(uiState.lastQuery) {
        if (!uiState.isLoading && uiState.lastQuery != query) {
            query = uiState.lastQuery
        }
    }

    HomeScreen(
        query = query,
        state = uiState,
        onQueryChange = { query = it },
        onSearch = { viewModel.search(query) },
        onMovieClick = onNavigateToDetails,
        onToggleFavorite = { viewModel.toggleFavorite(it) },
        onNavigateToFavorites = onNavigateToFavorites,
        onRetry = { viewModel.search(query.ifBlank { uiState.lastQuery }) }
    )
}

@Composable
fun HomeScreen(
    query: String,
    state: HomeUiState,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    onMovieClick: (String) -> Unit,
    onToggleFavorite: (MovieSummary) -> Unit,
    onNavigateToFavorites: () -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Movie Explorer") },
                actions = {
                    IconButton(onClick = onNavigateToFavorites) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Favorites"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = query,
                onValueChange = onQueryChange,
                label = { Text("Search movies") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Button(
                onClick = onSearch,
                enabled = query.isNotBlank(),
                modifier = Modifier.align(Alignment.End)
            ) {
                Icon(imageVector = Icons.Default.Search, contentDescription = null)
                Spacer(modifier = Modifier.size(8.dp))
                Text("Hledat")
            }

            if (state.isLoading) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                    Text("Načítám výsledky…")
                }
            }

            state.errorMessage?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error
                )
                TextButton(onClick = onRetry) {
                    Text("Zkusit znovu")
                }
            }

            when {
                state.movies.isEmpty() && !state.isLoading && state.errorMessage == null -> {
                    Text(
                        text = "Začni vyhledávat podle názvu filmu.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                else -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(state.movies, key = { it.imdbId }) { movie ->
                            MovieListItem(
                                movie = movie,
                                isFavorite = state.favoriteIds.contains(movie.imdbId),
                                onClick = { onMovieClick(movie.imdbId) },
                                onToggleFavorite = { onToggleFavorite(movie) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MovieListItem(
    movie: MovieSummary,
    isFavorite: Boolean,
    onClick: () -> Unit,
    onToggleFavorite: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(movie.posterUrl.takeIf { it.isNotBlank() })
                    .crossfade(true)
                    .build(),
                contentDescription = movie.title,
                modifier = Modifier
                    .size(72.dp),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.ic_launcher_foreground),
                error = painterResource(id = R.drawable.ic_launcher_foreground)
            )
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(text = movie.year)
                Text(text = movie.type.uppercase())
            }
            IconButton(onClick = onToggleFavorite) {
                val icon = if (isFavorite) Icons.Filled.Star else Icons.Outlined.StarOutline
                Icon(
                    imageVector = icon,
                    contentDescription = if (isFavorite) "Remove from favorites" else "Add to favorites"
                )
            }
        }
    }
}

