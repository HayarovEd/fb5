package apex.play.time.domain

interface Keeper {
    fun getSharedUrl(): String?
    fun setSharedUrl(url:String)
}