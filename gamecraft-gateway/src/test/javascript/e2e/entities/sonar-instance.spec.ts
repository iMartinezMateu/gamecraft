import { browser, element, by, $ } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';
const path = require('path');

describe('SonarInstance e2e test', () => {

    let navBarPage: NavBarPage;
    let sonarInstanceDialogPage: SonarInstanceDialogPage;
    let sonarInstanceComponentsPage: SonarInstanceComponentsPage;
    const fileToUpload = '../../../../main/webapp/content/images/logo-jhipster.png';
    const absolutePath = path.resolve(__dirname, fileToUpload);
    

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load SonarInstances', () => {
        navBarPage.goToEntity('sonar-instance');
        sonarInstanceComponentsPage = new SonarInstanceComponentsPage();
        expect(sonarInstanceComponentsPage.getTitle()).toMatch(/gamecraftgatewayApp.sonarInstance.home.title/);

    });

    it('should load create SonarInstance dialog', () => {
        sonarInstanceComponentsPage.clickOnCreateButton();
        sonarInstanceDialogPage = new SonarInstanceDialogPage();
        expect(sonarInstanceDialogPage.getModalTitle()).toMatch(/gamecraftgatewayApp.sonarInstance.home.createOrEditLabel/);
        sonarInstanceDialogPage.close();
    });

    it('should create and save SonarInstances', () => {
        sonarInstanceComponentsPage.clickOnCreateButton();
        sonarInstanceDialogPage.setSonarInstanceNameInput('sonarInstanceName');
        expect(sonarInstanceDialogPage.getSonarInstanceNameInput()).toMatch('sonarInstanceName');
        sonarInstanceDialogPage.setSonarInstanceDescriptionInput('sonarInstanceDescription');
        expect(sonarInstanceDialogPage.getSonarInstanceDescriptionInput()).toMatch('sonarInstanceDescription');
        sonarInstanceDialogPage.setSonarInstanceRunnerPathInput('sonarInstanceRunnerPath');
        expect(sonarInstanceDialogPage.getSonarInstanceRunnerPathInput()).toMatch('sonarInstanceRunnerPath');
        sonarInstanceDialogPage.getSonarInstanceEnabledInput().isSelected().then(function (selected) {
            if (selected) {
                sonarInstanceDialogPage.getSonarInstanceEnabledInput().click();
                expect(sonarInstanceDialogPage.getSonarInstanceEnabledInput().isSelected()).toBeFalsy();
            } else {
                sonarInstanceDialogPage.getSonarInstanceEnabledInput().click();
                expect(sonarInstanceDialogPage.getSonarInstanceEnabledInput().isSelected()).toBeTruthy();
            }
        });
        sonarInstanceDialogPage.save();
        expect(sonarInstanceDialogPage.getSaveButton().isPresent()).toBeFalsy();
    }); 

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class SonarInstanceComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-sonar-instance div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class SonarInstanceDialogPage {
    modalTitle = element(by.css('h4#mySonarInstanceLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    sonarInstanceNameInput = element(by.css('input#field_sonarInstanceName'));
    sonarInstanceDescriptionInput = element(by.css('input#field_sonarInstanceDescription'));
    sonarInstanceRunnerPathInput = element(by.css('input#field_sonarInstanceRunnerPath'));
    sonarInstanceEnabledInput = element(by.css('input#field_sonarInstanceEnabled'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setSonarInstanceNameInput = function (sonarInstanceName) {
        this.sonarInstanceNameInput.sendKeys(sonarInstanceName);
    }

    getSonarInstanceNameInput = function () {
        return this.sonarInstanceNameInput.getAttribute('value');
    }

    setSonarInstanceDescriptionInput = function (sonarInstanceDescription) {
        this.sonarInstanceDescriptionInput.sendKeys(sonarInstanceDescription);
    }

    getSonarInstanceDescriptionInput = function () {
        return this.sonarInstanceDescriptionInput.getAttribute('value');
    }

    setSonarInstanceRunnerPathInput = function (sonarInstanceRunnerPath) {
        this.sonarInstanceRunnerPathInput.sendKeys(sonarInstanceRunnerPath);
    }

    getSonarInstanceRunnerPathInput = function () {
        return this.sonarInstanceRunnerPathInput.getAttribute('value');
    }

    getSonarInstanceEnabledInput = function () {
        return this.sonarInstanceEnabledInput;
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
