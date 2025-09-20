const { Builder, By, until } = require('selenium-webdriver');
const chrome = require('selenium-webdriver/chrome');
const assert = require('chai').assert;

async function loginTest() {
  const options = new chrome.Options();
  // options.addArguments("--headless"); // Uncomment for headless mode

  const driver = await new Builder().forBrowser("chrome").setChromeOptions(options).build();

  try {
    await driver.get("http://localhost:YOUR_PORT/login"); // Change the URL as needed

    // Enter username and password
    await driver.wait(until.elementLocated(By.css('input[placeholder="Enter username"]')), 5000);
    await driver.findElement(By.css('input[placeholder="Enter username"]')).sendKeys("admin");
    await driver.findElement(By.css('input[placeholder="Enter password"]')).sendKeys("12345");
    await driver.findElement(By.xpath('//button[text()="Login"]')).click();

    // Wait for logout button to appear (login success)
    await driver.wait(until.elementLocated(By.css(".logout-button")), 10000);
    const logoutVisible = await driver.findElement(By.css(".logout-button")).isDisplayed();
    assert.isTrue(logoutVisible, "Logout button should be visible after login");

    console.log("Login test passed!");
  } finally {
    await driver.quit();
  }
}

loginTest();