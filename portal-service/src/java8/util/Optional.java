/*
 * Copyright (c) 2012, 2013, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package java8.util;

import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * [[@]] This is a striped-down copy of `java.util.Optional` from JDK8. We need
 * functionality of some classes introduced with Java 8. We could take some 3rd
 * party library (like Guava), but that would just increase the already big set
 * of libs we are using. More important, these classes need to be available for
 * portal-services. Doing this way we can we migrate to Java8 one day very
 * simply.
 * TODO: can we name package just `java.util` ? AFAIK that is forbidden.
 */
public final class Optional<T> {
	/**
	 * Returns an empty {@code Optional} instance.  No value is present for this
	 * Optional.
	 *
	 * @param <T> Type of the non-existent value
	 * @return an empty {@code Optional}
	 *
	 * @apiNote Though it may be tempting to do so, avoid testing if an object
	 * is empty by comparing with {@code ==} against instances returned by
	 * {@code Option.empty()}. There is no guarantee that it is a singleton.
	 * Instead, use {@link #isPresent()}.
	 */
	public static <T> Optional<T> empty() {
		@SuppressWarnings("unchecked")
		Optional<T> t = (Optional<T>) EMPTY;
		return t;
	}

	/**
	 * Returns an {@code Optional} with the specified present non-null value.
	 *
	 * @param <T> the class of the value
	 * @param value the value to be present, which must be non-null
	 * @return an {@code Optional} with the value present
	 *
	 * @throws NullPointerException if value is null
	 */
	public static <T> Optional<T> of(T value) {
		return new Optional<>(value);
	}

	/**
	 * Returns an {@code Optional} describing the specified value, if non-null,
	 * otherwise returns an empty {@code Optional}.
	 *
	 * @param <T> the class of the value
	 * @param value the possibly-null value to describe
	 * @return an {@code Optional} with a present value if the specified value
	 * is non-null, otherwise an empty {@code Optional}
	 */
	public static <T> Optional<T> ofNullable(T value) {
		return value == null ? (Optional<T>) empty() : of(value);
	}

	/**
	 * Indicates whether some other object is "equal to" this Optional. The
	 * other object is considered equal if:
	 * <ul>
	 * <li>it is also an {@code Optional} and;
	 * <li>both instances have no value present or;
	 * <li>the present values are "equal to" each other via {@code equals()}.
	 * </ul>
	 *
	 * @param obj an object to be tested for equality
	 * @return {code true} if the other object is "equal to" this object
	 * otherwise {@code false}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof Optional)) {
			return false;
		}

		Optional<?> other = (Optional<?>) obj;
		return Objects.equals(value, other.value);
	}

	/**
	 * If a value is present, apply the provided {@code Optional}-bearing
	 * mapping function to it, return that result, otherwise return an empty
	 * {@code Optional}.  This method is similar to {@link #map(Function)},
	 * but the provided mapper is one whose result is already an {@code Optional},
	 * and if invoked, {@code flatMap} does not wrap it with an additional
	 * {@code Optional}.
	 *
	 * @param <U> The type parameter to the {@code Optional} returned by
	 * @param mapper a mapping function to apply to the value, if present
	 *           the mapping function
	 * @return the result of applying an {@code Optional}-bearing mapping
	 * function to the value of this {@code Optional}, if a value is present,
	 * otherwise an empty {@code Optional}
	 * @throws NullPointerException if the mapping function is null or returns
	 * a null result
	 */
	public<U> Optional<U> flatMap(Function<? super T, Optional<U>> mapper) {
		Objects.requireNonNull(mapper);
		if (!isPresent())
			return empty();
		else {
			return Objects.requireNonNull(mapper.apply(value));
		}
	}

	/**
	 * If a value is present in this {@code Optional}, returns the value,
	 * otherwise throws {@code NoSuchElementException}.
	 *
	 * @return the non-null value held by this {@code Optional}
	 *
	 * @throws NoSuchElementException if there is no value present
	 * @see Optional#isPresent()
	 */
	public T get() {
		if (value == null) {
			throw new NoSuchElementException("No value present");
		}
		return value;
	}

	/**
	 * Returns the hash code value of the present value, if any, or 0 (zero) if
	 * no value is present.
	 *
	 * @return hash code value of the present value or 0 if no value is present
	 */
	@Override
	public int hashCode() {
		return Objects.hashCode(value);
	}

	/**
	 * If a value is present, invoke the specified consumer with the value,
	 * otherwise do nothing.
	 *
	 * Parameters:
	 * consumer block to be executed if a value is present
	 *
	 * Throws:
	 * java.lang.NullPointerException if value is present and consumer is null
	 */
	public void ifPresent(Consumer<? super T> consumer) {
		if (value != null)
			consumer.accept(value);
	}

	/**
	 * Return {@code true} if there is a value present, otherwise {@code false}.
	 *
	 * @return {@code true} if there is a value present, otherwise {@code false}
	 */
	public boolean isPresent() {
		return value != null;
	}

	/**
	 * Return the value if present, otherwise return {@code other}.
	 *
	 * @param other the value to be returned if there is no value present, may
	 * be null
	 * @return the value, if present, otherwise {@code other}
	 */
	public T orElse(T other) {
		return value != null ? value : other;
	}

	/**
	 * If a value is present, apply the provided mapping function to it,
	 * and if the result is non-null, return an {@code Optional} describing the
	 * result.  Otherwise return an empty {@code Optional}.
	 *
	 * @apiNote This method supports post-processing on optional values, without
	 * the need to explicitly check for a return status.  For example, the
	 * following code traverses a stream of file names, selects one that has
	 * not yet been processed, and then opens that file, returning an
	 * {@code Optional<FileInputStream>}:
	 *
	 * <pre>{@code
	 *     Optional<FileInputStream> fis =
	 *         names.stream().filter(name -> !isProcessedYet(name))
	 *                       .findFirst()
	 *                       .map(name -> new FileInputStream(name));
	 * }</pre>
	 *
	 * Here, {@code findFirst} returns an {@code Optional<String>}, and then
	 * {@code map} returns an {@code Optional<FileInputStream>} for the desired
	 * file if one exists.
	 *
	 * @param <U> The type of the result of the mapping function
	 * @param mapper a mapping function to apply to the value, if present
	 * @return an {@code Optional} describing the result of applying a mapping
	 * function to the value of this {@code Optional}, if a value is present,
	 * otherwise an empty {@code Optional}
	 * @throws NullPointerException if the mapping function is null
	 */
	public<U> Optional<U> map(Function<? super T, ? extends U> mapper) {
		Objects.requireNonNull(mapper);
		if (!isPresent())
			return empty();
		else {
			return Optional.ofNullable(mapper.apply(value));
		}
	}

	/**
	 * Returns a non-empty string representation of this Optional suitable for
	 * debugging. The exact presentation format is unspecified and may vary
	 * between implementations and versions.
	 *
	 * @return the string representation of this instance
	 *
	 * @implSpec If a value is present the result must include its string
	 * representation in the result. Empty and present Optionals must be
	 * unambiguously differentiable.
	 */
	@Override
	public String toString() {
		return value != null
			? String.format("Optional[%s]", value)
			: "Optional.empty";
	}

	/**
	 * Constructs an empty instance.
	 *
	 * @implNote Generally only one empty instance, {@link Optional#EMPTY},
	 * should exist per VM.
	 */
	private Optional() {
		this.value = null;
	}

	/**
	 * Constructs an instance with the value present.
	 *
	 * @param value the non-null value to be present
	 * @throws NullPointerException if value is null
	 */
	private Optional(T value) {
		this.value = Objects.requireNonNull(value);
	}

	/**
	 * Common instance for {@code empty()}.
	 */
	private static final Optional<?> EMPTY = new Optional<>();
	/**
	 * If non-null, the value; if null, indicates no value is present
	 */
	private final T value;
}
