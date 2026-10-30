# Caching Proxy

A simple caching proxy server built with Spring Boot. It forwards `GET` requests to an origin server and caches the responses in memory, so repeated requests are served from the cache.

## Requirements

- Java 21

## Running

```bash
./mvnw spring-boot:run -Dspring-boot.run.arguments="--port=3000 --origin=http://dummyjson.com"
```

Or build a jar and run it:

```bash
./mvnw package
java -jar target/caching-proxy-0.0.1-SNAPSHOT.jar --port=3000 --origin=http://dummyjson.com
```

| Option     | Description                    | Default                  |
|------------|--------------------------------|--------------------------|
| `--port`   | Port the proxy listens on      | `8080`                   |
| `--origin` | Server requests are forwarded to | `https://www.google.com` |

## Usage

```bash
curl -i http://localhost:3000/products
```

Each response includes an `X-Cache` header:

- `X-Cache: MISS` — fetched from the origin server
- `X-Cache: HIT` — served from the cache

To clear the cache:

```bash
curl http://localhost:3000/_proxy/clear-cache
```
