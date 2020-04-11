# Использование FTS в PostgreSQL
#### 1. Тип tsvector

> Задание 1
1. Изучите документацию к функции `to_tsvector`
2. Вызовите эту функцию для следующей строки: `Съешь ещё этих мягких французских булок, да выпей чаю`
3. Почему в векторе нет слова `да`?

Output: ``'булок':6 'вып':8 'ещё':2 'мягк':4 'съеш':1 'французск':5 'ча':9 'эт':3``

:Слово 'да' находится в словаре лексем

#### 2. Тип tsquery
Выполните по очереди запросы
```sql
--№1
SELECT to_tsvector('The quick brown fox jumped over the lazy dog')
    @@ to_tsquery('fox');
--№2
SELECT to_tsvector('The quick brown fox jumped over the lazy dog')
    @@ to_tsquery('foxes');
--№3
SELECT to_tsvector('The quick brown fox jumped over the lazy dog')
    @@ to_tsquery('foxhound');
```

> Задание 2
1. Что означают символы `@@`

: Оператор соотвествия документа (tsvector) запросу (tsquery).

2. Почему второй запрос возвращает `true`, а третий не возвращает

: Слово foxes производное от fox

3. Выполните запрос
```sql
SELECT to_tsvector('Russian', 'Съешь ещё этих мягких французских булок, да выпей чаю.')
    @@ to_tsquery('Russian','булка');
```
Почему слово булка не находится?

:Документ не соотвествует запросу

4. Используйте функцию `select ts_lexize('russian_stem', 'булок');` для того чтобы понять почему.
5. Замените в предложении слово `булок`, на слово `пирожков`
Выполните запросы
```sql
--№1
SELECT to_tsvector('Russian', 'Съешь ещё этих мягких французских пирожков, да выпей чаю.')
    @@ to_tsquery('Russian','пирожки');
--№2
SELECT to_tsvector('Russian', 'Съешь ещё этих мягких французских пирожков, да выпей чаю.')
    @@ to_tsquery('Russian','пирожок');
```
Почему первый запрос возвращает `true`, а второй не возвращает?

Лексемы пирожок нет в словаре
#### 3. Операторы
Выполните запрос
```sql
--И
SELECT to_tsvector('The quick brown fox jumped over the lazy dog')
    @@ to_tsquery('fox & dog');

--ИЛИ
SELECT to_tsvector('The quick brown fox jumped over the lazy dog')
    @@ to_tsquery('fox | rat');

--отрицание
SELECT to_tsvector('The quick brown fox jumped over the lazy dog')
    @@ to_tsquery('!clown');

--группировка
SELECT to_tsvector('The quick brown fox jumped over the lazy dog')
    @@ to_tsquery('fox & (dog | rat) & !mice');
```
> Задание 3
1. Приведите аналогичные запросы для любого предложения на русском

```
SELECT to_tsvector('Russian', 'Почему для английского языка не нужно указывать язык') @@ to_tsquery('английский & язык');
```
```
SELECT to_tsvector('Russian', 'Почему для английского языка не нужно указывать язык') @@ to_tsquery('английский | язык');
```
```
SELECT to_tsvector('Russian', 'Почему для английского языка не нужно указывать язык') @@ to_tsquery('!русский');
```
```
SELECT to_tsvector('Russian', 'Почему для английского языка не нужно указывать язык') @@ to_tsquery('английский & (языка | указать) & !русский');
```

2. Почему для английского языка не нужно указывать язык в первом аргументе и какой анализатор используется если никакой не указан?

:Он по умолчанию английский. Выбор анализатора, словарей и индексируемых типов фрагментов определяется конфигурацией текстового поиска.
#### 4. Поиск фраз
Изучите документацию по [операторам](https://www.postgresql.org/docs/current/functions-textsearch.html) FTS
Выполните запрос
```sql

SELECT to_tsvector('Russian', 'Съешь ещё этих мягких французских булок, да выпей чаю.') @@ to_tsquery('Russian','съешь<->ещё<2>мягких<2>булок');
```
> Задание 4
1. Что означает число 2 в операторе `<->`

: Дистанцию следования между первым и вторым запросом (по сути между словами).
Когда мы указываем одном слово за другим, то нужно указать <->, через одно <2>.

2. Модифицируйте запрос так, чтобы можно было найти фразу `съешь ещё`

3. Для чего нужно использовать функцию `phraseto_tsquery`

: Чтобы очищать фразу от пунктуации.

#### 5. Утилиты
1. Приведите примеры использования функций `ts_debug` и  `ts_headline`


```sql
SELECT ts_debug('english', 'The Brightest supernovaes');
```

```
Output:
"(asciiword,"Word, all ASCII",The,{english_stem},english_stem,{})"
"(blank,"Space symbols"," ",{},,)"
"(asciiword,"Word, all ASCII",Brightest,{english_stem},english_stem,{brightest})"
"(blank,"Space symbols"," ",{},,)"
"(asciiword,"Word, all ASCII",supernovaes,{english_stem},english_stem,{supernova})"
```

----

```sql
SELECT ts_headline('x y z', 'z'::tsquery);
```

```
Output:
"x y <b>z</b>"
```