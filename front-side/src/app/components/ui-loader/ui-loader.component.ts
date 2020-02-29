import { Component, OnInit, Input, EventEmitter, Output, SimpleChange, ChangeDetectorRef } from '@angular/core';
import { FormControl} from '@angular/forms';

@Component({
  selector: 'ui-loader',
  templateUrl: './ui-loader.component.html',
  styleUrls: ['./ui-loader.component.scss'],
})
export class UiLoaderComponent implements OnInit {

  constructor(private cdr: ChangeDetectorRef) { }

  ngOnInit(): void {

  }

}
