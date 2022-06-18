package com.example.weather.domain.usecase.address

import com.example.weather.domain.di.IoDispatcher
import com.example.weather.domain.repository.AddressRepository
import com.example.weather.domain.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLastCityUseCase @Inject constructor(
    private val addressRepository: AddressRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : UseCase<Void, String>(dispatcher) {

    override fun execute(params: Void?): Flow<String> {
        return addressRepository.getLastCityName()
    }
}
