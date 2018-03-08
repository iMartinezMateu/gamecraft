/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { GamecraftgatewayTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { GroupsDetailComponent } from '../../../../../../main/webapp/app/entities/groups/groups-detail.component';
import { GroupsService } from '../../../../../../main/webapp/app/entities/groups/groups.service';
import { Groups } from '../../../../../../main/webapp/app/entities/groups/groups.model';

describe('Component Tests', () => {

    describe('Groups Management Detail Component', () => {
        let comp: GroupsDetailComponent;
        let fixture: ComponentFixture<GroupsDetailComponent>;
        let service: GroupsService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GamecraftgatewayTestModule],
                declarations: [GroupsDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    GroupsService,
                    JhiEventManager
                ]
            }).overrideTemplate(GroupsDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(GroupsDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(GroupsService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Groups(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.groups).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
