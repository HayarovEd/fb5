package apex.play.time.domain

import apex.play.time.data.KeeperImpl
import apex.play.time.data.ServiceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DiModule {
    @Binds
    @Singleton
    abstract fun bindService(serviceImpl: ServiceImpl): Service

    @Binds
    @Singleton
    abstract fun bindKeeper(keeperImpl: KeeperImpl): Keeper
}