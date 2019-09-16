package com.aweorih.sheets_sql.utils;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Objects;
import java.util.function.Consumer;

@SuppressWarnings("ALL")
public class IOExceptionWrapper {

  public void throwRun(IOExceptionRunnable runnable) {
    throwRun(null, runnable);
  }

  public <C> void throwAccept(IOExceptionConsumer<C> consumer, C c) {
    throwAccept(null, c, consumer);
  }

  public <I, O> O throwApply(IOExceptionFunction<I, O> func, I i) {
    return throwApply(null, i, func);
  }

  public <O> O throwGet(IOExceptionSupplier<O> consumer) {
    return throwGet(null, consumer);
  }

  public void run(Class<?> clazz, IOExceptionRunnable runnable) {
    run(clazz, null, runnable);
  }

  public <I, O> O apply(Class<?> clazz, IOExceptionFunction<I, O> func, I i) {
    return apply(clazz, null, i, func);
  }

  public <O> O get(Class<?> clazz, IOExceptionSupplier<O> consumer) {
    return get(clazz, null, consumer);
  }

  public <C> void accept(Class<?> clazz, IOExceptionConsumer<C> consumer, C c) {
    accept(clazz, null, c, consumer);
  }

  public void throwRun(Consumer<String> exceptionConsumer, IOExceptionRunnable runnable) {

    Objects.requireNonNull(runnable);
    try {
      runnable.run();
    } catch (IOException e) {
      runExceptionCallbackIfPresent(exceptionConsumer, e.getMessage());
      throw new UncheckedIOException(e);
    }
  }

  public <C> void throwAccept(
    Consumer<String> exceptionConsumer, C c, IOExceptionConsumer<C> consumer
  ) {

    Objects.requireNonNull(consumer);

    try {

      consumer.accept(c);
    } catch (IOException e) {
      runExceptionCallbackIfPresent(exceptionConsumer, e.getMessage());
      throw new UncheckedIOException(e);
    }
  }

  public <I, O> O throwApply(
    Consumer<String> exceptionConsumer, I i, IOExceptionFunction<I, O> func
  ) {

    Objects.requireNonNull(func);

    try {
      return func.apply(i);
    } catch (IOException e) {
      runExceptionCallbackIfPresent(exceptionConsumer, e.getMessage());
      throw new UncheckedIOException(e);
    }
  }

  public <O> O throwGet(Consumer<String> exceptionConsumer, IOExceptionSupplier<O> consumer) {

    Objects.requireNonNull(consumer);

    try {

      return consumer.get();
    } catch (IOException e) {
      runExceptionCallbackIfPresent(exceptionConsumer, e.getMessage());
      throw new UncheckedIOException(e);
    }
  }

  public void run(
    Class<?> clazz, Consumer<String> exceptionConsumer, IOExceptionRunnable runnable
  ) {

    Objects.requireNonNull(runnable);

    try {
      runnable.run();
    } catch (IOException e) {
      LoggingUtils.logException(clazz, e);
      runExceptionCallbackIfPresent(exceptionConsumer, e.getMessage());
    }
  }

  public <I, O> O apply(
    Class<?> clazz,
    Consumer<String> exceptionConsumer,
    I i,
    IOExceptionFunction<I, O> func
  ) {

    Objects.requireNonNull(func);

    try {
      return func.apply(i);
    } catch (IOException e) {
      LoggingUtils.logException(clazz, e);
      runExceptionCallbackIfPresent(exceptionConsumer, e.getMessage());
    }

    return null;
  }

  public <O> O get(
    Class<?> clazz,
    Consumer<String> exceptionConsumer,
    IOExceptionSupplier<O> consumer
  ) {

    Objects.requireNonNull(consumer);

    try {

      return consumer.get();
    } catch (IOException e) {
      LoggingUtils.logException(clazz, e);
      runExceptionCallbackIfPresent(exceptionConsumer, e.getMessage());
    }

    return null;
  }

  public <C> void accept(
    Class<?> clazz,
    Consumer<String> onExceptionCallback,
    C c,
    IOExceptionConsumer<C> consumer
  ) {

    Objects.requireNonNull(consumer);

    try {

      consumer.accept(c);
    } catch (IOException e) {
      LoggingUtils.logException(clazz, e);
      runExceptionCallbackIfPresent(onExceptionCallback, e.getMessage());
    }
  }

  private void runExceptionCallbackIfPresent(
    Consumer<String> onExceptionCallback,
    String exception
  ) {
    if (null == onExceptionCallback) return;
    onExceptionCallback.accept(exception);
  }

  @FunctionalInterface
  public interface IOExceptionSupplier<O> {

    O get() throws IOException;
  }

  @FunctionalInterface
  public interface IOExceptionFunction<I, O> {

    O apply(I i) throws IOException;
  }

  @FunctionalInterface
  public interface IOExceptionConsumer<I> {

    void accept(I i) throws IOException;
  }

  @FunctionalInterface
  public interface IOExceptionRunnable {

    void run() throws IOException;
  }
}
