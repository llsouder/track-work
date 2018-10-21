(ns user
  (:require [track-work.config :refer [env]]
            [clojure.spec.alpha :as s]
            [expound.alpha :as expound]
            [mount.core :as mount]
            [track-work.figwheel :refer [start-fw stop-fw cljs]]
            [track-work.core :refer [start-app]]
            [track-work.db.core]
            [conman.core :as conman]
            [luminus-migrations.core :as migrations]))

(alter-var-root #'s/*explain-out* (constantly expound/printer))

(defn start []
  (mount/start-without #'track-work.core/repl-server))

(defn stop []
  (mount/stop-except #'track-work.core/repl-server))

(defn restart []
  (stop)
  (start))

(defn restart-db []
  (mount/stop #'track-work.db.core/*db*)
  (mount/start #'track-work.db.core/*db*)
  (binding [*ns* 'track-work.db.core]
    (conman/bind-connection track-work.db.core/*db* "sql/queries.sql")))

(defn reset-db []
  (migrations/migrate ["reset"] (select-keys env [:database-url])))

(defn migrate []
  (migrations/migrate ["migrate"] (select-keys env [:database-url])))

(defn rollback []
  (migrations/migrate ["rollback"] (select-keys env [:database-url])))

(defn create-migration [name]
  (migrations/create name (select-keys env [:database-url])))


