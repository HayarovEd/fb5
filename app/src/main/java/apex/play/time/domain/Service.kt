package apex.play.time.domain

interface Service {
    fun checkedInternetConnection(): Boolean
    fun vpnActive(): Boolean
    fun batteryLevel(): Int
    fun checkIsEmu(): Boolean
}