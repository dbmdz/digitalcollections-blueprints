# DigitalCollections: Blueprints 3: REST Webservice Client (Frontend IMPL OpenFeign)

Part of the webservice blueprint is a REST webservice server providing a personal greeting message at his endpoint.
The endpoint's URL is http://localhost:9000/hello and provides a response like this:

```json
{
  "id": 2,
  "content": "Hello, Stranger!"
}
```

When passing a request param "name" (http://localhost:9000/hello?name=Sepp) you get a personalized greeting:

```json
{
  "id": 2,
  "content": "Hello, Sepp!"
}
```

In this webservice client blueprint we will be using the REST-client framework "OpenFeign" to use this webservice.

## OpenFeign

