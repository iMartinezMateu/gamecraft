import { browser, element, by, $ } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';
const path = require('path');

describe('HipchatBot e2e test', () => {

    let navBarPage: NavBarPage;
    let hipchatBotDialogPage: HipchatBotDialogPage;
    let hipchatBotComponentsPage: HipchatBotComponentsPage;
    const fileToUpload = '../../../../main/webapp/content/images/logo-gamecraft.png';
    const absolutePath = path.resolve(__dirname, fileToUpload);
    

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load HipchatBots', () => {
        navBarPage.goToEntity('hipchat-bot');
        hipchatBotComponentsPage = new HipchatBotComponentsPage();
        expect(hipchatBotComponentsPage.getTitle()).toMatch(/gamecraftgatewayApp.hipchatBot.home.title/);

    });

    it('should load create HipchatBot dialog', () => {
        hipchatBotComponentsPage.clickOnCreateButton();
        hipchatBotDialogPage = new HipchatBotDialogPage();
        expect(hipchatBotDialogPage.getModalTitle()).toMatch(/gamecraftgatewayApp.hipchatBot.home.createOrEditLabel/);
        hipchatBotDialogPage.close();
    });

    it('should create and save HipchatBots', () => {
        hipchatBotComponentsPage.clickOnCreateButton();
        hipchatBotDialogPage.setHipchatBotNameInput('hipchatBotName');
        expect(hipchatBotDialogPage.getHipchatBotNameInput()).toMatch('hipchatBotName');
        hipchatBotDialogPage.setHipchatBotDescriptionInput('hipchatBotDescription');
        expect(hipchatBotDialogPage.getHipchatBotDescriptionInput()).toMatch('hipchatBotDescription');
        hipchatBotDialogPage.setHipchatBotTokenInput('hipchatBotToken');
        expect(hipchatBotDialogPage.getHipchatBotTokenInput()).toMatch('hipchatBotToken');
        hipchatBotDialogPage.getHipchatBotEnabledInput().isSelected().then(function (selected) {
            if (selected) {
                hipchatBotDialogPage.getHipchatBotEnabledInput().click();
                expect(hipchatBotDialogPage.getHipchatBotEnabledInput().isSelected()).toBeFalsy();
            } else {
                hipchatBotDialogPage.getHipchatBotEnabledInput().click();
                expect(hipchatBotDialogPage.getHipchatBotEnabledInput().isSelected()).toBeTruthy();
            }
        });
        hipchatBotDialogPage.save();
        expect(hipchatBotDialogPage.getSaveButton().isPresent()).toBeFalsy();
    }); 

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class HipchatBotComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-hipchat-bot div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class HipchatBotDialogPage {
    modalTitle = element(by.css('h4#myHipchatBotLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    hipchatBotNameInput = element(by.css('input#field_hipchatBotName'));
    hipchatBotDescriptionInput = element(by.css('input#field_hipchatBotDescription'));
    hipchatBotTokenInput = element(by.css('input#field_hipchatBotToken'));
    hipchatBotEnabledInput = element(by.css('input#field_hipchatBotEnabled'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setHipchatBotNameInput = function (hipchatBotName) {
        this.hipchatBotNameInput.sendKeys(hipchatBotName);
    }

    getHipchatBotNameInput = function () {
        return this.hipchatBotNameInput.getAttribute('value');
    }

    setHipchatBotDescriptionInput = function (hipchatBotDescription) {
        this.hipchatBotDescriptionInput.sendKeys(hipchatBotDescription);
    }

    getHipchatBotDescriptionInput = function () {
        return this.hipchatBotDescriptionInput.getAttribute('value');
    }

    setHipchatBotTokenInput = function (hipchatBotToken) {
        this.hipchatBotTokenInput.sendKeys(hipchatBotToken);
    }

    getHipchatBotTokenInput = function () {
        return this.hipchatBotTokenInput.getAttribute('value');
    }

    getHipchatBotEnabledInput = function () {
        return this.hipchatBotEnabledInput;
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
