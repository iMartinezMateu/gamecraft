/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { GamecraftgatewayTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { TeamUserDetailComponent } from '../../../../../../main/webapp/app/entities/team-user/team-user-detail.component';
import { TeamUserService } from '../../../../../../main/webapp/app/entities/team-user/team-user.service';
import { TeamUser } from '../../../../../../main/webapp/app/entities/team-user/team-user.model';

describe('Component Tests', () => {

    describe('TeamUser Management Detail Component', () => {
        let comp: TeamUserDetailComponent;
        let fixture: ComponentFixture<TeamUserDetailComponent>;
        let service: TeamUserService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GamecraftgatewayTestModule],
                declarations: [TeamUserDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    TeamUserService,
                    JhiEventManager
                ]
            }).overrideTemplate(TeamUserDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(TeamUserDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TeamUserService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new TeamUser(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.teamUser).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
