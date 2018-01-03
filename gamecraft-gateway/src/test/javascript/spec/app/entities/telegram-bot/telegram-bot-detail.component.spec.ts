/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { GamecraftgatewayTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { TelegramBotDetailComponent } from '../../../../../../main/webapp/app/entities/telegram-bot/telegram-bot-detail.component';
import { TelegramBotService } from '../../../../../../main/webapp/app/entities/telegram-bot/telegram-bot.service';
import { TelegramBot } from '../../../../../../main/webapp/app/entities/telegram-bot/telegram-bot.model';

describe('Component Tests', () => {

    describe('TelegramBot Management Detail Component', () => {
        let comp: TelegramBotDetailComponent;
        let fixture: ComponentFixture<TelegramBotDetailComponent>;
        let service: TelegramBotService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GamecraftgatewayTestModule],
                declarations: [TelegramBotDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    TelegramBotService,
                    JhiEventManager
                ]
            }).overrideTemplate(TelegramBotDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(TelegramBotDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TelegramBotService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new TelegramBot(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.telegramBot).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
