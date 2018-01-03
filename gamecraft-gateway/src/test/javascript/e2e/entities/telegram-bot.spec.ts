import { browser, element, by, $ } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';
const path = require('path');

describe('TelegramBot e2e test', () => {

    let navBarPage: NavBarPage;
    let telegramBotDialogPage: TelegramBotDialogPage;
    let telegramBotComponentsPage: TelegramBotComponentsPage;
    const fileToUpload = '../../../../main/webapp/content/images/logo-jhipster.png';
    const absolutePath = path.resolve(__dirname, fileToUpload);
    

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load TelegramBots', () => {
        navBarPage.goToEntity('telegram-bot');
        telegramBotComponentsPage = new TelegramBotComponentsPage();
        expect(telegramBotComponentsPage.getTitle()).toMatch(/gamecraftgatewayApp.telegramBot.home.title/);

    });

    it('should load create TelegramBot dialog', () => {
        telegramBotComponentsPage.clickOnCreateButton();
        telegramBotDialogPage = new TelegramBotDialogPage();
        expect(telegramBotDialogPage.getModalTitle()).toMatch(/gamecraftgatewayApp.telegramBot.home.createOrEditLabel/);
        telegramBotDialogPage.close();
    });

    it('should create and save TelegramBots', () => {
        telegramBotComponentsPage.clickOnCreateButton();
        telegramBotDialogPage.setTelegramBotNameInput('telegramBotName');
        expect(telegramBotDialogPage.getTelegramBotNameInput()).toMatch('telegramBotName');
        telegramBotDialogPage.setTelegramBotDescriptionInput('telegramBotDescription');
        expect(telegramBotDialogPage.getTelegramBotDescriptionInput()).toMatch('telegramBotDescription');
        telegramBotDialogPage.setTelegramBotTokenInput('telegramBotToken');
        expect(telegramBotDialogPage.getTelegramBotTokenInput()).toMatch('telegramBotToken');
        telegramBotDialogPage.getTelegramBotEnabledInput().isSelected().then(function (selected) {
            if (selected) {
                telegramBotDialogPage.getTelegramBotEnabledInput().click();
                expect(telegramBotDialogPage.getTelegramBotEnabledInput().isSelected()).toBeFalsy();
            } else {
                telegramBotDialogPage.getTelegramBotEnabledInput().click();
                expect(telegramBotDialogPage.getTelegramBotEnabledInput().isSelected()).toBeTruthy();
            }
        });
        telegramBotDialogPage.save();
        expect(telegramBotDialogPage.getSaveButton().isPresent()).toBeFalsy();
    }); 

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class TelegramBotComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-telegram-bot div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class TelegramBotDialogPage {
    modalTitle = element(by.css('h4#myTelegramBotLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    telegramBotNameInput = element(by.css('input#field_telegramBotName'));
    telegramBotDescriptionInput = element(by.css('input#field_telegramBotDescription'));
    telegramBotTokenInput = element(by.css('input#field_telegramBotToken'));
    telegramBotEnabledInput = element(by.css('input#field_telegramBotEnabled'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setTelegramBotNameInput = function (telegramBotName) {
        this.telegramBotNameInput.sendKeys(telegramBotName);
    }

    getTelegramBotNameInput = function () {
        return this.telegramBotNameInput.getAttribute('value');
    }

    setTelegramBotDescriptionInput = function (telegramBotDescription) {
        this.telegramBotDescriptionInput.sendKeys(telegramBotDescription);
    }

    getTelegramBotDescriptionInput = function () {
        return this.telegramBotDescriptionInput.getAttribute('value');
    }

    setTelegramBotTokenInput = function (telegramBotToken) {
        this.telegramBotTokenInput.sendKeys(telegramBotToken);
    }

    getTelegramBotTokenInput = function () {
        return this.telegramBotTokenInput.getAttribute('value');
    }

    getTelegramBotEnabledInput = function () {
        return this.telegramBotEnabledInput;
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
