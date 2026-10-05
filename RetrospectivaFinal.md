# Retrospectiva final del proyecto slotMachine — DOPO-POOB 2026-2

Reúne las retrospectivas de **todos los ciclos** del proyecto, tomadas de los README de cada ciclo, para la entrega final. Autores: Kevin Garzón Romero y Daniel Blanco Salazar.

| Ciclo | Periodo | Entregó | README de origen | Estado |
|---|---|---|---|---|
| 1 | 17 – 23 ago | Requisitos 1–8: crear máquina, ruedas, símbolos, giro, consultas, jackpot, visibilidad, salir | README de los ciclos 1 y 2 | Completo |
| 2 | 31 ago – 6 sep | Requisitos 9–12: `swap`, `lock`/`unlock`, `spin(wheel, steps)`, `spin(String[])` | README de los ciclos 1 y 2 | Completo |
| 3 | 8 – 19 sep | Requisitos 13–15: `SlotMachine(n)`, `solve`, `simulate`; pruebas propias y compartidas | `README_Ciclo3.md` | Completo, falta republicar en Git |
| 4 | ~15 h | ~15 h | ~30 h | Mini-ciclos 1–6 completos; falta Astah, Git y Moodle |

Horas por ciclo (horas/hombre):

| Ciclo | Kevin | Daniel | Total |
|---|---|---|---|
| 1 | ~15 h | ~15 h | ~30 h |
| 2 | ~18–20 h | ~18–20 h | ~36–40 h |
| 3 | ~14 h | ~14 h | ~28 h |
| 4 | ~15 h | ~15 h | ~30 h |

---

## Ciclo 1 (17 – 23 agosto)

### Mini-ciclos del Ciclo 1

| # | Fechas | Descripción | Criterio de completitud | Estado |
|---|---|---|---|---|
| 1 | 17 ago | **Análisis y planificación** — Lectura del enunciado, comprensión del paquete `shapes`, definición de arquitectura y reparto de tareas. | Árbol de clases definido. | [x] |
| 2 | 18–19 ago | **Clases del dominio** — `Symbol` (círculo de color CSS) y `Wheel` (lista circular con marco rectangular). | `Symbol` y `Wheel` funcionando en modo invisible. | [x] |
| 3 | 19–20 ago | **Clase principal** — `addWheel`, `delWheel`, `addSymbol`, `delSymbol`, clamping y `ok()`. | Métodos de gestión pasan pruebas manuales en BlueJ. | [x] |
| 4 | 20–21 ago | **Giro y consultas** — `spin(wheel)`, `spin()`, `placeSymbol()`, `symbols()`, `distinctSymbols()`, `configuration()` e `isJackpot()`. | Jackpot detectado correctamente y consultas coherentes. | [x] |
| 5 | 21–23 ago | **Capa visual y entrega** — `Canvas` extendido, `SlotMachineView`, visibilidad, `exit()`, diagramas Astah, publicación en Git. | Simulador visible con banner dorado; repositorio publicado. | [x] |

---

### Ciclo 1

1. **¿Cuáles fueron los mini-ciclos definidos? Justifíquenlos.**

   Se definieron 5 mini-ciclos organizados por capas de responsabilidad: análisis y planificación, clases del dominio (`Symbol`, `Wheel`), clase principal `SlotMachine`, lógica de giro y consultas, y capa visual con entrega. Esta división permitió avanzar de forma incremental, validando cada capa antes de construir sobre ella.

2. **¿Cuál es el estado actual del proyecto en términos de mini-ciclos? ¿Por qué?**

   Los 5 mini-ciclos del Ciclo 1 están completados. Todos los requisitos funcionales fueron implementados dentro del plazo previsto (23 de agosto).

3. **¿Cuál fue el tiempo total invertido por cada uno de ustedes? (Horas/Hombre)**

   | Integrante | Horas |
   |---|---|
   | Kevin Garzón Romero | ~15 h |
   | Daniel Blanco Salazar | ~15 h |
   | **Total** | **~30 h** |

4. **¿Cuál consideran fue el mayor logro? ¿Por qué?**

   Lograr que el simulador funcione en modo visible e invisible, respetando la API definida en el enunciado, fue el mayor logro. Implicó coordinar el estado interno de las ruedas con la capa gráfica del `Canvas` extendido sin romper la lógica de negocio.

5. **¿Cuál consideran que fue el mayor problema técnico? ¿Qué hicieron para resolverlo?**

   La falta de conocimiento sobre el manejo del `Canvas` con buffer offscreen, los colores CSS en Java y el posicionamiento de formas con coordenadas absolutas generó retrasos. Lo resolvimos consultando la documentación de Java, W3Schools y usando Claude Sonnet 4.6 para dudas puntuales.

6. **¿Qué hicieron bien como equipo? ¿Qué se comprometen a hacer para mejorar los resultados?**

   Leímos todo el documento de requisitos al inicio y separamos el trabajo en mini-ciclos para organizar mejor el tiempo. Como compromiso para el siguiente ciclo, mejorar la comunicación continua y escribir pruebas más sistemáticas.

7. **Considerando las prácticas XP incluidas en los laboratorios, ¿cuál fue la más útil? ¿Por qué?**

   Los laboratorios 1 y 2 nos dieron base sobre el manejo de objetos en Java y el uso del paquete `shapes`. Eso nos permitió tener más estructura en el código desde el inicio.

8. **¿Qué referencias usaron? ¿Cuál fue la más útil?**

   | # | Referencia | Uso |
   |---|---|---|
   | 1 | W3Schools. (s.f.). *CSS Legal Color Values*. https://www.w3schools.com/cssref/css_colors.php | Nombres de colores CSS para los símbolos. |
   | 2 | Oracle. (s.f.). *ArrayList (Java SE 8)*. https://docs.oracle.com/javase/8/docs/api/java/util/ArrayList.html | Métodos de `ArrayList` en `Wheel` y `SlotMachine`. |
   | 3 | Oracle. (s.f.). *javax.swing.JOptionPane*. https://docs.oracle.com/javase/8/docs/api/javax/swing/JOptionPane.html | Diálogos de error en modo visible. |
   | 4 | Kolling, M. & Barnes, D. J. (2016). *Objects First with Java* (6.ª ed.). Pearson. | Referencia principal de diseño OO y uso de BlueJ. |
   | 5 | Anthropic. (2026). *Claude Sonnet 4.6* [IA]. https://www.anthropic.com | Resolución de dudas sobre el código. |

   La más útil fue **W3Schools (CSS Color Values)**.

---

## Ciclo 2 (31 agosto – 6 septiembre)

### Mini-ciclos del Ciclo 2

| # | Fechas | Descripción | Criterio de completitud | Estado |
|---|---|---|---|---|
| 1 | 31 ago | **Análisis Ciclo 2** — Lectura del enunciado, actualización de diagramas Astah con los 5 métodos nuevos. | Diseño actualizado y tareas distribuidas. | [x] |
| 2 | 1 sep | **Bloqueo en `Wheel`** — Campo `locked`, métodos `lock()`, `unlock()`, `isLocked()` e indicador visual (marco rojo/azul). | Rueda bloqueada no gira; indicador visual correcto. | [x] |
| 3 | 2 sep | **`swap` en `SlotMachine`** — Intercambio de ruedas con reposicionamiento visual. | Configuración visual intercambia correctamente. | [x] |
| 4 | 3 sep | **`spin(steps)` y `spin(String[])`** — Giro animado por pasos y configuración directa de la máquina. | Animación visible paso a paso; configuración aplicada. | [x] |
| 5 | 4–5 sep | **Pruebas unitarias** — `SlotMachineC2Test` (12 casos propios) y `SlotMachineCC2Test` (2 casos compartidos), todos en modo invisible. | Todos los casos pasan sin errores. | [x] |
| 6 | 5–6 sep | **Integración y entrega** — Revisión de código, ajuste de comentarios, actualización del README, publicación en Git. | Repositorio actualizado antes de la fecha de entrega. | [x] |

---

### Ciclo 2

1. **¿Cuáles fueron los mini-ciclos definidos? Justifíquenlos.**

   Se definieron 6 mini-ciclos de un día cada uno: análisis y diseño, bloqueo en `Wheel`, `swap`, `spin(steps)` y `spin(String[])`, pruebas unitarias, e integración y entrega. Un mini-ciclo por día permitió mantener el foco en una sola responsabilidad sin mezclar cambios.

2. **¿Cuál es el estado actual del proyecto en términos de mini-ciclos? ¿Por qué?**

   Casi todos los mini-ciclos están completados. Los 5 métodos nuevos funcionan correctamente y las pruebas unitarias cubren los casos principales. El mini-ciclo de integración y entrega se cerró el 6 de septiembre.

3. **¿Cuál fue el tiempo total invertido por cada uno de ustedes? (Horas/Hombre)**

   | Integrante | Horas |
   |---|---|
   | Kevin Garzón Romero | ~18–20 h |
   | Daniel Blanco Salazar | ~18–20 h |
   | **Total** | **~36–40 h** |

4. **¿Cuál consideran fue el mayor logro? ¿Por qué?**

   La correcta integración del código nuevo con el del Ciclo 1 sin romper nada de lo ya implementado. Agregar `locked`, `swap` y los nuevos `spin` requirió entender bien el estado interno de `Wheel` y `SlotMachine` para no generar efectos secundarios inesperados.

5. **¿Cuál consideran que fue el mayor problema técnico? ¿Qué hicieron para resolverlo?**

   La falta de conocimiento sobre cómo estructurar pruebas unitarias en BlueJ (sin JUnit formal) fue el mayor obstáculo. Lo resolvimos revisando los laboratorios 1 y 2 de la materia como referencia y consultando documentación de BlueJ para entender cómo ejecutar métodos de prueba manualmente.

6. **¿Qué hicieron bien como equipo? ¿Qué se comprometen a hacer para mejorar los resultados?**

   La comunicación fue bastante buena durante el ciclo y aprovechamos la base de los laboratorios 1 y 2 para estructurar mejor el código. Para mejorar, nos comprometemos a aumentar la eficiencia en la distribución de tareas y a mantener una comunicación más constante durante el desarrollo.

7. **Considerando las prácticas XP incluidas en los laboratorios, ¿cuál fue la más útil? ¿Por qué?**

   Los laboratorios 1 y 2 siguieron siendo la referencia más útil. El laboratorio 2 en particular, por el uso del paquete `shapes` y el `Canvas`, fue clave para entender cómo integrar los cambios visuales del Ciclo 2 (indicador de bloqueo, animación de giro) sin alterar la lógica existente.

8. **¿Qué referencias usaron? ¿Cuál fue la más útil?**

   | # | Referencia | Uso |
   |---|---|---|
   | 1 | W3Schools. (s.f.). *CSS Legal Color Values*. https://www.w3schools.com/cssref/css_colors.php | Colores CSS para símbolos. |
   | 2 | Oracle. (s.f.). *ArrayList (Java SE 8)*. https://docs.oracle.com/javase/8/docs/api/java/util/ArrayList.html | Métodos de colecciones en las clases del dominio. |
   | 3 | Oracle. (s.f.). *Thread.sleep / Canvas.wait*. https://docs.oracle.com/javase/8/docs/api/java/lang/Thread.html | Implementación del delay en `spin(steps)`. |
   | 4 | Kolling, M. & Barnes, D. J. (2016). *Objects First with Java* (6.ª ed.). Pearson. | Referencia de diseño OO, pruebas y BlueJ. |
   | 5 | Anthropic. (2026). *Claude Sonnet 4.6* [IA]. https://www.anthropic.com | Resolución de dudas sobre el código. |

   La más útil en este ciclo fue **Kolling & Barnes**, especialmente la sección de pruebas con BlueJ.
---

## Ciclo 3 (8 – 19 septiembre)

### Ciclo 3

**1. ¿Cuáles fueron los mini-ciclos definidos? Justifíquenlos.**

Se definieron 7 mini-ciclos organizados por responsabilidad incremental: análisis, constructor con inicialización aleatoria, algoritmo de resolución, simulación visual, pruebas propias, pruebas compartidas e integración final. Esta estructura permitió probar cada pieza de forma aislada antes de integrarla con las demás, reduciendo el riesgo de romper lo que ya funcionaba.

**2. ¿Cuál es el estado actual del proyecto en términos de mini-ciclos? ¿Por qué?**

Los mini-ciclos 1 a 6 y 8 están completos. El 8 se agregó cuando se descubrió que `distinctSymbols()` devolvía siempre `n` y que `solve` y `simulate` no llegaban al jackpot; se corrigió el método, se reescribió el algoritmo y se agregaron pruebas que comprueban el jackpot. El mini-ciclo 7 queda a medias: la documentación y el `.txt` están listos, pero falta republicar los cambios en Git. Los requisitos 13, 14 y 15 están cumplidos y todos los fallos documentados en `fallos.md` están resueltos; solo faltan rehacer los diagramas de Astah para que reflejen el diseño actual.

**3. ¿Cuál fue el tiempo total invertido por cada uno de ustedes? (Horas/Hombre)**

| Integrante | Horas |
|---|---|
| Kevin Garzón Romero | ~14 h |
| Daniel Blanco Salazar | ~14 h |
| **Total** | **~28 h** |

**4. ¿Cuál consideran fue el mayor logro? ¿Por qué?**

Resolver el problema usando únicamente `distinctSymbols()` como señal, sin acceso al estado interno de la máquina: se deduce la posición relativa de cada rueda a partir de cómo cambia el conteo al girarla una vuelta completa. Se verificó con 24 500 máquinas aleatorias (n de 2 a 50) sin ningún fallo y con un máximo de 2549 acciones. También se mantuvo la separación de responsabilidades entre `SlotMachine` y `SlotMachineContest` que pide el DOPO.

**5. ¿Cuál consideran que fue el mayor problema técnico? ¿Qué hicieron para resolverlo?**

El mayor problema fue que `distinctSymbols()` devolvía el número de símbolos definidos (siempre `n`) en lugar de los distintos visibles. Con ese valor el algoritmo de `solve` nunca detectaba una rueda alineada y no llegaba al jackpot, y las pruebas no lo detectaban porque solo validaban la forma del arreglo. Se corrigió `distinctSymbols()` para contar los colores distintos visibles.

Corregirlo no bastó: el algoritmo original (una pasada, elegir la primera posición que baja el conteo) tampoco llegaba al jackpot, por ejemplo con ruedas `[a, b, c]`. Se reescribió: cada rueda gira una vuelta completa para obtener su perfil (qué símbolos están ocupados), la diferencia de posición entre ruedas se deduce de esos perfiles y, cuando el conjunto es simétrico, se rompe la simetría moviendo la rueda 1. Se agregaron pruebas que aplican `solve` sobre una máquina conocida y comprueban `isJackpot()`.

**6. ¿Qué hicieron bien como equipo? ¿Qué se comprometen a hacer para mejorar los resultados?**

La comunicación durante el ciclo fue fluida y la planificación con mini-ciclos nos ayudó a mantener el foco. También logramos cumplir con todas las reglas de diseño del DOPO sin mezclar responsabilidades entre `SlotMachine` y `SlotMachineContest`. Falló que las pruebas solo validaran la forma del resultado y no el objetivo (jackpot), lo que ocultó el fallo principal durante el ciclo. Para mejorar, nos comprometemos a escribir primero pruebas que comprueben el resultado real (desarrollo guiado por pruebas) y a revisar el comportamiento de cada método que usa el algoritmo antes de darlo por terminado.

**7. Considerando las prácticas XP incluidas en los laboratorios, ¿cuál fue la más útil? ¿Por qué?**

La práctica de **diseño simple e incremental** fue la más útil en este ciclo. En lugar de tratar de construir el algoritmo completo desde el principio, lo construimos paso a paso: primero el constructor, luego el algoritmo de una sola rueda, luego el ciclo completo, y finalmente la simulación visual. Eso nos permitió verificar cada paso antes de avanzar.

**8. ¿Qué referencias usaron? ¿Cuál fue la más útil? Incluyan citas con estándares adecuados.**

| # | Referencia | Uso |
|---|---|---|
| 1 | ICPC Foundation. (2025). *ICPC World Finals 2025 — Problem I: Slot Machine*. https://icpc.global | Comprensión del problema de la maratón que se debía resolver. |
| 2 | Oracle. (s.f.). *java.util.Random (Java SE 8)*. https://docs.oracle.com/javase/8/docs/api/java/util/Random.html | Inicialización aleatoria de ruedas en el constructor `SlotMachine(n)`. |
| 3 | Oracle. (s.f.). *ArrayList (Java SE 8)*. https://docs.oracle.com/javase/8/docs/api/java/util/ArrayList.html | Construcción dinámica de la secuencia de acciones en `solve`. |
| 4 | Kolling, M. & Barnes, D. J. (2016). *Objects First with Java* (6.ª ed.). Pearson. | Referencia de diseño OO y pruebas con BlueJ. |
| 5 | Google DeepMind. (2026). *Antigravity (Gemini 2.5 Pro)* [IA]. https://deepmind.google | Asistente utilizado para resolver dudas sobre el algoritmo y la implementación. |

La más útil fue **la documentación del problema ICPC**, ya que definió con precisión las operaciones permitidas y el límite de 10 000 acciones que guiaron todo el diseño.

---

## Ciclo 4 (26 septiembre – 3 octubre)

### Mini-ciclos del Ciclo 4 (26 septiembre – 3 octubre)

| # | Descripción | Criterio de completitud | Estado |
|---|---|---|---|
| 1 | **Análisis, diseño y refactor (26 sep).** Lectura del enunciado, decisiones de diseño; `Symbol` y `Wheel` pasan a jerarquías (`NormalSymbol`, `NormalWheel`) y la secuencia de símbolos guarda el tipo. | Compila y las pruebas anteriores siguen en verde. | [x] |
| 2 | **Fábricas y creación por tipo (27 sep).** `WheelFactory`, `SymbolFactory`, `addWheel(type, pos)` y `addSymbol(type, pos, color)`; tipos inválidos. | Se crea una máquina con tipos mezclados; las firmas antiguas siguen creando `normal`. | [x] |
| 3 | **Ruedas `lefty` y `rebel` (28 sep).** Copia del estado de la vecina izquierda; rechazo de lock/swap/delete; `lock` devuelve `int`. | Pruebas de ambas ruedas verdes, incluida la vecina cambiante y el estado intacto tras un rechazo. | [x] |
| 4 | **Símbolos `ephemeral` y `shy` (29 sep).** Encogimiento hasta el punto; alternancia de visibilidad; regla de jackpot con símbolos escondidos. | Tamaños 48→4 y alternancia comprobados; `isJackpot()` no cambia. | [x] |
| 5 | **Tipo propio y revisión visual (30 sep).** `TurboWheel` (avanza el doble); distinción visual de los tipos; captura con pantalla real. | Los tipos se ven distintos; `solve` y `simulate` siguen igual. | [x] |
| 6 | **Pruebas y aceptación (1 – 2 oct).** `SlotMachineC4Test` (13), `SlotMachineCC4Test` (3), 2 pruebas de aceptación (manuales y automatizadas en `SlotMachineAcceptanceTest`), validación con 15 errores introducidos, documentación. | Todas las pruebas verdes; guion de aceptación ejecutado paso a paso. | [x] 13 + 3 pruebas JUnit 4; 15/15 errores detectados |
| 7 | **Retrospectiva y entrega (3 oct).** Diagramas en Astah, publicación en Git y entrega en Moodle. | Diagramas exportados, repositorio actualizado y `.txt` publicado. | [ ] Faltan los diagramas de Astah, publicar en Git y subir el `.txt` |

---

### Ciclo 4

**1. ¿Cuáles fueron los mini-ciclos definidos? Justifíquenlos.**

Se definieron 7 mini-ciclos ordenados de la base a lo visible: primero el refactor sin cambiar comportamiento (para que las 28 pruebas existentes sirvieran de red de seguridad), después las fábricas (el punto de extensión), luego las dos jerarquías por separado —ruedas y símbolos— porque sus reglas no se tocan entre sí, el tipo propio, las pruebas y la aceptación, y al final la entrega. Separar ruedas de símbolos permitió comprobar cada regla de forma aislada antes de combinarlas, y dejar el refactor primero evitó mezclar «cambio de estructura» con «cambio de comportamiento».

**2. ¿Cuál es el estado actual del proyecto en términos de mini-ciclos? ¿Por qué?**

Los mini-ciclos 1 a 6 están completos: los requisitos 16, 17, 18 y 19 están implementados (`lefty`, `rebel`, `ephemeral`, `shy` y el tipo propio `turbo`), con 13 pruebas propias y 3 compartidas en verde, y las 28 pruebas de los ciclos anteriores siguen pasando. El mini-ciclo 7 queda a medias: la retrospectiva y la documentación están escritas, pero **faltan los diagramas de Astah** (el diagrama de clases y los 9 diagramas de secuencia de lo que cambió ya se generaron con scripts de Astah; faltan los casos de uso y los estados), **publicar en Git** y subir el `.txt` a Moodle. También falta ejecutar y validar el proyecto dentro de BlueJ, ya que las pruebas se corrieron con `javac` y las librerías JUnit de BlueJ.

**3. ¿Cuál fue el tiempo total invertido por cada uno de ustedes? (Horas/Hombre)**

| Integrante | Horas |
|---|---|
| Kevin Garzón Romero | ~15 h |
| Daniel Blanco Salazar | ~15 h |
| **Total** | **~30 h** |

**4. ¿Cuál consideran fue el mayor logro? ¿Por qué?**

Que agregar un tipo nuevo no obligue a tocar `SlotMachine`: `TurboWheel` se añadió con una subclase, una constante y una línea en la fábrica, que es lo que pide el requisito implícito de extensibilidad. Además se mantuvo todo lo anterior intacto: `solve`, `simulate` y las 10 pruebas de `SlotMachineContestTest` no cambiaron y siguen verdes. Y las pruebas se validaron de verdad: de 15 errores introducidos a propósito en el código, las pruebas detectaron los 15.

**5. ¿Cuál consideran que fue el mayor problema técnico? ¿Qué hicieron para resolverlo?**

La secuencia de símbolos era una lista de colores compartida por todas las ruedas; no guardaba el tipo, así que un símbolo `shy` o `ephemeral` no podía crearse en una rueda nueva. Se resolvió guardando el tipo junto al color (`symbolTypes`) y haciendo que cada rueda construya sus propios símbolos con la fábrica; de ahí que una rueda creada después herede los símbolos con su tipo (hay una prueba para eso).

El otro problema fue **dibujar** los tipos de modo distinguible: un `ephemeral` que empieza en 48 px se parece a uno normal, y `Triangle` dibuja desde el vértice y no desde la esquina. Se resolvió con un aro negro fijo alrededor del disco que se encoge, y ajustando la posición del triángulo; se comprobó con una captura de pantalla real. Queda una limitación: un símbolo del mismo color que el marco de su rueda no se ve.

**6. ¿Qué hicieron bien como equipo? ¿Qué se comprometen a hacer para mejorar los resultados?**

Aplicamos lo que prometimos en el ciclo 3: las pruebas comprueban el resultado real (jackpot, tamaños, color mostrado, estado intacto tras un rechazo) y no solo la forma de la salida, y se validaron con errores introducidos a propósito. Planificar los mini-ciclos con fechas nos dio un orden claro. Lo que quedó mal: dejamos los diagramas de Astah para el final, otra vez, y es lo que sigue pendiente. Nos comprometemos a actualizar el diagrama al terminar cada mini-ciclo en los proyectos siguientes, a probar dentro de BlueJ antes de cerrar un ciclo y a consultar con el profesor las interpretaciones dudosas (por ejemplo qué significa «copia su estado» en `lefty`) antes de implementarlas, no después.

**7. Considerando las prácticas XP incluidas en los laboratorios, ¿cuál fue la más útil? ¿Por qué?**

El **refactoring con pruebas de regresión**. Pasar `Symbol` y `Wheel` a clases abstractas tocaba casi todo el proyecto; poder correr las 28 pruebas después de cada paso nos permitió hacer el cambio sin miedo. La segunda más útil fue la **integración continua en pequeño**: compilar y correr todas las pruebas al terminar cada mini-ciclo.

**8. ¿Qué referencias usaron? ¿Cuál fue la más útil? Incluyan citas con estándares adecuados.**

| # | Referencia | Uso |
|---|---|---|
| 1 | Escuela Colombiana de Ingeniería. (2026). *Proyecto inicial 2026-2 — Ciclo No. 4/4: Refactoring y Extensión* [Enunciado en PDF]. | Requisitos 16–19 y diagrama de clases de referencia. |
| 2 | Gamma, E., Helm, R., Johnson, R. & Vlissides, J. (1994). *Design Patterns: Elements of Reusable Object-Oriented Software*. Addison-Wesley. | Idea de fábrica (*Factory Method*) y de jerarquías por tipo. |
| 3 | Fowler, M. (2018). *Refactoring: Improving the Design of Existing Code* (2.ª ed.). Addison-Wesley. | Refactor de `Symbol` y `Wheel` sin cambiar el comportamiento. |
| 4 | Oracle. (s.f.). *Java SE 8 API: java.util.ArrayList, java.util.HashSet*. https://docs.oracle.com/javase/8/docs/api/ | Estructuras usadas en `SlotMachine` y en las pruebas. |
| 5 | JUnit Team. (s.f.). *JUnit 4 — Assume, @Test(timeout)*. https://junit.org/junit4/ | Pruebas, omitir la que necesita pantalla, tiempo máximo. |
| 6 | Kolling, M. & Barnes, D. J. (2016). *Objects First with Java* (6.ª ed.). Pearson. | Diseño OO y BlueJ. |
| 7 | Anthropic. (2026). *Claude Code (Sonnet 5.5)* [IA]. https://claude.com/claude-code | Asistente utilizado probar y documentar este ciclo; todo se revisó contra el código y las pruebas. |

La más útil fue el **enunciado del ciclo y su diagrama de clases**, porque fijó los nombres y firmas (`addWheel(type, pos)`, `addSymbol(type, pos, color)`, `lock`) que se respetaron.

---
