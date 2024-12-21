package com.example.lo7nim3ak;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class KeycloakLoginTest {

    private static WebDriver driver;

    @BeforeAll
    public static void setup() {3
        // Définir le chemin vers ChromeDriver
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\PC\\Downloads\\chromedriver-win64\\chromedriver.exe");
        driver = new ChromeDriver();
    }

    @Test
    public void testKeycloakLogin() {
        // Accéder à l'application Angular
        driver.get("http://localhost:4200");

        // Attendre que le bouton "Se connecter / S'inscrire" soit visible et cliquable
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button.btn.btn-secondary")));
        loginButton.click();

        // Vérifier la redirection vers Keycloak
        String currentUrl = driver.getCurrentUrl();
        assertTrue(currentUrl.contains("http://localhost:9000/realms/Lo7niM3ak/protocol/openid-connect/auth"),
                "La redirection vers la page de connexion Keycloak a échoué");

        // Attendre que les champs username et password soient visibles et remplis
        WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")));
        usernameField.sendKeys("driver"); // Remplacez par un nom d'utilisateur valide

        WebElement passwordField = driver.findElement(By.id("password"));
        passwordField.sendKeys("123"); // Remplacez par un mot de passe valide

        // Cliquer sur le bouton "Login"
        WebElement loginSubmitButton = driver.findElement(By.id("kc-login"));
        loginSubmitButton.click();

        // Attendre que l'utilisateur soit redirigé vers la page d'accueil de l'application
        // Vérifier si l'URL contient le fragment attendu après la redirection
        WebDriverWait waitForRedirection = new WebDriverWait(driver, Duration.ofSeconds(10));
        waitForRedirection.until(ExpectedConditions.urlContains("localhost:4200/#iss=http://localhost:9000/realms/Lo7niM3ak"));

        // Vérifier si l'URL contient le fragment attendu
        String redirectedUrl = driver.getCurrentUrl();
        assertTrue(redirectedUrl.contains("localhost:4200/#iss=http://localhost:9000/realms/Lo7niM3ak"),
                "L'utilisateur n'a pas été redirigé vers la page d'accueil après la connexion");
    }

    @AfterAll
    public static void tearDown() {
        // Fermer le navigateur après le test
        driver.quit();
    }
}
