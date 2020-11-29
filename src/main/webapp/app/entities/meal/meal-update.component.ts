import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiDataUtils, JhiFileLoadError, JhiEventManager, JhiEventWithContent } from 'ng-jhipster';

import { IMeal, Meal } from 'app/shared/model/meal.model';
import { MealService } from './meal.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';
import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/user/account.model';

@Component({
  selector: 'jhi-meal-update',
  templateUrl: './meal-update.component.html',
})
export class MealUpdateComponent implements OnInit {
  isSaving = false;
  users: IUser[] = [];

  currentAccount: Account | undefined;

  editForm = this.fb.group({
    id: [],
    description: [null, [Validators.required]],
    quantity: [],
    portionSize: [],
    type: [null, [Validators.required]],
    date: [null, [Validators.required]],
    photo: [],
    photoContentType: [],
    calories: [],
    comment: [],
    recipe: [],
    myMealId: [],
  });

  constructor(
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected mealService: MealService,
    protected userService: UserService,
    protected accountService: AccountService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ meal }) => {
      if (!meal.id) {
        const today = moment().startOf('day');
        meal.date = today;
      }

      this.updateForm(meal);

      this.accountService.identity().subscribe(account => {
        if (account) {
          this.currentAccount = account;
        }
      });

      this.userService.query().subscribe((res: HttpResponse<IUser[]>) => (this.users = res.body || []));
    });
  }

  updateForm(meal: IMeal): void {
    this.editForm.patchValue({
      id: meal.id,
      description: meal.description,
      quantity: meal.quantity,
      portionSize: meal.portionSize,
      type: meal.type,
      date: meal.date ? meal.date.format(DATE_TIME_FORMAT) : null,
      photo: meal.photo,
      photoContentType: meal.photoContentType,
      calories: meal.calories,
      comment: meal.comment,
      recipe: meal.recipe,
      myMealId: meal.myMealId,
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType: string, base64String: string): void {
    this.dataUtils.openFile(contentType, base64String);
  }

  setFileData(event: any, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe(null, (err: JhiFileLoadError) => {
      this.eventManager.broadcast(
        new JhiEventWithContent<AlertError>('foodJournalApp.error', { ...err, key: 'error.file.' + err.key })
      );
    });
  }

  clearInputImage(field: string, fieldContentType: string, idInput: string): void {
    this.editForm.patchValue({
      [field]: null,
      [fieldContentType]: null,
    });
    if (this.elementRef && idInput && this.elementRef.nativeElement.querySelector('#' + idInput)) {
      this.elementRef.nativeElement.querySelector('#' + idInput).value = null;
    }
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const meal = this.createFromForm();
    if (meal.id !== undefined) {
      this.subscribeToSaveResponse(this.mealService.update(meal));
    } else {
      this.subscribeToSaveResponse(this.mealService.create(meal));
    }
  }

  private createFromForm(): IMeal {
    return {
      ...new Meal(),
      id: this.editForm.get(['id'])!.value,
      description: this.editForm.get(['description'])!.value,
      quantity: this.editForm.get(['quantity'])!.value,
      portionSize: this.editForm.get(['portionSize'])!.value,
      type: this.editForm.get(['type'])!.value,
      date: this.editForm.get(['date'])!.value ? moment(this.editForm.get(['date'])!.value, DATE_TIME_FORMAT) : undefined,
      photoContentType: this.editForm.get(['photoContentType'])!.value,
      photo: this.editForm.get(['photo'])!.value,
      calories: this.editForm.get(['calories'])!.value,
      comment: this.editForm.get(['comment'])!.value,
      recipe: this.editForm.get(['recipe'])!.value,
      myMealId: this.users.find(e => e.login === this.currentAccount?.login)?.id,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMeal>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: IUser): any {
    return item.id;
  }
}
