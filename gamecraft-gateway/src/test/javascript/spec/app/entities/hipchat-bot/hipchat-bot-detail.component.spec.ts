/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { GamecraftgatewayTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { HipchatBotDetailComponent } from '../../../../../../main/webapp/app/entities/hipchat-bot/hipchat-bot-detail.component';
import { HipchatBotService } from '../../../../../../main/webapp/app/entities/hipchat-bot/hipchat-bot.service';
import { HipchatBot } from '../../../../../../main/webapp/app/entities/hipchat-bot/hipchat-bot.model';

describe('Component Tests', () => {

    describe('HipchatBot Management Detail Component', () => {
        let comp: HipchatBotDetailComponent;
        let fixture: ComponentFixture<HipchatBotDetailComponent>;
        let service: HipchatBotService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GamecraftgatewayTestModule],
                declarations: [HipchatBotDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    HipchatBotService,
                    JhiEventManager
                ]
            }).overrideTemplate(HipchatBotDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(HipchatBotDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(HipchatBotService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new HipchatBot(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.hipchatBot).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
