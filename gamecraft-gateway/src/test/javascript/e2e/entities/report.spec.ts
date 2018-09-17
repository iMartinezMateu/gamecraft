import { browser, element, by, $ } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';
const path = require('path');

describe('Report e2e test', () => {

    let navBarPage: NavBarPage;
    let reportDialogPage: ReportDialogPage;
    let reportComponentsPage: ReportComponentsPage;
    const fileToUpload = '../../../../main/webapp/content/images/logo-jhipster.png';
    const absolutePath = path.resolve(__dirname, fileToUpload);
    

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load Reports', () => {
        navBarPage.goToEntity('report');
        reportComponentsPage = new ReportComponentsPage();
        expect(reportComponentsPage.getTitle()).toMatch(/gamecraftgatewayApp.report.home.title/);

    });

    it('should load create Report dialog', () => {
        reportComponentsPage.clickOnCreateButton();
        reportDialogPage = new ReportDialogPage();
        expect(reportDialogPage.getModalTitle()).toMatch(/gamecraftgatewayApp.report.home.createOrEditLabel/);
        reportDialogPage.close();
    });

    it('should create and save Reports', () => {
        reportComponentsPage.clickOnCreateButton();
        reportDialogPage.setPipelineIdInput('5');
        expect(reportDialogPage.getPipelineIdInput()).toMatch('5');
        reportDialogPage.setReportDateInput('2000-12-31');
        expect(reportDialogPage.getReportDateInput()).toMatch('2000-12-31');
        reportDialogPage.setReportContentInput('reportContent');
        expect(reportDialogPage.getReportContentInput()).toMatch('reportContent');
        reportDialogPage.save();
        expect(reportDialogPage.getSaveButton().isPresent()).toBeFalsy();
    }); 

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class ReportComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-report div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class ReportDialogPage {
    modalTitle = element(by.css('h4#myReportLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    pipelineIdInput = element(by.css('input#field_pipelineId'));
    reportDateInput = element(by.css('input#field_reportDate'));
    reportContentInput = element(by.css('input#field_reportContent'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setPipelineIdInput = function (pipelineId) {
        this.pipelineIdInput.sendKeys(pipelineId);
    }

    getPipelineIdInput = function () {
        return this.pipelineIdInput.getAttribute('value');
    }

    setReportDateInput = function (reportDate) {
        this.reportDateInput.sendKeys(reportDate);
    }

    getReportDateInput = function () {
        return this.reportDateInput.getAttribute('value');
    }

    setReportContentInput = function (reportContent) {
        this.reportContentInput.sendKeys(reportContent);
    }

    getReportContentInput = function () {
        return this.reportContentInput.getAttribute('value');
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
