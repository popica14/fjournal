import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IImporter } from 'app/shared/model/importer.model';
import { ImporterService } from './importer.service';

@Component({
  templateUrl: './importer-delete-dialog.component.html',
})
export class ImporterDeleteDialogComponent {
  importer?: IImporter;

  constructor(protected importerService: ImporterService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.importerService.delete(id).subscribe(() => {
      this.eventManager.broadcast('importerListModification');
      this.activeModal.close();
    });
  }
}
