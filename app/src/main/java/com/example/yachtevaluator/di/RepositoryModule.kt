package com.example.yachtevaluator.di

import com.example.yachtevaluator.data.repository.EvaluationRepository
import com.example.yachtevaluator.data.repository.EvaluationRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindEvaluationRepository(
        impl: EvaluationRepositoryImpl
    ): EvaluationRepository
}
