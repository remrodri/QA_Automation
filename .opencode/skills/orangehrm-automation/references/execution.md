# Ejecución y diagnóstico

Ejecuta desde la raíz del repositorio. En PowerShell, conserva entre comillas los argumentos `-D` cuando sea necesario para evitar que se interpreten incorrectamente.

## Comandos

Regresión completa:

```powershell
.\maven\bin\mvn.cmd -pl OrangeHRM -am test
```

Smoke:

```powershell
.\maven\bin\mvn.cmd -pl OrangeHRM -am "-Dgroups=smoke" test
```

Smoke visible:

```powershell
.\maven\bin\mvn.cmd -pl OrangeHRM -am "-Dgroups=smoke" "-Dorangehrm.headless=false" test
```

Login:

```powershell
.\maven\bin\mvn.cmd -pl OrangeHRM -am "-Dgroups=login" test
```

E2E:

```powershell
.\maven\bin\mvn.cmd -pl OrangeHRM -am "-Dgroups=e2e" test
```

Test aislado:

```powershell
.\maven\bin\mvn.cmd -pl OrangeHRM -am "-Dtest=NombreDeClase" "-DfailIfNoTests=false" test
```

## Evidencias

- Reportes Surefire: `OrangeHRM/target/surefire-reports/`.
- Consolidado: `OrangeHRM/target/surefire-reports/TEST-TestSuite.xml`.
- Capturas ante fallos: `OrangeHRM/target/evidence/`.

## Diagnóstico

Cuando una prueba falle:

1. Conserva el primer error real y la evidencia.
2. Identifica si el navegador abrió y si el hook de preparación se ejecutó.
3. Revisa selectores, esperas, datos y estado de la demo.
4. Reproduce el caso de forma aislada.
5. Clasifica el resultado como producto, automatización o ambiente.
6. Corrige solo la causa demostrada y vuelve a ejecutar la prueba específica.
7. Ejecuta regresión si el cambio afecta componentes compartidos.

No uses clases antiguas de `target/` como prueba de que el código fuente compila. No borres evidencia antes de revisarla.

## Referencia de resultado estable

La regresión final verificada produjo 17 ejecuciones, 17 PASS, 0 FAIL y 0 SKIPPED. Este dato es histórico, no una razón para asumir que una ejecución futura también pasará.

