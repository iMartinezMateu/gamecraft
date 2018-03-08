/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { GamecraftgatewayTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { GroupsUserDetailComponent } from '../../../../../../main/webapp/app/entities/groups-user/groups-user-detail.component';
import { GroupsUserService } from '../../../../../../main/webapp/app/entities/groups-user/groups-user.service';
import { GroupsUser } from '../../../../../../main/webapp/app/entities/groups-user/groups-user.model';

describe('Component Tests', () => {

    describe('GroupsUser Management Detail Component', () => {
        let comp: GroupsUserDetailComponent;
        let fixture: ComponentFixture<GroupsUserDetailComponent>;
        let service: GroupsUserService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GamecraftgatewayTestModule],
                declarations: [GroupsUserDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    GroupsUserService,
                    JhiEventManager
                ]
            }).overrideTemplate(GroupsUserDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(GroupsUserDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(GroupsUserService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new GroupsUser(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.groupsUser).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
