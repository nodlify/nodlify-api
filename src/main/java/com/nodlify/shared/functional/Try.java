package com.nodlify.shared.functional;


public interface Try {

    static <T> T of(TrySupplier<T> supplier) throws RuntimeException {
        try {
            return supplier.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static void run(TryRunnable supplier) throws RuntimeException {
        try {
            supplier.run();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    interface TrySupplier<T> {
        T get() throws Exception;
    }

    interface TryRunnable {
        void run() throws Exception;
    }
}
