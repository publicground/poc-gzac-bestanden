import {PluginConfigurationComponent, PluginManagementService, PluginTranslationService} from "@valtimo/plugin";
import {Component, EventEmitter, Input, OnDestroy, OnInit, Output} from "@angular/core";
import {BehaviorSubject, combineLatest, map, Observable, Subscription, take} from "rxjs";
import {HaalCentraalBagPluginConfig} from "../../models/haal-centraal-bag-plugin-config";
import {TranslateService} from "@ngx-translate/core";

@Component({
    // eslint-disable-next-line @angular-eslint/component-selector
    selector: 'haal-centraal-bag-plugin-configuration',
    templateUrl: './haal-centraal-bag-plugin-configuration.component.html',
    styleUrls: ['./haal-centraal-bag-plugin-configuration.component.scss']
})
export class HaalCentraalBagPluginConfigurationComponent
    // The component explicitly implements the PluginConfigurationComponent interface
    implements PluginConfigurationComponent, OnInit, OnDestroy {
    @Input() save$: Observable<void>;
    @Input() disabled$: Observable<boolean>;
    @Input() pluginId: string
    // If the plugin had already been saved, a prefilled configuration of the type HaalCentraalBagPluginConfig is expected
    @Input() prefillConfiguration$: Observable<HaalCentraalBagPluginConfig>;

    @Output() valid: EventEmitter<boolean> = new EventEmitter<boolean>();
    @Output() configuration: EventEmitter<HaalCentraalBagPluginConfig> =
        new EventEmitter<HaalCentraalBagPluginConfig>();

    private saveSubscription!: Subscription;

    private readonly formValue$ = new BehaviorSubject<HaalCentraalBagPluginConfig | null>(null);
    private readonly valid$ = new BehaviorSubject<boolean>(false);

    readonly authenticationPluginSelectItems$: Observable<Array<{ id: string; text: string }>> =
        combineLatest([
            this.pluginManagementService.getPluginConfigurationsByCategory(
                'haal-centraal-authentication-plugin'
            ),
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
    ) {
    }

    ngOnInit(): void {
        this.openSaveSubscription();
    }

    ngOnDestroy() {
        this.saveSubscription?.unsubscribe();
    }

    formValueChange(formValue: any): void {
        this.formValue$.next(formValue);
        this.handleValid(formValue);
    }

    private handleValid(formValue: HaalCentraalBagPluginConfig): void {
        // The configuration is valid when a configuration title and url are defined
        const valid = !!(
            formValue.configurationTitle &&
            formValue.bagBaseUrl
        );

        this.valid$.next(valid);
        this.valid.emit(valid);
    }

    private openSaveSubscription(): void {
        /*
        If the save observable is triggered, check if the configuration is valid, and if so,
        output the configuration using the configuration EventEmitter.
         */
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
