# Feature Specification: Carrito de Compras y Pedidos

**Version**: 1.0.0  
**Creado**: 28/05/2026  
**Estado**: Implementado  
**Autor**: Equipo AgroTrace  

---

## 1. Carrito de Compras

### 1.1 Entidades

| Entidad | Campos |
|---------|--------|
| **Cart** | `id`, `userId` (FK unique), `status` (ACTIVE/CONVERTED/ABANDONED), `createdAt`, `updatedAt` |
| **CartItem** | `id`, `cartId` (FK), `productId` (FK → products), `quantity`, `unitPrice`, `addedAt` |

### 1.2 Reglas

| ID | Regla |
|----|-------|
| CR-01 | Un usuario tiene maximo 1 carrito ACTIVE |
| CR-02 | El precio se congela al agregar el producto |
| CR-03 | Si el producto ya esta en el carrito, se suma la cantidad |
| CR-04 | La cantidad no puede exceder `quantityAvailable` del producto |
| CR-05 | Solo se pueden agregar productos PUBLICADO |
| CR-06 | 7 dias sin actividad → ABANDONED |
| CR-07 | Al hacer checkout, el carrito pasa a CONVERTED |

### 1.3 Endpoints

| Metodo | Endpoint | Descripcion |
|--------|----------|-------------|
| `GET` | `/cart` | Ver mi carrito activo |
| `POST` | `/cart/items` | Agregar producto `{productId, quantity}` |
| `PUT` | `/cart/items/{itemId}` | Modificar cantidad |
| `DELETE` | `/cart/items/{itemId}` | Quitar producto |
| `DELETE` | `/cart` | Vaciar carrito |
| `POST` | `/cart/checkout` | Convertir carrito en orden |

---

## 2. Pedidos

### 2.1 Entidades

| Entidad | Campos |
|---------|--------|
| **Order** | `id`, `userId`, `totalAmount`, `currency`, `status`, `country`, `shippingAddress`, `contactEmail`, `contactPhone`, `notes`, `createdAt`, `updatedAt` |
| **OrderItem** | `id`, `orderId` (FK), `productId`, `lotId`, `productName`, `quantity`, `unitPrice`, `subtotal` |
| **OrderStatusHistory** | `id`, `orderId` (FK), `status`, `changedAt`, `changedBy`, `notes` |

### 2.2 Estados del Pedido

```
PENDING → CONFIRMED → PROCESSING → SHIPPED → DELIVERED
    ↓          ↓           ↓          ↓
CANCELLED  CANCELLED   CANCELLED  CANCELLED
```

| Estado | Quien cambia |
|--------|-------------|
| `PENDING` | Sistema (checkout) |
| `CONFIRMED` | Productor |
| `PROCESSING` | Productor |
| `SHIPPED` | Exportador (requiere tracking) |
| `DELIVERED` | Exportador (final, no reversible) |
| `CANCELLED` | Comprador (PENDING) o Productor |

### 2.3 Endpoints

| Metodo | Endpoint | Descripcion |
|--------|----------|-------------|
| `GET` | `/orders` | Mis ordenes (comprador) |
| `GET` | `/orders/selling` | Ordenes recibidas (productor) |
| `GET` | `/orders/{id}` | Detalle + historial de estados |
| `PATCH` | `/orders/{id}/status` | Cambiar estado `{status, notes}` |
| `POST` | `/orders/{id}/cancel` | Cancelar orden |
