package com.rahulrv.mvianimalcompose

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import coil.compose.rememberAsyncImagePainter
import com.rahulrv.mvianimalcompose.api.AnimalService
import com.rahulrv.mvianimalcompose.model.Animal
import com.rahulrv.mvianimalcompose.ui.theme.MVIAnimalComposeTheme
import com.rahulrv.mvianimalcompose.view.MainIntent
import com.rahulrv.mvianimalcompose.view.MainState
import com.rahulrv.mvianimalcompose.view.MainViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels { MainViewModel.Factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val onButtonClick: () -> Unit = {
            lifecycleScope.launch {
                mainViewModel.userIntent.send(MainIntent.FetchAnimals)
            }
        }

        setContent {
            MVIAnimalComposeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(
                        viewModel = mainViewModel,
                        onButtonClick = onButtonClick
                    )
                }
            }
        }
    }
}

@Composable
fun MainScreen(viewModel: MainViewModel, onButtonClick: () -> Unit) {
    val state = viewModel.state.value

    when (state) {
        is MainState.Idle -> IdleScreen(onButtonClick)
        is MainState.Loading -> LoadingScreen()
        is MainState.Animals -> AnimalListScreen(animals = state.animals)
        is MainState.Error -> {
            IdleScreen(onButtonClick)
            Toast.makeText(LocalContext.current, state.error, Toast.LENGTH_SHORT).show()
        }
    }

}

@Composable
fun IdleScreen(
    onButtonClick: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Button(onClick = onButtonClick) {
            Text(text = stringResource(R.string.fetch_animals))
        }
    }
}

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun AnimalListScreen(animals: List<Animal>) {
    LazyColumn {
        items(items = animals) {
            AnimalItem(animalItem = it)
            HorizontalDivider(
                color = Color.Gray,
                modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)
            )
        }
    }
}


@Composable
fun AnimalItem(animalItem: Animal) {
    Row(modifier = Modifier.fillMaxWidth().height(100.dp)) {
        val url = AnimalService.BASE_URL + animalItem.image
        val painter = rememberAsyncImagePainter(model = url)

        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier.size(100.dp),
            contentScale = ContentScale.FillHeight
        )

        Column(modifier = Modifier.fillMaxSize().padding(start = 4.dp)) {
            Text(text = animalItem.name, fontWeight = FontWeight.Bold)
            Text(text = animalItem.location)
        }
    }
}