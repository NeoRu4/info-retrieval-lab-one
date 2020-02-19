import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpQueryService } from 'src/app/service/http-query.service';
import { Observable, Subject } from 'rxjs';
import { takeUntil, debounceTime, map } from 'rxjs/operators';
import { FormControl } from '@angular/forms';

@Component({
  selector: 'movies-list',
  templateUrl: './movies-list.component.html',
  styleUrls: ['./movies-list.component.scss']
})
export class MoviesListComponent implements OnInit, OnDestroy {

  constructor(private httpQuery: HttpQueryService) { }

  private $unSubscriber = new Subject();

  searchFormControl: FormControl = new FormControl();

  moviesList = [];

  ngOnInit(): void {
    this.subToFormsControl();
  }

  ngOnDestroy(): void {
    this.$unSubscriber.next();
    this.$unSubscriber.complete();
  }

  subToFormsControl() {

    this.searchFormControl.valueChanges.pipe(
      debounceTime(600),
      // takeUntil(this.$unSubscriber)
    ).subscribe(value => {

      this.searchMovie().subscribe();
    })

    this.searchFormControl.setValue('');
  }

  searchMovie(): Observable<any> {

    this.$unSubscriber.next();

    return this.httpQuery.getListMovies(this.searchFormControl.value, 100, 0).pipe(
      // takeUntil(this.$unSubscriber),
      map(movies => {
        this.moviesList = movies;
      })
    );
  }

}
