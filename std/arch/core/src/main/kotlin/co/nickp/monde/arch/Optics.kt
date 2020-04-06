package co.nickp.monde.arch

data class Lens<S, T>(
    val get: (S) -> T,
    val set: (S, T) -> S
)

data class Prism<S, T>(
    val extract: (S) -> T?,
    val embed: (T) -> S
)
