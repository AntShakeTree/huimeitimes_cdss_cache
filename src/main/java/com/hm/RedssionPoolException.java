package com.hm;

/**
 * Created by ant_shake_tree on 15/10/26.
 */
public class RedssionPoolException extends RuntimeException {


        private static final long serialVersionUID = 9172336149805414947L;

        public RedssionPoolException(String s, Throwable cause) {
            super(s, cause);
            setStackTrace(cause.getStackTrace());
        }
        public RedssionPoolException(Throwable cause) {
            super(cause.getMessage(), cause);
            setStackTrace(cause.getStackTrace());
        }

}
