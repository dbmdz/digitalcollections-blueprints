Services
============

**Integration Overview:**

![Integration Overview](images/diagram.jpg "Integration Overview")

Verfügbare Jobs auflisten (http://localhost:60080/job/list)
------------

Integration starten:

~~~
$ mvn clean install spring-boot:run
~~~

Endpoint aufrufen:

~~~
http://localhost:60080/job/list
~~~

Ergebnis:

~~~
[
    "testJob",
    "cmsToFedoraJob",
    "nasToFedoraJob",
    "fedoraToSolrJob",
    "fedoraToImgJob"
]
~~~

Verfügbare Jobs
------------

Die (meisten) Jobs laufen selbstsständig durch einen auf eine Minute getackteten Poller,
können aber auch über die weiter unten beschriebenen Endpoints aufgerufen werden (außer nasToFedoraJob).

| JobName         |Description                                                                  |Parameters						|
|-----------------|-----------------------------------------------------------------------------|---------------------|
|testJob          |Job zum Test                                                                 |											|
|cmsToFedoraJob   |Alle Daten aus dem CMS in das Fedora einspielen                              |											|
|nasToFedoraJob   |Alle Daten aus dem Verzeichnis in fedora importieren                         |											|
|fedoraToSolrJob  |Alle Daten aus dem Fedora in die SolrCloud schieben                          |hasOCR=(true|false)	|
|fedoraToImgJob   |Alle zoomViewer-Daten aus dem Fedora in den Bildähnlichkeitsserver schieben  |											|

**fedoraToSolrJob und fedoraToImgJob**

> Diese zwei Jobs lesen den TripleStore von Fedora mit SPARQL aus.
> Dazu werden die Variablen {from} und {until} durch das Start-Datum aus dem Schedule-Pojo oder 1970 (timestamp = 0) ersetzt,
> je nach dem, ob der HTTP-Endpoint oder der Poller den Prozess angestoßen hat.

> Der Job fedoraToSolrJob kann noch mit hasOCR=(true|false) parametrisiert werden.
> Default ist "false". Der automatische Service indexiert nur Objekte ohne OCR (hasOCR=false), sollen die Volltexte (Bavarica)
> indexiert werden muss der Job mit dem Parameter entsprechend händisch aufgerufen werden.Die Voltlext-indexierung dauert recht lange
> und die Objekte an sich ändern sich nicht großartig, somit muss dieser Schritt nur bei der Neuinstallation ausgeführt werden.

> Der Job fedoraToImgJob wird nicht mehr automatisch ausgeführt und muss von Hand angestossen werden.

~~~
SELECT
	?id ?state ?date ?itemID
WHERE {
	?id <http://www.openarchives.org/OAI/2.0/itemID> ?itemID .
	?id <http://www.digitale-sammlungen.de/hasStatus> ?state .
	?id <info:fedora/fedora-system:def/view#lastModifiedDate> ?date .
	?id <info:fedora/fedora-system:def/model#hasModel> <info:fedora/fedora-system:FedoraObject-3.0> .
	?id <info:fedora/fedora-system:def/relations-external#isMemberOf> <info:fedora/oaiset:viewer.zoom>
	FILTER (
		?date >= "#{from}"^^xsd:dateTime && ?date < "#{until}"^^xsd:dateTime
	)
}
~~~

Fertiges Statement mit Daten, diese kann man auch auf dem risearch-Endpoint des Fedora-Servers testen:

~~~
SELECT
	?id ?state ?date ?itemID ?hasOCR
WHERE {
	?id <http://www.openarchives.org/OAI/2.0/itemID> ?itemID .
	?id <http://www.digitale-sammlungen.de/hasStatus> ?state .
	?id <info:fedora/fedora-system:def/view#lastModifiedDate> ?date .
	?id <info:fedora/fedora-system:def/model#hasModel> <info:fedora/fedora-system:FedoraObject-3.0> .
	?id <http://www.digitale-sammlungen.de/hasOCR> ?hasOCR .
	?id <http://www.digitale-sammlungen.de/hasOCR> "false" .
	FILTER (
		?date >= "2015-01-31T10:29:00.180Z"^^xsd:dateTime && ?date < "2015-01-31T10:29:00.285Z"^^xsd:dateTime
	)
}
~~~

**cmsToFedoraJob**

> Dieser Job greift via OAI auf die Datenbank des CMS zu und importiert die letzten Änderungen.
> Der Zeitpunkt des letzten Imports wird in Schedule persistiert.

**nasToFedoraJob**

> Dieser Job verfolgt Änderungen auf dem NAS und sendet Nachrichten an Integration, wenn sich eine Datei ändert.
> Das Datum der letzten Änderung wird in der integrierten Derby-DB als ModifiedObject persistiert.

Job ausführen (http://localhost:60080/job/execute/{jobName})
------------

Die von Hand ausführbaren Jobs dienen momentan in erster Linie dazu, den ganzen Bestand von Bavarikon zu verarbeiten.
In Kürze sollen weitere Parameter hinzugefügt werden, welche es ermöglichen, nur Tiele des Bestandes z.B. neu zu Indexieren oder neu in Fedora zu importieren.
Alle Jobs, ausser der TestJob, werden außerdem minütlich aufgerufen um eventuelle Änderungen aus dem TripleStore oder der Datenbank des CMS auf die Systemem zu distributieren.

Integration starten:

~~~
$ mvn clean install spring-boot:run
~~~

Endpoint aufrufen:

~~~
http://localhost:60080/job/execute/cmsToFedoraJob
~~~
