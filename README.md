#  Práctica 3: Árbol B Acotado

## 👥Equipo

**La Orden del Fénix**

### Integrantes

* Astrid Flores Corona
* Ivone Alejandra Flores Leyva
* Jimena Ortiz Trujillo

---

## 📌 Descripción

En esta práctica se implementó **desde cero un Árbol B acotado de orden 4**, capaz de almacenar llaves enteras sin valores asociados.

La implementación permite realizar las siguientes operaciones:

* `insertar(x)`
* `buscar(x)`
* `eliminar(x)`
* `imprimirPorNiveles()`

El árbol mantiene sus llaves ordenadas y utiliza diferentes mecanismos para conservar las propiedades de un Árbol B:

* **Split**
* **Promoción de llaves**
* **Redistribución**
* **Fusión**

La práctica utiliza un orden fijo:

```text
m = 4
```

Por lo tanto, cada nodo puede tener como máximo **4 hijos y 3 llaves**.

---

## 💻 Lenguaje utilizado

La implementación fue realizada en:

**Java**

No se utilizaron bibliotecas que implementen directamente árboles B o estructuras equivalentes.

La estructura se implementó utilizando arreglos para almacenar las llaves y referencias a los hijos.

---

# ▶️ Ejecución

## Ejecutar el programa principal

Desde la carpeta del proyecto se debe compilar el código:

```bash
javac -d . src/implementacion/Nodo.java src/implementacion/ArbolB.java src/Main.java
```

Después se ejecuta:

```bash
java Main
```

El programa principal muestra las operaciones de **inserción, búsqueda y eliminación**, además de permitir observar la estructura del árbol mediante `imprimirPorNiveles()`.

---

# 🧪 Ejecutar las pruebas

Las pruebas se encuentran en:

```text
tests/PruebasArbolB.java
```

Se pueden compilar junto con la implementación:

```bash
javac -d . src/implementacion/Nodo.java src/implementacion/ArbolB.java tests/PruebasArbolB.java
```

Y ejecutar mediante:

```bash
java PruebasArbolB
```

Las pruebas muestran el resultado esperado y el resultado obtenido en las operaciones estructurales, además de utilizar comprobaciones para las búsquedas.

---

# 📁 Estructura del proyecto

```text
practica-3-btree/
│
├── src/
│   ├── Main.java
│   └── implementacion/
│       ├── ArbolB.java
│       └── Nodo.java
│
├── tests/
│   └── PruebasArbolB.java
│
└── README.md
```

---

# 🧩 Representación del nodo

La clase `Nodo` representa cada nodo del Árbol B mediante tres elementos principales:

```java
int[] claves;
Nodo[] hijos;
int numClaves;
```

* `claves`: almacena las llaves enteras del nodo en orden.
* `hijos`: almacena las referencias a los hijos.
* `numClaves`: indica cuántas llaves están actualmente almacenadas.

En nuestra implementación:

```java
claves = new int[4];
hijos = new Nodo[5];
```

Se utilizan arreglos de tamaño 4 y 5 porque durante una inserción un nodo puede llegar temporalmente a tener cuatro llaves antes de realizar el `split`.

Un nodo es hoja cuando su primer hijo es `null`:

```java
public boolean esHoja() {
    return hijos[0] == null;
}
```

---

# 🔢 ¿Qué significa `m = 4`?

En un Árbol B de orden `m`, cada nodo puede tener como máximo `m` hijos y `m - 1` llaves.

En esta práctica:

```text
m = 4
```

Por lo tanto:

```text
Máximo de hijos = 4
Máximo de llaves = 3
```

Un nodo puede representarse como:

```text
[10 | 20 | 30]
```

Y, si es un nodo interno:

```text
          [10 | 20 | 30]
         /    |    |    \
       P0    P1   P2     P3
```

Los nodos que no son la raíz deben conservar al menos una llave.

La raíz es un caso especial y puede tener menos llaves.

---

# 🔎 ¿Cómo se decide qué hijo seguir durante una búsqueda?

La búsqueda comienza en la raíz y compara la llave que se está buscando con las llaves ordenadas del nodo actual.

Por ejemplo:

```text
[20 | 40 | 70]
```

Los intervalos son:

```text
P0: x < 20
P1: 20 < x < 40
P2: 40 < x < 70
P3: x > 70
```

Por lo tanto, no es necesario recorrer todos los hijos.

Primero se comparan las llaves del nodo para determinar el intervalo en el que se encuentra `x`. Después se continúa únicamente por el hijo correspondiente.

Por ejemplo, si buscamos `50`:

```text
40 < 50 < 70
```

Por lo tanto, debemos continuar por el tercer hijo, considerando que los hijos se numeran desde `0`.

La búsqueda continúa de esta forma hasta encontrar la llave o llegar a una hoja donde la llave no exista.

### ¿Por qué una búsqueda no debe recorrer todos los hijos?

Porque las llaves del nodo dividen el espacio de búsqueda en intervalos. Una vez que se compara `x` con las llaves del nodo, es posible determinar exactamente qué hijo contiene el intervalo en el que podría encontrarse `x`.

Recorrer todos los hijos sería innecesario y eliminaría una de las ventajas principales de la estructura.

---

# ➕ Inserción

Para insertar una llave se busca primero la hoja donde debe colocarse.

La nueva llave se inserta manteniendo el orden de las llaves del nodo.

Por ejemplo:

```text
[10 | 20 | 40]
```

Al insertar `30`, temporalmente se obtiene:

```text
[10 | 20 | 30 | 40]
```

Un nodo no puede conservar cuatro llaves permanentemente, por lo que se produce un desbordamiento y se realiza un **split**.

---

# ✂️ Split y promoción

La práctica utiliza una convención específica para realizar el `split`.

Cuando aparecen cuatro llaves ordenadas:

```text
[k1 | k2 | k3 | k4]
```

se promueve siempre la **tercera llave**:

```text
k3
```

El resultado es:

```text
          [k3]
         /    \
 [k1 | k2]    [k4]
```

Por ejemplo:

```text
[10 | 20 | 30 | 40]
```

se divide como:

```text
          [30]
         /    \
 [10 | 20]    [40]
```

Por lo tanto, la llave promovida no es la mayor. En esta práctica siempre se utiliza la tercera llave de las cuatro llaves ordenadas.

Si el padre también alcanza cuatro llaves como consecuencia de la promoción, el `split` se continúa hacia arriba.

Si la división llega a la raíz, se crea una nueva raíz.

---

# ⬆️ Propagación de un split

Una división puede provocar que el padre también se desborde.

El proceso es:

```text
Insertar llave
      ↓
La hoja llega a 4 llaves
      ↓
    Split
      ↓
Promover tercera llave
      ↓
Insertar la llave promovida en el padre
      ↓
¿El padre tiene 4 llaves?
      ↓
   Sí → Split
      ↓
Continuar hacia arriba
```

Cuando la raíz se divide, se crea una nueva raíz.

Esto permite que el árbol conserve su estructura balanceada.

---

# 🔁 Llaves repetidas

La práctica **no permite llaves repetidas**.

Antes de realizar una inserción, el método `insertar(x)` verifica si la llave ya existe mediante `buscar(x)`.

Si la llave ya se encuentra en el árbol, la operación termina sin modificar la estructura.

Por ejemplo:

```java
arbol.insertar(35);
arbol.insertar(35);
```

La llave `35` debe aparecer solamente una vez.

---

# 📊 Impresión por niveles

La operación:

```java
imprimirPorNiveles()
```

permite observar la estructura del árbol nivel por nivel.

Para la secuencia de inserción indicada en la práctica, la estructura esperada es:

```text
Nivel 0: [45]
Nivel 1: [15 | 30] [60]
Nivel 2: [5 | 10] [20 | 25] [35 | 40] [50] [70]
```

En nuestra implementación se utiliza un arreglo como cola auxiliar para recorrer los nodos por niveles.

---

# 🗑️ Eliminación

La eliminación comienza buscando la llave que se desea eliminar.

Si la llave se encuentra en una hoja, se elimina directamente.

Por ejemplo:

```text
[20 | 25]
```

al realizar:

```text
eliminar(25)
```

queda:

```text
[20]
```

En este caso no existe una subocupación porque el nodo todavía conserva una llave.

---

# ⚠️ Subocupación (Underflow)

Para un Árbol B de orden 4, los nodos que no son la raíz deben tener al menos una llave.

Por lo tanto, si después de una eliminación un nodo queda con:

```text
[]
```

existe una subocupación (`underflow`).

La eliminación debe continuar con una reparación.

La reparación puede realizarse mediante:

1. **Redistribución**
2. **Fusión**

---

# 🔄 Redistribución

La redistribución se utiliza cuando un hermano tiene una llave adicional que puede prestar.

En nuestra implementación se intenta primero con el **hermano izquierdo** y después con el **hermano derecho**.

La llave no pasa directamente de un hermano al otro. El padre participa en el proceso.

De manera conceptual:

```text
hermano → padre → nodo con underflow
```

Esto permite mantener correctamente las llaves separadoras entre los hijos.

---

# 🔗 Fusión

Si ninguno de los hermanos puede prestar una llave, se realiza una fusión.

La fusión combina:

```text
nodo con underflow
        +
llave separadora del padre
        +
      hermano
```

Al realizar la fusión, el padre pierde una llave y uno de sus hijos.

Por esta razón, una fusión puede provocar una nueva subocupación en el padre y la reparación puede continuar hacia arriba.

---

# 📉 Reducción de la raíz

La raíz es un caso especial.

Si después de una eliminación la raíz queda sin llaves pero conserva un único hijo, ese hijo se convierte en la nueva raíz.

Conceptualmente:

```text
raíz vacía
    |
    ↓
único hijo
```

se transforma en:

```text
nueva raíz = único hijo
```

Esto disminuye la altura del árbol.

---

# 🧪 Casos de prueba

La práctica contiene **nueve casos principales de prueba**.

### Prueba 1: Árbol vacío

Se crea un árbol vacío y se ejecuta:

```text
buscar(10)
```

Resultado esperado:

```text
NOT_FOUND
```

### Prueba 2: Inserción sin split

Se insertan:

```text
20, 40, 10
```

Resultado esperado:

```text
[10 | 20 | 40]
```

### Prueba 3: Primer split de la raíz

Se inserta:

```text
30
```

El nodo temporalmente contiene:

```text
[10 | 20 | 30 | 40]
```

Se promueve `30`.

Resultado:

```text
[30]
[10 | 20] [40]
```

### Prueba 4: Búsqueda

Se realizan las búsquedas:

```text
buscar(20)
buscar(30)
buscar(99)
```

Resultados esperados:

```text
20 -> FOUND
30 -> FOUND
99 -> NOT_FOUND
```

### Prueba 5: División y propagación

Se crea un árbol nuevo y se insertan:

```text
20, 40, 10, 30, 50, 60, 70, 5, 15, 25, 35, 45
```

Resultado esperado:

```text
[45]
[15 | 30] [60]
[5 | 10] [20 | 25] [35 | 40] [50] [70]
```

Esta prueba permite comprobar la propagación de divisiones hasta la raíz.

### Prueba 6: Llave repetida

Se intenta insertar nuevamente:

```text
35
```

El árbol no debe modificarse y `35` debe aparecer una sola vez.

### Prueba 7: Eliminación sin underflow

Se ejecuta:

```text
eliminar(25)
```

La hoja:

```text
[20 | 25]
```

debe quedar:

```text
[20]
```

La altura del árbol no debe cambiar.

### Prueba 8: Reparación de underflow

Después de la prueba anterior se ejecuta:

```text
eliminar(10)
eliminar(70)
```

Resultado esperado:

```text
[30]
[15] [45]
[5] [20] [35 | 40] [50 | 60]
```

### Prueba 9: Reducción de altura

Finalmente se ejecuta:

```text
eliminar(5)
```

Resultado esperado:

```text
[30 | 45]
[15 | 20] [35 | 40] [50 | 60]
```

En este caso, la raíz anterior queda vacía durante la reparación y su único hijo se convierte en la nueva raíz.

---

# ✅ Secuencia final de verificación

La secuencia completa utilizada para comprobar el funcionamiento del árbol es:

```text
insertar(20)
insertar(40)
insertar(10)
insertar(30)
insertar(50)
insertar(60)
insertar(70)
insertar(5)
insertar(15)
insertar(25)
insertar(35)
insertar(45)

imprimirPorNiveles()

buscar(35)
buscar(99)

eliminar(25)
eliminar(10)
eliminar(70)
eliminar(5)

imprimirPorNiveles()

buscar(25)
buscar(35)
```

Los resultados relevantes esperados son:

```text
buscar(35) -> FOUND
buscar(99) -> NOT_FOUND
buscar(25) -> NOT_FOUND
buscar(35) -> FOUND
```

Y el árbol final esperado es:

```text
[30 | 45]
[15 | 20] [35 | 40] [50 | 60]
```

---

# 🔍 Invariantes comprobadas

Después de las operaciones se verifica que el árbol conserve sus propiedades principales:

* Las llaves de cada nodo permanecen ordenadas.
* Ningún nodo conserva más de tres llaves después de terminar una operación.
* Ningún nodo distinto de la raíz queda vacío.
* Un nodo interno con `r` llaves tiene `r + 1` hijos.
* Los hijos corresponden a los intervalos determinados por las llaves del padre.
* Todas las hojas permanecen en el mismo nivel.
* Una llave insertada puede ser encontrada mediante `buscar`.
* Una llave eliminada ya no puede ser encontrada mediante `buscar`.

---

# ❓ Preguntas para el README

## ¿Por qué al insertar una llave nueva no podemos decidir el hijo únicamente comparando con la primera llave del nodo?

Porque un nodo puede contener varias llaves y cada una de ellas divide el árbol en diferentes intervalos.

Por ejemplo:

```text
[20 | 40 | 70]
```

existen cuatro posibles intervalos:

```text
x < 20
20 < x < 40
40 < x < 70
x > 70
```

Si solamente comparáramos con la primera llave (`20`), únicamente sabríamos si el valor es menor o mayor que `20`, pero no podríamos determinar correctamente cuál de los otros intervalos corresponde.

Por ejemplo, para insertar `50`:

```text
50 > 20
```

pero todavía necesitamos comparar con `40` y `70` para determinar que:

```text
40 < 50 < 70
```

y así elegir el hijo correcto.

Por esta razón, se deben comparar las llaves ordenadas del nodo hasta encontrar el intervalo correspondiente.

---

## ¿Por qué una búsqueda no debe recorrer todos los hijos de un nodo?

Porque las llaves del nodo están ordenadas y funcionan como separadores de intervalos.

Una vez que se compara la llave buscada con las llaves del nodo, se puede determinar exactamente qué hijo puede contenerla.

Por ejemplo:

```text
[15 | 30 | 60]
```

Si buscamos `45`:

```text
30 < 45 < 60
```

por lo que solamente necesitamos continuar por el hijo correspondiente a ese intervalo.

Recorrer todos los hijos sería innecesario, ya que los demás hijos representan intervalos donde la llave buscada no puede encontrarse.

La búsqueda de un Árbol B aprovecha precisamente esta organización para recorrer solamente la rama correspondiente en cada nivel.

---

# 📝 Conclusión

La práctica permitió implementar **un Árbol B de orden 4 desde cero** y observar cómo la estructura mantiene el balance durante las operaciones de inserción y eliminación.

Los principales mecanismos utilizados son:

```text
Búsqueda por intervalos
        ↓
Inserción ordenada
        ↓
Split y promoción
        ↓
Propagación hacia el padre
        ↓
Redistribución o fusión durante eliminación
        ↓
Reducción de la raíz
```

De esta manera, el árbol puede mantener sus llaves ordenadas, limitar el número de llaves por nodo y conservar todas sus hojas al mismo nivel.
