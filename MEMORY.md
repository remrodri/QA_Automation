# Project Memory

Registro breve de decisiones, verificaciones y restricciones duraderas del proyecto. Leer AGENTS.md primero y consultar este archivo antes de planificar cambios. No almacenar secretos, salidas completas de comandos ni información temporal innecesaria.

### 2026-10-02 — Fase 1 Auditoría inicial

**Estado:** Completado

**Resumen:**
El workspace es Eclipse, no reactor Maven: `Core` es framework y `eCommerce` el proyecto de pruebas, unidos solo por `.classpath`. Ambos `mvn test` reportan SUCCESS únicamente porque reutilizan clases obsoletas de `target/`; el código fuente no compila con su POM. El libro de datos tiene estructura válida pero un desajuste crítico con el código.

**Hechos confirmados:**
- `mvn test` en `Core` y `eCommerce`: "Nothing to compile", 1 test JUnit, BUILD SUCCESS engañoso; clases en `target/` datadas 2026-04-07.
- Compilación real a directorio temporal: `Core` falla con `package org.testng does not exist`; TestNG está declarado `test` pero se usa en `src/main/java`. `eCommerce` añade que no hay dependencia Maven hacia `Core`.
- `Excel.readGeneralUserSheet` ignora su parámetro y lee siempre la hoja `Email`; las credenciales están en la hoja `General User`, que el código nunca lee. Resultado esperado: usuario y contraseña vacíos.
- `tests_to_run.xls` (6 hojas) selecciona: server `Local machine`, entorno `UAT`, navegador `firefox` sin versión, clase `com.automation.eCommerce.TestCases` y método `TestButton`. Hoja `Email` vacía, por lo que no se envían resultados.
- El encabezado de la hoja `Browsers` dice "no poner X" mientras `Excel` selecciona las celdas vacías: convenciones opuestas entre hojas.
- Rutas absolutas: `UserConstants` escribe en `C:\QA_Automation\` y fija el nombre de proyecto `eCommerce`; `TestSuite` concatena `user.dir` con `\` (solo Windows).
- Ambos POM comparten las coordenadas `com.automation:myFirstProject:0.0.1-SNAPSHOT`.
- No existe repositorio Git ni `.gitignore`. Maven incluido 3.9.11; Java disponible 26.0.2.1 con POM en source/target 1.8.
- La clase `com.automation.eCommerce.Test` colisiona con `org.testng.annotations.Test` en el mismo paquete.

**Decisiones:**
- Ninguna en esta fase; no se corrigió nada.

**Pendiente:**
- Ninguna reparación autorizada todavía; fase 2 requiere aprobación.
- Verificar si debe corregirse el ámbito de TestNG o moverse el código a `src/test/java`.
- Definir el tratamiento del secreto almacenado en el libro.

**Verificación:**
- `..\maven\bin\mvn.cmd test` en ambos proyectos: SUCCESS por artefactos obsoletos, no por compilación válida.
- `javac` a directorio temporal con y sin TestNG: confirma los errores de ámbito y la dependencia ausente de `Core`.
- Lectura de `tests_to_run.xls` con Apache POI en modo solo lectura.

**Archivos relacionados:**
- Core/pom.xml
- eCommerce/pom.xml
- Core/src/main/java/com/automation/core/Excel.java
- Core/src/main/java/com/automation/core/TestSuite.java
- Core/src/main/java/com/automation/core/UserConstants.java
- Core/src/main/java/com/automation/core/TestEnvironment.java
- eCommerce/src/main/java/com/automation/eCommerce/TestCases.java
- eCommerce/tests_to_run.xls

### 2026-10-02 — Fase 2 IntelliJ y OpenCode

**Estado:** Completado

**Resumen:**
El workspace se convirtió en un reactor Maven importable desde IntelliJ, se reparó la compilación limpia de los módulos existentes y se creó un módulo independiente `OrangeHRM`. Se incorporó configuración inicial de OpenCode sin crear todavía una skill.

**Cambios confirmados:**
- Se creó un `pom.xml` raíz con los módulos `Core`, `eCommerce` y `OrangeHRM`.
- `Core` usa el artifactId `automation-core` y TestNG quedó disponible para el código ubicado en `src/main/java`.
- `eCommerce` usa el artifactId `ecommerce-automation` y declara una dependencia Maven explícita hacia `Core`.
- `eCommerce` se conserva como ejemplo original de DemoQA; los casos nuevos deben implementarse en `OrangeHRM`.
- Se creó `OrangeHRM/pom.xml`, su README y `OrangeHrmUrls.java` con la URL de la demo pública.
- Se añadió `opencode.json` y los comandos `/build`, `/smoke`, `/regression` y `/review` en `.opencode/commands/`.
- `AGENTS.md` contiene ahora las reglas para IntelliJ, OpenCode, trazabilidad, Page Objects, datos únicos y manejo de secretos.
- No se creó una skill. La decisión es crear `orangehrm-automation` después de implementar y verificar un flujo repetible.

**Verificación:**
- `maven\bin\mvn.cmd clean test` desde la raíz: BUILD SUCCESS.
- Reactor verificado: `Core`, `eCommerce`, `OrangeHRM` y `qa-automation-parent` en SUCCESS.
- Los smoke tests JUnit existentes ejecutaron 2 pruebas en total, sin fallos.
- `OrangeHRM` compila pero todavía no contiene pruebas TestNG de navegador.

**Restricciones y deuda pendiente:**
- El código continúa configurado con `source` y `target` Java 8; el JDK actual muestra advertencias de obsolescencia.
- `Excel.readGeneralUserSheet` continúa leyendo `Email` en lugar de la hoja solicitada.
- Las rutas de resultados continúan fijadas en `C:\QA_Automation\`.
- Aún faltan Page Objects y la automatización de `TC-AUTH-001`.
- No existe repositorio Git ni `.gitignore` en la raíz inspeccionada.

**Siguiente paso acordado:**
- Crear los Page Objects de Login y Dashboard y automatizar el inicio de sesión válido antes de diseñar la skill compartida.

### 2026-10-02 — Fase 3 Smoke de autenticación

**Estado:** Completado

**Resumen:**
Se implementó y ejecutó satisfactoriamente `TC-AUTH-001` contra la demo pública de OrangeHRM mediante Selenium, TestNG y Chrome headless.

**Implementación:**
- `LoginPage` encapsula usuario, contraseña, botón Login y esperas explícitas.
- `DashboardPage` valida la URL autenticada y el encabezado Dashboard.
- `LoginSmokeTest` contiene el caso `TestAuth001ValidLogin`, grupo `smoke` y descripción trazable `TC-AUTH-001`.
- Las credenciales se obtienen de `ORANGEHRM_USERNAME` y `ORANGEHRM_PASSWORD`, o de propiedades Maven equivalentes; no están almacenadas en el código.
- El navegador funciona headless por defecto y puede mostrarse con `-Dorangehrm.headless=false`.

**Problemas encontrados y corregidos:**
- Al filtrar TestNG por grupo, `@BeforeMethod` no inicializaba el driver; se corrigió con `alwaysRun = true`.
- Un ChromeDriver 152 presente en `PATH` era incompatible con Chrome 154; el test usa WebDriverManager para resolver el driver compatible.

**Verificación:**
- Compilación del módulo y sus dependencias: BUILD SUCCESS.
- Smoke real: 1 test ejecutado, 1 PASS, 0 FAIL, 0 SKIPPED.
- Selenium confirmó el Dashboard después del inicio de sesión.

**Observaciones:**
- Selenium muestra una advertencia CDP para Chrome 154, pero el flujo WebDriver utilizado no depende de CDP y la prueba terminó correctamente.
- El smoke todavía es autónomo dentro del módulo `OrangeHRM`; la integración con `tests_to_run.xls` se realizará después de estabilizar la configuración de datos.

**Siguiente paso:**
- Incorporar captura de evidencia en fallo y automatizar los casos negativos de Login antes del flujo PIM.

### 2026-10-02 — Fase 4 Regresión de Login

**Estado:** Completado

**Resumen:**
Se automatizaron los cinco escenarios negativos restantes de Login y se añadió captura automática de pantalla para cualquier prueba fallida.

**Implementación:**
- `BaseOrangeHrmTest` centraliza Chrome, configuración, credenciales, cierre de sesión técnica y evidencia de fallos.
- Las capturas fallidas se guardan en `OrangeHRM/target/evidence/` con método y fecha en el nombre.
- `LoginPage` permite enviar valores vacíos, esperar `Invalid credentials` y contar mensajes `Required`.
- `LoginNegativeTest` cubre `TC-AUTH-002` a `TC-AUTH-006`.
- `LoginSmokeTest` continúa cubriendo `TC-AUTH-001` y pertenece a los grupos `smoke` y `login`.

**Verificación real:**
- Comando: `maven\bin\mvn.cmd -pl OrangeHRM -am -Dgroups=login test`.
- Resultado TestNG: 6 ejecutados, 6 PASS, 0 FAIL, 0 SKIPPED.
- Cada caso abrió y cerró una sesión de Chrome independiente.

**Observaciones:**
- Las advertencias CDP de Chrome 154 permanecen, pero no afectaron ninguna validación WebDriver.
- `TC-AUTH-007` Logout aún no está automatizado.

**Siguiente paso:**
- Automatizar Logout y luego iniciar Page Objects y casos del módulo PIM.

### 2026-10-02 — Fase 5 Logout

**Estado:** Completado

**Resumen:**
Se automatizó `TC-AUTH-007` y quedó cerrada la cobertura funcional automatizada de los siete casos de autenticación.

**Implementación:**
- `DashboardPage.logout()` abre el menú de usuario, selecciona Logout y devuelve un `LoginPage` cargado.
- `LoginPage.isDisplayed()` valida el encabezado Login y la URL `/auth/login`.
- `LogoutTest.TestAuth007Logout` realiza Login válido, Logout y confirma el retorno a Login.

**Verificación real:**
- `TC-AUTH-007`: 1 ejecutado, 1 PASS, 0 FAIL, 0 SKIPPED.
- Comando aislado: `maven\bin\mvn.cmd -pl OrangeHRM -am -Dtest=LogoutTest -DfailIfNoTests=false test`.
- BUILD SUCCESS para `Core` y `OrangeHRM`.

**Nota de ejecución:**
- La ejecución conjunta de siete sesiones puede exceder la ventana corta del ejecutor; `TC-AUTH-001` a `TC-AUTH-006` ya tenían una ejecución conjunta 6/6 PASS y Logout se verificó aisladamente.

**Siguiente paso:**
- Crear Page Objects de PIM y automatizar primero el alta válida `TC-PIM-001` con datos únicos.

### 2026-10-02 — Fase 6 Alta válida PIM

**Estado:** Completado

**Resumen:**
Se implementó y aprobó `TC-PIM-001`: Login, navegación a PIM, alta con datos únicos y comprobación de persistencia en Personal Details.

**Implementación:**
- `DashboardPage.openPim()` inicia la navegación al módulo.
- `PimEmployeeListPage` valida Employee List y abre Add Employee.
- `AddEmployeePage` completa nombre, segundo nombre, apellido e Employee ID y guarda.
- `PersonalDetailsPage` espera la carga de la ficha y compara los cuatro valores persistidos.
- `PimCreateEmployeeTest` genera sufijos únicos a partir del tiempo para reducir colisiones en la demo compartida.

**Problemas encontrados y corregidos:**
- `WebElement.clear()` no reemplazaba el Employee ID reactivo: el valor se anexaba al ID generado y superaba 10 caracteres. Se corrigió con selección completa y Backspace.
- Personal Details mostraba el encabezado antes de terminar de cargar los campos. Se corrigió esperando hasta que los cuatro valores coincidan.
- La captura automática produjo evidencia útil para ambos fallos de automatización.

**Verificación real:**
- `TC-PIM-001`: 1 ejecutado, 1 PASS, 0 FAIL, 0 SKIPPED.
- BUILD SUCCESS para `Core` y `OrangeHRM`.
- La ficha final conservó nombre, segundo nombre, apellido e Employee ID.

**Siguiente paso:**
- Automatizar `TC-PIM-002` y `TC-PIM-003`, validaciones de nombre y apellido obligatorios.

### 2026-10-02 — Fase 7 Campos obligatorios PIM

**Estado:** Completado

**Resumen:**
Se automatizaron y aprobaron `TC-PIM-002` y `TC-PIM-003`, comprobando por separado que First Name y Last Name sean obligatorios en Add Employee.

**Implementación:**
- `AddEmployeePage.submitForValidation()` permite enviar combinaciones incompletas sin asumir una navegación exitosa.
- `AddEmployeePage.isFirstNameRequired()` y `isLastNameRequired()` verifican el mensaje `Required` asociado al campo correcto y confirman que el formulario permanece en `/pim/addEmployee`.
- `PimRequiredFieldsTest` genera Employee ID únicos y contiene los dos casos trazables.
- El grupo TestNG `pim-validation` permite ejecutar únicamente estas validaciones.

**Verificación real:**
- Comando: `maven\bin\mvn.cmd -pl OrangeHRM -am -Dgroups=pim-validation test`.
- Resultado TestNG: 2 ejecutados, 2 PASS, 0 FAIL, 0 SKIPPED.
- BUILD SUCCESS para `Core` y `OrangeHRM`.

**Observaciones:**
- La advertencia de CDP para Chrome 154 no afectó los flujos WebDriver.
- Los resultados de ejecución se consolidarán en los entregables DOCX/XLSX al finalizar la ronda de automatización, evitando actualizaciones repetitivas.

**Siguiente paso:**
- Automatizar `TC-PIM-004` con datos parametrizados y continuar la cobertura PIM definida en la suite preliminar.

### 2026-10-02 — Fase 8 Alta PIM Data Driven

**Estado:** Completado

**Resumen:**
Se automatizó y aprobó `TC-PIM-004` mediante un `DataProvider` de TestNG con los tres datasets válidos definidos en la suite preliminar.

**Implementación:**
- `PimDataDrivenEmployeeTest` ejecuta `EMP-D01`, `EMP-D04` y `EMP-D05` como invocaciones independientes.
- Cada dataset conserva sus variantes de nombre y segundo nombre y recibe un sufijo e Employee ID únicos para evitar colisiones en la demo compartida.
- Cada ejecución valida en Personal Details que First Name, Middle Name, Last Name y Employee ID hayan persistido.
- El grupo `pim-data-driven` permite ejecutar únicamente `TC-PIM-004`.

**Verificación real:**
- Comando: `maven\bin\mvn.cmd -pl OrangeHRM -am -Dgroups=pim-data-driven test`.
- Resultado TestNG: 3 ejecutados, 3 PASS, 0 FAIL, 0 SKIPPED.
- BUILD SUCCESS para `Core` y `OrangeHRM`.

**Observaciones:**
- La advertencia de CDP para Chrome 154 continuó sin afectar los flujos WebDriver.
- Los resultados se incorporarán al DOCX y XLSX en la consolidación final.

**Siguiente paso:**
- Automatizar `TC-PIM-005`, validando la búsqueda de un empleado creado con datos derivados de la propia ejecución.

### 2026-10-02 — Fase 9 Búsqueda de empleado PIM

**Estado:** Completado

**Resumen:**
Se automatizó y aprobó `TC-PIM-005`: crear un empleado, confirmar su persistencia y localizarlo desde Employee List usando el Employee ID generado en la misma ejecución.

**Ajuste del criterio:**
- La suite preliminar describía búsqueda por nombre.
- La exploración de la versión actual mostró que el filtro de nombre usa autocompletado y no ofrece una coincidencia estable para automatización.
- Se adoptó Employee ID como clave de búsqueda inequívoca, conservando la intención de `RF-PIM-04`; la fila se valida además contra nombre y apellido.

**Implementación:**
- `PersonalDetailsPage.openEmployeeList()` regresa a Employee List después del alta.
- `PimEmployeeListPage.searchByEmployeeId()` completa el filtro, ejecuta Search y espera el resultado observable.
- `PimEmployeeListPage.hasEmployeeResult()` comprueba Employee ID, First Name y Last Name en la misma fila.
- `PimSearchEmployeeTest` conserva alta y búsqueda en una sola sesión para no depender de datos ajenos.
- El grupo `pim-search` ejecuta únicamente este flujo.

**Verificación real:**
- Comando: `maven\bin\mvn.cmd -pl OrangeHRM -am -Dgroups=pim-search test`.
- Resultado TestNG: 1 ejecutado, 1 PASS, 0 FAIL, 0 SKIPPED.
- BUILD SUCCESS para `Core` y `OrangeHRM`.

**Siguiente paso:**
- Automatizar `TC-PIM-006`, búsqueda de un empleado inexistente y validación del estado sin resultados.

### 2026-10-02 — Fase 10 Búsqueda PIM sin resultados

**Estado:** Completado

**Resumen:**
Se automatizó y aprobó `TC-PIM-006`, verificando el comportamiento de Employee List ante un Employee ID inexistente.

**Implementación:**
- `PimSearchNoResultsTest` genera un identificador alfanumérico único que no requiere crear datos de prueba.
- `PimEmployeeListPage.searchByEmployeeIdExpectingNoResults()` ejecuta el filtro y espera el estado vacío observable.
- `hasNoRecordsFound()` exige simultáneamente el mensaje `No Records Found` y ausencia de filas.
- El grupo `pim-search-negative` ejecuta únicamente este caso.

**Verificación real:**
- Comando: `maven\bin\mvn.cmd -pl OrangeHRM -am -Dgroups=pim-search-negative test`.
- Resultado TestNG: 1 ejecutado, 1 PASS, 0 FAIL, 0 SKIPPED.
- BUILD SUCCESS para `Core` y `OrangeHRM`.

**Observaciones:**
- Se conserva un dato alfanumérico sin guiones porque la exploración mostró que valores con guiones pueden producir `Invalid Parameter` en vez de una búsqueda válida sin coincidencias.
- La advertencia de CDP para Chrome 154 no afectó el flujo.

**Siguiente paso:**
- Automatizar `TC-PIM-007`, cancelación de Add Employee, comprobando la navegación y que el ID controlado no haya sido creado.

### 2026-10-02 — Fase 11 Cancelación de alta PIM

**Estado:** Completado

**Resumen:**
Se automatizó y aprobó `TC-PIM-007`, confirmando que Cancel abandona Add Employee y no crea el registro parcialmente completado.

**Implementación:**
- `AddEmployeePage.fillPartialFormAndCancel()` completa First Name, Last Name y un Employee ID único antes de pulsar Cancel.
- El método espera el regreso observable a Employee List.
- `PimCancelEmployeeTest` busca inmediatamente el mismo ID y exige `No Records Found` sin filas.
- El grupo `pim-cancel` ejecuta únicamente este caso.

**Verificación real:**
- Comando: `maven\bin\mvn.cmd -pl OrangeHRM -am -Dgroups=pim-cancel test`.
- Resultado TestNG: 1 ejecutado, 1 PASS, 0 FAIL, 0 SKIPPED.
- BUILD SUCCESS para `Core` y `OrangeHRM`.

**Cobertura acumulada:**
- `TC-PIM-001` a `TC-PIM-007` están implementados y cuentan con ejecuciones aisladas exitosas.
- `TC-PIM-004` representa tres invocaciones por sus datasets válidos.

**Siguiente paso:**
- Automatizar `TC-E2E-001` y después ejecutar la regresión conjunta de autenticación y PIM antes de consolidar DOCX/XLSX.

### 2026-10-02 — Fase 12 Flujo E2E completo

**Estado:** Completado

**Resumen:**
Se automatizó y aprobó `TC-E2E-001` en una sola sesión: Login, alta, búsqueda, reapertura y verificación de ficha, y Logout.

**Implementación:**
- `EmployeeLifecycleE2ETest` genera datos únicos y encadena Page Objects ya verificados.
- Después del alta valida los cuatro datos persistidos.
- Busca por Employee ID y valida la fila por ID, nombre y apellido.
- `PimEmployeeListPage.openEmployeeDetails()` abre la ficha mediante la acción Edit de la fila coincidente.
- La ficha reabierta vuelve a validar First Name, Middle Name, Last Name y Employee ID.
- `PersonalDetailsPage.logout()` finaliza la sesión y confirma el retorno al formulario Login.
- El grupo `e2e` ejecuta únicamente el flujo completo.

**Verificación real:**
- Comando: `maven\bin\mvn.cmd -pl OrangeHRM -am -Dgroups=e2e test`.
- Resultado TestNG: 1 ejecutado, 1 PASS, 0 FAIL, 0 SKIPPED.
- BUILD SUCCESS para `Core` y `OrangeHRM`.

**Siguiente paso:**
- Ejecutar la regresión conjunta del módulo OrangeHRM y, si queda estable, consolidar los resultados finales en DOCX/XLSX.

### 2026-10-02 — Fase 13 Regresión completa OrangeHRM

**Estado:** Completado

**Resumen:**
Se ejecutó conjuntamente toda la automatización de autenticación, PIM y E2E después de completar los 15 casos trazables.

**Verificación real:**
- Comando: `maven\bin\mvn.cmd -pl OrangeHRM -am test`.
- OrangeHRM/TestNG: 17 ejecuciones, 17 PASS, 0 FAIL, 0 ERRORS, 0 SKIPPED.
- Las 17 ejecuciones representan 15 casos trazables; `TC-PIM-004` aporta tres invocaciones Data Driven.
- El smoke técnico de `Core` también pasó: 1 ejecutado, 1 PASS.
- BUILD SUCCESS para el reactor `Core` + `OrangeHRM`.
- Duración OrangeHRM: 3 min 30 s; duración total: 3 min 32 s.

**Observaciones:**
- No se generaron evidencias de fallo durante la regresión.
- Las advertencias de CDP para Chrome 154 no afectaron las validaciones WebDriver.

**Siguiente paso:**
- Consolidar los resultados verificados, criterios ajustados y métricas finales en el DOCX y XLSX, y volver a validar visualmente ambos entregables.

### 2026-10-02 — Fase 14 Integración segura con JIRA

**Estado:** Implementación y dry-run completados; publicación pendiente de configuración y autorización.

**Resumen:**
Se añadió a `Core` una integración reutilizable con JIRA Cloud REST API v3 y se prepararon tres incidencias académicas claramente identificadas como simuladas.

**Implementación:**
- `JiraConfig` carga modo y credenciales desde propiedades o variables de entorno y bloquea publicación incompleta.
- `JiraClient` construye el payload ADF y crea incidencias mediante `POST /rest/api/3/issue` solo en modo `publish`.
- `JiraBugReporter` genera JSON locales en modo `dry-run`, que es el comportamiento predeterminado.
- `SimulatedJiraBugIT` prepara `BUG-SIM-001` a `BUG-SIM-003`, todos con `[SIMULADO]`, etiquetas de entrenamiento y advertencia de que no fueron observados.
- Se añadió Jackson Databind a `Core` para serialización JSON.

**Verificación real:**
- Comando dry-run: `maven\bin\mvn.cmd -pl OrangeHRM -am "-Dtest=SimulatedJiraBugIT" "-Djira.mode=dry-run" "-DfailIfNoTests=false" test`.
- Resultado: 1 ejecutado, 1 PASS, 0 FAIL, 0 SKIPPED; BUILD SUCCESS.
- Se generaron tres borradores bajo `OrangeHRM/target/jira-dry-run/`.
- Ninguna incidencia fue publicada externamente.

**Siguiente paso:**
- Revisar los tres JSON y, cuando existan URL, correo, API token y clave de proyecto JIRA, solicitar autorización inmediata antes de publicar las incidencias simuladas.

### 2026-10-02 — Fase 15 Publicación simulada en JIRA y consolidación final

**Estado:** Completado

**Resumen:**
Se configuró Jira Cloud, se habilitó el tipo de trabajo Bug para el proyecto `KAN` y se publicaron los tres escenarios académicos simulados. Después se consolidaron el informe final y el libro de gestión de pruebas.

**Publicación confirmada:**
- `KAN-4`: `[SIMULADO] BUG-SIM-001 - Login permite acceso con contraseña incorrecta`.
- `KAN-5`: `[SIMULADO] BUG-SIM-002 - Add Employee informa éxito pero no persiste el registro`.
- `KAN-6`: `[SIMULADO] BUG-SIM-003 - Logout no invalida la sesión autenticada`.
- Los tres tickets conservan las etiquetas `simulado`, `qa-training` y `orangehrm`.
- No corresponden a defectos observados; la regresión real terminó con todos los casos PASS.

**Entregables finales:**
- DOCX final con alcance, 15 casos, resultados manuales y automatizados, herramientas, Jira, evidencia, métricas, recomendaciones y conclusiones.
- XLSX final con requisitos, casos, datos, trazabilidad, ejecución automatizada, bugs simulados y métricas.

**Precaución:**
- No volver a ejecutar `jira.mode=publish` sin intención explícita, porque cada ejecución crea tres tickets adicionales.
- Mantener `JIRA_API_TOKEN` únicamente en variables de entorno locales.
