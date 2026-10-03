# Guía del Repositorio

## Estructura
- Este workspace puede abrirse directamente en IntelliJ desde el `pom.xml` raíz. `Core/` es el framework compartido de Selenium/TestNG/Excel; `eCommerce/` conserva el ejemplo original de DemoQA; `OrangeHRM/` contiene la automatización nueva.
- `eCommerce/.classpath` aporta la dependencia no declarada hacia el proyecto hermano de Eclipse `/Core`. Ambos POM usan las mismas coordenadas de Maven y ningún POM vincula los proyectos.
- El test real de navegador es `eCommerce/src/main/java/com/automation/eCommerce/TestCases.java`; los archivos `src/test/java/.../AppTest.java` son solo smoke tests de JUnit generados.

## Comandos Y Estado Actual
- Usar el Maven incluido desde la raíz en Windows: `maven\bin\mvn.cmd test`. El POM raíz agrega `Core`, `eCommerce` y `OrangeHRM`.
- Ejecutar un único smoke test de JUnit con `..\maven\bin\mvn.cmd -Dtest=AppTest test`, una vez restaurada la compilación.
- No reportar una compilación o prueba como exitosa basándose en artefactos antiguos de `target/`. Ejecutar Maven y conservar el primer error real cuando exista.
- El repositorio no tiene tareas de lint, formateo, typecheck, CI ni pre-commit.

## Ejecución De Tests
- El entrypoint basado en datos es `com.automation.eCommerce.Test.main()`: `Core.Excel` lee `eCommerce/tests_to_run.xls`, construye suites de TestNG y selecciona servidor, entornos, navegadores, clases y métodos a partir de las hojas del libro.
- `eCommerce/testng.xml` es la alternativa directa con TestNG y proporciona los cuatro `@Parameters` obligatorios: `server`, `environment`, `browserName` y `browserVersion`. Mantener sincronizados los nombres de los parámetros con `TestCases.setUpClass`.
- Ejecutar la automatización del navegador con el directorio de trabajo en la raíz del workspace o en `eCommerce/`; `TestSuite` resuelve `tests_to_run.xls` usando `user.dir` y el nombre literal de proyecto `eCommerce`.
- Las ejecuciones locales abren un Chrome, Edge o Firefox real y no headless mediante Selenium Manager. Requieren el navegador seleccionado y acceso de red al target configurado; `browserVersion` no se usa para los drivers locales.
- La salida en runtime está hardcodeada en `Core/UserConstants.java` bajo `C:\QA_Automation\` (`tests_results`, `download` y `testng_output`), no bajo ningún directorio `target/` de Maven.

## Automatización OrangeHRM
- La aplicación objetivo es la demo pública de OrangeHRM. La URL inicial está centralizada en `OrangeHRM/src/main/java/com/automation/orangehrm/OrangeHrmUrls.java`.
- Mantener `eCommerce` como referencia del material original. No trasladar nuevos casos de OrangeHRM a ese módulo.
- Implementar Page Objects separados para Login, Dashboard, Add Employee y Employee List. Evitar `Thread.sleep`; usar esperas explícitas y condiciones observables.
- Nombrar los métodos de prueba con el prefijo `Test` mientras `Core.Excel` conserve ese contrato, e incluir el ID trazable `TC-*` en la descripción TestNG.
- No guardar contraseñas, tokens, datos personales ni identificadores sensibles en código, XML, Excel o reportes. Usar configuración local ignorada o variables de entorno.
- La demo es compartida: generar Employee ID único por ejecución y no asumir que los datos ajenos permanecen estables.
- No modificar un caso o debilitar una aserción únicamente para obtener PASS. Distinguir fallo del producto, fallo de automatización y bloqueo de ambiente.

## IntelliJ y OpenCode
- En IntelliJ, importar el `pom.xml` raíz como proyecto Maven y usar el JDK compatible configurado para todo el reactor.
- OpenCode debe leer `AGENTS.md` y `opencode.json`. Los comandos reutilizables están en `.opencode/commands/`.
- Crear una skill solo después de comprobar manualmente un flujo repetible. Las reglas permanentes del repositorio pertenecen a este archivo, no a una skill.

## Integración con JIRA
- La integración reutilizable reside en `Core/src/main/java/com/automation/core/jira/` y usa JIRA Cloud REST API v3.
- El modo predeterminado es `dry-run`; nunca cambiarlo a publicación automática desde listeners o hooks generales de TestNG.
- Publicar únicamente con `-Djira.mode=publish` explícito y las variables `JIRA_BASE_URL`, `JIRA_EMAIL`, `JIRA_API_TOKEN` y `JIRA_PROJECT_KEY` presentes.
- Nunca guardar el API token en código, POM, README, Excel, reportes ni archivos de configuración versionables.
- Los ejemplos académicos deben conservar `[SIMULADO]`, la etiqueta `simulado` y la advertencia de que no fueron observados en la ejecución real.

## Configuración Acoplada
- Los nombres de las hojas y las reglas de selección son contratos a nivel de código en `Core/Excel.java`: `Server`, `Environment`, `Browsers`, `Tests` y `Email`. Una primera celda vacía marca las filas seleccionadas; los nombres de clase de test empiezan por `com` y los nombres de método seleccionados por `Test`.
- Las etiquetas de entorno distinguen mayúsculas y minúsculas en `Core/TestEnvironment.java`; solo `UAT` resuelve una URL actualmente, mientras que `QA` devuelve una cadena vacía.
- Los métodos de test dependen de los hooks de ciclo de vida de `TestSuite` para inicializar `excel`, `driver` y `commonCore`, y para cerrar el driver tras cada método. Preservar esa herencia y ese ciclo de vida al añadir tests.
