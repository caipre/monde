package co.nickp.monde.arch

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class EffectTest {
  @Test
  fun `construction api`() {
    Effect.of("value")
        .exec { assertThat(it).isEqualTo("value") }

    val expected = mutableListOf("value1", "value2")
    Effect.of("value1", "value2")
        .exec { assertThat(it).isEqualTo(expected.removeAt(0)) }
  }

  @Test
  fun `subscriber contract`() {
    var onStart = false
    var onComplete = false
    var onError = false
    var onValue = false

    Effect.of("value")
        .exec(
            Subscriber(
                { onStart = true },
                { onComplete = true },
                { onError = true },
                { onValue = true })
        )

    assertThat(onStart).isTrue()
    assertThat(onComplete).isTrue()
    assertThat(onError).isFalse()
    assertThat(onValue).isTrue()
  }

  @Test
  fun `fire and forget`() {
    assertThat(Effect.of("value").fire())
        .isNotInstanceOf(Error::class.java)
  }
}
