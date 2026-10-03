# Integración segura con Jira

La implementación reutilizable está en `Core/src/main/java/com/automation/core/jira/` y utiliza Jira Cloud REST API v3.

## Configuración

La integración recibe valores mediante variables de entorno o propiedades locales:

- `JIRA_BASE_URL`
- `JIRA_EMAIL`
- `JIRA_API_TOKEN`
- `JIRA_PROJECT_KEY`

No leas ni muestres el valor del token. No lo guardes en el repositorio, POM, documentación, Excel, capturas ni reportes.

## Dry-run

El modo predeterminado genera JSON bajo `OrangeHRM/target/jira-dry-run/` y no crea tickets.

```powershell
.\maven\bin\mvn.cmd -pl OrangeHRM -am "-Dtest=SimulatedJiraBugIT" "-Djira.mode=dry-run" "-DfailIfNoTests=false" test
```

Revisa los borradores antes de cualquier publicación.

## Publicación

`jira.mode=publish` produce un cambio externo. Antes de ejecutarlo:

1. Confirma que el usuario pidió publicar esos tickets concretos.
2. Verifica URL, proyecto y disponibilidad del tipo Bug sin exponer secretos.
3. Revisa el resumen, la descripción, las etiquetas y la condición simulada o real.
4. Publica una sola vez.
5. Si el resultado es incierto, consulta Jira antes de reintentar.

No conectes la publicación a listeners generales de TestNG.

## Escenarios académicos existentes

- `KAN-4`: Login con contraseña incorrecta.
- `KAN-5`: alta informada como exitosa pero no persistida.
- `KAN-6`: Logout sin invalidación de sesión.

Estos registros son simulados. Conserva `[SIMULADO]`, la etiqueta `simulado` y la advertencia de que el comportamiento no fue observado. No los uses como evidencia de defectos reales.

Para un bug real, exige reproducción y evidencia antes de proponer su publicación.
