package com.octopus.edu.kotlin.feature.launches

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.octopus.edu.kotlin.core.ui.common.setComposableContent

class LaunchesFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? = setComposableContent { LaunchesScreen() }
}
