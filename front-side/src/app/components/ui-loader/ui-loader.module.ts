import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { UiLoaderComponent } from "./ui-loader.component";
import { CommonModule } from '@angular/common';

@NgModule({
  declarations: [
    UiLoaderComponent
  ],
  exports: [ 
    UiLoaderComponent 
  ],
  imports: [
    CommonModule,
  ],
  schemas: [ CUSTOM_ELEMENTS_SCHEMA ]
})
export class UiLoaderModule { }
