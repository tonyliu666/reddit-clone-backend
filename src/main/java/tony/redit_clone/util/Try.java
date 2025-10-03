package tony.redit_clone.util;

import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public sealed interface Try<T> permits Try.Success, Try.Failure {

    record Success<T>(T value) implements Try<T> {}

    record Failure<T>(Throwable error) implements Try<T> {}

    static <T> Try<T> success(T value) {
        return new Success<>(value);
    }

    static <T> Try<T> failure(Throwable error) {
        return new Failure<>(error);
    }

    @FunctionalInterface
    interface CheckedSupplier<T> {
        T get() throws Exception;
    }

    static <T> Try<T> of(Supplier<T> supplier) {
        try {
            return new Success<>(supplier.get());
        } catch (Throwable t) {
            return new Failure<>(t);
        }
    }

    static <T> Try<T> ofChecked(CheckedSupplier<T> supplier) {
        try {
            return new Success<>(supplier.get());
        } catch (Throwable t) {
            return new Failure<>(t);
        }
    }

    default <U> Try<U> map(Function<? super T, ? extends U> mapper) {
        if (this instanceof Success<T> s) {
            try {
                return new Success<>(mapper.apply(s.value()));
            } catch (Throwable t) {
                return new Failure<>(t);
            }
        }
        return (Failure<U>) this;
    }

    default <U> Try<U> flatMap(Function<? super T, Try<U>> mapper) {
        if (this instanceof Success<T> s) {
            try {
                return mapper.apply(s.value());
            } catch (Throwable t) {
                return new Failure<>(t);
            }
        }
        return (Failure<U>) this;
    }

    // ✅ add proper filter
    default Try<T> filter(Predicate<? super T> predicate) {
        if (this instanceof Success<T> s) {
            try {
                if (predicate.test(s.value())) {
                    return this;
                } else {
                    return new Failure<>(new NoSuchElementException("Predicate does not hold for " + s.value()));
                }
            } catch (Throwable t) {
                return new Failure<>(t);
            }
        }
        return this;
    }
    // ✅ New: mapFailure
    default Try<T> mapFailure(Function<Throwable, Throwable> mapper) {
        if (this instanceof Failure<T> f) {
            try {
                return new Failure<>(mapper.apply(f.error()));
            } catch (Throwable t) {
                return new Failure<>(t);
            }
        }
        return this;
    }

    default boolean isSuccess() {
        return this instanceof Success<T>;
    }

    default boolean isFailure() {
        return this instanceof Failure<T>;
    }

    
}
