package me.harrydrummond.cafeapplication.logic

/**
 * A simple counter class that supports incrementing, decrementing, and has minimum
 * and maximum bounds.
 *
 * @param initialValue The initial value of the counter.
 * @param minValue The minimum bound for the counter.
 * @param maxValue The maximum bound for the counter.
 */
class Counter(
    private var value: Int = 0,
    private val minValue: Int = Int.MIN_VALUE,
    private val maxValue: Int = Int.MAX_VALUE
) {

    init {
        require(value in minValue..maxValue) {
            "Initial value must be within the specified bounds."
        }
    }

    /**
     * Increment the counter by the specified amount.
     *
     * @param amount The amount to increment the counter by. Defaults to 1.
     * @return The new value of the counter after incrementing.
     */
    fun increment(amount: Int = 1): Int {
        value += amount
        checkBounds()
        return value
    }

    /**
     * Decrement the counter by the specified [amount].
     *
     * @param amount The amount to decrement the counter by. Defaults to 1.
     * @return The new value of the counter after decrementing.
     */
    fun decrement(amount: Int = 1): Int {
        value -= amount
        checkBounds()
        return value
    }

    /**
     * Get the current value of the counter.
     *
     * @return The current value of the counter.
     */
    fun getValue(): Int {
        return value
    }

    private fun checkBounds() {
        value = when {
            value < minValue -> minValue
            value > maxValue -> maxValue
            else -> value
        }
    }
}