package pt.rikmartins.adn.popularmoviesstage2.domain;

import androidx.arch.core.util.Function;

class Utils {
    static <I, O> io.reactivex.functions.Function<I, O> toXFunction(final Function<I, O> original) {
        return new io.reactivex.functions.Function<I, O>() {
            @Override
            public O apply(I input) throws Exception {
                return original.apply(input);
            }
        };
    }

    static <I> Function<I,I> forward() {
        return new Function<I, I>() {
            @Override
            public I apply(I input) {
                return input;
            }
        };
    }
}
