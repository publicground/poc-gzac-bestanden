import {Component, EventEmitter, Input, OnDestroy, OnInit, Output} from '@angular/core';
import {FunctionConfigurationComponent} from '@valtimo/plugin';
import {BehaviorSubject, combineLatest, Observable, Subscription, take} from 'rxjs';
import {CustomValueLoggerActionConfig, OutputFormat} from './custom-value-logger-action-config';

@Component({
  selector: 'custom-value-logger-action-configuration',
  templateUrl: './custom-value-logger-action-configuration.component.html',
})
export class CustomValueLoggerActionConfigurationComponent
  // The component explicitly implements the FunctionConfigurationComponent interface
  implements FunctionConfigurationComponent, OnInit, OnDestroy
{
  @Input() save$: Observable<void>;
  @Input() disabled$: Observable<boolean>;
  @Input() pluginId: string;
  @Input() prefillConfiguration$: Observable<CustomValueLoggerActionConfig>;
  @Output() valid: EventEmitter<boolean> = new EventEmitter<boolean>();
  @Output() configuration: EventEmitter<CustomValueLoggerActionConfig> =
    new EventEmitter<CustomValueLoggerActionConfig>();

  readonly OUTPUT_FORMATS: Array<OutputFormat> = ['JSON', 'Smart Documents XML', 'Smart Documents JSON'];
  readonly OUTPUT_FORMAT_SELECT_ITEMS: Array<{id: string; text: string}> = this.OUTPUT_FORMATS.map(format => ({
    id: format,
    text: format,
  }));

  private saveSubscription!: Subscription;

  private readonly formValue$ = new BehaviorSubject<CustomValueLoggerActionConfig | null>(null);
  private readonly valid$ = new BehaviorSubject<boolean>(false);

  ngOnInit(): void {
    this.openSaveSubscription();
  }

  ngOnDestroy() {
    this.saveSubscription?.unsubscribe();
  }

  formValueChange(formValue: CustomValueLoggerActionConfig): void {
    this.formValue$.next(formValue);
    this.handleValid(formValue);
  }

  private handleValid(formValue: CustomValueLoggerActionConfig): void {
    const valid = !!(formValue.templateData && formValue.outputFormat);

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
