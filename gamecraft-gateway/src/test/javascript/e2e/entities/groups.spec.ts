import { browser, element, by, $ } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';
const path = require('path');

describe('Groups e2e test', () => {

    let navBarPage: NavBarPage;
    let groupsDialogPage: GroupsDialogPage;
    let groupsComponentsPage: GroupsComponentsPage;
    const fileToUpload = '../../../../main/webapp/content/images/logo-jhipster.png';
    const absolutePath = path.resolve(__dirname, fileToUpload);
    

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load Groups', () => {
        navBarPage.goToEntity('groups');
        groupsComponentsPage = new GroupsComponentsPage();
        expect(groupsComponentsPage.getTitle()).toMatch(/gamecraftgatewayApp.groups.home.title/);

    });

    it('should load create Groups dialog', () => {
        groupsComponentsPage.clickOnCreateButton();
        groupsDialogPage = new GroupsDialogPage();
        expect(groupsDialogPage.getModalTitle()).toMatch(/gamecraftgatewayApp.groups.home.createOrEditLabel/);
        groupsDialogPage.close();
    });

    it('should create and save Groups', () => {
        groupsComponentsPage.clickOnCreateButton();
        groupsDialogPage.setGroupNameInput('groupName');
        expect(groupsDialogPage.getGroupNameInput()).toMatch('groupName');
        groupsDialogPage.setGroupDescriptionInput('groupDescription');
        expect(groupsDialogPage.getGroupDescriptionInput()).toMatch('groupDescription');
        groupsDialogPage.groupRoleSelectLastOption();
        groupsDialogPage.getGroupEnabledInput().isSelected().then(function (selected) {
            if (selected) {
                groupsDialogPage.getGroupEnabledInput().click();
                expect(groupsDialogPage.getGroupEnabledInput().isSelected()).toBeFalsy();
            } else {
                groupsDialogPage.getGroupEnabledInput().click();
                expect(groupsDialogPage.getGroupEnabledInput().isSelected()).toBeTruthy();
            }
        });
        groupsDialogPage.save();
        expect(groupsDialogPage.getSaveButton().isPresent()).toBeFalsy();
    }); 

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class GroupsComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-groups div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class GroupsDialogPage {
    modalTitle = element(by.css('h4#myGroupsLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    groupNameInput = element(by.css('input#field_groupName'));
    groupDescriptionInput = element(by.css('input#field_groupDescription'));
    groupRoleSelect = element(by.css('select#field_groupRole'));
    groupEnabledInput = element(by.css('input#field_groupEnabled'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setGroupNameInput = function (groupName) {
        this.groupNameInput.sendKeys(groupName);
    }

    getGroupNameInput = function () {
        return this.groupNameInput.getAttribute('value');
    }

    setGroupDescriptionInput = function (groupDescription) {
        this.groupDescriptionInput.sendKeys(groupDescription);
    }

    getGroupDescriptionInput = function () {
        return this.groupDescriptionInput.getAttribute('value');
    }

    setGroupRoleSelect = function (groupRole) {
        this.groupRoleSelect.sendKeys(groupRole);
    }

    getGroupRoleSelect = function () {
        return this.groupRoleSelect.element(by.css('option:checked')).getText();
    }

    groupRoleSelectLastOption = function () {
        this.groupRoleSelect.all(by.tagName('option')).last().click();
    }
    getGroupEnabledInput = function () {
        return this.groupEnabledInput;
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
