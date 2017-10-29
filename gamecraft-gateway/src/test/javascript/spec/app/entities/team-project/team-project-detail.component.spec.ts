/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { GamecraftgatewayTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { TeamProjectDetailComponent } from '../../../../../../main/webapp/app/entities/team-project/team-project-detail.component';
import { TeamProjectService } from '../../../../../../main/webapp/app/entities/team-project/team-project.service';
import { TeamProject } from '../../../../../../main/webapp/app/entities/team-project/team-project.model';

describe('Component Tests', () => {

    describe('TeamProject Management Detail Component', () => {
        let comp: TeamProjectDetailComponent;
        let fixture: ComponentFixture<TeamProjectDetailComponent>;
        let service: TeamProjectService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GamecraftgatewayTestModule],
                declarations: [TeamProjectDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    TeamProjectService,
                    JhiEventManager
                ]
            }).overrideTemplate(TeamProjectDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(TeamProjectDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TeamProjectService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new TeamProject(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.teamProject).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
