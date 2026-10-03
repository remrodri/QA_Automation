# OrangeHRM Automation

Módulo destinado a la automatización de los casos manuales validados sobre la demo pública de OrangeHRM.

## Estado final

La suite contiene 15 casos trazables y produce 17 invocaciones por los tres datasets de `TC-PIM-004`. La regresión conjunta verificada terminó con 17 PASS, 0 FAIL y 0 SKIPPED. Los reportes se guardan en `target/surefire-reports/`.

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

## Regresión completa

Desde la raíz del repositorio:

```powershell
.\maven\bin\mvn.cmd -pl OrangeHRM -am test
```

## Jira

La integración utiliza `JIRA_BASE_URL`, `JIRA_EMAIL`, `JIRA_API_TOKEN` y `JIRA_PROJECT_KEY`. El modo predeterminado es `dry-run`; la publicación requiere `"-Djira.mode=publish"` explícito. Los tickets `KAN-4`, `KAN-5` y `KAN-6` son ejemplos académicos marcados como `[SIMULADO]` y no representan defectos observados.

No guardar credenciales ni tokens en código, POM, XML, Excel o reportes. No repetir una publicación sin revisar Jira, porque generaría tickets duplicados.
