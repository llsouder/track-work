(ns track-work.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[track-work started successfully]=-"))
   :stop
   (fn []
     (log/info "\n-=[track-work has shut down successfully]=-"))
   :middleware identity})
