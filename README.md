# QuicKit

QuicKit is a lightweight Java toolkit that helps teams assemble concurrent and asynchronous workflows without rebuilding common infrastructure. The library wraps proven utilities such as Netty's hashed-wheel timer and Guava's retry support in focused helpers that are simple to adopt inside existing services.

## Core Features
- Parallel composition of lightweight tasks via `ParallelTask` with either the common fork-join pool or a supplied `ExecutorService`.
- Rate-limited task execution streams (`ExecutionFrequencyUtils`) backed by a reusable hashed-wheel timer.
- Configurable delayed task scheduling through `DelayQueueUtils.delay`.
- Resilient retry orchestration on top of `guava-retrying` with sensible defaults (`RetryUtils`).
- Read/write cache warm-up flows with automatic lock upgrading through `ReadWriteLockWrapper`.
- Everyday helpers (`CommonUtils`, `DateUtil`) for time, UUIDs, random codes, and domain parsing.

## Requirements
- Java 8 or newer (configured via Gradle toolchains).
- Gradle 8.x (the provided wrapper is recommended).

## Building From Source

```bash
./gradlew clean build
```

The command produces a consumable JAR under `lib/build/libs/`. You can reference it directly or publish to a private repository as needed.

## Using the Library

### Parallel execution

```java
ParallelTask.newTask()
    .addTask(() -> inventoryService.refresh())
    .addTask(() -> pricingService.recalculate())
    .addTask(() -> cacheEvictor.run())
    .execute();
```

To run on a custom `ExecutorService`, pass it to `execute(executor)`.

### Rate limiting batches

```java
ExecutionFrequencyUtils.submitAsync(
    "product-sync",
    tasks,
    20 // fallback throughput when no config override is supplied
);
```

The helper partitions the workload, honours optional `limit` and `stop` flags retrieved via `ConfigUtil`, and uses the hashed-wheel timer to phase out execution over time.

### Delayed execution

```java
DelayQueueUtils.delay(() -> notificationSender.send(orderId), 5L);
```

The task is scheduled once after the specified number of seconds.

### Retry with defaults

```java
String confirmation = RetryUtils.retryCall(
    "confirm-order",
    result -> result == null,
    3,
    () -> orderGateway.confirm(orderId),
    "PENDING"
);
```

`RetryUtils` automatically honours configuration overrides exposed via `ConfigUtil`.

### Thread-safe read/write cache warm-up

```java
ReadWriteLockWrapper<List<String>> wrapper = ReadWriteLockWrapper.newLock();
List<String> items = wrapper.execute(
    this::loadFromRemote,
    this::loadFromCache,
    list -> list != null && !list.isEmpty()
);
```

The wrapper keeps cache refresh logic compact while preventing redundant remote calls under concurrent access.

## Configuration Hooks

`ConfigUtil` currently provides stubbed getters that return supplied defaults. Integrate it with your configuration system (Apollo, Spring Config, etc.) to control runtime behaviour such as retry counts or per-task throughput throttles without code changes.

## Project Layout
- `lib/` &mdash; Gradle module containing the core sources, utilities, and tests.
- `work/` &mdash; scratch directory used by certain performance tests.

## Running Tests

```bash
./gradlew test
```

Some performance-focused tests rely on the ContiPerf JUnit rule. They can generate HTML reports under `lib/build/reports/` when a JDK is available.

## License

QuicKit is licensed under the terms of the [Apache License 2.0](https://github.com/kingson4wu/QuicKit/blob/main/LICENSE).

