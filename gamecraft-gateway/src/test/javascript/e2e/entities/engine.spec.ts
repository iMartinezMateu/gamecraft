import { browser, element, by, $ } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';
const path = require('path');

describe('Engine e2e test', () => {

    let navBarPage: NavBarPage;
    let engineDialogPage: EngineDialogPage;
    let engineComponentsPage: EngineComponentsPage;
    const fileToUpload = '../../../../main/webapp/content/images/logo-gamecraft.png';
    const absolutePath = path.resolve(__dirname, fileToUpload);
    

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load Engines', () => {
        navBarPage.goToEntity('engine');
        engineComponentsPage = new EngineComponentsPage();
        expect(engineComponentsPage.getTitle()).toMatch(/gamecraftgatewayApp.engine.home.title/);

    });

    it('should load create Engine dialog', () => {
        engineComponentsPage.clickOnCreateButton();
        engineDialogPage = new EngineDialogPage();
        expect(engineDialogPage.getModalTitle()).toMatch(/gamecraftgatewayApp.engine.home.createOrEditLabel/);
        engineDialogPage.close();
    });

    it('should create and save Engines', () => {
        engineComponentsPage.clickOnCreateButton();
        engineDialogPage.setEngineNameInput('engineName');
        expect(engineDialogPage.getEngineNameInput()).toMatch('engineName');
        engineDialogPage.setEngineDescriptionInput('engineDescription');
        expect(engineDialogPage.getEngineDescriptionInput()).toMatch('engineDescription');
        engineDialogPage.setEngineCompilerPathInput('engineCompilerPath');
        expect(engineDialogPage.getEngineCompilerPathInput()).toMatch('engineCompilerPath');
        engineDialogPage.setEngineCompilerArgumentsInput('engineCompilerArguments');
        expect(engineDialogPage.getEngineCompilerArgumentsInput()).toMatch('engineCompilerArguments');
        engineDialogPage.save();
        expect(engineDialogPage.getSaveButton().isPresent()).toBeFalsy();
    }); 

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class EngineComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-engine div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class EngineDialogPage {
    modalTitle = element(by.css('h4#myEngineLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    engineNameInput = element(by.css('input#field_engineName'));
    engineDescriptionInput = element(by.css('input#field_engineDescription'));
    engineCompilerPathInput = element(by.css('input#field_engineCompilerPath'));
    engineCompilerArgumentsInput = element(by.css('input#field_engineCompilerArguments'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setEngineNameInput = function (engineName) {
        this.engineNameInput.sendKeys(engineName);
    }

    getEngineNameInput = function () {
        return this.engineNameInput.getAttribute('value');
    }

    setEngineDescriptionInput = function (engineDescription) {
        this.engineDescriptionInput.sendKeys(engineDescription);
    }

    getEngineDescriptionInput = function () {
        return this.engineDescriptionInput.getAttribute('value');
    }

    setEngineCompilerPathInput = function (engineCompilerPath) {
        this.engineCompilerPathInput.sendKeys(engineCompilerPath);
    }

    getEngineCompilerPathInput = function () {
        return this.engineCompilerPathInput.getAttribute('value');
    }

    setEngineCompilerArgumentsInput = function (engineCompilerArguments) {
        this.engineCompilerArgumentsInput.sendKeys(engineCompilerArguments);
    }

    getEngineCompilerArgumentsInput = function () {
        return this.engineCompilerArgumentsInput.getAttribute('value');
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
