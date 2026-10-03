# Arquitectura del módulo OrangeHRM

## Estructura

- `Core/`: componentes compartidos y cliente reutilizable de Jira.
- `OrangeHRM/`: Page Objects, configuración y pruebas de OrangeHRM.
- `eCommerce/`: ejemplo original; no agregar aquí nuevos casos de OrangeHRM.

El `pom.xml` raíz agrega `Core`, `eCommerce` y `OrangeHRM`. Ejecuta Maven desde la raíz con `maven\bin\mvn.cmd`.

## Page Object Model

Los tests describen el escenario y las aserciones. Los Page Objects encapsulan selectores, esperas y acciones.

Page Objects principales:

- `LoginPage`: carga, credenciales, Login, errores y mensajes Required.
- `DashboardPage`: confirmación del Dashboard, navegación a PIM y Logout.
- `PimEmployeeListPage`: Employee List, Add Employee, búsqueda y apertura de ficha.
- `AddEmployeePage`: alta, validaciones obligatorias y cancelación.
- `PersonalDetailsPage`: persistencia de datos, regreso a la lista y Logout.

Antes de crear una clase nueva, comprueba si la acción pertenece a uno de estos objetos. Mantén los selectores en la página que representan.

## Base de pruebas

`BaseOrangeHrmTest` centraliza navegador, configuración, credenciales, limpieza y captura ante fallos. Conserva `alwaysRun = true` en hooks que deban ejecutarse incluso al filtrar por grupos.

El navegador funciona headless por defecto. `-Dorangehrm.headless=false` habilita una ejecución visible.

## Datos y estabilidad

La demo es compartida. Los casos que crean empleados deben generar datos únicos y buscar dentro de la misma sesión cuando sea posible.

Usa esperas explícitas sobre estados observables. No consideres suficiente que aparezca el encabezado de una página cuando todavía faltan datos reactivos por cargar.

Para reemplazar Employee ID, selecciona y elimina el valor existente antes de escribir; `clear()` puede no funcionar correctamente con el campo reactivo.

## Dependencias confirmadas

- Selenium `4.38.0`.
- TestNG `7.11.0`.
- Maven Surefire `3.0.0-M5`.
- Compilación configurada para source y target Java 8.

No actualices versiones durante una corrección funcional salvo que el usuario lo solicite o la incompatibilidad sea la causa demostrada.

