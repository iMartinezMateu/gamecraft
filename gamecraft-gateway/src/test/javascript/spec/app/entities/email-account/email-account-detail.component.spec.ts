/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { GamecraftgatewayTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { EmailAccountDetailComponent } from '../../../../../../main/webapp/app/entities/email-account/email-account-detail.component';
import { EmailAccountService } from '../../../../../../main/webapp/app/entities/email-account/email-account.service';
import { EmailAccount } from '../../../../../../main/webapp/app/entities/email-account/email-account.model';

describe('Component Tests', () => {

    describe('EmailAccount Management Detail Component', () => {
        let comp: EmailAccountDetailComponent;
        let fixture: ComponentFixture<EmailAccountDetailComponent>;
        let service: EmailAccountService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GamecraftgatewayTestModule],
                declarations: [EmailAccountDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    EmailAccountService,
                    JhiEventManager
                ]
            }).overrideTemplate(EmailAccountDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(EmailAccountDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(EmailAccountService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new EmailAccount(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.emailAccount).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
