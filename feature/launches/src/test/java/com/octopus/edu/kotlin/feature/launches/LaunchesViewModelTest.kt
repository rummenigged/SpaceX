package com.octopus.edu.kotlin.feature.launches

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.octopus.edu.kotlin.core.domain.common.ResponseOperation
import com.octopus.edu.kotlin.core.domain.models.launch.Launch
import com.octopus.edu.kotlin.core.domain.models.launch.mockList
import com.octopus.edu.kotlin.core.domain.repository.LaunchRepository
import com.octopus.edu.kotlin.core.testing.MainCoroutineRule
import com.octopus.edu.kotlin.feature.launches.LaunchesUiContract.Tab.Past
import com.octopus.edu.kotlin.feature.launches.LaunchesUiContract.Tab.Upcoming
import com.octopus.edu.kotlin.feature.launches.LaunchesUiContract.UiEffect.NavigateToLaunchDetails
import com.octopus.edu.kotlin.feature.launches.LaunchesUiContract.UiEffect.ShowError
import com.octopus.edu.kotlin.feature.launches.LaunchesUiContract.UiEvent.OnLaunchClicked
import com.octopus.edu.kotlin.feature.launches.LaunchesUiContract.UiEvent.OnTabSelected
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class LaunchesViewModelTest {
    @get:Rule
    val mainCoroutine = MainCoroutineRule()

    @get:Rule
    val mockkRule = MockKRule(this)

    @MockK
    private lateinit var launchesRepository: LaunchRepository

    @MockK
    private lateinit var savedStateHandle: SavedStateHandle

    private lateinit var launchesViewModel: LaunchesViewModel

    @Before
    fun setUp() {
        coEvery { launchesRepository.getPastLaunches() } returns
            ResponseOperation.Success(Launch.mockList(3))

        launchesViewModel = LaunchesViewModel.test()
    }

    @Test
    fun `when the selected tab is Past method getPastLaunches should be called`() =
        runTest {
            clearMocks(launchesRepository)

            coEvery { launchesRepository.getPastLaunches() } returns
                ResponseOperation.Success(Launch.mockList(3))

            launchesViewModel.processEvent(OnTabSelected(Past()))

            coVerify(exactly = 1) {
                launchesRepository.getPastLaunches()
            }
        }

    @Test
    fun `when the selected tab is Upcoming method getUpcomingLaunches should be called`() =
        runTest {
            coEvery { launchesRepository.getUpcomingLaunches() } returns
                ResponseOperation.Success(Launch.mockList(3))

            launchesViewModel.processEvent(OnTabSelected(Upcoming()))

            coVerify(exactly = 1) {
                launchesRepository.getUpcomingLaunches()
            }
        }

    @Test
    fun `when the getPastLaunches is called and return error the ui state should be updated`() =
        runTest {
            coEvery { launchesRepository.getPastLaunches() } returns
                ResponseOperation.Error("Error")

            launchesViewModel.processEvent(OnTabSelected(Past()))

            launchesViewModel.effect.test {
                assertEquals(ShowError("Error"), awaitItem())
            }
        }

    @Test
    fun `when the getUpcomingLaunches is called and return error the ui state should be updated`() =
        runTest {
            coEvery { launchesRepository.getUpcomingLaunches() } returns
                ResponseOperation.Error("Error")

            launchesViewModel.processEvent(OnTabSelected(Upcoming()))

            launchesViewModel.effect.test {
                assertEquals(ShowError("Error"), awaitItem())
            }
        }

    @Test
    fun `when the getPastLaunches is called and return success the ui state should be updated`() =
        runTest {
            val expectedList = Launch.mockList(3)
            coEvery { launchesRepository.getPastLaunches() } returns
                ResponseOperation.Success(expectedList)

            launchesViewModel.processEvent(OnTabSelected(Past()))

            val launches = launchesViewModel.viewState.launches

            assertEquals(expectedList, launches)
        }

    @Test
    fun `when the getUpcomingLaunches is called and return success the ui state should be updated`() =
        runTest {
            val expectedList = Launch.mockList(3)
            coEvery { launchesRepository.getUpcomingLaunches() } returns
                ResponseOperation.Success(expectedList)

            launchesViewModel.processEvent(OnTabSelected(Upcoming()))

            val launches = launchesViewModel.viewState.launches

            assertEquals(expectedList, launches)
        }

    @Test
    fun `when a launch is selected the navigate effect should be called`() =
        runTest {
            val flightNumber = 1

            launchesViewModel.processEvent(OnLaunchClicked(flightNumber))

            launchesViewModel.effect.test {
                assertEquals(NavigateToLaunchDetails(flightNumber), awaitItem())
            }
        }

    private fun LaunchesViewModel.Companion.test() =
        LaunchesViewModel(
            launchRepository = launchesRepository,
            savedState = savedStateHandle,
        )
}
