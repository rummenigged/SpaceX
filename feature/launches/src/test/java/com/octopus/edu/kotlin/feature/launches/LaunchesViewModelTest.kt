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
import com.octopus.edu.kotlin.feature.launches.LaunchesUiContract.UiEvent.ReloadLaunches
import com.octopus.edu.kotlin.feature.launches.LaunchesUiContract.UiEvent.SearchLaunches
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
            val expectedErrorMessage = "Error"
            val expectedLoadingState = false

            coEvery { launchesRepository.getPastLaunches() } returns
                ResponseOperation.Error(expectedErrorMessage)

            launchesViewModel.processEvent(OnTabSelected(Past()))
            val isLoading = launchesViewModel.viewState.isLoading

            assertEquals(expectedLoadingState, isLoading)

            launchesViewModel.effect.test {
                assertEquals(ShowError(expectedErrorMessage), awaitItem())
            }
        }

    @Test
    fun `when the getUpcomingLaunches is called and return error the ui state should be updated`() =
        runTest {
            val expectedErrorMessage = "Error"
            val expectedLoadingState = false

            coEvery { launchesRepository.getUpcomingLaunches() } returns
                ResponseOperation.Error(expectedErrorMessage)

            launchesViewModel.processEvent(OnTabSelected(Upcoming()))

            val isLoading = launchesViewModel.viewState.isLoading

            assertEquals(expectedLoadingState, isLoading)

            launchesViewModel.effect.test {
                assertEquals(ShowError(expectedErrorMessage), awaitItem())
            }
        }

    @Test
    fun `when the getPastLaunches is called and return success the ui state should be updated`() =
        runTest {
            val expectedList = Launch.mockList(3)
            val expectedLoadingState = false

            coEvery { launchesRepository.getPastLaunches() } returns
                ResponseOperation.Success(expectedList)

            launchesViewModel.processEvent(OnTabSelected(Past()))

            val launches = launchesViewModel.viewState.launches
            val isLoading = launchesViewModel.viewState.isLoading

            assertEquals(expectedLoadingState, isLoading)
            assertEquals(expectedList, launches)
        }

    @Test
    fun `when the getUpcomingLaunches is called and return success the ui state should be updated`() =
        runTest {
            val expectedList = Launch.mockList(3)
            val expectedLoadingState = false

            coEvery { launchesRepository.getUpcomingLaunches() } returns
                ResponseOperation.Success(expectedList)

            launchesViewModel.processEvent(OnTabSelected(Upcoming()))

            val launches = launchesViewModel.viewState.launches
            val isLoading = launchesViewModel.viewState.isLoading

            assertEquals(expectedLoadingState, isLoading)
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

    @Test
    fun `when the ReloadLaunches is triggered and return success the ui state should be updated`() =
        runTest {
            val expectedList = Launch.mockList(3)
            val expectedLoadingState = false

            coEvery { launchesRepository.getUpcomingLaunches() } returns
                ResponseOperation.Success(expectedList)

            launchesViewModel.processEvent(ReloadLaunches)

            val launches = launchesViewModel.viewState.launches
            val isLoading = launchesViewModel.viewState.isLoading

            assertEquals(expectedLoadingState, isLoading)
            assertEquals(expectedList, launches)
        }

    @Test
    fun `when the ReloadLaunches is triggered and return error the ui state should be updated`() =
        runTest {
            val expectedErrorMessage = "Error"
            val expectedLoadingState = false

            when (launchesViewModel.viewState.tabSelected) {
                is Past -> {
                    coEvery { launchesRepository.getPastLaunches() } returns
                        ResponseOperation.Error(expectedErrorMessage)
                }
                is Upcoming -> {
                    coEvery { launchesRepository.getUpcomingLaunches() } returns
                        ResponseOperation.Error(expectedErrorMessage)
                }
            }

            launchesViewModel.processEvent(ReloadLaunches)

            val isLoading = launchesViewModel.viewState.isLoading

            assertEquals(expectedLoadingState, isLoading)

            launchesViewModel.effect.test {
                assertEquals(ShowError(expectedErrorMessage), awaitItem())
            }
        }

    @Test
    fun `when the SearchLaunches is triggered getLaunchesByGroup should be called with a query`() =
        runTest {
            val expectedList = Launch.mockList(3)
            val query = "query"

            when (launchesViewModel.viewState.tabSelected) {
                is Past -> {
                    coEvery { launchesRepository.getPastLaunches(query) } returns
                        ResponseOperation.Success(expectedList)
                }
                is Upcoming -> {
                    coEvery { launchesRepository.getUpcomingLaunches(query) } returns
                        ResponseOperation.Success(expectedList)
                }
            }

            launchesViewModel.processEvent(SearchLaunches(query))

            coVerify(exactly = 1) {
                when (launchesViewModel.viewState.tabSelected) {
                    is Past -> launchesRepository.getPastLaunches(query)
                    is Upcoming -> launchesRepository.getUpcomingLaunches(query)
                }
            }
        }

    @Test
    fun `when the SearchLaunches is triggered and the result is success the ui state should be updated`() =
        runTest {
            val expectedList = Launch.mockList(3)
            val expectedLoadingState = false
            val query = "query"

            when (launchesViewModel.viewState.tabSelected) {
                is Past -> {
                    coEvery { launchesRepository.getPastLaunches(query) } returns
                        ResponseOperation.Success(expectedList)
                }
                is Upcoming -> {
                    coEvery { launchesRepository.getUpcomingLaunches(query) } returns
                        ResponseOperation.Success(expectedList)
                }
            }

            launchesViewModel.processEvent(SearchLaunches(query))

            val launches = launchesViewModel.viewState.launches
            val isLoading = launchesViewModel.viewState.isLoading

            assertEquals(expectedLoadingState, isLoading)
            assertEquals(expectedList, launches)
        }

    @Test
    fun `when the SearchLaunches is triggered and the result is error the ui state should be updated`() =
        runTest {
            val expectedLoadingState = false
            val expectedErrorMessage = "Error"
            val query = "query"

            when (launchesViewModel.viewState.tabSelected) {
                is Past -> {
                    coEvery { launchesRepository.getPastLaunches(query) } returns
                        ResponseOperation.Error(expectedErrorMessage)
                }
                is Upcoming -> {
                    coEvery { launchesRepository.getUpcomingLaunches(query) } returns
                        ResponseOperation.Error(expectedErrorMessage)
                }
            }

            launchesViewModel.processEvent(SearchLaunches(query))

            val isLoading = launchesViewModel.viewState.isLoading

            assertEquals(expectedLoadingState, isLoading)

            launchesViewModel.effect.test {
                assertEquals(ShowError(expectedErrorMessage), awaitItem())
            }
        }

    private fun LaunchesViewModel.Companion.test() =
        LaunchesViewModel(
            launchRepository = launchesRepository,
            savedState = savedStateHandle,
        )
}
