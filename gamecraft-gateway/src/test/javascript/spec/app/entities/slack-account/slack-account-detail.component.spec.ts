/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { GamecraftgatewayTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { SlackAccountDetailComponent } from '../../../../../../main/webapp/app/entities/slack-account/slack-account-detail.component';
import { SlackAccountService } from '../../../../../../main/webapp/app/entities/slack-account/slack-account.service';
import { SlackAccount } from '../../../../../../main/webapp/app/entities/slack-account/slack-account.model';

describe('Component Tests', () => {

    describe('SlackAccount Management Detail Component', () => {
        let comp: SlackAccountDetailComponent;
        let fixture: ComponentFixture<SlackAccountDetailComponent>;
        let service: SlackAccountService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GamecraftgatewayTestModule],
                declarations: [SlackAccountDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    SlackAccountService,
                    JhiEventManager
                ]
            }).overrideTemplate(SlackAccountDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(SlackAccountDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SlackAccountService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new SlackAccount(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.slackAccount).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
