package com.example.authentication.auth.domain.use_cases

import com.example.authentication.auth.domain.repository.AuthRepository
import com.example.authentication.core.domain.DataError
import com.example.authentication.core.domain.Result

class SignUpUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(phone: String,password: String , name:String): Result<Unit, DataError>{
        return repository.signUp(phone,password,name)
    }
}