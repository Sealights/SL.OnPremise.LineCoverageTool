package io.sealights.tool

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe

class SimpleTest : ShouldSpec({
    should("return the length of the string") {
        "sammy".length shouldBe 5
        "".length shouldBe 0
    }

    should("return the length of the asdasd") {
        "sammy".length shouldBe 5
        "".length shouldBe 0
    }
})