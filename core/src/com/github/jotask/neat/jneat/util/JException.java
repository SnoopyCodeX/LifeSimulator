package com.github.jotask.neat.jneat.util;

/**
 * JException
 *
 * @author Jose Vives Iznardo
 * @since 10/03/2017
 */
public class JException extends RuntimeException {

    public JException(String message) { super(message); }

    public static void lanzar(final String text){
        throw new JException(text);
    }

}
