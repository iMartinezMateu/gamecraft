import { browser, element, by, $ } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';
const path = require('path');

describe('TeamUser e2e test', () => {

    let navBarPage: NavBarPage;
    let teamUserDialogPage: TeamUserDialogPage;
    let teamUserComponentsPage: TeamUserComponentsPage;
    const fileToUpload = '../../../../main/webapp/content/images/logo-gamecraft.png';
    const absolutePath = path.resolve(__dirname, fileToUpload);
    

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load TeamUsers', () => {
        navBarPage.goToEntity('team-user');
        teamUserComponentsPage = new TeamUserComponentsPage();
        expect(teamUserComponentsPage.getTitle()).toMatch(/gamecraftgatewayApp.teamUser.home.title/);

    });

    it('should load create TeamUser dialog', () => {
        teamUserComponentsPage.clickOnCreateButton();
        teamUserDialogPage = new TeamUserDialogPage();
        expect(teamUserDialogPage.getModalTitle()).toMatch(/gamecraftgatewayApp.teamUser.home.createOrEditLabel/);
        teamUserDialogPage.close();
    });

    it('should create and save TeamUsers', () => {
        teamUserComponentsPage.clickOnCreateButton();
        teamUserDialogPage.setUserIdInput('5');
        expect(teamUserDialogPage.getUserIdInput()).toMatch('5');
        teamUserDialogPage.setTeamIdInput('5');
        expect(teamUserDialogPage.getTeamIdInput()).toMatch('5');
        teamUserDialogPage.save();
        expect(teamUserDialogPage.getSaveButton().isPresent()).toBeFalsy();
    }); 

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class TeamUserComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-team-user div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class TeamUserDialogPage {
    modalTitle = element(by.css('h4#myTeamUserLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    userIdInput = element(by.css('input#field_userId'));
    teamIdInput = element(by.css('input#field_teamId'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setUserIdInput = function (userId) {
        this.userIdInput.sendKeys(userId);
    }

    getUserIdInput = function () {
        return this.userIdInput.getAttribute('value');
    }

    setTeamIdInput = function (teamId) {
        this.teamIdInput.sendKeys(teamId);
    }

    getTeamIdInput = function () {
        return this.teamIdInput.getAttribute('value');
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
