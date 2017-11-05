import { browser, element, by, $ } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';
const path = require('path');

describe('SlackAccount e2e test', () => {

    let navBarPage: NavBarPage;
    let slackAccountDialogPage: SlackAccountDialogPage;
    let slackAccountComponentsPage: SlackAccountComponentsPage;
    const fileToUpload = '../../../../main/webapp/content/images/logo-gamecraft.png';
    const absolutePath = path.resolve(__dirname, fileToUpload);
    

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load SlackAccounts', () => {
        navBarPage.goToEntity('slack-account');
        slackAccountComponentsPage = new SlackAccountComponentsPage();
        expect(slackAccountComponentsPage.getTitle()).toMatch(/gamecraftgatewayApp.slackAccount.home.title/);

    });

    it('should load create SlackAccount dialog', () => {
        slackAccountComponentsPage.clickOnCreateButton();
        slackAccountDialogPage = new SlackAccountDialogPage();
        expect(slackAccountDialogPage.getModalTitle()).toMatch(/gamecraftgatewayApp.slackAccount.home.createOrEditLabel/);
        slackAccountDialogPage.close();
    });

    it('should create and save SlackAccounts', () => {
        slackAccountComponentsPage.clickOnCreateButton();
        slackAccountDialogPage.setSlackAccountNameInput('slackAccountName');
        expect(slackAccountDialogPage.getSlackAccountNameInput()).toMatch('slackAccountName');
        slackAccountDialogPage.setSlackAccountDescriptionInput('slackAccountDescription');
        expect(slackAccountDialogPage.getSlackAccountDescriptionInput()).toMatch('slackAccountDescription');
        slackAccountDialogPage.setSlackAccountTokenInput('slackAccountToken');
        expect(slackAccountDialogPage.getSlackAccountTokenInput()).toMatch('slackAccountToken');
        slackAccountDialogPage.getSlackAccountEnabledInput().isSelected().then(function (selected) {
            if (selected) {
                slackAccountDialogPage.getSlackAccountEnabledInput().click();
                expect(slackAccountDialogPage.getSlackAccountEnabledInput().isSelected()).toBeFalsy();
            } else {
                slackAccountDialogPage.getSlackAccountEnabledInput().click();
                expect(slackAccountDialogPage.getSlackAccountEnabledInput().isSelected()).toBeTruthy();
            }
        });
        slackAccountDialogPage.save();
        expect(slackAccountDialogPage.getSaveButton().isPresent()).toBeFalsy();
    }); 

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class SlackAccountComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-slack-account div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class SlackAccountDialogPage {
    modalTitle = element(by.css('h4#mySlackAccountLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    slackAccountNameInput = element(by.css('input#field_slackAccountName'));
    slackAccountDescriptionInput = element(by.css('input#field_slackAccountDescription'));
    slackAccountTokenInput = element(by.css('input#field_slackAccountToken'));
    slackAccountEnabledInput = element(by.css('input#field_slackAccountEnabled'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setSlackAccountNameInput = function (slackAccountName) {
        this.slackAccountNameInput.sendKeys(slackAccountName);
    }

    getSlackAccountNameInput = function () {
        return this.slackAccountNameInput.getAttribute('value');
    }

    setSlackAccountDescriptionInput = function (slackAccountDescription) {
        this.slackAccountDescriptionInput.sendKeys(slackAccountDescription);
    }

    getSlackAccountDescriptionInput = function () {
        return this.slackAccountDescriptionInput.getAttribute('value');
    }

    setSlackAccountTokenInput = function (slackAccountToken) {
        this.slackAccountTokenInput.sendKeys(slackAccountToken);
    }

    getSlackAccountTokenInput = function () {
        return this.slackAccountTokenInput.getAttribute('value');
    }

    getSlackAccountEnabledInput = function () {
        return this.slackAccountEnabledInput;
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
