package io.jay.wishlistapp.data

data class Wish(
    val id: Long = 0,
    val title: String = "",
    val description: String = ""
)

object StubWish {
    val wishList = listOf(
        Wish(1L, "Mechanical keyboard", "Tactile switches, 75% layout"),
        Wish(2L, "Espresso machine", "Dual boiler with PID control"),
        Wish(3L, "Noise cancelling headphones", "Over-ear, for long flights"),
        Wish(4L, "Standing desk", "Electric, memory presets"),
        Wish(5L, "Hiking boots", "Waterproof, broken in before autumn"),
        Wish(6L, "Film camera", "35mm rangefinder, fully manual"),
        Wish(7L, "Cast iron skillet", "12 inch, pre-seasoned"),
        Wish(8L, "Ergonomic chair", "Lumbar support, adjustable arms"),
        Wish(9L, "Portable monitor", "USB-C powered, 1440p"),
        Wish(10L, "Weekend duffel", "Leather trim, carry-on sized")
    )
}
