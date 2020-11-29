import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { FoodJournalSharedModule } from 'app/shared/shared.module';
import { ImporterComponent } from './importer.component';
import { ImporterDetailComponent } from './importer-detail.component';
import { ImporterUpdateComponent } from './importer-update.component';
import { ImporterDeleteDialogComponent } from './importer-delete-dialog.component';
import { importerRoute } from './importer.route';

@NgModule({
  imports: [FoodJournalSharedModule, RouterModule.forChild(importerRoute)],
  declarations: [ImporterComponent, ImporterDetailComponent, ImporterUpdateComponent, ImporterDeleteDialogComponent],
  entryComponents: [ImporterDeleteDialogComponent],
})
export class FoodJournalImporterModule {}
