import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiDataUtils, JhiFileLoadError, JhiEventManager, JhiEventWithContent } from 'ng-jhipster';

import { IImporter, Importer } from 'app/shared/model/importer.model';
import { ImporterService } from './importer.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';
import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/user/account.model';

@Component({
  selector: 'jhi-importer-update',
  templateUrl: './importer-update.component.html',
})
export class ImporterUpdateComponent implements OnInit {
  isSaving = false;
  users: IUser[] = [];

  currentAccount: Account | undefined;

  editForm = this.fb.group({
    id: [],
    file: [],
    fileContentType: [],
    importDate: [null, [Validators.required]],
    separator: [],
    ownerId: [],
    uploadeId: [],
  });

  constructor(
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected importerService: ImporterService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    protected accountService: AccountService,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ importer }) => {
      if (!importer.id) {
        const today = moment().startOf('day');
        importer.importDate = today;
      }

      this.updateForm(importer);

      this.userService.query().subscribe((res: HttpResponse<IUser[]>) => (this.users = res.body || []));
    });

    this.accountService.identity().subscribe(account => {
      if (account) {
        this.currentAccount = account;
      }
    });
  }

  updateForm(importer: IImporter): void {
    this.editForm.patchValue({
      id: importer.id,
      file: importer.file,
      fileContentType: importer.fileContentType,
      importDate: importer.importDate ? importer.importDate.format(DATE_TIME_FORMAT) : null,
      separator: importer.separator,
      ownerId: importer.ownerId,
      uploaderId: importer.uploaderId,
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

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const importer = this.createFromForm();
    if (importer.id !== undefined) {
      this.subscribeToSaveResponse(this.importerService.update(importer));
    } else {
      this.subscribeToSaveResponse(this.importerService.create(importer));
    }
  }

  private createFromForm(): IImporter {
    return {
      ...new Importer(),
      id: this.editForm.get(['id'])!.value,
      fileContentType: this.editForm.get(['fileContentType'])!.value,
      file: this.editForm.get(['file'])!.value,
      importDate: this.editForm.get(['importDate'])!.value ? moment(this.editForm.get(['importDate'])!.value, DATE_TIME_FORMAT) : undefined,
      separator: this.editForm.get(['separator'])!.value,
      ownerId: this.editForm.get(['ownerId'])!.value,
      uploaderId: this.users.find(e => e.login === this.currentAccount?.login)?.id,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IImporter>>): void {
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
