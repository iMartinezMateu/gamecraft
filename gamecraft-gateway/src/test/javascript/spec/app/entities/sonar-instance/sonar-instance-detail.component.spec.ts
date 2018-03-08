/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { GamecraftgatewayTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { SonarInstanceDetailComponent } from '../../../../../../main/webapp/app/entities/sonar-instance/sonar-instance-detail.component';
import { SonarInstanceService } from '../../../../../../main/webapp/app/entities/sonar-instance/sonar-instance.service';
import { SonarInstance } from '../../../../../../main/webapp/app/entities/sonar-instance/sonar-instance.model';

describe('Component Tests', () => {

    describe('SonarInstance Management Detail Component', () => {
        let comp: SonarInstanceDetailComponent;
        let fixture: ComponentFixture<SonarInstanceDetailComponent>;
        let service: SonarInstanceService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GamecraftgatewayTestModule],
                declarations: [SonarInstanceDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    SonarInstanceService,
                    JhiEventManager
                ]
            }).overrideTemplate(SonarInstanceDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(SonarInstanceDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SonarInstanceService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new SonarInstance(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.sonarInstance).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
