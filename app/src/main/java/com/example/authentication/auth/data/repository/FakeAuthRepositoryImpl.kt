package com.example.authentication.auth.data.repository

import android.util.Log
import com.example.authentication.auth.domain.model.AuthTokens
import com.example.authentication.auth.domain.model.User
import com.example.authentication.auth.domain.repository.AuthRepository
import com.example.authentication.core.domain.DataError
import com.example.authentication.core.domain.Result

class FakeAuthRepositoryImpl : AuthRepository {

    private var loggedIn = false
    private val usersMemory = mutableMapOf<String, String>(
        "7829155438" to "hhhhhh"
    )
    private val otpsMemory = mutableMapOf<String, String>()

    override suspend fun signUp(
        phone: String,
        password: String,
        name: String
    ): Result<Unit, DataError> {
        usersMemory[phone] = password
        val fakeOtp = "123456"
        otpsMemory[phone] = fakeOtp

        Log.d("FakeOTP", "تم إرسال رمز التحقق: $fakeOtp إلى الرقم $phone")
        return Result.Success(Unit)
    }

    override suspend fun verify(
        phone: String,
        otp: String
    ): Result<Pair<User, AuthTokens>, DataError> {
        val savedOtp = otpsMemory[phone]

        if (savedOtp != null && savedOtp == otp) {
            val mockUser = User(id = 1, phoneNumber = phone, isVerified = true)
            val mockTokens = AuthTokens(accessToken = "fake_access_token", refreshToken = "fake_refresh_token")
            loggedIn = true
            return Result.Success(Pair(mockUser, mockTokens))
        } else {
            // في حال إدخال كود خاطئ
            return Result.Error(DataError.Network.UNAUTHORIZED)
        }
        loggedIn = true
    }

    override suspend fun logInWithPassword(
        phone: String,
        password: String
    ): Result<Pair<User, AuthTokens>, DataError> {
        val savedPassword = usersMemory[phone]
        if (savedPassword == null || savedPassword != password) {
            return Result.Error(DataError.Network.UNAUTHORIZED)
        }
        val mockUser = User(id = 1, phoneNumber = phone, isVerified = true)
        val mockTokens = AuthTokens(accessToken = "fake_access_token", refreshToken = "fake_refresh_token")
        loggedIn = true
        return Result.Success(Pair(mockUser, mockTokens))
    }

    override suspend fun logInWithOtp(phone: String): Result<Unit, DataError> {
        if (!usersMemory.containsKey(phone)) {
            usersMemory[phone] = "12345" // باسوورد وهمي احتياطي
        }

        // توليد كود الـ OTP
        val fakeOtp = "123456"
        otpsMemory[phone] = fakeOtp

        // طباعة الكود حتى تشوفه بالـ Logcat
        Log.d("FakeOTP", "تم إرسال رمز التحقق: $fakeOtp إلى الرقم $phone")

        // إرجاع نجاح حتى الـ ViewModel ينقلك لصفحة الـ OTP أو الـ Reset Password
        return Result.Success(Unit)
    }

    override suspend fun resentOtp(phone: String): Result<Unit, DataError> {
        val fakeOtp = "123456"
        otpsMemory[phone] = fakeOtp
        Log.d("FakeOTP", "تم إعادة إرسال رمز التحقق: $fakeOtp إلى الرقم $phone")
        return Result.Success(Unit)
    }

    override suspend fun resetPassword(phone: String, newPassword: String): Result<Unit, DataError> {
        if (usersMemory.containsKey(phone)) {
            usersMemory[phone] = newPassword
            return Result.Success(Unit)
        }
        return Result.Error(DataError.Network.UNAUTHORIZED)
    }

    override suspend fun logout(): Result<Unit, DataError> {
        loggedIn = false
        return Result.Success(Unit)
    }

    override suspend fun isLoggedIn(): Boolean = loggedIn
}