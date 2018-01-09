import { browser, element, by, $ } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';
const path = require('path');

describe('TwitterBot e2e test', () => {

    let navBarPage: NavBarPage;
    let twitterBotDialogPage: TwitterBotDialogPage;
    let twitterBotComponentsPage: TwitterBotComponentsPage;
    const fileToUpload = '../../../../main/webapp/content/images/logo-jhipster.png';
    const absolutePath = path.resolve(__dirname, fileToUpload);
    

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load TwitterBots', () => {
        navBarPage.goToEntity('twitter-bot');
        twitterBotComponentsPage = new TwitterBotComponentsPage();
        expect(twitterBotComponentsPage.getTitle()).toMatch(/gamecraftgatewayApp.twitterBot.home.title/);

    });

    it('should load create TwitterBot dialog', () => {
        twitterBotComponentsPage.clickOnCreateButton();
        twitterBotDialogPage = new TwitterBotDialogPage();
        expect(twitterBotDialogPage.getModalTitle()).toMatch(/gamecraftgatewayApp.twitterBot.home.createOrEditLabel/);
        twitterBotDialogPage.close();
    });

    it('should create and save TwitterBots', () => {
        twitterBotComponentsPage.clickOnCreateButton();
        twitterBotDialogPage.setTwitterBotNameInput('twitterBotName');
        expect(twitterBotDialogPage.getTwitterBotNameInput()).toMatch('twitterBotName');
        twitterBotDialogPage.setTwitterBotDescriptionInput('twitterBotDescription');
        expect(twitterBotDialogPage.getTwitterBotDescriptionInput()).toMatch('twitterBotDescription');
        twitterBotDialogPage.setTwitterBotConsumerKeyInput('twitterBotConsumerKey');
        expect(twitterBotDialogPage.getTwitterBotConsumerKeyInput()).toMatch('twitterBotConsumerKey');
        twitterBotDialogPage.setTwitterBotConsumerSecretInput('twitterBotConsumerSecret');
        expect(twitterBotDialogPage.getTwitterBotConsumerSecretInput()).toMatch('twitterBotConsumerSecret');
        twitterBotDialogPage.setTwitterBotAccessTokenInput('twitterBotAccessToken');
        expect(twitterBotDialogPage.getTwitterBotAccessTokenInput()).toMatch('twitterBotAccessToken');
        twitterBotDialogPage.setTwitterBotAccessTokenSecretInput('twitterBotAccessTokenSecret');
        expect(twitterBotDialogPage.getTwitterBotAccessTokenSecretInput()).toMatch('twitterBotAccessTokenSecret');
        twitterBotDialogPage.getTwitterBotEnabledInput().isSelected().then(function (selected) {
            if (selected) {
                twitterBotDialogPage.getTwitterBotEnabledInput().click();
                expect(twitterBotDialogPage.getTwitterBotEnabledInput().isSelected()).toBeFalsy();
            } else {
                twitterBotDialogPage.getTwitterBotEnabledInput().click();
                expect(twitterBotDialogPage.getTwitterBotEnabledInput().isSelected()).toBeTruthy();
            }
        });
        twitterBotDialogPage.save();
        expect(twitterBotDialogPage.getSaveButton().isPresent()).toBeFalsy();
    }); 

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class TwitterBotComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-twitter-bot div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class TwitterBotDialogPage {
    modalTitle = element(by.css('h4#myTwitterBotLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    twitterBotNameInput = element(by.css('input#field_twitterBotName'));
    twitterBotDescriptionInput = element(by.css('input#field_twitterBotDescription'));
    twitterBotConsumerKeyInput = element(by.css('input#field_twitterBotConsumerKey'));
    twitterBotConsumerSecretInput = element(by.css('input#field_twitterBotConsumerSecret'));
    twitterBotAccessTokenInput = element(by.css('input#field_twitterBotAccessToken'));
    twitterBotAccessTokenSecretInput = element(by.css('input#field_twitterBotAccessTokenSecret'));
    twitterBotEnabledInput = element(by.css('input#field_twitterBotEnabled'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setTwitterBotNameInput = function (twitterBotName) {
        this.twitterBotNameInput.sendKeys(twitterBotName);
    }

    getTwitterBotNameInput = function () {
        return this.twitterBotNameInput.getAttribute('value');
    }

    setTwitterBotDescriptionInput = function (twitterBotDescription) {
        this.twitterBotDescriptionInput.sendKeys(twitterBotDescription);
    }

    getTwitterBotDescriptionInput = function () {
        return this.twitterBotDescriptionInput.getAttribute('value');
    }

    setTwitterBotConsumerKeyInput = function (twitterBotConsumerKey) {
        this.twitterBotConsumerKeyInput.sendKeys(twitterBotConsumerKey);
    }

    getTwitterBotConsumerKeyInput = function () {
        return this.twitterBotConsumerKeyInput.getAttribute('value');
    }

    setTwitterBotConsumerSecretInput = function (twitterBotConsumerSecret) {
        this.twitterBotConsumerSecretInput.sendKeys(twitterBotConsumerSecret);
    }

    getTwitterBotConsumerSecretInput = function () {
        return this.twitterBotConsumerSecretInput.getAttribute('value');
    }

    setTwitterBotAccessTokenInput = function (twitterBotAccessToken) {
        this.twitterBotAccessTokenInput.sendKeys(twitterBotAccessToken);
    }

    getTwitterBotAccessTokenInput = function () {
        return this.twitterBotAccessTokenInput.getAttribute('value');
    }

    setTwitterBotAccessTokenSecretInput = function (twitterBotAccessTokenSecret) {
        this.twitterBotAccessTokenSecretInput.sendKeys(twitterBotAccessTokenSecret);
    }

    getTwitterBotAccessTokenSecretInput = function () {
        return this.twitterBotAccessTokenSecretInput.getAttribute('value');
    }

    getTwitterBotEnabledInput = function () {
        return this.twitterBotEnabledInput;
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
