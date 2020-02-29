import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpQueryService } from 'src/app/service/http-query.service';
import { Observable, Subject } from 'rxjs';
import { takeUntil, debounceTime, map, mergeMap } from 'rxjs/operators';
import { FormControl } from '@angular/forms';

@Component({
  selector: 'movies-list',
  templateUrl: './movies-list.component.html',
  styleUrls: ['./movies-list.component.scss']
})
export class MoviesListComponent implements OnInit, OnDestroy {

  constructor(private httpQuery: HttpQueryService) { }

  private $unSubscriber: Subject<any> = new Subject();

  searchFormControl: FormControl = new FormControl();

  moviesList = [];

  sortInfo = {
    field: 'year',
    order: 'desc'
  }

  componentState: 'results' | 'noResults' | 'loading' = 'loading';

  ngOnInit(): void {
    this.subToFormsControl();
  }

  ngOnDestroy(): void {
    this.$unSubscriber.next();
    this.$unSubscriber.unsubscribe();
  }

  subToFormsControl() {

    this.searchFormControl.valueChanges.pipe(
      debounceTime(600),
      mergeMap(() => this.searchMovie()),
    ).subscribe()

    this.searchFormControl.setValue('');
  }

  searchMovie(): Observable<any> {

    this.componentState = 'loading';

    return this.httpQuery.getListMovies(this.searchFormControl.value, 100, 0, this.sortInfo.order, this.sortInfo.field).pipe(
      takeUntil(this.$unSubscriber),
      map(movies => {
        this.moviesList = movies || [];
        this.componentState = (this.moviesList.length ? 'results' : 'noResults');
      })
    );
  }

  setOrder(field: string) {

    this.$unSubscriber.next();
    
    this.sortInfo.field = field;
    this.sortInfo.order = this.sortInfo.order == 'desc' ? 'asc' : 'desc';
    
    this.searchMovie().subscribe();
  }

}
