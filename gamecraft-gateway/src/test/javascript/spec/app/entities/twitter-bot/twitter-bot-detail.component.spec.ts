/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { GamecraftgatewayTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { TwitterBotDetailComponent } from '../../../../../../main/webapp/app/entities/twitter-bot/twitter-bot-detail.component';
import { TwitterBotService } from '../../../../../../main/webapp/app/entities/twitter-bot/twitter-bot.service';
import { TwitterBot } from '../../../../../../main/webapp/app/entities/twitter-bot/twitter-bot.model';

describe('Component Tests', () => {

    describe('TwitterBot Management Detail Component', () => {
        let comp: TwitterBotDetailComponent;
        let fixture: ComponentFixture<TwitterBotDetailComponent>;
        let service: TwitterBotService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GamecraftgatewayTestModule],
                declarations: [TwitterBotDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    TwitterBotService,
                    JhiEventManager
                ]
            }).overrideTemplate(TwitterBotDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(TwitterBotDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TwitterBotService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new TwitterBot(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.twitterBot).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
