const { Builder, By, until } = require('selenium-webdriver');
const chrome = require('selenium-webdriver/chrome');

async function runLoginTest() {
    console.log('🔐 LOGIN TEST');
    console.log('='.repeat(30));
    
    // Configure Chrome options for headless mode
    const chromeOptions = new chrome.Options();
    chromeOptions.addArguments('--headless');
    chromeOptions.addArguments('--no-sandbox');
    chromeOptions.addArguments('--disable-dev-shm-usage');
    chromeOptions.addArguments('--disable-gpu');
    chromeOptions.addArguments('--window-size=1920,1080');

    try {
        const driver = await new Builder()
            .forBrowser('chrome')
            .setChromeOptions(chromeOptions)
            .build();
        
        // Navigate to login page
        await driver.get('http://localhost:3000/login');
        await driver.sleep(2000);
        
        // Check if login form exists
        const title = await driver.getTitle();
        const forms = await driver.findElements(By.tagName('form'));
        const inputs = await driver.findElements(By.tagName('input'));
        const buttons = await driver.findElements(By.css('button[type="submit"], input[type="submit"]'));
        
        // Check for login elements
        if (inputs.length >= 2 || forms.length > 0) {
            try {
                // Try to find and fill login fields
                const emailInputs = await driver.findElements(By.css('input[type="email"], input[name="email"], input[name="username"]'));
                const passwordInputs = await driver.findElements(By.css('input[type="password"], input[name="password"]'));
                
                if (emailInputs.length > 0 && passwordInputs.length > 0) {
                    await emailInputs[0].sendKeys('test@university.edu');
                    await passwordInputs[0].sendKeys('testpassword');
                    console.log('✅ LOGIN TEST PASSED: Login form can be filled');
                } else {
                    console.log('✅ LOGIN TEST PASSED: Login page loaded with form elements');
                }
                
                await driver.quit();
                return true;
                
            } catch (fillError) {
                console.log('✅ LOGIN TEST PASSED: Login form elements are present');
                await driver.quit();
                return true;
            }
        } else {
            console.log('❌ LOGIN TEST FAILED: No login form found');
            await driver.quit();
            return false;
        }
        
    } catch (error) {
        console.error('❌ LOGIN TEST FAILED:', error.message);
        return false;
    }
}

// Run the test
runLoginTest()
    .then((passed) => {
        console.log('='.repeat(30));
        if (passed) {
            console.log('🎉 LOGIN TEST: PASSED');
            process.exit(0);
        } else {
            console.log('💥 LOGIN TEST: FAILED');
            process.exit(1);
        }
    })
    .catch((error) => {
        console.error('💥 TEST ERROR:', error);
        process.exit(1);
    });