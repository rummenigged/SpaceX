package com.octopus.edu.kotlin.spacex

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.octopus.edu.kotlin.core.ui.common.SpaceXDestination
import com.octopus.edu.kotlin.core.ui.common.SpaceXNavigation
import com.octopus.edu.kotlin.feature.launches.LaunchesFragmentDirections.Companion.toLaunchDetails
import com.octopus.edu.kotlin.spacex.databinding.MainActivityBinding
import com.octopus.edu.kotlin.spacex.utils.safelyNavigate
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity :
    AppCompatActivity(),
    SpaceXNavigation {
    lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun navigate(destination: SpaceXDestination) {
        when (destination) {
            is SpaceXDestination.LaunchDetails -> {
                findNavController(R.id.mainHost).safelyNavigate(
                    toLaunchDetails(destination.flightNumber),
                )
            }
        }
    }
}
