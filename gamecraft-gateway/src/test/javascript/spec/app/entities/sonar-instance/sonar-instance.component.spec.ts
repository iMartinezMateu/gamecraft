/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { GamecraftgatewayTestModule } from '../../../test.module';
import { SonarInstanceComponent } from '../../../../../../main/webapp/app/entities/sonar-instance/sonar-instance.component';
import { SonarInstanceService } from '../../../../../../main/webapp/app/entities/sonar-instance/sonar-instance.service';
import { SonarInstance } from '../../../../../../main/webapp/app/entities/sonar-instance/sonar-instance.model';

describe('Component Tests', () => {

    describe('SonarInstance Management Component', () => {
        let comp: SonarInstanceComponent;
        let fixture: ComponentFixture<SonarInstanceComponent>;
        let service: SonarInstanceService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GamecraftgatewayTestModule],
                declarations: [SonarInstanceComponent],
                providers: [
                    SonarInstanceService
                ]
            })
            .overrideTemplate(SonarInstanceComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(SonarInstanceComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SonarInstanceService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new SonarInstance(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.sonarInstances[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
