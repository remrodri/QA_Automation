package com.automation.orangehrm.jira;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.automation.core.jira.JiraBugReporter;
import com.automation.core.jira.JiraIssue;
import com.automation.core.jira.JiraSubmissionResult;

public class SimulatedJiraBugIT {
    @Test(description = "Genera o publica tres tickets JIRA simulados para la práctica académica")
    public void generateSimulatedJiraBugs() throws IOException {
        List<JiraIssue> issues = Arrays.asList(
                simulated(
                        "BUG-SIM-001", "Login permite acceso con contraseña incorrecta",
                        "TC-AUTH-003", "El sistema debe rechazar la contraseña inválida.",
                        "Hipótesis: el usuario accede al Dashboard con una contraseña incorrecta."),
                simulated(
                        "BUG-SIM-002", "Add Employee informa éxito pero no persiste el registro",
                        "TC-PIM-001", "El empleado guardado debe quedar disponible en Personal Details y Employee List.",
                        "Hipótesis: aparece Successfully Saved, pero el empleado no puede consultarse posteriormente."),
                simulated(
                        "BUG-SIM-003", "Logout no invalida la sesión autenticada",
                        "TC-AUTH-007", "Después de Logout, una URL autenticada no debe ser accesible sin iniciar sesión.",
                        "Hipótesis: al volver a una URL anterior se recupera contenido autenticado sin nuevas credenciales."));

        JiraBugReporter reporter = JiraBugReporter.configured();
        for (JiraIssue issue : issues) {
            JiraSubmissionResult result = reporter.submit(issue);
            if (result.isPublished()) {
                Assert.assertFalse(result.getIssueKey().isEmpty(), "JIRA no devolvió una clave de incidencia.");
                System.out.println("Ticket simulado publicado: " + result.getIssueKey());
            } else {
                Assert.assertTrue(result.getDraftPath().toFile().isFile(), "No se generó el borrador JIRA.");
                System.out.println("Borrador JIRA: " + result.getDraftPath());
            }
        }
    }

    private JiraIssue simulated(String id, String title, String caseId,
            String expected, String hypotheticalResult) {
        String description =
                "ESCENARIO SIMULADO PARA PRÁCTICA ACADÉMICA. NO FUE OBSERVADO EN LA EJECUCIÓN REAL.\n\n"
                + "Identificador: " + id + "\n"
                + "Caso relacionado: " + caseId + "\n"
                + "Severidad propuesta: Crítica\n"
                + "Prioridad propuesta: Alta\n\n"
                + "Resultado esperado: " + expected + "\n"
                + "Resultado hipotético: " + hypotheticalResult + "\n\n"
                + "Estado inicial: Simulado / pendiente de triage.\n"
                + "Evidencia: No aplica; ejemplo de gestión de defectos.";
        return new JiraIssue("[SIMULADO] " + id + " - " + title, description,
                "simulado", "qa-training", "orangehrm");
    }
}
