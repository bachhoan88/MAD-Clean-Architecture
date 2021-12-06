package com.example.weather.domain.usecase.address

import com.example.weather.domain.repository.AddressRepository
import com.example.weather.domain.usecase.UseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLastCityUseCase @Inject constructor(
    private val addressRepository: AddressRepository
) : UseCase<Void, String>() {

    override fun execute(params: Void?): Flow<String> {
        return addressRepository.getLastCityName()
    }
}
