# Example

Minimal example to demonstrate a bug in sleuth, where the wrong traceId is logged in ReactorNettyClient

## Setup

Start the DB, and create a table:

```
$ docker run -p 5432:5432 -e 'POSTGRES_PASSWORD=password' -e 'POSTGRES_DB=test' postgres:alpine3.15 -c log_statement=all -c log_destination=stderr
$ echo "CREATE TABLE test( id TEXT NOT NULL PRIMARY KEY, value INT NOT NULL )" | psql -h localhost -U postgres test
```

Run the service:

```
$ mvn spring-boot:run -Pdev
```

## Trigger the bug

Run some traffic:

```
$ curl http://localhost:8080/find/1
$ curl http://localhost:8080/find/1
```

When processing the second curl command, the wrong traceId is logged. The ReactorNettyClient object is logging the traceId from the first request.

```
2022-07-07 14:50:34.400 DEBUG [,,] 53092 --- [ctor-http-nio-4] reactor.netty.transport.TransportConfig  : [4c629589, L:/127.0.0.1:8080 - R:/127.0.0.1:55650] Initialized pipeline DefaultChannelPipeline{(reactor.left.httpCodec = io.netty.handler.codec.http.HttpServerCodec), (reactor.left.httpTrafficHandler = reactor.netty.http.server.HttpTrafficHandler), (reactor.right.reactiveBridge = reactor.netty.channel.ChannelOperationsHandler)}
2022-07-07 14:50:34.402  INFO [,ae0208e5cceee3fd,ae0208e5cceee3fd] 53092 --- [ctor-http-nio-4] test.service.OpLoggingFilter             : processing start
2022-07-07 14:50:34.404 DEBUG [,ae0208e5cceee3fd,ae0208e5cceee3fd] 53092 --- [ctor-http-nio-4] io.r2dbc.pool.ConnectionPool             : Obtaining new connection from the pool
2022-07-07 14:50:34.405 DEBUG [,ae0208e5cceee3fd,ae0208e5cceee3fd] 53092 --- [ctor-http-nio-4] o.s.r2dbc.core.DefaultDatabaseClient     : Executing SQL statement [SELECT test.* FROM test WHERE test.id = $1 LIMIT 2]
2022-07-07 14:50:34.405 DEBUG [,ae0208e5cceee3fd,ae0208e5cceee3fd] 53092 --- [ctor-http-nio-4] io.r2dbc.postgresql.PARAM                : [cid: 0x1][pid: 69] Bind parameter [0] to: 1
2022-07-07 14:50:34.406 DEBUG [,ae0208e5cceee3fd,ae0208e5cceee3fd] 53092 --- [ctor-http-nio-4] io.r2dbc.postgresql.QUERY                : [cid: 0x1][pid: 69] Executing query: SELECT test.* FROM test WHERE test.id = $1 LIMIT 2
******** Unknown traceId: 63e18222d0b220d0
2022-07-07 14:50:34.406 DEBUG [,63e18222d0b220d0,63e18222d0b220d0] 53092 --- [ctor-http-nio-4] i.r.p.client.ReactorNettyClient          : [cid: 0x1][pid: 69] Request:  [Bind{name='B_2', parameterFormats=[FORMAT_TEXT], parameters=[CompositeByteBuf(ridx: 0, widx: 1, cap: 1, components=1)], resultFormats=[], source='S_0'}, Describe{name='B_2', type=PORTAL}, Execute{name='B_2', rows=0}, Close{name='B_2', type=PORTAL}, Sync{}]
```

## Findings

- Disabling the DB connection pool causes the mixed up traceId to go away

