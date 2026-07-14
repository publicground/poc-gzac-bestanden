/*
 * Copyright 2015-2024 Ritense BV, the Netherlands.
 *
 * Licensed under EUPL, Version 1.2 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://joinup.ec.europa.eu/collection/eupl/eupl-text-eupl-12
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import {Component, EventEmitter, Input, OnDestroy, OnInit, Output} from '@angular/core';
import {BehaviorSubject, combineLatest, map, Observable, Subscription, take} from 'rxjs';
import {FunctionConfigurationComponent, PluginManagementService, PluginTranslationService} from '@valtimo/plugin';
import {StoreTrefwoordenConfig} from '../../models';
import {TranslateService} from '@ngx-translate/core';

@Component({
  selector: 'store-trefwoorden',
  templateUrl: './store-trefwoorden-configuration.component.html',
  styleUrls: ['./store-trefwoorden-configuration.component.scss']
})
export class StoreTrefwoordenConfigurationComponent
  implements FunctionConfigurationComponent, OnInit, OnDestroy {
  @Input() save$: Observable<void>;
  @Input() disabled$: Observable<boolean>;
  @Input() pluginId: string;
  @Input() prefillConfiguration$: Observable<StoreTrefwoordenConfig>;
  @Output() valid: EventEmitter<boolean> = new EventEmitter<boolean>();
  @Output() configuration: EventEmitter<StoreTrefwoordenConfig> = new EventEmitter<StoreTrefwoordenConfig>();

  private saveSubscription!: Subscription;
  private readonly formValue$ = new BehaviorSubject<StoreTrefwoordenConfig | null>(null);
  private readonly valid$ = new BehaviorSubject<boolean>(false);

  readonly authenticationPluginSelectItems$: Observable<Array<{id: string; text: string}>> =
      combineLatest([
        this.pluginManagementService.getPluginConfigurationsByCategory('store-informatieobjecttype-trefwoord'),
        this.translateService.stream('key'),
      ]).pipe(
          map(([configurations]) =>
              configurations.map(configuration => ({
                id: configuration.id,
                text: `${configuration.title} - ${this.pluginTranslationService.instant(
                    'title',
                    configuration.pluginDefinition.key
                )}`,
              }))
          )
      );

  constructor(
      private readonly pluginManagementService: PluginManagementService,
      private readonly translateService: TranslateService,
      private readonly pluginTranslationService: PluginTranslationService
  ) {}

  ngOnInit(): void {
    this.openSaveSubscription();
  }

  ngOnDestroy(): void {
    this.saveSubscription?.unsubscribe();
  }

  formValueChange(formValue: StoreTrefwoordenConfig): void {
    this.formValue$.next(formValue);
    this.handleValid(formValue);
  }

  private handleValid(formValue: StoreTrefwoordenConfig): void {
    const valid = !!formValue.documentUrl;

    this.valid$.next(valid);
    this.valid.emit(valid);
  }

  private openSaveSubscription(): void {
    this.saveSubscription = this.save$?.subscribe(save => {
      combineLatest([this.formValue$, this.valid$])
        .pipe(take(1))
        .subscribe(([formValue, valid]) => {
          if (valid) {
            this.configuration.emit(formValue);
          }
        });
    });
  }
}
