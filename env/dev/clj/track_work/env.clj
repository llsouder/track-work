(ns track-work.env
  (:require [selmer.parser :as parser]
            [clojure.tools.logging :as log]
            [track-work.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[track-work started successfully using the development profile]=-"))
   :stop
   (fn []
     (log/info "\n-=[track-work has shut down successfully]=-"))
   :middleware wrap-dev})
