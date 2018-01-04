/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { GamecraftgatewayTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { IrcBotDetailComponent } from '../../../../../../main/webapp/app/entities/irc-bot/irc-bot-detail.component';
import { IrcBotService } from '../../../../../../main/webapp/app/entities/irc-bot/irc-bot.service';
import { IrcBot } from '../../../../../../main/webapp/app/entities/irc-bot/irc-bot.model';

describe('Component Tests', () => {

    describe('IrcBot Management Detail Component', () => {
        let comp: IrcBotDetailComponent;
        let fixture: ComponentFixture<IrcBotDetailComponent>;
        let service: IrcBotService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GamecraftgatewayTestModule],
                declarations: [IrcBotDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    IrcBotService,
                    JhiEventManager
                ]
            }).overrideTemplate(IrcBotDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(IrcBotDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(IrcBotService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new IrcBot(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.ircBot).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
