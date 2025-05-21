
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
  "customerAmount": 150.75,
  "customerId": "CUST7890",
  "orderItems": ["item1", "item2", "item3"]
}
```

#### Response

```text
Pedido procesado: ORD123456
```

> ⚙️ Internamente, se usa `Mono` para representar una respuesta asincrónica y reactiva.

## 🧪 Pruebas

```bash
mvn test
```

## 👨‍💻 Autor

Desarrollado por [Wottan](https://github.com/Wottan)
