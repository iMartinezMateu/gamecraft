import { browser, element, by, $ } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';
const path = require('path');

describe('Team e2e test', () => {

    let navBarPage: NavBarPage;
    let teamDialogPage: TeamDialogPage;
    let teamComponentsPage: TeamComponentsPage;
    const fileToUpload = '../../../../main/webapp/content/images/logo-gamecraft.png';
    const absolutePath = path.resolve(__dirname, fileToUpload);
    

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load Teams', () => {
        navBarPage.goToEntity('team');
        teamComponentsPage = new TeamComponentsPage();
        expect(teamComponentsPage.getTitle()).toMatch(/gamecraftgatewayApp.team.home.title/);

    });

    it('should load create Team dialog', () => {
        teamComponentsPage.clickOnCreateButton();
        teamDialogPage = new TeamDialogPage();
        expect(teamDialogPage.getModalTitle()).toMatch(/gamecraftgatewayApp.team.home.createOrEditLabel/);
        teamDialogPage.close();
    });

    it('should create and save Teams', () => {
        teamComponentsPage.clickOnCreateButton();
        teamDialogPage.setTeamNameInput('teamName');
        expect(teamDialogPage.getTeamNameInput()).toMatch('teamName');
        teamDialogPage.setTeamDescriptionInput('teamDescription');
        expect(teamDialogPage.getTeamDescriptionInput()).toMatch('teamDescription');
        // teamDialogPage.teamUserSelectLastOption();
        teamDialogPage.save();
        expect(teamDialogPage.getSaveButton().isPresent()).toBeFalsy();
    }); 

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class TeamComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-team div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class TeamDialogPage {
    modalTitle = element(by.css('h4#myTeamLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    teamNameInput = element(by.css('input#field_teamName'));
    teamDescriptionInput = element(by.css('input#field_teamDescription'));
    teamUserSelect = element(by.css('select#field_teamUser'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setTeamNameInput = function (teamName) {
        this.teamNameInput.sendKeys(teamName);
    }

    getTeamNameInput = function () {
        return this.teamNameInput.getAttribute('value');
    }

    setTeamDescriptionInput = function (teamDescription) {
        this.teamDescriptionInput.sendKeys(teamDescription);
    }

    getTeamDescriptionInput = function () {
        return this.teamDescriptionInput.getAttribute('value');
    }

    teamUserSelectLastOption = function () {
        this.teamUserSelect.all(by.tagName('option')).last().click();
    }

    teamUserSelectOption = function (option) {
        this.teamUserSelect.sendKeys(option);
    }

    getTeamUserSelect = function () {
        return this.teamUserSelect;
    }

    getTeamUserSelectedOption = function () {
        return this.teamUserSelect.element(by.css('option:checked')).getText();
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
