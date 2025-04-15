package com.octopus.edu.kotlin.spacex

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.octopus.edu.kotlin.spacex.core.ui.SpaceXDestination
import com.octopus.edu.kotlin.spacex.core.ui.SpaceXNavigation
import com.octopus.edu.kotlin.spacex.databinding.MainActivityBinding
import com.octopus.edu.kotlin.spacex.feature.launches.list.LaunchesFragmentDirections.Companion.toLaunchDetails
import com.octopus.edu.kotlin.spacex.utils.safelyNavigate
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), SpaceXNavigation {

    lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        enableEdgeToEdge()
//        setContent {
//            SpaceXTheme {
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    Greeting(
//                        name = "Android",
//                        modifier = Modifier.padding(innerPadding)
//                    )
//                }
//            }
//        }
    }

    override fun navigate(destination: SpaceXDestination){
        when (destination){
            is SpaceXDestination.LaunchDetails -> {
                findNavController(R.id.mainHost).safelyNavigate(
                    toLaunchDetails(destination.flightNumber)
                )
            }
        }
    }
}
//
//@Composable
//fun Greeting(name: String, modifier: Modifier = Modifier) {
//    Text(
//        text = "Hello $name!",
//        modifier = modifier
//    )
//}
//
//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    SpaceXTheme {
//        Greeting("Android")
//    }
//}