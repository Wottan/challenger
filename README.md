
# 📦 Challenger

Aplicación desarrollada con **Spring Boot** y **WebFlux** para simular el procesamiento reactivo de pedidos. Utiliza programación no bloqueante para manejar múltiples solicitudes concurrentemente con alta eficiencia.

## 🚀 Tecnologías utilizadas

- Java 21
- Spring Boot
- Spring WebFlux
- Maven
- Reactor (Mono/Flux)

## ⚙️ Configuración y ejecución

### Prerrequisitos

- Java 17+
- Maven 3.8+

### Ejecutar localmente

```bash
git clone https://github.com/Wottan/challenger.git
cd challenger
mvn spring-boot:run
```

La aplicación estará disponible en: `http://localhost:8080`

## 📡 API REST

### `POST /processOrder`

Este endpoint procesa un pedido de forma reactiva y no bloqueante.

#### Request

```json
{
  "orderId": "ORD123456",
  "customerAmount": 150.3,
  "customerId": "CUST7890",
  "orderItems": [
    {
      "description": "item1",
      "price": 10.0
    },
    {
      "description": "item2",
      "price": 20.0
    },
    {
      "description": "item3",
      "price": 30.0
    }
  ]
}
```

#### Response

```json
{
    "orderId": "ORD123456",
    "customerId": "CUST7890",
    "totalPrice": 210.3
}
```

> ⚙️ Internamente, se usa `Mono` para representar una respuesta asincrónica y reactiva.

## Dentro de la carpeta resources se encuentra una collection para Postman:

- Pedido API Collection.postman_collection.json

## 🧪 Pruebas

```bash
mvn test
```

## 📈 Correr LoadTest

## 📊 Resumen de Métricas Clave

| Métrica                         | Valor promedio     | Fuente                         |
|---------------------------------|--------------------|--------------------------------|
| ⏱️ Tiempo de respuesta promedio | 415 ms             | `http_server_requests_seconds` |
| 🚀 Throughput (RPS)             | 17 solicitudes/seg | `http_server_requests_seconds_count` |
| 🧠 Memoria usada (heap)         | 59.2 MiB           | `jvm_memory_used_bytes`        |
| ⚙️ CPU utilizada                | 0.078%             | `process_cpu_usage`    |
| 🕒 Latencia máxima observada    | 785 ms             | `http_server_requests_seconds_max` |

---

## 🔬 Detalles Técnicos

Las métricas fueron obtenidas usando Prometheus a través del endpoint `/actuator/prometheus`. Las siguientes expresiones PromQL fueron utilizadas:

### ⏱️ Tiempo de Respuesta Promedio

rate(http_server_requests_seconds_sum[1m]) / rate(http_server_requests_seconds_count[1m])

### 🚀 Throughput (Requests per Second)

rate(http_server_requests_seconds_count[1m])

### ⚙️ Uso de CPU
process_cpu_usage * 100

### 🧠 Uso de Memoria

jvm_memory_used_bytes{area="heap"}


En el enpoint `actuator/metrics/pedido_procesamiento_tiempo` se pueden observar:

- Count (cantidad de solicitudes)
- total_time (el total de todas las solicitudes)
- MAX (tiempo maximo)


## 👨‍💻 Autor

Desarrollado por [Wottan](https://github.com/Wottan)
