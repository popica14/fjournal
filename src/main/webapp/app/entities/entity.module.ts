import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'meal',
        loadChildren: () => import('./meal/meal.module').then(m => m.FoodJournalMealModule),
      },
      {
        path: 'weight',
        loadChildren: () => import('./weight/weight.module').then(m => m.FoodJournalWeightModule),
      },
      {
        path: 'importer',
        loadChildren: () => import('./importer/importer.module').then(m => m.FoodJournalImporterModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class FoodJournalEntityModule {}
