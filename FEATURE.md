## Using the Application

It is very much recommended to use or test the application using the swagger-ui
docs on `http://localhost:8080/swagger-ui/index.html`. If you prefer seeing the
actual code then the controller tests on the `controller` module will be your
best bet. Although the tests uses in memory repositories for faster execution,
the databases behave exactly the same. But if you prefer using the JPA
repositories instead, just remove the `Bean` configuration on the
`TestConfiguration` and import the JPA repositories in any way you like.

## Old and New Shoe Core

The old shoe core consists of hard coded methods to return data, on the new
shoe core, only the version 1 has hard coded data. Version 2 of the shoe core
uses a shoe repository to query shoes that are in the database.

Version 2 allows API consumers to either query for all of the shoes in the
database or just the shoes in the `ShoeFilter`. Careful though, both fields of
the `ShoeFilter` must be present or else, it will query for all the shoes.

Additionally, it uses a ``ShoeEntity`` class to map to database tables. 

## Stock Core

The stock core is pretty much the same with the new shoe core, except that it
uses a `StockService` for handling all the business logic such as constraints,
some optimizations, and what not. ``StockService`` also uses the
`ShoeRepository` since the global stock can be queried without any additional
database tables.

Version 1 of the stock core is strict and does not support multiple update to
shoes. Querying the stock with a null `ShoeFilter` or if one of its fields is
null, then it will throw an exception which is also handled. Whereas version
two will allow such `ShoeFilter` to be processed. The difference is, if such
`ShoeFilter` is processed, it will query for all shoes in the database much
like version 2 of shoe core.

## Shoe DTO

`Shoe` DTO did not have any changes and is still the same.

## Stock DTO

`Stock` DTO holds shoes that are grouped by its Colors. So different shoe
models are still together as long as they have the same shoe color.

The `Stock` DTO looks like this:
```json
{
  "state": "FULL",
  "shoes": [
    {
      "color": "BLACK",
      "quantity": 30,
      "models": [
        {
          "shoe": {
            "name": "Random",
            "size": 8,
            "color": "BLACK"
          },
          "quantity": 0
        },
        {
          "shoe": {
            "name": "nike",
            "size": 8,
            "color": "BLACK"
          },
          "quantity": 20
        },
        {
          "shoe": {
            "name": "Lincoln",
            "size": 8,
            "color": "BLACK"
          },
          "quantity": 10
        }
      ]
    }
  ]
}
```
