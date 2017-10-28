import { browser, element, by, $ } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';
const path = require('path');

describe('Project e2e test', () => {

    let navBarPage: NavBarPage;
    let projectDialogPage: ProjectDialogPage;
    let projectComponentsPage: ProjectComponentsPage;
    const fileToUpload = '../../../../main/webapp/content/images/logo-gamecraft.png';
    const absolutePath = path.resolve(__dirname, fileToUpload);
    

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load Projects', () => {
        navBarPage.goToEntity('project');
        projectComponentsPage = new ProjectComponentsPage();
        expect(projectComponentsPage.getTitle()).toMatch(/gamecraftgatewayApp.project.home.title/);

    });

    it('should load create Project dialog', () => {
        projectComponentsPage.clickOnCreateButton();
        projectDialogPage = new ProjectDialogPage();
        expect(projectDialogPage.getModalTitle()).toMatch(/gamecraftgatewayApp.project.home.createOrEditLabel/);
        projectDialogPage.close();
    });

    it('should create and save Projects', () => {
        projectComponentsPage.clickOnCreateButton();
        projectDialogPage.setProjectNameInput('projectName');
        expect(projectDialogPage.getProjectNameInput()).toMatch('projectName');
        projectDialogPage.setProjectDescriptionInput('projectDescription');
        expect(projectDialogPage.getProjectDescriptionInput()).toMatch('projectDescription');
        projectDialogPage.setProjectWebsiteInput('projectWebsite');
        expect(projectDialogPage.getProjectWebsiteInput()).toMatch('projectWebsite');
        projectDialogPage.setProjectLogoInput('projectLogo');
        expect(projectDialogPage.getProjectLogoInput()).toMatch('projectLogo');
        projectDialogPage.getProjectArchivedInput().isSelected().then(function (selected) {
            if (selected) {
                projectDialogPage.getProjectArchivedInput().click();
                expect(projectDialogPage.getProjectArchivedInput().isSelected()).toBeFalsy();
            } else {
                projectDialogPage.getProjectArchivedInput().click();
                expect(projectDialogPage.getProjectArchivedInput().isSelected()).toBeTruthy();
            }
        });
        projectDialogPage.save();
        expect(projectDialogPage.getSaveButton().isPresent()).toBeFalsy();
    }); 

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class ProjectComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-project div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class ProjectDialogPage {
    modalTitle = element(by.css('h4#myProjectLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    projectNameInput = element(by.css('input#field_projectName'));
    projectDescriptionInput = element(by.css('input#field_projectDescription'));
    projectWebsiteInput = element(by.css('input#field_projectWebsite'));
    projectLogoInput = element(by.css('input#field_projectLogo'));
    projectArchivedInput = element(by.css('input#field_projectArchived'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setProjectNameInput = function (projectName) {
        this.projectNameInput.sendKeys(projectName);
    }

    getProjectNameInput = function () {
        return this.projectNameInput.getAttribute('value');
    }

    setProjectDescriptionInput = function (projectDescription) {
        this.projectDescriptionInput.sendKeys(projectDescription);
    }

    getProjectDescriptionInput = function () {
        return this.projectDescriptionInput.getAttribute('value');
    }

    setProjectWebsiteInput = function (projectWebsite) {
        this.projectWebsiteInput.sendKeys(projectWebsite);
    }

    getProjectWebsiteInput = function () {
        return this.projectWebsiteInput.getAttribute('value');
    }

    setProjectLogoInput = function (projectLogo) {
        this.projectLogoInput.sendKeys(projectLogo);
    }

    getProjectLogoInput = function () {
        return this.projectLogoInput.getAttribute('value');
    }

    getProjectArchivedInput = function () {
        return this.projectArchivedInput;
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
