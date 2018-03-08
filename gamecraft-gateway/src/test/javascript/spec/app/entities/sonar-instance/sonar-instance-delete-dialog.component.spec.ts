/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { GamecraftgatewayTestModule } from '../../../test.module';
import { SonarInstanceDeleteDialogComponent } from '../../../../../../main/webapp/app/entities/sonar-instance/sonar-instance-delete-dialog.component';
import { SonarInstanceService } from '../../../../../../main/webapp/app/entities/sonar-instance/sonar-instance.service';

describe('Component Tests', () => {

    describe('SonarInstance Management Delete Component', () => {
        let comp: SonarInstanceDeleteDialogComponent;
        let fixture: ComponentFixture<SonarInstanceDeleteDialogComponent>;
        let service: SonarInstanceService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GamecraftgatewayTestModule],
                declarations: [SonarInstanceDeleteDialogComponent],
                providers: [
                    SonarInstanceService
                ]
            })
            .overrideTemplate(SonarInstanceDeleteDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(SonarInstanceDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SonarInstanceService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        spyOn(service, 'delete').and.returnValue(Observable.of({}));

                        // WHEN
                        comp.confirmDelete(123);
                        tick();

                        // THEN
                        expect(service.delete).toHaveBeenCalledWith(123);
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
