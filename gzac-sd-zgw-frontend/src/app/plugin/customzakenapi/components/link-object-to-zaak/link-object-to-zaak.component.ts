/*
 * Copyright 2015-2020 Ritense BV, the Netherlands.
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
import {FunctionConfigurationComponent, PluginTranslatePipeModule} from '@valtimo/plugin';
import {BehaviorSubject, combineLatest, Observable, Subscription, take} from 'rxjs';
import {LinkObjectToZaakConfig} from '../../models';

@Component({
    selector: 'link-object',
    templateUrl: './link-object-to-zaak.component.html',
    styleUrls: ['./link-object-to-zaak.component.scss']
})

export class LinkObjectToZaakComponent
    implements FunctionConfigurationComponent, OnInit, OnDestroy {
    @Input() save$: Observable<void>;
    @Input() disabled$: Observable<boolean>;
    @Input() pluginId: string;
    @Input() prefillConfiguration$: Observable<LinkObjectToZaakConfig>;
    @Output() valid: EventEmitter<boolean> = new EventEmitter<boolean>();
    @Output() configuration: EventEmitter<LinkObjectToZaakConfig> = new EventEmitter<LinkObjectToZaakConfig>();

    private saveSubscription!: Subscription;
    private readonly formValue$ = new BehaviorSubject<LinkObjectToZaakConfig | null>(null);
    private readonly valid$ = new BehaviorSubject<boolean>(false);

    readonly objectTypes: Array<{ id: string; text: string }> = [
        { id: 'ADRES', text: 'Adres' },
        { id: 'BESLUIT', text: 'Besluit' },
        { id: 'BUURT', text: 'Buurt' },
        { id: 'ENKELVOUDIG_DOCUMENT', text: 'Enkelvoudig document' },
        { id: 'GEMEENTE', text: 'Gemeente' },
        { id: 'GEMEENTELIJKE_OPENBARE_RUIMTE', text: 'Gemeentelijke openbare ruimte' },
        { id: 'HUISHOUDEN', text: 'Huishouden' },
        { id: 'INRICHTINGSELEMENT', text: 'Inrichtingselement' },
        { id: 'KADASTRALE_ONROERENDE_ZAAK', text: 'Kadastrale onroerende zaak' },
        { id: 'KUNSTWERKDEEL', text: 'Kunstwerkdeel' },
        { id: 'MAATSCHAPPELIJKE_ACTIVITEIT', text: 'Maatschappelijke activiteit' },
        { id: 'MEDEWERKER', text: 'Medewerker' },
        { id: 'NATUURLIJK_PERSOON', text: 'Natuurlijk persoon' },
        { id: 'NIET_NATUURLIJK_PERSOON', text: 'Niet-natuurlijk persoon' },
        { id: 'OPENBARE_RUIMTE', text: 'Openbare ruimte' },
        { id: 'ORGANISATORISCHE_EENHEID', text: 'Organisatorische eenheid' },
        { id: 'PAND', text: 'Pand' },
        { id: 'SPOORBAANDEEL', text: 'Spoorbaandeel' },
        { id: 'STATUS', text: 'Status' },
        { id: 'TERREINDEEL', text: 'Terreindeel' },
        { id: 'TERREIN_GEBOUWD_OBJECT', text: 'Terrein gebouwd object' },
        { id: 'VESTIGING', text: 'Vestiging' },
        { id: 'WATERDEEL', text: 'Waterdeel' },
        { id: 'WEGDEEL', text: 'Wegdeel' },
        { id: 'WIJK', text: 'Wijk' },
        { id: 'WOONPLAATS', text: 'Woonplaats' },
        { id: 'WOZ_DEELOBJECT', text: 'WOZ deelobject' },
        { id: 'WOZ_OBJECT', text: 'WOZ object' },
        { id: 'WOZ_WAARDE', text: 'WOZ waarde' },
        { id: 'ZAKELIJK_RECHT', text: 'Zakelijk recht' },
        { id: 'OVERIGE', text: 'Overige' }
    ];

    ngOnInit(): void {
        this.openSaveSubscription();
    }

    ngOnDestroy(): void {
        this.saveSubscription?.unsubscribe();
    }

    formValueChange(formValue: LinkObjectToZaakConfig): void {
        this.formValue$.next(formValue);
        this.handleValid(formValue);
    }

    private handleValid(formValue: LinkObjectToZaakConfig): void {
        const valid = !!(
            formValue.objectUrl &&
            formValue.objectType
        );

        this.valid$.next(valid);
        this.valid.emit(valid);
    }

    private openSaveSubscription(): void {
        this.saveSubscription = this.save$?.subscribe(() => {
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
