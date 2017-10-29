import { browser, element, by, $ } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';
const path = require('path');

describe('TeamProject e2e test', () => {

    let navBarPage: NavBarPage;
    let teamProjectDialogPage: TeamProjectDialogPage;
    let teamProjectComponentsPage: TeamProjectComponentsPage;
    const fileToUpload = '../../../../main/webapp/content/images/logo-gamecraft.png';
    const absolutePath = path.resolve(__dirname, fileToUpload);
    

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load TeamProjects', () => {
        navBarPage.goToEntity('team-project');
        teamProjectComponentsPage = new TeamProjectComponentsPage();
        expect(teamProjectComponentsPage.getTitle()).toMatch(/gamecraftgatewayApp.teamProject.home.title/);

    });

    it('should load create TeamProject dialog', () => {
        teamProjectComponentsPage.clickOnCreateButton();
        teamProjectDialogPage = new TeamProjectDialogPage();
        expect(teamProjectDialogPage.getModalTitle()).toMatch(/gamecraftgatewayApp.teamProject.home.createOrEditLabel/);
        teamProjectDialogPage.close();
    });

    it('should create and save TeamProjects', () => {
        teamProjectComponentsPage.clickOnCreateButton();
        teamProjectDialogPage.setTeamIdInput('5');
        expect(teamProjectDialogPage.getTeamIdInput()).toMatch('5');
        teamProjectDialogPage.setProjectIdInput('5');
        expect(teamProjectDialogPage.getProjectIdInput()).toMatch('5');
        teamProjectDialogPage.save();
        expect(teamProjectDialogPage.getSaveButton().isPresent()).toBeFalsy();
    }); 

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class TeamProjectComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-team-project div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class TeamProjectDialogPage {
    modalTitle = element(by.css('h4#myTeamProjectLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    teamIdInput = element(by.css('input#field_teamId'));
    projectIdInput = element(by.css('input#field_projectId'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setTeamIdInput = function (teamId) {
        this.teamIdInput.sendKeys(teamId);
    }

    getTeamIdInput = function () {
        return this.teamIdInput.getAttribute('value');
    }

    setProjectIdInput = function (projectId) {
        this.projectIdInput.sendKeys(projectId);
    }

    getProjectIdInput = function () {
        return this.projectIdInput.getAttribute('value');
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
