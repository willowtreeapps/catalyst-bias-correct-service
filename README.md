# Bias Correction Service

A microservice to provide detection and suggestion corrections for gendered language.  Intended to be used in integration with chat services (i.e. integrated into a Slack app).

## Setup

* Download [IntelliJ IDEA](https://www.jetbrains.com/idea/download/)
* Open this folder as a project in IDEA
* Allow IDEA to download/configure the project as necessary
* Navigate to the `sbt shell` pane and allow IDEA to configure it as necessary
* When the shell is ready, type `run`
* Navigate to http://localhost:9000 to verify that the service is running

## Data format

Trigger words and their corrective suggestions are pulled from CSV files.  The first value in a row is the trigger word.  The second value is the suggestion.  Values beyond the second are ignored and you must provide at least two values for the service to consider the row.  You may provide multiple suggestions for any given trigger by repeating the trigger in a new row and using a new suggestion.  Those suggestions are collated to use the same trigger.  The service will choose a random suggestion from the list associated with those triggers.  Suggestions are uniqued and you may not weight a suggestion list by repeating individual suggestions.

Support for multiple languages is also configured via a CSV file.  When the service starts, it will check for a CSV file at a given URL.  That CSV file may contain multiple rows.  The first value in each row is the supported language (or language and region) as specified by an ISO 639-1 code (eg. "en" or "en_US").  You may provide support for multiple regions for any given language as well as a default base language data set.  The second column is a URL to a CSV file containing the trigger words and suggestions as documented above.

At this time, the canonical source for both the configuration and trigger words+suggestions data sets is a Google Sheet.  The first tab of the Google Sheet is the configuration and the other tabs are language support.  The URL for the first tab in the Google Sheet is embedded in the application in the `application.conf` file.  In the current implementation, the configuration is self-referencing in that the language data sets are other tabs within that same Sheet.  If you wanted to add a new language, you could create a new tab, add data to it, and then create a new entry in the "supported languages" tab.  You would use the ISO 639-1 code for the supported language in the first column.  The second column would be a URL that uniquely referenced your newly created tab.  You can locate that URL by clicking `File -> Publish to the web` from the Sheet's toolbar, selecting the tab in the `Link` section of the modal window, changing the type to "Comma-separated values (.csv)" (default is "Web Page"), and copying the link below those selectors.
