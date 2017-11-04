import { browser, element, by, $ } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';
const path = require('path');

describe('EmailAccount e2e test', () => {

    let navBarPage: NavBarPage;
    let emailAccountDialogPage: EmailAccountDialogPage;
    let emailAccountComponentsPage: EmailAccountComponentsPage;
    const fileToUpload = '../../../../main/webapp/content/images/logo-gamecraft.png';
    const absolutePath = path.resolve(__dirname, fileToUpload);
    

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load EmailAccounts', () => {
        navBarPage.goToEntity('email-account');
        emailAccountComponentsPage = new EmailAccountComponentsPage();
        expect(emailAccountComponentsPage.getTitle()).toMatch(/gamecraftgatewayApp.emailAccount.home.title/);

    });

    it('should load create EmailAccount dialog', () => {
        emailAccountComponentsPage.clickOnCreateButton();
        emailAccountDialogPage = new EmailAccountDialogPage();
        expect(emailAccountDialogPage.getModalTitle()).toMatch(/gamecraftgatewayApp.emailAccount.home.createOrEditLabel/);
        emailAccountDialogPage.close();
    });

    it('should create and save EmailAccounts', () => {
        emailAccountComponentsPage.clickOnCreateButton();
        emailAccountDialogPage.setEmailAccountNameInput('emailAccountName');
        expect(emailAccountDialogPage.getEmailAccountNameInput()).toMatch('emailAccountName');
        emailAccountDialogPage.setEmailSmtpServerInput('emailSmtpServer');
        expect(emailAccountDialogPage.getEmailSmtpServerInput()).toMatch('emailSmtpServer');
        emailAccountDialogPage.setEmailSmtpUsernameInput('emailSmtpUsername');
        expect(emailAccountDialogPage.getEmailSmtpUsernameInput()).toMatch('emailSmtpUsername');
        emailAccountDialogPage.setEmailSmtpPasswordInput('emailSmtpPassword');
        expect(emailAccountDialogPage.getEmailSmtpPasswordInput()).toMatch('emailSmtpPassword');
        emailAccountDialogPage.getEmailSmtpUseSSLInput().isSelected().then(function (selected) {
            if (selected) {
                emailAccountDialogPage.getEmailSmtpUseSSLInput().click();
                expect(emailAccountDialogPage.getEmailSmtpUseSSLInput().isSelected()).toBeFalsy();
            } else {
                emailAccountDialogPage.getEmailSmtpUseSSLInput().click();
                expect(emailAccountDialogPage.getEmailSmtpUseSSLInput().isSelected()).toBeTruthy();
            }
        });
        emailAccountDialogPage.setEmailSmtpPortInput('5');
        expect(emailAccountDialogPage.getEmailSmtpPortInput()).toMatch('5');
        emailAccountDialogPage.setEmailAccountDescriptionInput('emailAccountDescription');
        expect(emailAccountDialogPage.getEmailAccountDescriptionInput()).toMatch('emailAccountDescription');
        emailAccountDialogPage.getEmailAccountEnabledInput().isSelected().then(function (selected) {
            if (selected) {
                emailAccountDialogPage.getEmailAccountEnabledInput().click();
                expect(emailAccountDialogPage.getEmailAccountEnabledInput().isSelected()).toBeFalsy();
            } else {
                emailAccountDialogPage.getEmailAccountEnabledInput().click();
                expect(emailAccountDialogPage.getEmailAccountEnabledInput().isSelected()).toBeTruthy();
            }
        });
        emailAccountDialogPage.save();
        expect(emailAccountDialogPage.getSaveButton().isPresent()).toBeFalsy();
    }); 

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class EmailAccountComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-email-account div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class EmailAccountDialogPage {
    modalTitle = element(by.css('h4#myEmailAccountLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    emailAccountNameInput = element(by.css('input#field_emailAccountName'));
    emailSmtpServerInput = element(by.css('input#field_emailSmtpServer'));
    emailSmtpUsernameInput = element(by.css('input#field_emailSmtpUsername'));
    emailSmtpPasswordInput = element(by.css('input#field_emailSmtpPassword'));
    emailSmtpUseSSLInput = element(by.css('input#field_emailSmtpUseSSL'));
    emailSmtpPortInput = element(by.css('input#field_emailSmtpPort'));
    emailAccountDescriptionInput = element(by.css('input#field_emailAccountDescription'));
    emailAccountEnabledInput = element(by.css('input#field_emailAccountEnabled'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setEmailAccountNameInput = function (emailAccountName) {
        this.emailAccountNameInput.sendKeys(emailAccountName);
    }

    getEmailAccountNameInput = function () {
        return this.emailAccountNameInput.getAttribute('value');
    }

    setEmailSmtpServerInput = function (emailSmtpServer) {
        this.emailSmtpServerInput.sendKeys(emailSmtpServer);
    }

    getEmailSmtpServerInput = function () {
        return this.emailSmtpServerInput.getAttribute('value');
    }

    setEmailSmtpUsernameInput = function (emailSmtpUsername) {
        this.emailSmtpUsernameInput.sendKeys(emailSmtpUsername);
    }

    getEmailSmtpUsernameInput = function () {
        return this.emailSmtpUsernameInput.getAttribute('value');
    }

    setEmailSmtpPasswordInput = function (emailSmtpPassword) {
        this.emailSmtpPasswordInput.sendKeys(emailSmtpPassword);
    }

    getEmailSmtpPasswordInput = function () {
        return this.emailSmtpPasswordInput.getAttribute('value');
    }

    getEmailSmtpUseSSLInput = function () {
        return this.emailSmtpUseSSLInput;
    }
    setEmailSmtpPortInput = function (emailSmtpPort) {
        this.emailSmtpPortInput.sendKeys(emailSmtpPort);
    }

    getEmailSmtpPortInput = function () {
        return this.emailSmtpPortInput.getAttribute('value');
    }

    setEmailAccountDescriptionInput = function (emailAccountDescription) {
        this.emailAccountDescriptionInput.sendKeys(emailAccountDescription);
    }

    getEmailAccountDescriptionInput = function () {
        return this.emailAccountDescriptionInput.getAttribute('value');
    }

    getEmailAccountEnabledInput = function () {
        return this.emailAccountEnabledInput;
    }
    save() {
        this.saveButton.click();
    }

    close() {
        this.closeButton.click();
    }

    getSaveButton() {
        return this.saveButton;
    }
}
