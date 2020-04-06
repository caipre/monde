package co.nickp.monde.arch

import co.nickp.monde.arch.Next.Companion.next
import co.nickp.monde.arch.Next.Companion.send
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import java.util.concurrent.Semaphore
import kotlin.concurrent.thread

class StoreImplTest {
  @Test
  fun `sends events created by effects`() {
    val store = StoreImpl<Int, Unit, Effect<Unit>>(
        { m, _, f -> if (m == 0) next(m + 1, f) else next(m + 1) }, 0, Effect.of(Unit, Unit)
    )
    store.send(Unit)
    assertThat(store.model)
        .isEqualTo(3)
  }

  @Test
  fun `drops events created after close`() {
    val semaphore = Semaphore(1)
    val effect = { onValue: (Unit) -> Unit -> thread { semaphore.acquire(); onValue(Unit) } }
    val store = StoreImpl<Unit, Unit, Effect<Unit>>(
        { _, _, f -> send(f) }, Unit, Effect.from { effect(it) }
    )
    semaphore.acquire()
    store.send(Unit)
    store.close()
//        semaphore.release()
  }

  @Test(expected = IllegalStateException::class)
  fun `throws on send after close`() {
    val store = StoreImpl<Int, Unit, Unit>(identity(), 0, Unit)
    store.close()
    store.send(Unit)
  }
}
