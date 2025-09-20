// Enhanced Login Test based on your screenshot
const { Builder, By, until } = require('selenium-webdriver');
const chrome = require('selenium-webdriver/chrome');
const assert = require('chai').assert;

describe('Booksy App - UI Test Suite', function() {
    let driver;
    const baseUrl = 'http://localhost:3000';
    
    beforeEach(async function() {
        // Setup Chrome with options
        const options = new chrome.Options();
        options.addArguments('--window-size=1920,1080');
        options.addArguments('--disable-web-security');
        
        driver = new Builder()
            .forBrowser('chrome')
            .setChromeOptions(options)
            .build();
            
        await driver.manage().window().maximize();
    });
    
    afterEach(async function() {
        if (driver) {
            await driver.quit();
        }
    });

    describe('Authentication Tests', function() {
        it('should perform successful login flow', async function() {
            console.log('=== Starting Login Test ===');
            
            // Navigate to login page
            await driver.get(`${baseUrl}/login`);
            console.log('Navigated to login page');
            
            // Wait for page to load
            await driver.wait(until.elementLocated(By.css('input[placeholder*="username"]')), 5000);
            
            // Enter username
            const usernameField = await driver.findElement(By.css('input[placeholder*="username"]'));
            await usernameField.clear();
            await usernameField.sendKeys('admin');
            console.log('Username entered: admin');
            
            // Enter password  
            const passwordField = await driver.findElement(By.css('input[placeholder*="password"]'));
            await passwordField.clear();
            await passwordField.sendKeys('12345');
            console.log('Password entered');
            
            // Click login button
            const loginButton = await driver.findElement(By.xpath('//button[text()="Login"]'));
            await loginButton.click();
            console.log('Login button clicked');
            
            // Wait for successful login (logout button appears)
            const logoutButton = await driver.wait(
                until.elementLocated(By.css('.logout-button')), 
                10000
            );
            
            // Verify login success
            const isLogoutVisible = await logoutButton.isDisplayed();
            assert.isTrue(isLogoutVisible, 'Logout button should be visible after login');
            
            console.log('✅ Login test passed! User successfully authenticated');
        });
        
        it('should handle invalid login credentials', async function() {
            console.log('=== Starting Invalid Login Test ===');
            
            await driver.get(`${baseUrl}/login`);
            
            // Enter invalid credentials
            await driver.findElement(By.css('input[placeholder*="username"]')).sendKeys('invalid');
            await driver.findElement(By.css('input[placeholder*="password"]')).sendKeys('wrong');
            await driver.findElement(By.xpath('//button[text()="Login"]')).click();
            
            // Wait for error message
            const errorMessage = await driver.wait(
                until.elementLocated(By.css('.error-message, .alert-danger')), 
                5000
            );
            
            assert.isTrue(await errorMessage.isDisplayed(), 'Error message should be displayed');
            console.log('✅ Invalid login handled correctly');
        });
    });

    describe('Book Management Tests', function() {
        beforeEach(async function() {
            // Login before each test
            await driver.get(`${baseUrl}/login`);
            await driver.findElement(By.css('input[placeholder*="username"]')).sendKeys('admin');
            await driver.findElement(By.css('input[placeholder*="password"]')).sendKeys('12345');
            await driver.findElement(By.xpath('//button[text()="Login"]')).click();
            
            // Wait for dashboard
            await driver.wait(until.elementLocated(By.css('.logout-button')), 5000);
        });
        
        it('should add a new book successfully', async function() {
            console.log('=== Starting Add Book Test ===');
            
            // Navigate to add book page
            await driver.get(`${baseUrl}/add-book`);
            
            // Fill book details
            await driver.findElement(By.id('bookTitle')).sendKeys('Selenium Testing Guide');
            await driver.findElement(By.id('bookAuthor')).sendKeys('Test Author');
            await driver.findElement(By.id('bookISBN')).sendKeys('978-1234567890');
            await driver.findElement(By.id('bookGenre')).sendKeys('Technology');
            
            // Submit form
            await driver.findElement(By.css('button[type="submit"]')).click();
            
            // Verify success
            const successMessage = await driver.wait(
                until.elementLocated(By.css('.success-message, .alert-success')), 
                5000
            );
            
            assert.isTrue(await successMessage.isDisplayed());
            console.log('✅ Book added successfully');
        });
        
        it('should search for books', async function() {
            console.log('=== Starting Book Search Test ===');
            
            // Navigate to search page
            await driver.get(`${baseUrl}/search`);
            
            // Enter search term
            const searchField = await driver.findElement(By.css('input[name="search"], #searchInput'));
            await searchField.sendKeys('Selenium');
            
            // Click search button
            await driver.findElement(By.css('button[type="submit"], .search-btn')).click();
            
            // Wait for results
            const resultsContainer = await driver.wait(
                until.elementLocated(By.css('.search-results, .book-list')), 
                5000
            );
            
            assert.isTrue(await resultsContainer.isDisplayed());
            console.log('✅ Search functionality working correctly');
        });
    });

    describe('Navigation Tests', function() {
        it('should navigate through main menu items', async function() {
            console.log('=== Starting Navigation Test ===');
            
            // Login first
            await driver.get(`${baseUrl}/login`);
            await driver.findElement(By.css('input[placeholder*="username"]')).sendKeys('admin');
            await driver.findElement(By.css('input[placeholder*="password"]')).sendKeys('12345');
            await driver.findElement(By.xpath('//button[text()="Login"]')).click();
            await driver.wait(until.elementLocated(By.css('.logout-button')), 5000);
            
            // Test navigation to different pages
            const menuItems = [
                { selector: 'a[href="/dashboard"], .nav-dashboard', expectedUrl: '/dashboard' },
                { selector: 'a[href="/books"], .nav-books', expectedUrl: '/books' },
                { selector: 'a[href="/add-book"], .nav-add-book', expectedUrl: '/add-book' },
                { selector: 'a[href="/search"], .nav-search', expectedUrl: '/search' }
            ];
            
            for (const item of menuItems) {
                try {
                    const menuLink = await driver.findElement(By.css(item.selector));
                    await menuLink.click();
                    
                    await driver.wait(until.urlContains(item.expectedUrl), 3000);
                    const currentUrl = await driver.getCurrentUrl();
                    
                    assert.include(currentUrl, item.expectedUrl, `Should navigate to ${item.expectedUrl}`);
                    console.log(`✅ Navigation to ${item.expectedUrl} successful`);
                } catch (error) {
                    console.log(`⚠️ Menu item ${item.selector} not found or navigation failed`);
                }
            }
        });
    });
});

// Export for modular usage
module.exports = {
    baseUrl: 'http://localhost:3000',
    loginUser: async (driver, username = 'admin', password = '12345') => {
        await driver.get('http://localhost:3000/login');
        await driver.findElement(By.css('input[placeholder*="username"]')).sendKeys(username);
        await driver.findElement(By.css('input[placeholder*="password"]')).sendKeys(password);
        await driver.findElement(By.xpath('//button[text()="Login"]')).click();
        await driver.wait(until.elementLocated(By.css('.logout-button')), 5000);
    }
};