/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { GamecraftgatewayTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { ReportDetailComponent } from '../../../../../../main/webapp/app/entities/report/report-detail.component';
import { ReportService } from '../../../../../../main/webapp/app/entities/report/report.service';
import { Report } from '../../../../../../main/webapp/app/entities/report/report.model';

describe('Component Tests', () => {

    describe('Report Management Detail Component', () => {
        let comp: ReportDetailComponent;
        let fixture: ComponentFixture<ReportDetailComponent>;
        let service: ReportService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GamecraftgatewayTestModule],
                declarations: [ReportDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    ReportService,
                    JhiEventManager
                ]
            }).overrideTemplate(ReportDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ReportDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ReportService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Report(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.report).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
