package io.sealights.tool.configuration

import io.kotest.core.spec.style.ShouldSpec
import kotlin.test.assertEquals

class TokenResolverTest : ShouldSpec({

    val VALID_TOKEN =
        "eyJhbGciOiJSUzUxMiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJodHRwczovL0RFVi1qYXZhMy5hdXRoLnNlYWxpZ2h0cy5pby8iLCJqd3RpZCI6IkRFVi1qYXZhMyxuZWVkVG9SZW1vdmUsQVBJR1ctNmYzNDczNDEtODUzYy00YWU1LTgxZjAtNzgxMWM0M2MzZDNmLDE3MTUwNzY2NDcyMzIiLCJzdWJqZWN0IjoiU2VhTGlnaHRzQGFnZW50IiwiYXVkaWVuY2UiOlsiYWdlbnRzIl0sIngtc2wtcm9sZSI6ImFnZW50IiwieC1zbC1zZXJ2ZXIiOiJodHRwczovL2Rldi1qYXZhMy1ndy5kZXYuc2VhbGlnaHRzLmNvL2FwaSIsInNsX2ltcGVyX3N1YmplY3QiOiIiLCJpYXQiOjE3MTUwNzY2NDd9.cEzCqV-YJUO4dsMbvMcQbwNtdGrjVUB68wXN4yF5huQeNO_2W8jb3KCw5q_S6ebSJ5WpVB81kI4uv_WbhbBcf5rw8CcHKpqsM-ReqPCPQXMeXvviSHEDlQ8tfeLK74x-myOYWMM6AABecTwjKzt_znizWlLRy44aSRTW258EfGqrBfkr3s0rmqzNK42ZwlhvnQ9eX7bHZ7X_jxTqj1JJoNElz5wMPiF77jXAB1Qj6lFEdN9WOGaCBVgZO6CU2UqZlpm-0qWzjks-Oii-TSEggorhg03OOnB7Jr5nULyjKscxAVursyIzetRZkkA8k_hPJ4Rc_GzsO0WWUmZH-u89BDLrm-pE5NqQAeuhvUqfWIu-tUr-Kx0g85aa_hO1D0oGbeCZsjBPEuUs_LvJX4iVdpa0e4c8APc-iszSyfYaJ8omHqDVc1CvPdUs37lsxRaDqlPBnbJAB9YbWnDczDN4n5CKt9mhvFHrwgaj74-LP6Pzi32CZqv4flrZxCHDK2ZWkyF784M91A9BdnagvVtgWDTQsPsBWbSa6keitTN3KSdl0AejcPYkEFNIsZSsLsXcrUVTQ0nGK97gzo-cT0WGqyQYEmQ9gkpVQaA6BQ_FDWr3UmGadwNq7fJUzettih35EdSVcVYwCiu05ZPShkiZQ_HtFCkTYo-zKJfgU5Ue5Rk"
    val INVALID_TOKEN =
        "invalid-unparsable-token"

    should("resolve valid token") {
        val tokenResolver = TokenResolver()

        // when
        val tokenDataEither = tokenResolver.resolve(VALID_TOKEN)

        // then
        val tokenData = tokenDataEither.let { it.getOrNull() }
        assertEquals("agent", tokenData?.role)
        assertEquals("https://dev-java3-gw.dev.sealights.co", tokenData?.apiUrl)
        assertEquals(VALID_TOKEN, tokenData?.token)

    }

    should("return message in invalid token resolution") {
        val tokenResolver = TokenResolver()

        // when
        val tokenDataEither = tokenResolver.resolve(INVALID_TOKEN)

        // then
        assertEquals("Could not extract data from provided token", tokenDataEither.leftOrNull()?.trim())

    }
    
})