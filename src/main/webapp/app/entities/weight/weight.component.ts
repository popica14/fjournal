import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiDataUtils } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IWeight } from 'app/shared/model/weight.model';
import { WeightService } from './weight.service';
import { WeightDeleteDialogComponent } from './weight-delete-dialog.component';

@Component({
  selector: 'jhi-weight',
  templateUrl: './weight.component.html',
})
export class WeightComponent implements OnInit, OnDestroy {
  weights?: IWeight[];
  eventSubscriber?: Subscription;

  constructor(
    protected weightService: WeightService,
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal
  ) {}

  loadAll(): void {
    this.weightService.query().subscribe((res: HttpResponse<IWeight[]>) => (this.weights = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInWeights();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IWeight): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType = '', base64String: string): void {
    return this.dataUtils.openFile(contentType, base64String);
  }

  registerChangeInWeights(): void {
    this.eventSubscriber = this.eventManager.subscribe('weightListModification', () => this.loadAll());
  }

  delete(weight: IWeight): void {
    const modalRef = this.modalService.open(WeightDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.weight = weight;
  }
}
