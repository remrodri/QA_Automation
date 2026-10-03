# OrangeHRM Automation

Módulo destinado a la automatización de los casos manuales validados sobre la demo pública de OrangeHRM.

## Estado final

- 15 casos trazables implementados para autenticación, PIM y flujo E2E.
- 17 invocaciones TestNG en la regresión conjunta: 17 PASS, 0 FAIL y 0 SKIPPED.
- Page Object Model y DataProvider aplicados.
- Reportes disponibles en `OrangeHRM/target/surefire-reports/` y capturas de fallo en `OrangeHRM/target/evidence/`.
- Integración con Jira Cloud validada; `KAN-4`, `KAN-5` y `KAN-6` son tickets académicos simulados, no defectos observados.

Ejecutar la regresión completa desde la raíz:

```powershell
.\maven\bin\mvn.cmd -pl OrangeHRM -am test
```

## Primera fase

- Conservar `eCommerce` como ejemplo original.
- Reutilizar `Core` para Selenium, TestNG, Excel, evidencias y ciclo de vida.
- Implementar Page Objects para Login, Dashboard, Add Employee y Employee List.
- Automatizar primero el flujo smoke de autenticación.
- Incorporar después el flujo E2E de alta, búsqueda y cierre de sesión.

No se deben incluir contraseñas reales ni datos personales en el repositorio.

## Ejecutar el smoke test

Configurar `ORANGEHRM_USERNAME` y `ORANGEHRM_PASSWORD` en el entorno y ejecutar desde la raíz:

```powershell
maven\bin\mvn.cmd -pl OrangeHRM -am -Dgroups=smoke test
```

El navegador se ejecuta en modo headless por defecto. Para verlo durante la ejecución, añadir `-Dorangehrm.headless=false`.

Para ejecutar los siete casos de autenticación:

```powershell
maven\bin\mvn.cmd -pl OrangeHRM -am -Dgroups=login test
```

Si una prueba falla, se guarda automáticamente una captura PNG en `OrangeHRM/target/evidence/`.

Para ejecutar el alta válida de PIM:

```powershell
maven\bin\mvn.cmd -pl OrangeHRM -am -Dgroups=pim test
```

El caso genera un nombre y un Employee ID únicos para reducir colisiones en la demo compartida.

Para ejecutar únicamente las validaciones obligatorias de PIM:

```powershell
maven\bin\mvn.cmd -pl OrangeHRM -am -Dgroups=pim-validation test
```

Para ejecutar el alta parametrizada con los datasets válidos de PIM:

```powershell
maven\bin\mvn.cmd -pl OrangeHRM -am -Dgroups=pim-data-driven test
```

Para crear un empleado y comprobar su búsqueda por Employee ID:

```powershell
maven\bin\mvn.cmd -pl OrangeHRM -am -Dgroups=pim-search test
```

Para comprobar la búsqueda negativa de un Employee ID inexistente:

```powershell
maven\bin\mvn.cmd -pl OrangeHRM -am -Dgroups=pim-search-negative test
```

Para comprobar que Cancel abandona el alta sin crear el empleado:

```powershell
maven\bin\mvn.cmd -pl OrangeHRM -am -Dgroups=pim-cancel test
```

Para ejecutar el flujo completo Login, alta, búsqueda, verificación y Logout:

```powershell
maven\bin\mvn.cmd -pl OrangeHRM -am -Dgroups=e2e test
```

## Integración con JIRA

La integración genera borradores por defecto y nunca publica incidencias accidentalmente. Los tres tickets de práctica se identifican como `[SIMULADO]` y no representan fallos observados en OrangeHRM.

Generar y revisar los JSON sin conectarse a JIRA:

```powershell
maven\bin\mvn.cmd -pl OrangeHRM -am "-Dtest=SimulatedJiraBugIT" "-Djira.mode=dry-run" "-DfailIfNoTests=false" test
```

Los borradores quedan en `OrangeHRM/target/jira-dry-run/`.

Para publicar después de revisar los borradores, definir `JIRA_BASE_URL`, `JIRA_EMAIL`, `JIRA_API_TOKEN` y `JIRA_PROJECT_KEY`, y ejecutar explícitamente:

```powershell
maven\bin\mvn.cmd -pl OrangeHRM -am "-Dtest=SimulatedJiraBugIT" "-Djira.mode=publish" "-DfailIfNoTests=false" test
```

El API token no debe guardarse en el repositorio, POM, código, Excel ni reportes.

En PowerShell, los argumentos con nombres que contienen puntos deben escribirse entre comillas. No repetir el modo `publish` sin revisar Jira, porque cada ejecución crea tres tickets nuevos.
