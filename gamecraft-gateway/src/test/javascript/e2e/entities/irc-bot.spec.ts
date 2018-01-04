import { browser, element, by, $ } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';
const path = require('path');

describe('IrcBot e2e test', () => {

    let navBarPage: NavBarPage;
    let ircBotDialogPage: IrcBotDialogPage;
    let ircBotComponentsPage: IrcBotComponentsPage;
    const fileToUpload = '../../../../main/webapp/content/images/logo-jhipster.png';
    const absolutePath = path.resolve(__dirname, fileToUpload);
    

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load IrcBots', () => {
        navBarPage.goToEntity('irc-bot');
        ircBotComponentsPage = new IrcBotComponentsPage();
        expect(ircBotComponentsPage.getTitle()).toMatch(/gamecraftgatewayApp.ircBot.home.title/);

    });

    it('should load create IrcBot dialog', () => {
        ircBotComponentsPage.clickOnCreateButton();
        ircBotDialogPage = new IrcBotDialogPage();
        expect(ircBotDialogPage.getModalTitle()).toMatch(/gamecraftgatewayApp.ircBot.home.createOrEditLabel/);
        ircBotDialogPage.close();
    });

    it('should create and save IrcBots', () => {
        ircBotComponentsPage.clickOnCreateButton();
        ircBotDialogPage.setIrcBotNameInput('ircBotName');
        expect(ircBotDialogPage.getIrcBotNameInput()).toMatch('ircBotName');
        ircBotDialogPage.setIrcBotDescriptionInput('ircBotDescription');
        expect(ircBotDialogPage.getIrcBotDescriptionInput()).toMatch('ircBotDescription');
        ircBotDialogPage.getIrcBotEnabledInput().isSelected().then(function (selected) {
            if (selected) {
                ircBotDialogPage.getIrcBotEnabledInput().click();
                expect(ircBotDialogPage.getIrcBotEnabledInput().isSelected()).toBeFalsy();
            } else {
                ircBotDialogPage.getIrcBotEnabledInput().click();
                expect(ircBotDialogPage.getIrcBotEnabledInput().isSelected()).toBeTruthy();
            }
        });
        ircBotDialogPage.setIrcServerAddressInput('ircServerAddress');
        expect(ircBotDialogPage.getIrcServerAddressInput()).toMatch('ircServerAddress');
        ircBotDialogPage.setIrcServerPortInput('5');
        expect(ircBotDialogPage.getIrcServerPortInput()).toMatch('5');
        ircBotDialogPage.setIrcBotNicknameInput('ircBotNickname');
        expect(ircBotDialogPage.getIrcBotNicknameInput()).toMatch('ircBotNickname');
        ircBotDialogPage.save();
        expect(ircBotDialogPage.getSaveButton().isPresent()).toBeFalsy();
    }); 

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class IrcBotComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-irc-bot div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class IrcBotDialogPage {
    modalTitle = element(by.css('h4#myIrcBotLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    ircBotNameInput = element(by.css('input#field_ircBotName'));
    ircBotDescriptionInput = element(by.css('input#field_ircBotDescription'));
    ircBotEnabledInput = element(by.css('input#field_ircBotEnabled'));
    ircServerAddressInput = element(by.css('input#field_ircServerAddress'));
    ircServerPortInput = element(by.css('input#field_ircServerPort'));
    ircBotNicknameInput = element(by.css('input#field_ircBotNickname'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setIrcBotNameInput = function (ircBotName) {
        this.ircBotNameInput.sendKeys(ircBotName);
    }

    getIrcBotNameInput = function () {
        return this.ircBotNameInput.getAttribute('value');
    }

    setIrcBotDescriptionInput = function (ircBotDescription) {
        this.ircBotDescriptionInput.sendKeys(ircBotDescription);
    }

    getIrcBotDescriptionInput = function () {
        return this.ircBotDescriptionInput.getAttribute('value');
    }

    getIrcBotEnabledInput = function () {
        return this.ircBotEnabledInput;
    }
    setIrcServerAddressInput = function (ircServerAddress) {
        this.ircServerAddressInput.sendKeys(ircServerAddress);
    }

    getIrcServerAddressInput = function () {
        return this.ircServerAddressInput.getAttribute('value');
    }

    setIrcServerPortInput = function (ircServerPort) {
        this.ircServerPortInput.sendKeys(ircServerPort);
    }

    getIrcServerPortInput = function () {
        return this.ircServerPortInput.getAttribute('value');
    }

    setIrcBotNicknameInput = function (ircBotNickname) {
        this.ircBotNicknameInput.sendKeys(ircBotNickname);
    }

    getIrcBotNicknameInput = function () {
        return this.ircBotNicknameInput.getAttribute('value');
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
