package pt.rikmartins.adn.popularmoviesstage2.domain;

import androidx.arch.core.util.Function;

class Utils {
    static <I, O> io.reactivex.functions.Function<I, O> toXFunction(final Function<I, O> original) {
        return original::apply;
    }

    static <I> Function<I,I> forward() {
        return input -> input;
    }
}
