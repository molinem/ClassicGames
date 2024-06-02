# ClassicGames


### Descripción del Proyecto
Se desea desarrollar una aplicación web para jugar a juegos de tablero de dos jugadores. El sistema ofrecerá a los usuarios al menos dos juegos: "Las Cuatro en Raya" y otro juego a elegir por el grupo, en nuestro caso el juego de la escoba.
***

### Requisitos Técnicos</br>
🔹 **Comunicación HTTP**: Los envíos de información del user agent al servidor se realizarán por HTTP. </br>
🔹 **Actualizaciones de Tablero**: Las actualizaciones de tablero desde el servidor al user agent se harán por WebSockets (ws). </br>
🔹 **Acceso de Usuarios**: Podrá jugar todo el mundo, independientemente de que esté o no registrado.</br>
🔹 **Robot**: Si un jugador desea jugar a "Las Cuatro en Raya" y lleva 30 segundos sin encontrar oponente, jugará contra un robot.</br>
🔹 **Pruebas**: Será preciso hacer pruebas de dos tipos: con Selenium y con JMeter.
***
### Tecnologías Utilizadas

#### Frameworks</br>
🔸 **Angular** (para el frontend) </br>
🔸 **Spring Boot** (para el backend)

#### Bibliotecas</br>
🔸 **JUnit** (para pruebas unitarias) </br>
🔸 **Selenium** (para pruebas end-to-end) </br>
🔸 **JMeter** (para pruebas de rendimiento) </br>
🔸 **Stripe** (para el sistema de pagos) </br>
***

| Ítem                                                                                                      | Realizado |
|-----------------------------------------------------------------------------------------------------------|:--------:|
| Funcionamiento correcto de todos los casos de uso (incluyendo robot)                                       |    ✅    |
| Si el robot no es puramente aleatorio, sino que juega un poco bien                                         |    ✅    |
| Tests con Selenium                                                                                        |    ✅    |
| Tests con JMeter                                                                                          |    ✅    |
| Adición de un chat de texto                                                                               |    ✅    |
| Servicio de pagos: las partidas de 4 en raya cuestan 1 euro cada una, con lo que habrá que usar un proveedor de pagos. |    ✅    |
***

### Instalación y Ejecución
#### Requisitos Previos
- Node.js
- Eclipse
- Java JDK 11 o superior

#### Pasos para el Frontend
1. Clonar el repositorio: `git clone https://github.com/molinem/tyweb2023.git`
2. Navegar al directorio del frontend: `cd proyecto/frontend`
3. Instalar dependencias: `npm install`
4. Ejecutar: `ng serve`

#### Pasos para el Backend y la Base de Datos
1. Navegar al directorio del backend: `cd tysweb2023`
2. Inicializar la base de datos con los parámetros del backend. Esto puede implicar configurar las propiedades de la base de datos en `application.properties`.
4. Ejecutar la aplicación: `Hacer click sobre el botón de Play en el entorno de desarrollo Eclipse`

