package io.sealights.tool.configuration

import arrow.core.Either
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

class TokenResolver {
    @OptIn(ExperimentalEncodingApi::class)
    fun resolve(jwtToken: String): Either<String, TokenData> {
        try {
            val afterFirstDotPosition = jwtToken.indexOf(".") + 1
            val jwtPayload = jwtToken.substring(afterFirstDotPosition, jwtToken.indexOf(".", afterFirstDotPosition))

            val decodedPayloadArray = Base64.decode(jwtPayload)
            val decodedPayload = String(decodedPayloadArray, Charsets.UTF_8)

            val type = object : TypeToken<Map<String, Any>>() {}.type
            val fromJson = Gson().fromJson<Map<String, Any>>(decodedPayload, type)

            return Either.Right(
                TokenData(
                    role = fromJson["x-sl-role"] as String,
                    apiUrl = fromJson["x-sl-server"] as String
                )
            )
        } catch (exception: Exception) {
            return Either.Left("Could not extract data from provided token")
        }
    }
}

data class TokenData(
    val role: String,
    val apiUrl: String
)