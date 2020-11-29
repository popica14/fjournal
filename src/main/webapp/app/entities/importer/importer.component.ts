import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiDataUtils } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IImporter } from 'app/shared/model/importer.model';
import { ImporterService } from './importer.service';
import { ImporterDeleteDialogComponent } from './importer-delete-dialog.component';

@Component({
  selector: 'jhi-importer',
  templateUrl: './importer.component.html',
})
export class ImporterComponent implements OnInit, OnDestroy {
  importers?: IImporter[];
  eventSubscriber?: Subscription;

  constructor(
    protected importerService: ImporterService,
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal
  ) {}

  loadAll(): void {
    this.importerService.query().subscribe((res: HttpResponse<IImporter[]>) => (this.importers = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInImporters();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IImporter): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType = '', base64String: string): void {
    return this.dataUtils.openFile(contentType, base64String);
  }

  registerChangeInImporters(): void {
    this.eventSubscriber = this.eventManager.subscribe('importerListModification', () => this.loadAll());
  }

  delete(importer: IImporter): void {
    const modalRef = this.modalService.open(ImporterDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.importer = importer;
  }
}
