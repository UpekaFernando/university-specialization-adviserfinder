# WebDriver Setup Guide for JavaScript/Node.js Applications

## 📋 Step 1: Install Dependencies

For your JavaScript application, install these packages:

```bash
npm install --save-dev selenium-webdriver
npm install --save-dev chromedriver
npm install --save-dev geckodriver
npm install --save-dev @wdio/selenium-standalone-service
npm install --save-dev mocha chai
```

## 🌐 Step 2: Multiple Browser Support

### Package.json dependencies:
```json
{
  "devDependencies": {
    "selenium-webdriver": "^4.15.0",
    "chromedriver": "^117.0.0",
    "geckodriver": "^4.2.0",
    "edgedriver": "^5.4.0",
    "mocha": "^10.2.0",
    "chai": "^4.3.8"
  }
}
```

## 🛠️ Step 3: WebDriver Factory (JavaScript)

Create `test/utils/WebDriverFactory.js`:

```javascript
const { Builder } = require('selenium-webdriver');
const chrome = require('selenium-webdriver/chrome');
const firefox = require('selenium-webdriver/firefox');
const edge = require('selenium-webdriver/edge');

class WebDriverFactory {
    static createDriver(browserType = 'chrome', headless = false) {
        let driver;
        
        switch (browserType.toLowerCase()) {
            case 'chrome':
                driver = this.createChromeDriver(headless);
                break;
            case 'firefox':
                driver = this.createFirefoxDriver(headless);
                break;
            case 'edge':
                driver = this.createEdgeDriver(headless);
                break;
            default:
                throw new Error(`Unsupported browser: ${browserType}`);
        }
        
        return driver;
    }
    
    static createChromeDriver(headless) {
        const options = new chrome.Options();
        
        if (headless) {
            options.addArguments('--headless');
        }
        
        options.addArguments('--no-sandbox');
        options.addArguments('--disable-dev-shm-usage');
        options.addArguments('--window-size=1920,1080');
        
        return new Builder()
            .forBrowser('chrome')
            .setChromeOptions(options)
            .build();
    }
    
    static createFirefoxDriver(headless) {
        const options = new firefox.Options();
        
        if (headless) {
            options.addArguments('--headless');
        }
        
        options.addArguments('--width=1920');
        options.addArguments('--height=1080');
        
        return new Builder()
            .forBrowser('firefox')
            .setFirefoxOptions(options)
            .build();
    }
    
    static createEdgeDriver(headless) {
        const options = new edge.Options();
        
        if (headless) {
            options.addArguments('--headless');
        }
        
        options.addArguments('--window-size=1920,1080');
        
        return new Builder()
            .forBrowser('MicrosoftEdge')
            .setEdgeOptions(options)
            .build();
    }
}

module.exports = WebDriverFactory;
```

## 📝 Step 4: Test Structure Examples

### Login Test (like your current one):
```javascript
const { By, until } = require('selenium-webdriver');
const WebDriverFactory = require('../utils/WebDriverFactory');
const assert = require('chai').assert;

describe('Login Tests', function() {
    let driver;
    
    beforeEach(async function() {
        driver = WebDriverFactory.createDriver('chrome');
        await driver.manage().window().maximize();
    });
    
    afterEach(async function() {
        if (driver) {
            await driver.quit();
        }
    });
    
    it('should login successfully with valid credentials', async function() {
        // Navigate to login page
        await driver.get('http://localhost:3000/login');
        
        // Enter username
        const usernameField = await driver.findElement(By.css('input[placeholder="Enter username"]'));
        await usernameField.sendKeys('admin');
        
        // Enter password
        const passwordField = await driver.findElement(By.css('input[placeholder="Enter password"]'));
        await passwordField.sendKeys('12345');
        
        // Click login button
        const loginButton = await driver.findElement(By.xpath('//button[text()="Login"]'));
        await loginButton.click();
        
        // Wait for logout button to appear (login success)
        const logoutButton = await driver.wait(
            until.elementLocated(By.css('.logout-button')), 
            10000
        );
        
        const isLogoutVisible = await logoutButton.isDisplayed();
        assert.isTrue(isLogoutVisible, 'Logout button should be visible after login');
        
        console.log('Login test passed!');
    });
});
```

## 🎯 Step 5: Screenshot Capture for Presentation

### Create test files for different scenarios:

1. **test/ui/loginTest.js** - Login functionality
2. **test/ui/addBookTest.js** - Add book functionality  
3. **test/ui/searchTest.js** - Search functionality
4. **test/ui/navigationTest.js** - Navigation testing

### Example AddBook Test:
```javascript
describe('Add Book Tests', function() {
    it('should add a new book successfully', async function() {
        await driver.get('http://localhost:3000/add-book');
        
        // Fill book form
        await driver.findElement(By.id('bookTitle')).sendKeys('Test Book');
        await driver.findElement(By.id('bookAuthor')).sendKeys('Test Author');
        await driver.findElement(By.id('bookISBN')).sendKeys('1234567890');
        
        // Submit form
        await driver.findElement(By.css('button[type="submit"]')).click();
        
        // Verify success message
        const successMessage = await driver.wait(
            until.elementLocated(By.css('.success-message')), 
            5000
        );
        
        assert.isTrue(await successMessage.isDisplayed());
    });
});
```

## 🏃‍♂️ Step 6: Running Tests

### Add to package.json scripts:
```json
{
  "scripts": {
    "test:ui": "mocha test/ui/**/*.js --timeout 30000",
    "test:ui-chrome": "BROWSER=chrome npm run test:ui",
    "test:ui-firefox": "BROWSER=firefox npm run test:ui",
    "test:ui-headless": "HEADLESS=true npm run test:ui"
  }
}
```

### Run commands:
```bash
# Run all UI tests
npm run test:ui

# Run with specific browser
npm run test:ui-chrome
npm run test:ui-firefox

# Run in headless mode
npm run test:ui-headless
```

## 📸 Step 7: Creating Screenshot-Ready Tests

Structure your tests like this for presentation:

1. **Login Tests** - Screenshot 1
2. **Add Item Tests** - Screenshot 2  
3. **Search/Filter Tests** - Screenshot 3
4. **Navigation Tests** - Screenshot 4
5. **Form Validation Tests** - Screenshot 5

Each test file should have clear comments and console.log statements showing what's being tested.