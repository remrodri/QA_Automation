# Catálogo trazable de pruebas

La suite contiene 15 casos y produce 17 invocaciones. `TC-PIM-004` aporta tres invocaciones mediante DataProvider.

## Autenticación

| ID | Escenario |
|---|---|
| TC-AUTH-001 | Login con credenciales válidas |
| TC-AUTH-002 | Login con usuario inválido |
| TC-AUTH-003 | Login con contraseña inválida |
| TC-AUTH-004 | Login con usuario vacío |
| TC-AUTH-005 | Login con contraseña vacía |
| TC-AUTH-006 | Login con ambos campos vacíos |
| TC-AUTH-007 | Logout y regreso a Login |

## PIM

| ID | Escenario |
|---|---|
| TC-PIM-001 | Alta válida y persistencia en Personal Details |
| TC-PIM-002 | First Name obligatorio |
| TC-PIM-003 | Last Name obligatorio |
| TC-PIM-004 | Alta con tres datasets válidos |
| TC-PIM-005 | Crear y buscar por Employee ID |
| TC-PIM-006 | Buscar ID inexistente y obtener No Records Found |
| TC-PIM-007 | Cancelar alta y confirmar que el ID no fue creado |

## E2E

| ID | Escenario |
|---|---|
| TC-E2E-001 | Login, alta, búsqueda, reapertura, validación y Logout |

## Criterios que no deben degradarse

- La búsqueda estable de `TC-PIM-005` usa Employee ID y valida ID, nombre y apellido en la misma fila.
- `TC-PIM-006` exige simultáneamente `No Records Found` y ausencia de filas.
- `TC-PIM-007` busca el mismo ID después de Cancel para confirmar que no persistió.
- El E2E reabre la ficha y vuelve a comprobar First Name, Middle Name, Last Name y Employee ID.
- Un mensaje de éxito no sustituye la comprobación de persistencia.

