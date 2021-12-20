package com.trinitcore.trinreact.util

import kotlinx.serialization.*
import kotlinx.serialization.modules.SerializersModule

interface Interval {

    companion object {
        const val packageName = "com.clickcostz.common.util"

        fun determineEquality(other: Any?, self: Interval): Boolean {
            return if (other is Interval) other.toString() == self.toString() else false
        }

        fun generateHashcode(self: Interval): Int {
            var result = self.lowerBound.hashCode()
            result = 31 * result + self.upperBound.hashCode()
            return result
        }
    }

    val lowerBound: Double
    val upperBound: Double

    fun contains(value: Double, midValue: Double = 0.0): Boolean

    fun scale(factor: Double): Interval

    @Serializable
    class Open(override val lowerBound: Double, override val upperBound: Double) : Interval {

        override fun toString(): String = "($lowerBound, $upperBound)"

        override fun contains(value: Double, midValue: Double): Boolean = value > (lowerBound + midValue) && value < (upperBound + midValue)

        override fun scale(factor: Double): Open = Open(lowerBound * factor, upperBound * factor)

        override fun equals(other: Any?): Boolean = determineEquality(other, this)

        override fun hashCode(): Int = generateHashcode(this)

    }

    @Serializable
    class OpenClosed(override val lowerBound: Double, override val upperBound: Double) : Interval {

        override fun toString(): String = "($lowerBound, $upperBound]"

        override fun contains(value: Double, midValue: Double): Boolean = value > (lowerBound + midValue) && value <= (upperBound + midValue)

        override fun scale(factor: Double): OpenClosed = OpenClosed(lowerBound * factor, upperBound * factor)

        override fun equals(other: Any?): Boolean = determineEquality(other, this)

        override fun hashCode(): Int = generateHashcode(this)

    }

    @Serializable
    class Closed(override val lowerBound: Double, override val upperBound: Double) : Interval {

        override fun toString(): String = "[$lowerBound, $upperBound]"

        override fun contains(value: Double, midValue: Double): Boolean = value in (lowerBound + midValue)..(upperBound + midValue)

        override fun scale(factor: Double): Closed = Closed(lowerBound * factor, upperBound * factor)

        override fun equals(other: Any?): Boolean = determineEquality(other, this)

        override fun hashCode(): Int = generateHashcode(this)

    }

    @Serializable
    class ClosedOpen(override val lowerBound: Double, override val upperBound: Double) : Interval {

        override fun toString(): String = "[$lowerBound, $upperBound)"

        override fun contains(value: Double, midValue: Double): Boolean = value >= (lowerBound + midValue) && value < (upperBound + midValue)

        override fun scale(factor: Double): ClosedOpen = ClosedOpen(lowerBound * factor, upperBound * factor)

        override fun equals(other: Any?): Boolean = determineEquality(other, this)

        override fun hashCode(): Int = generateHashcode(this)

    }

}