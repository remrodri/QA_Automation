---
name: orangehrm-automation
description: Desarrollar, revisar, ejecutar y mantener la automatización Java Selenium TestNG del módulo OrangeHRM de este repositorio. Usar para casos TC-AUTH, TC-PIM y TC-E2E, Page Objects, datos únicos, regresión, evidencias o integración segura con Jira. No usar para modificar el ejemplo eCommerce ni para publicar bugs sin autorización explícita.
compatibility: opencode
metadata:
  audience: qa-automation
  project: orangehrm
---

# Automatización OrangeHRM

Trabaja exclusivamente sobre el módulo `OrangeHRM` y los componentes reutilizables necesarios de `Core`. Conserva `eCommerce` como material original de referencia.

Lee primero `AGENTS.md`. Después carga solo la referencia necesaria:

- Para crear o modificar código, lee [references/architecture.md](references/architecture.md).
- Para identificar cobertura e IDs, lee [references/test-catalog.md](references/test-catalog.md).
- Para ejecutar o diagnosticar pruebas, lee [references/execution.md](references/execution.md).
- Para preparar o publicar incidencias, lee [references/jira-integration.md](references/jira-integration.md).

## Reglas esenciales

- Mantén la trazabilidad incluyendo el ID `TC-*` en la descripción TestNG.
- Usa Page Objects para selectores, esperas y acciones de interfaz.
- Evita `Thread.sleep`; espera condiciones observables.
- Genera nombres y Employee ID únicos en los casos que crean datos.
- No debilites aserciones ni cambies el criterio del caso solo para obtener PASS.
- Distingue fallo del producto, fallo de automatización y bloqueo del ambiente.
- Obtén credenciales y tokens mediante variables de entorno o propiedades locales no versionadas.
- No escribas secretos en código, configuración, reportes, Excel, documentación o salida de consola.
- Mantén Jira en `dry-run` por defecto. Solicita autorización inmediatamente antes de usar `jira.mode=publish`.
- Una publicación Jira crea registros externos y puede duplicar tickets; detente después de un intento confirmado o incierto y verifica antes de repetir.

## Resultado esperado

Entrega cambios pequeños y trazables. Ejecuta la validación más específica que cubra el cambio y amplía a regresión cuando el riesgo lo justifique. Informa el comando utilizado, casos ejecutados, PASS, FAIL, SKIPPED, evidencia generada y cualquier limitación del ambiente.

