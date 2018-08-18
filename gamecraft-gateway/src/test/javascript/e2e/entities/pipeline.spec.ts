import { browser, element, by, $ } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';
const path = require('path');

describe('Pipeline e2e test', () => {

    let navBarPage: NavBarPage;
    let pipelineDialogPage: PipelineDialogPage;
    let pipelineComponentsPage: PipelineComponentsPage;
    const fileToUpload = '../../../../main/webapp/content/images/logo-jhipster.png';
    const absolutePath = path.resolve(__dirname, fileToUpload);
    

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load Pipelines', () => {
        navBarPage.goToEntity('pipeline');
        pipelineComponentsPage = new PipelineComponentsPage();
        expect(pipelineComponentsPage.getTitle()).toMatch(/gamecraftgatewayApp.pipeline.home.title/);

    });

    it('should load create Pipeline dialog', () => {
        pipelineComponentsPage.clickOnCreateButton();
        pipelineDialogPage = new PipelineDialogPage();
        expect(pipelineDialogPage.getModalTitle()).toMatch(/gamecraftgatewayApp.pipeline.home.createOrEditLabel/);
        pipelineDialogPage.close();
    });

    it('should create and save Pipelines', () => {
        pipelineComponentsPage.clickOnCreateButton();
        pipelineDialogPage.setPipelineNameInput('pipelineName');
        expect(pipelineDialogPage.getPipelineNameInput()).toMatch('pipelineName');
        pipelineDialogPage.setPipelineDescriptionInput('pipelineDescription');
        expect(pipelineDialogPage.getPipelineDescriptionInput()).toMatch('pipelineDescription');
        pipelineDialogPage.pipelineRepositoryTypeSelectLastOption();
        pipelineDialogPage.setPipelineRepositoryAddressInput('pipelineRepositoryAddress');
        expect(pipelineDialogPage.getPipelineRepositoryAddressInput()).toMatch('pipelineRepositoryAddress');
        pipelineDialogPage.setPipelineRepositoryUsernameInput('pipelineRepositoryUsername');
        expect(pipelineDialogPage.getPipelineRepositoryUsernameInput()).toMatch('pipelineRepositoryUsername');
        pipelineDialogPage.setPipelineRepositoryPasswordInput('pipelineRepositoryPassword');
        expect(pipelineDialogPage.getPipelineRepositoryPasswordInput()).toMatch('pipelineRepositoryPassword');
        pipelineDialogPage.setPipelineRepositoryBranchInput('pipelineRepositoryBranch');
        expect(pipelineDialogPage.getPipelineRepositoryBranchInput()).toMatch('pipelineRepositoryBranch');
        pipelineDialogPage.setPipelineEngineIdInput('5');
        expect(pipelineDialogPage.getPipelineEngineIdInput()).toMatch('5');
        pipelineDialogPage.setPipelineNotificatorIdInput('5');
        expect(pipelineDialogPage.getPipelineNotificatorIdInput()).toMatch('5');
        pipelineDialogPage.setPipelineFtpAddressInput('pipelineFtpAddress');
        expect(pipelineDialogPage.getPipelineFtpAddressInput()).toMatch('pipelineFtpAddress');
        pipelineDialogPage.setPipelineFtpUsernameInput('pipelineFtpUsername');
        expect(pipelineDialogPage.getPipelineFtpUsernameInput()).toMatch('pipelineFtpUsername');
        pipelineDialogPage.setPipelineFtpPasswordInput('pipelineFtpPassword');
        expect(pipelineDialogPage.getPipelineFtpPasswordInput()).toMatch('pipelineFtpPassword');
        pipelineDialogPage.pipelineScheduleSelectLastOption();
        pipelineDialogPage.setPipelineScheduleDetailsInput('pipelineScheduleDetails');
        expect(pipelineDialogPage.getPipelineScheduleDetailsInput()).toMatch('pipelineScheduleDetails');
        pipelineDialogPage.save();
        expect(pipelineDialogPage.getSaveButton().isPresent()).toBeFalsy();
    }); 

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class PipelineComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-pipeline div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class PipelineDialogPage {
    modalTitle = element(by.css('h4#myPipelineLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    pipelineNameInput = element(by.css('input#field_pipelineName'));
    pipelineDescriptionInput = element(by.css('input#field_pipelineDescription'));
    pipelineRepositoryTypeSelect = element(by.css('select#field_pipelineRepositoryType'));
    pipelineRepositoryAddressInput = element(by.css('input#field_pipelineRepositoryAddress'));
    pipelineRepositoryUsernameInput = element(by.css('input#field_pipelineRepositoryUsername'));
    pipelineRepositoryPasswordInput = element(by.css('input#field_pipelineRepositoryPassword'));
    pipelineRepositoryBranchInput = element(by.css('input#field_pipelineRepositoryBranch'));
    pipelineEngineIdInput = element(by.css('input#field_pipelineEngineId'));
    pipelineNotificatorIdInput = element(by.css('input#field_pipelineNotificatorId'));
    pipelineFtpAddressInput = element(by.css('input#field_pipelineFtpAddress'));
    pipelineFtpUsernameInput = element(by.css('input#field_pipelineFtpUsername'));
    pipelineFtpPasswordInput = element(by.css('input#field_pipelineFtpPassword'));
    pipelineScheduleSelect = element(by.css('select#field_pipelineSchedule'));
    pipelineScheduleDetailsInput = element(by.css('input#field_pipelineScheduleDetails'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setPipelineNameInput = function (pipelineName) {
        this.pipelineNameInput.sendKeys(pipelineName);
    }

    getPipelineNameInput = function () {
        return this.pipelineNameInput.getAttribute('value');
    }

    setPipelineDescriptionInput = function (pipelineDescription) {
        this.pipelineDescriptionInput.sendKeys(pipelineDescription);
    }

    getPipelineDescriptionInput = function () {
        return this.pipelineDescriptionInput.getAttribute('value');
    }

    setPipelineRepositoryTypeSelect = function (pipelineRepositoryType) {
        this.pipelineRepositoryTypeSelect.sendKeys(pipelineRepositoryType);
    }

    getPipelineRepositoryTypeSelect = function () {
        return this.pipelineRepositoryTypeSelect.element(by.css('option:checked')).getText();
    }

    pipelineRepositoryTypeSelectLastOption = function () {
        this.pipelineRepositoryTypeSelect.all(by.tagName('option')).last().click();
    }
    setPipelineRepositoryAddressInput = function (pipelineRepositoryAddress) {
        this.pipelineRepositoryAddressInput.sendKeys(pipelineRepositoryAddress);
    }

    getPipelineRepositoryAddressInput = function () {
        return this.pipelineRepositoryAddressInput.getAttribute('value');
    }

    setPipelineRepositoryUsernameInput = function (pipelineRepositoryUsername) {
        this.pipelineRepositoryUsernameInput.sendKeys(pipelineRepositoryUsername);
    }

    getPipelineRepositoryUsernameInput = function () {
        return this.pipelineRepositoryUsernameInput.getAttribute('value');
    }

    setPipelineRepositoryPasswordInput = function (pipelineRepositoryPassword) {
        this.pipelineRepositoryPasswordInput.sendKeys(pipelineRepositoryPassword);
    }

    getPipelineRepositoryPasswordInput = function () {
        return this.pipelineRepositoryPasswordInput.getAttribute('value');
    }

    setPipelineRepositoryBranchInput = function (pipelineRepositoryBranch) {
        this.pipelineRepositoryBranchInput.sendKeys(pipelineRepositoryBranch);
    }

    getPipelineRepositoryBranchInput = function () {
        return this.pipelineRepositoryBranchInput.getAttribute('value');
    }

    setPipelineEngineIdInput = function (pipelineEngineId) {
        this.pipelineEngineIdInput.sendKeys(pipelineEngineId);
    }

    getPipelineEngineIdInput = function () {
        return this.pipelineEngineIdInput.getAttribute('value');
    }

    setPipelineNotificatorIdInput = function (pipelineNotificatorId) {
        this.pipelineNotificatorIdInput.sendKeys(pipelineNotificatorId);
    }

    getPipelineNotificatorIdInput = function () {
        return this.pipelineNotificatorIdInput.getAttribute('value');
    }

    setPipelineFtpAddressInput = function (pipelineFtpAddress) {
        this.pipelineFtpAddressInput.sendKeys(pipelineFtpAddress);
    }

    getPipelineFtpAddressInput = function () {
        return this.pipelineFtpAddressInput.getAttribute('value');
    }

    setPipelineFtpUsernameInput = function (pipelineFtpUsername) {
        this.pipelineFtpUsernameInput.sendKeys(pipelineFtpUsername);
    }

    getPipelineFtpUsernameInput = function () {
        return this.pipelineFtpUsernameInput.getAttribute('value');
    }

    setPipelineFtpPasswordInput = function (pipelineFtpPassword) {
        this.pipelineFtpPasswordInput.sendKeys(pipelineFtpPassword);
    }

    getPipelineFtpPasswordInput = function () {
        return this.pipelineFtpPasswordInput.getAttribute('value');
    }

    setPipelineScheduleSelect = function (pipelineSchedule) {
        this.pipelineScheduleSelect.sendKeys(pipelineSchedule);
    }

    getPipelineScheduleSelect = function () {
        return this.pipelineScheduleSelect.element(by.css('option:checked')).getText();
    }

    pipelineScheduleSelectLastOption = function () {
        this.pipelineScheduleSelect.all(by.tagName('option')).last().click();
    }
    setPipelineScheduleDetailsInput = function (pipelineScheduleDetails) {
        this.pipelineScheduleDetailsInput.sendKeys(pipelineScheduleDetails);
    }

    getPipelineScheduleDetailsInput = function () {
        return this.pipelineScheduleDetailsInput.getAttribute('value');
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
