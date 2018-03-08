import { browser, element, by, $ } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';
const path = require('path');

describe('GroupsUser e2e test', () => {

    let navBarPage: NavBarPage;
    let groupsUserDialogPage: GroupsUserDialogPage;
    let groupsUserComponentsPage: GroupsUserComponentsPage;
    const fileToUpload = '../../../../main/webapp/content/images/logo-jhipster.png';
    const absolutePath = path.resolve(__dirname, fileToUpload);
    

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load GroupsUsers', () => {
        navBarPage.goToEntity('groups-user');
        groupsUserComponentsPage = new GroupsUserComponentsPage();
        expect(groupsUserComponentsPage.getTitle()).toMatch(/gamecraftgatewayApp.groupsUser.home.title/);

    });

    it('should load create GroupsUser dialog', () => {
        groupsUserComponentsPage.clickOnCreateButton();
        groupsUserDialogPage = new GroupsUserDialogPage();
        expect(groupsUserDialogPage.getModalTitle()).toMatch(/gamecraftgatewayApp.groupsUser.home.createOrEditLabel/);
        groupsUserDialogPage.close();
    });

    it('should create and save GroupsUsers', () => {
        groupsUserComponentsPage.clickOnCreateButton();
        groupsUserDialogPage.setGroupIdInput('5');
        expect(groupsUserDialogPage.getGroupIdInput()).toMatch('5');
        groupsUserDialogPage.setUserIdInput('5');
        expect(groupsUserDialogPage.getUserIdInput()).toMatch('5');
        groupsUserDialogPage.save();
        expect(groupsUserDialogPage.getSaveButton().isPresent()).toBeFalsy();
    }); 

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class GroupsUserComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-groups-user div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class GroupsUserDialogPage {
    modalTitle = element(by.css('h4#myGroupsUserLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    groupIdInput = element(by.css('input#field_groupId'));
    userIdInput = element(by.css('input#field_userId'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setGroupIdInput = function (groupId) {
        this.groupIdInput.sendKeys(groupId);
    }

    getGroupIdInput = function () {
        return this.groupIdInput.getAttribute('value');
    }

    setUserIdInput = function (userId) {
        this.userIdInput.sendKeys(userId);
    }

    getUserIdInput = function () {
        return this.userIdInput.getAttribute('value');
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
