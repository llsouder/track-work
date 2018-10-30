#!/bin/bash

testconfig="test-config.edn"
if [ -f $testconfig ]; then
  echo found $testconfig
else
  echo writing $testconfig
  echo ';; WARNING
;; The test-config.edn file is used for local environment variables, such as database credentials.
;; This file is listed in .gitignore and will be excluded from version control by Git.

{:port 3000
 :database-url "postgresql://localhost/track_work_test?user=webdb&password=Postgres"
}' > $testconfig
fi

devconfig="dev-config.edn"
if [ -f $devconfig ]; then
  echo found $devconfig
else
  echo writing $devconfig
  echo ';; WARNING
;; The dev-config.edn file is used for local environment variables, such as database credentials.
;; This file is listed in .gitignore and will be excluded from version control by Git.

{:dev true
 :port 3000
 ;; when :nrepl-port is set the application starts the nREPL server on load
 :nrepl-port 7000

 :database-url "postgresql://localhost/track_work_dev?user=webdb&password=Postgres"
}' > $devconfig
fi

echo done
