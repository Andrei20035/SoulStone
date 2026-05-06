package com.example.soulstone.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InactivityManager @Inject constructor() {

    // Emits an event when the timer finishes
    private val _timeoutEvent = MutableSharedFlow<Unit>(replay = 0)
    val timeoutEvent = _timeoutEvent.asSharedFlow()

    // Scope for the timer (lives as long as the app is alive)
    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private var timerJob: Job? = null

    // 5 Minutes in milliseconds
    private val TIMEOUT_MS = 5 * 60 * 1000L

    init {
        startTimer() // Start counting immediately when app starts
    }

    /**
     * Called whenever the user touches the screen.
     * Resets the countdown.
     */
    fun onUserInteraction() {
        startTimer()
    }

    private fun startTimer() {
        // Cancel the previous timer
        timerJob?.cancel()

        // Start a new one
        timerJob = scope.launch {
            delay(TIMEOUT_MS)
            _timeoutEvent.emit(Unit) // Time is up!
        }
    }
}