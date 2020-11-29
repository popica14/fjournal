import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiDataUtils, JhiFileLoadError, JhiEventManager, JhiEventWithContent } from 'ng-jhipster';

import { IWeight, Weight } from 'app/shared/model/weight.model';
import { WeightService } from './weight.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';
import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/user/account.model';

@Component({
  selector: 'jhi-weight-update',
  templateUrl: './weight-update.component.html',
})
export class WeightUpdateComponent implements OnInit {
  isSaving = false;
  users: IUser[] = [];

  currentAccount: Account | undefined;

  editForm = this.fb.group({
    id: [],
    value: [null, [Validators.required, Validators.min(40), Validators.max(150)]],
    date: [],
    photo: [],
    photoContentType: [],
    observation: [],
    myWeigthId: [],
  });

  constructor(
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected weightService: WeightService,
    protected userService: UserService,
    protected accountService: AccountService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ weight }) => {
      if (!weight.id) {
        const today = moment().startOf('day');
        weight.date = today;
      }

      this.updateForm(weight);

      this.accountService.identity().subscribe(account => {
        if (account) {
          this.currentAccount = account;
        }
      });

      this.userService.query().subscribe((res: HttpResponse<IUser[]>) => (this.users = res.body || []));
    });
  }

  updateForm(weight: IWeight): void {
    this.editForm.patchValue({
      id: weight.id,
      value: weight.value,
      date: weight.date ? weight.date.format(DATE_TIME_FORMAT) : null,
      photo: weight.photo,
      photoContentType: weight.photoContentType,
      observation: weight.observation,
      myWeigthId: weight.myWeigthId,
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
    const weight = this.createFromForm();
    if (weight.id !== undefined) {
      this.subscribeToSaveResponse(this.weightService.update(weight));
    } else {
      this.subscribeToSaveResponse(this.weightService.create(weight));
    }
  }

  private createFromForm(): IWeight {
    return {
      ...new Weight(),
      id: this.editForm.get(['id'])!.value,
      value: this.editForm.get(['value'])!.value,
      date: this.editForm.get(['date'])!.value ? moment(this.editForm.get(['date'])!.value, DATE_TIME_FORMAT) : undefined,
      photoContentType: this.editForm.get(['photoContentType'])!.value,
      photo: this.editForm.get(['photo'])!.value,
      observation: this.editForm.get(['observation'])!.value,
      myWeigthId: this.users.find(e => e.login === this.currentAccount?.login)?.id,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IWeight>>): void {
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
