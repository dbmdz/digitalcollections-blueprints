# Migration for Bootstrap 3 to 4

In this section we migrate and upgrade our Bootstrap 3 webpage to the latest Boostrap 4 version.

Unfortunately, Bootstrap 4 has a lot of breaking changes, and a good starting point for a migration guide line
is the official migration document from Bootstrap:

<https://getbootstrap.com/docs/4.0/migration/>

## Bump version of the Bootstrap WebJar

The first step in our webpage migration is to change the version of the Bootstrap WebJar in the `pom.xml`.

**Before:**

```xml
...
<version.webjar-bootstrap>3.3.7</version.webjar-bootstrap>
...
<dependency>
  <groupId>org.webjars.npm</groupId>
  <artifactId>bootstrap</artifactId>
  <version>${version.webjar-bootstrap}</version>
  <exclusions>
    <exclusion>
      <groupId>org.webjars</groupId>
      <artifactId>jquery</artifactId>
    </exclusion>
  </exclusions>
</dependency>
...
```

**After:**

```xml
...
<version.webjar-bootstrap>4.2.1</version.webjar-bootstrap>
...
<dependency>
  <groupId>org.webjars.npm</groupId>
  <artifactId>bootstrap</artifactId>
  <version>${version.webjar-bootstrap}</version>
  <exclusions>
    <exclusion>
      <groupId>org.webjars</groupId>
      <artifactId>jquery</artifactId>
    </exclusion>
  </exclusions>
</dependency>
```

## Migrate static templates

In the third step of this tutorial, the static templates located under `src/main/resources/templates` needs to
be migrated to Bootstrap 4 syntax.

This step is a bit time consuming, but at the end you'll have a shiny Bootstrap 4 webpage 🤗

### Base template

The `navbar` component was heavily refactored in Bootstrap 4:

* Responsive navbar behaviors are now applied to the `.navbar` class via the required `.navbar-expand-{breakpoint}`
  where you choose where to collapse the `navbar`. Previously this was a Less variable modification and required recompiling.
* `.navbar-default` is now `.navbar-light`, though `.navbar-dark` remains the same.
  One of these is required on each `navbar`. However, these classes no longer set background-colors;
  instead they essentially only affect color.
* Navbars now require a background declaration of some kind. Choose from our background utilities (`.bg-*`) or set your own
  with the light/inverse classes above for mad customization.
* Given flexbox styles, navbars can now use flexbox utilities for easy alignment options.
* `.navbar-toggle` is now `.navbar-toggler` and has different styles and inner markup (no more three `<span>`s).
* Dropped the `.navbar-form` class entirely. It’s no longer necessary; instead, just use `.form-inline`
  and apply margin utilities as necessary.
* Navbars no longer include `margin-bottom` or `border-radius` by default. Use utilities as necessary.

Thus, the old `navbar` declaration for our webpage:

```html
    <nav class="navbar navbar-inverse navbar-fixed-top">
      <div class="container">
        <div class="navbar-header">
          <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="navbar-brand" href="">DigitalCollections</a>
        </div>
        <div id="navbar" class="collapse navbar-collapse">
          <ul class="nav navbar-nav">
            <li class="active"><a href="#">Home</a></li>
            <li><a th:href="@{''(lang=de)}">de</a> | <a th:href="@{''(lang=en)}">en</a></li>
          </ul>
        </div><!--/.nav-collapse -->
      </div>
    </nav>
```

Needs to be rewritten and migrated to:

```html
    <nav class="navbar navbar-expand-md navbar-dark bg-dark fixed-top">
      <a class="navbar-brand" href="#">DigitalCollections</a>
      <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarsExampleDefault" aria-controls="navbarsExampleDefault" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
      </button>

      <div class="collapse navbar-collapse" id="navbarsExampleDefault">
        <ul class="navbar-nav mr-auto">
          <li class="nav-item active"><a class="nav-link" href="#">Home</a></li>
          <li class="nav-item"><a class="nav-link" th:href="@{''(lang=de)}">de</a> | <a class="nav-link" th:href="@{''(lang=en)}">en</a></li>
        </ul>
      </div>
    </nav>
```

#### CSS

The `.navbar-nav` CSS section (located in `src/main/resources/static/css/main.css`) must be changed from:

```css
.navbar-nav li a{
  display: inline-block;
}
```

to:

```css
.navbar-dark .navbar-nav .nav-link{
  display: inline-block;
}
```

# Comparison

Congratulations! After following this step-by-step guide the webpage is successfully migrated to Bootstrap 4 🎉

Here's a screenshot that shows the old webpage in Bootstrap 3:

![Blueprings - Bootstrap 3](images/screenshot-thymeleaf-page.png)

The new migrated webpage in Bootstrap 4 looks like:

![Blueprings - Bootstrap 4](images/screenshot-thymeleaf-page-bootstrap-4.png)

# Further migrations

Here comes an overview (not feature complete!) of class changes from Bootstrap 3 to 4, mainly taken from
[here](http://upgrade-bootstrap.bootply.com/).

## `panel`

The `panel`, `well` and `thumbnail` components were removed in Bootstrap 4. Just use the `card` component instead.

Documentation of `card` component can be found [here](https://getbootstrap.com/docs/4.0/components/card/).


| Bootstrap 3.x    | Bootstrap 4
| ---------------- | ----------------------------
| `.panel`         | `.card`
| `.panel-heading` | `.card-header`
| `.panel-title`   | `.card-title`
| `.panel-body`    | `.card-body`
| `.panel-footer`  | `.card-footer`
| `.panel-primary` | `.card.bg-primary.text-white`
| `.panel-success` | `.card.bg-success.text-white`
| `.panel-info`    | `.card.text-white.bg-info`
| `.panel-warning` | `.card.bg-warning`
| `.panel-danger`  | `.card.bg-danger.text-white`
| `.well`          | `.card.card-body`
| `.thumbnail`     | `.card.card-body`

## `navbar`

Some CSS attributes and HTML attributes for the `navbar` have changed:

| Bootstrap 3.x          | Bootstrap 4
| ---------------------- | --------------
| `.nav navbar > li`     | `.nav-item`
| `.nav navbar > li > a` | `.nav-link`
| `.navbar-right`        | `.ml-auto`
| `.navbar-btn`          | `.nav-item`
| `.navbar-fixed-top`    | `.fixed-top`
| `.nav-stacked`         | `.flex-column`

## Other components

| Bootstrap 3.x          | Bootstrap 4
| ---------------------- | --------------
| `.col-*-offset-*`      | `.offset-*`
| `.col-*-push-*`        | `.order-*-2`
| `.col-*-pull-*`        | ` .order-*-1`
| `.btn-default`         | `.btn-secondary`
| `.img-responsive`      | ` .img-fluid`
| `.img-circle`          | ` .rounded-circle`
| `.img-rounded`         | `.rounded`
| `.form-horizontal`     | (removed)
| `.radio`               | `.form-check`
| `.checkbox`            | `.form-check`
| `.input-lg`            | `.form-control-lg`
| `.input-sm`            | `.form-control-sm`
| `.control-label`       | `.col-form-label`
| `.table-condensed`     | `.table-sm`
| `.pagination > li`     | `.page-item`
| `.pagination > li > a` | `.page-link`
| `.item`                | `.carousel-item`
| `.help-block`          | ` .form-text`
| `.pull-right`          | ` .float-right`
| `.pull-left`           | `.float-left`
| `.center-block`        | ` .mx-auto.d-block`
| `.hidden-xs`           | `.d-none`
| `.hidden-sm`           | `.d-sm-none`
| `.hidden-md`           | `.d-md-none`
| `.hidden-lg`           | `.d-lg-none`
| `.visible-xs`          | ` .d-block.d-sm-none`
| `.visible-sm`          | ` .d-none.d-sm-block.d-md-none`
| `.visible-md`          | ` .d-none.d-md-block.d-lg-none`
| `.visible-lg`          | ` .d-none.d-lg-block.d-xl-none`
| `.label`               | `.badge`
| `.badge`               | `.badge.badge-pill`
